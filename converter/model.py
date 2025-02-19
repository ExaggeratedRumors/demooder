"""
Imports
"""
import numpy as np
import tensorflow as tf
from tensorflow.keras import Sequential
from tensorflow.keras.layers import (
    Conv2D, MaxPooling2D, Flatten, Dense, Dropout, Input
)
import tf2onnx

"""
Pathing
"""
MODEL_DIR = f"data/data_models/DL_8c_3d_100e_32b"

"""
Functions
"""


def build_model():
    image_width = 138
    image_height = 128
    num_channels = 1
    num_classes = 6

    c2 = Conv2D(filters=32, kernel_size=(3, 3), activation="relu")

    model = Sequential([
        Input(shape=(image_width, image_height, num_channels)),

        Conv2D(filters=32, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        Conv2D(filters=32, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        MaxPooling2D(pool_size=(2, 2)),

        Conv2D(filters=64, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        Conv2D(filters=64, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        MaxPooling2D(pool_size=(2, 2)),

        Conv2D(filters=128, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        Conv2D(filters=128, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        MaxPooling2D(pool_size=(2, 2)),

        Conv2D(filters=256, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        Conv2D(filters=256, kernel_size=(3, 3), activation="relu", strides=(1, 1), padding="same"),
        MaxPooling2D(pool_size=(2, 2)),

        Flatten(),

        Dense(units=64, activation="relu"),
        Dropout(rate=0.25),

        Dense(units=64, activation="relu"),
        Dropout(rate=0.25),

        Dense(units=num_classes, activation="softmax")
    ])
    model.compile(
        optimizer="adam",
        loss="categorical_crossentropy",
        metrics=["accuracy"]
    )
    model.summary()
    return model


def load_weights_from_txt(model, model_dir):
    variable_file = f"{model_dir}/variableNames.txt"
    with open(variable_file, "r") as f:
        variable_names = [line.strip() for line in f]

    weight_layers_count = 0
    for layer in model.layers:
        if isinstance(layer, tf.keras.layers.Dense):
            weight_layers_count += 1
        elif isinstance(layer, tf.keras.layers.Conv2D):
            weight_layers_count += 1
    weight_layers_count = 2 * weight_layers_count

    if len(variable_names) != weight_layers_count:
        raise ValueError(
            f"Number of layers with weights ({weight_layers_count}) in model does not match "
            f"number of variables ({len(variable_names)}) in variableNames.txt"
        )

    i = 0
    for layer in model.layers:
        if str(layer.name).__contains__("dense") or str(layer.name).__contains__("conv"):
            weight_file = f"{model_dir}/{variable_names[i]}.txt"
            weights = np.loadtxt(weight_file)
            biases_file = f"{model_dir}/{variable_names[i + 1]}.txt"
            biases = np.loadtxt(biases_file)
            weights = reshape_weights(layer, weights)
            layer.set_weights([np.array(weights), np.array(biases)])
            i = i + 2

    print("#### Read all weights from txt files")


def reshape_weights(layer, weights_list):
    expected_shape = layer.get_weights()[0].shape
    expected_size = np.prod(expected_shape)

    if len(weights_list) != expected_size:
        raise ValueError(f"#### Incorrect weights length. Received: {len(weights_list)}, "
                         f"expected: {expected_size}). Are you sure you set correct padding for "
                         f"convolution layers?")

    reshaped_weights = np.array(weights_list).reshape(expected_shape)

    return reshaped_weights

def convert_to_onnx(model):
    tf2onnx.convert.from_keras(model, output_path=f"{MODEL_DIR}/model.onnx")
    print(f"#### Model converted to onnx in data_models/{MODEL_DIR}/model.onnx")


"""
Program
"""

if __name__ == "__main__":
    keras_model = build_model()
    load_weights_from_txt(keras_model, MODEL_DIR)
    convert_to_onnx(keras_model)
