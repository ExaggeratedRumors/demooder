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
import pandas as pd
import csv

"""
Pathing
"""
MODEL_DIR = f"data/data_models/DL_8c_3d_10e_32b"

"""
Functions
"""


def build_model():
    image_width = 283
    image_height = 128
    num_channels = 3
    num_classes = 6

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

def load_next_number(file):
    number = []
    while True:
        char = file.read(1)
        if not char:
            if number:
                yield float("".join(number))
            break
        if char.isspace():
            if number:
                yield float("".join(number))
                number = []
        else:
            number.append(char)


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
            with open(weight_file, "r") as f:
                weights = np.fromiter(load_next_number(f), dtype=np.float32)

            #weights = np.loadtxt(weight_file)
            biases_file = f"{model_dir}/{variable_names[i + 1]}.txt"
            biases = np.loadtxt(biases_file)
            print(f"#### Loading layer {str(layer.name)} weights {len(weights)} and biases {len(biases)}")
            weights = reshape_weights(layer, weights)
            layer.set_weights([np.array(weights), np.array(biases)])
            i = i + 2

    print("#### Read all weights from txt files")


def reshape_weights(layer, weights_list):
    expected_shape = layer.get_weights()[0].shape
    expected_size = np.prod(expected_shape)

    if len(weights_list) != expected_size:
        raise ValueError(f"#### Incorrect weights length for layer {str(layer.name)}. Received: {len(weights_list)}, "
                         f"expected: {expected_size}). Are you sure you set correct padding for "
                         f"convolution layers?")

    reshaped_weights = np.array(weights_list).reshape(expected_shape)

    return reshaped_weights


def convert_to_onnx(model):
    tf2onnx.convert.from_keras(model, output_path=f"{MODEL_DIR}/model.onnx")
    print(f"#### Model converted to onnx in data_models/{MODEL_DIR}/model.onnx")


def load_graph(pb_file):
    with tf.io.gfile.GFile(pb_file, "rb") as f:
        graph_def = tf.compat.v1.GraphDef()
        graph_def.ParseFromString(f.read())

    with tf.compat.v1.Graph().as_default() as graph:
        tf.import_graph_def(graph_def, name="")
    return graph


def extract_layers_from_graph(graph):
    keras_model = Sequential()

    for op in graph.get_operations():
        if "conv2d" in op.name.lower():
            input_shape = op.inputs[0].shape.as_list()
            kernel_shape = op.outputs[0].shape.as_list()

            filters = kernel_shape[-1]
            kernel_size = (3, 3)
            padding = "same" if "SAME" in str(op.get_attr("padding")) else "valid"

            if not keras_model.layers:
                keras_model.add(Input(shape=(input_shape[1], input_shape[2], input_shape[3])))
            keras_model.add(Conv2D(filters=filters, kernel_size=kernel_size, activation="relu",
                                   padding=padding))

        elif "maxpool" in op.name.lower():
            keras_model.add(MaxPooling2D(pool_size=(2, 2)))

        elif "dense" in op.name.lower():
            units = op.outputs[0].shape.as_list()[-1]
            if len(keras_model.layers) > 0 and not isinstance(keras_model.layers[-1], Flatten):
                keras_model.add(Flatten())
            keras_model.add(Dense(units=units, activation="relu"))

    return keras_model


def convert_pb_to_keras(pb_path):
    graph = load_graph(pb_path)
    keras_model = extract_layers_from_graph(graph)
    keras_model.summary()
    return keras_model


"""
Program
"""

if __name__ == "__main__":
    keras_model = build_model()
    load_weights_from_txt(keras_model, MODEL_DIR)
    convert_to_onnx(keras_model)
