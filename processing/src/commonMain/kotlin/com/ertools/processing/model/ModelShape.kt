package com.ertools.processing.model

data class ModelShape(
    val width: Int,
    val height: Int,
    val channels: Int,
    val batch: Int
    ) {
    companion object {
        fun fromShapeArray(shapeArray: IntArray): ModelShape {
            when(shapeArray.size) {
                1 -> return ModelShape(
                    width = 1,
                    height = 1,
                    channels = 1,
                    batch = shapeArray[0]
                )
                2 -> return ModelShape(
                    width = shapeArray[1],
                    height = 1,
                    channels = 1,
                    batch = shapeArray[0]
                )
                3 -> return ModelShape(
                    width = shapeArray[1],
                    height = shapeArray[2],
                    channels = 1,
                    batch = shapeArray[0]
                )
                else -> return ModelShape(
                    width = shapeArray[1],
                    height = shapeArray[2],
                    channels = shapeArray[3],
                    batch = shapeArray[0]
                )
            }
        }
    }

}