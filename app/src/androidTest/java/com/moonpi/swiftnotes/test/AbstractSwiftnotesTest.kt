package com.moonpi.swiftnotes.test

import android.util.Log
import org.junit.Before
import com.moonpi.swiftnotes.util.targetContext

abstract class AbstractSwiftnotesTest {

    @Before
    fun setup() {
        clearCache()
        clearAppFiles()
    }

    private fun clearCache() {
        targetContext.applicationContext.cacheDir.deleteRecursively()
        Log.d("Setup", "Cache has been cleared")
    }

    private fun clearAppFiles() {
        targetContext.applicationContext.filesDir.deleteRecursively()
        Log.d("Setup", "App files have been cleared")
    }
}
