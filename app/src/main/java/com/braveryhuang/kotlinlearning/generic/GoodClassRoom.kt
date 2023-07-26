package com.braveryhuang.kotlinlearning.generic

import android.util.Log
import java.lang.reflect.ParameterizedType

class GoodClassRoom : ClassRoom<GoodStudent, GoodTeacher>() {
    fun getClassInfo() {
        val modelClass: Class<*>
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            modelClass = type.actualTypeArguments[1] as Class<*>
        } else {
            // 如果没有指定泛型参数，则默认使用BaseViewModel
            modelClass = Teacher::class.java
        }

        Log.i(TAG, "modelClass=$modelClass")

        //viewModel = createViewModel(this, modelClass) as VM
    }

    companion object {
        private const val TAG = "GoodClassRoom"
    }
}