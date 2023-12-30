# Raspberry Pi Environmental Monitoring System

![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![Raspberry Pi](https://img.shields.io/badge/Raspberry_Pi-A22846?style=for-the-badge&logo=raspberry-pi&logoColor=white)

## Overview
The Raspberry Pi Environmental Monitoring System is a robust application designed to monitor temperature, humidity, and air quality using connected sensors. It sends data to a server, ensuring efficient management of environmental parameters. A key feature includes an LED alert system for critical conditions. The application leverages TensorFlow to validate sensor functionality and performs on-device data processing to minimize server load.

## Deployment

### Initial Setup

1. Clone the repository.
2. Activate the virtual environment:
   ```shell
   source myenv/bin/activate
    ```
3. Install dependencies:
    ```shell
    pip install Adafruit_DHT tflite_runtime requests asyncio websockets RPi.GPIO threading json numpy spidev
    ```
### Running the Application

- To start the monitoring system:
  ```shell
  python program.py
  ```
## Features

- Real-time temperature and humidity monitoring.
- Air quality assessment with sensor integration.
- LED alerts for critical environmental changes.
- TensorFlow integration for sensor health checks.
- Optimized data processing for reduced server requests.

## Known Bugs

- None known.

## Contributors

Designed and developed by a dedicated team:

- [Lukas Olivier](https://www.linkedin.com/in/lukas-olivier/)
- [Niels Soete](https://www.linkedin.com/in/niels-soete/)
- [Lucas Guillemyn](https://www.linkedin.com/in/lucas-guillemyn-2b060b291/)
