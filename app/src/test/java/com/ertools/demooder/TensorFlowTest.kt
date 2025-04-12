package com.ertools.demooder

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.tensorflow.lite.TensorFlowLite


class TensorFlowTest {
    @Test
    fun `print tensorflow version`() {
        assertEquals("TensorFlow version.","1.15.0", TensorFlowLite.runtimeVersion())
    }
}