import Adafruit_DHT
import tflite_runtime.interpreter as tflite
import requests
import asyncio
import websockets
import RPi.GPIO as GPIO
import threading
import json
import numpy as np
import spidev


spi = spidev.SpiDev()
spi.open(0, 0)  # Open SPI port 0, device (CS) 0
spi.max_speed_hz = 1000000  # 1 MHz

# Globale vlag voor de WebSocket-client thread
running = True

# GPIO setup voor LED
LED_PIN = 18  # Pas dit aan aan de GPIO-pin die u gebruikt
GPIO.setmode(GPIO.BCM)
GPIO.setup(LED_PIN, GPIO.OUT)

# Configureer de WebSocket URI en het kanaal
WEBSOCKET_URI = 'ws://eventiz-ws.lukasolivier.be/app/websocket'
CHANNEL_NAME = 'alarm'

# Aanmeldbericht voor het kanaal
subscribe_message = json.dumps({
    'event': 'pusher:subscribe',
    'data': {
        'channel': CHANNEL_NAME
    }
})

# WebSocket client functies
# Functie om berichten te verwerken ontvangen via de WebSocket
async def listen_to_server(uri):
    async with websockets.connect(uri) as websocket:
        # Stuur het abonneebericht
        await websocket.send(subscribe_message)
        
        # Blijf luisteren naar berichten
        while True:
            message = await websocket.recv()
            print(f"Ontvangen bericht: {message}")
            # Aanvullende berichtverwerkingslogica hier...
            data = json.loads(message)
            handle_message(data)

def handle_message(data):
    payload = data.get('data')
    if payload == '{"alarm":"on"}':
        GPIO.output(LED_PIN, GPIO.HIGH)
        print("LED turned on")
    elif payload == '{"alarm":"off"}':
        GPIO.output(LED_PIN, GPIO.LOW)
        print("LED turned off")


# Start de WebSocket-client in een aparte thread
# Start de WebSocket-client in een aparte thread
def start_websocket_client():
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    loop.run_until_complete(listen_to_server(WEBSOCKET_URI))

# Start de WebSocket-client
websocket_thread = threading.Thread(target=start_websocket_client)
websocket_thread.start()

# Sensor en TensorFlow Lite setup
sensor = Adafruit_DHT.DHT22

pin = 4
interpreter = tflite.Interpreter(model_path="./model_training/anomaly_detection_model.tflite")
interpreter.allocate_tensors()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

previous_temperature = None
margin = 0.5  # Temperatuur veranderingsmarge

TEMPRATURE_URL = "http://eventiz-laravel.lukasolivier.be/api/sensors/temperature"
HUMIDITY_URL = "http://eventiz-laravel.lukasolivier.be/api/sensors/humidity"
AIR_URL = "http://eventiz-laravel.lukasolivier.be/api/sensors/air_quality"

def read_adc(channel):
    adc = spi.xfer2([1, (8 + channel) << 4, 0])
    data = ((adc[1] & 3) << 8) + adc[2]
    return data

def prepare_data(temperature, humidity):
    # Zet de data om naar float32
    return np.array([[temperature, humidity]], dtype=np.float32)

def read_air_quality():
    value = read_adc(0)  
    return value

def is_anomaly(prediction):
    return prediction[0][0] > 0.5

def send_to_api(temperature, humidity, air_quality):
    TempData = {
        'value': temperature,
        'key': "secret"
        }
    HumData = {
        'value': humidity,
        'key': "secret"
        }
    AirData = {
	    'value': air_quality,
	    'key': "secret"
}

    print(TempData, HumData, AirData)

    try:
        responseTemp = requests.patch(TEMPRATURE_URL, data=TempData)
        responseHum = requests.patch(HUMIDITY_URL, data=HumData)
        responseAir = requests.patch(AIR_URL, data=AirData)
        print(responseTemp, responseHum, responseAir)
        return responseTemp, responseHum, responseAir
    except requests.exceptions.ConnectionError as e:
        print(f"Er is een fout opgetreden bij het verbinden met de server: {e}")
        # Retourneer None of een default response als er een fout optreedt
        return None, None


# Hoofdlus voor sensoraflezing en modelinferentie
try:
    while True:
        humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)
        air_quality = read_air_quality()
        if humidity is not None and temperature is not None:
            print(f'Temperature: {temperature:.2f}°C, Humidity: {humidity:.2f}%, air_quality: {air_quality:.2f}ppm')
            input_data = prepare_data(temperature, humidity)
            interpreter.set_tensor(input_details[0]['index'], input_data)
            interpreter.invoke()
            prediction = interpreter.get_tensor(output_details[0]['index'])

            if is_anomaly(prediction):
                print("Anomaly detected!")

            if previous_temperature is None or abs(temperature - previous_temperature) >= margin:
                response = send_to_api(temperature, humidity, air_quality)
                if response[0] is not None:
                    print(f'Data sent to API. Response: {response[0].status_code}')
                else:
                    print('Failed to send data to API')
                previous_temperature = temperature
            else:
                print('Temperature has not changed enough to send data to API')

        else:
            print('Failed to retrieve data from the sensor. Check your connections.')

except KeyboardInterrupt:
    running = False  # Stop de WebSocket-client thread
    websocket_thread.join()  # Wacht tot de thread is afgesloten
    GPIO.cleanup()
    print("Programma netjes gestopt")
