package com.ertools.demooder

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.tensorflow.TensorFlow


class TensorFlowTest {
    @Test
    fun `print tensorflow version`() {
        assertEquals("TensorFlow version.","1.15.0", TensorFlow.version())
    }
}