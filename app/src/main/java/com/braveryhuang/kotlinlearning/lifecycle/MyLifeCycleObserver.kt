package com.braveryhuang.kotlinlearning.lifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MyLifeCycleObserver : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                // 在 Activity 创建时执行的逻辑
                Log.i(TAG, "MyLifeCycleObserver Lifecycle.Event.ON_CREATE")
            }
            Lifecycle.Event.ON_RESUME -> {
                // 在 Activity 恢复时执行的逻辑
                Log.i(TAG, "MyLifeCycleObserver Lifecycle.Event.ON_RESUME")
            }
            Lifecycle.Event.ON_PAUSE -> {
                // 在 Activity 暂停时执行的逻辑
                Log.i(TAG, "MyLifeCycleObserver Lifecycle.Event.ON_PAUSE")
            }
            // 其他生命周期事件的处理逻辑
            else -> {}
        }
    }

    companion object {
        private const val TAG = "MyLifeCycleObserver"
    }
}
