package com.braveryhuang.kotlinlearning

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.braveryhuang.kotlinlearning.data.LoginData
import com.braveryhuang.kotlinlearning.databinding.ActivityMainBinding
import com.braveryhuang.kotlinlearning.generic.GoodClassRoom
import com.braveryhuang.kotlinlearning.lifecycle.MyLifeCycleObserver
import com.braveryhuang.kotlinlearning.viewmodel.CustomViewModel
import com.braveryhuang.kotlinlearning.viewmodel.CustomViewModelFactory
import com.braveryhuang.kotlinlearning.viewmodel.Repository
import com.permissionx.guolindev.PermissionX
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.reflect.ParameterizedType

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = "MainActivity"
        const val CHANNEL_ID = "test"

        const val NOTIFICATION_ID_1 = 1
    }

    private lateinit var binding: ActivityMainBinding
    private var loginData = LoginData("test", 1)

    private val myLifeCycleObserver = MyLifeCycleObserver()

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name_1"//getString(R.string.channel_name)
            val descriptionText = "channel_desc_1"//getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = loginData
        binding.btnPopNotification.setOnClickListener {
            loginData.name = "testbinding"
        }

        //测试 泛型子类获取泛型参数真实类型
        //用途，见于Activity基类中，获取Viewmodel的真实类型，从而实例化viewmodel
        val g = GoodClassRoom()
        g.getClassInfo()

        //自定义viewModel
        val repository = Repository()
        val viewModelFactory = CustomViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[CustomViewModel::class.java]

        //lifecycle使用
        lifecycle.addObserver(myLifeCycleObserver)

        /*binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)*/

        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                /*Manifest.permission.READ_EXTERNAL_STORAGE*/
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                val message = "PermissionX需要您同意以下权限才能正常使用"
                val dialog = CustomDialog(this, message, deniedList)
                scope.showRequestReasonDialog(dialog)

                /*scope.showRequestReasonDialog(
                    deniedList,
                    "即将申请的权限是程序必须依赖的权限",
                    "我已明白",
                    "取消"
                )*/
            }
            .onForwardToSettings { scope, deniedList ->
                val message = "您需要去设置中手动开启以下权限"
                val dialog = CustomDialog(this, message, deniedList)
                scope.showForwardToSettingsDialog(dialog)
                /*scope.showForwardToSettingsDialog(
                        deniedList,
                        "您需要去应用程序设置当中手动开启权限",
                        "我已明白",
                        "取消"
                    )*/
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                    /*binding.btnPopNotification.setOnClickListener {
                        Log.i(TAG, "btnNotification clicked")
                        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("测试通知标题")
                            .setContentText("通知内容===四大分卫二水电费水电费")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        createNotificationChannel()

                        NotificationManagerCompat.from(this).apply {
                            notify(NOTIFICATION_ID_1, builder.build())
                        }
                    }*/

                } else {
                    Toast.makeText(
                        this,
                        "以下权限被拒绝: $deniedList, 应用即将退出",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }

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

    /*private fun testClass() {
        val modelClass: Class<*>
        val type = javaClass.genericSuperclass
        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<*>
        } else {
            // 如果没有指定泛型参数，则默认使用BaseViewModel
            BaseViewModel::class.java
        }
        viewModel = createViewModel(this, modelClass) as VM
    }*/
}