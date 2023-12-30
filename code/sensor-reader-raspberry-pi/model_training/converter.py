import tensorflow as tf

# Load the Keras model
model = tf.keras.models.load_model('anomaly_detection_model.h5')

# Convert the model to the TensorFlow Lite format
converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save the TensorFlow Lite model to disk
with open('anomaly_detection_model.tflite', 'wb') as f:
    f.write(tflite_model)
