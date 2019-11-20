package com.moonpi.swiftnotes.test

import android.util.Log
import org.junit.Before
import com.moonpi.swiftnotes.util.targetContext
import org.junit.BeforeClass
import java.io.File

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

    companion object {
        private const val ALLURE_DIR_PATH = "sdcard/allure-results"

        @JvmStatic
        @BeforeClass
        fun clearAllureReports() {
            File(ALLURE_DIR_PATH).listFiles()?.forEach {
                it.delete()
            }
            Log.d("Setup", "Allure reports have been cleared")
        }
    }
}
