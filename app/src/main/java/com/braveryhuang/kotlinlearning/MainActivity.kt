package com.braveryhuang.kotlinlearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread {
            testFileOperation()
            val file = "test_file"
            readAssetFile(file)
        }.start()
    }

    private fun testFileOperation() {

        try {
            val output = openFileOutput("test", MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write("test")
                it.write("test2")
                it.write("test3")
                it.write("test4")
            }

            val input = openFileInput("test")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use { bufferReader ->
                var readLine = ""
                while (bufferReader.readLine()?.also { readLine = it } != null) {
                    Log.i(TAG, readLine + "\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 完成文件拷贝，将Asset目录下的文件拷贝至应用目录下，
     * /data/data/package name/xxx
     */
    private fun readAssetFile(assetFile: String) {
        val bufferSize = 1024 * 4
        val bufferArray = ByteArray(bufferSize)

        try {
            val output = openFileOutput(assetFile, MODE_PRIVATE)
            val bufferedOutput = BufferedOutputStream(output)

            val input = assets.open(assetFile)
            val bufferedInput = BufferedInputStream(input)
            bufferedInput.use { inputIt ->
                bufferedOutput.use { outputIt ->
                    var readCnt: Int
                    while (inputIt.read(bufferArray, 0, bufferSize).also { readCnt = it } != -1) {
                        outputIt.write(bufferArray, 0, readCnt)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.i(TAG, "copy finish")
    }
}