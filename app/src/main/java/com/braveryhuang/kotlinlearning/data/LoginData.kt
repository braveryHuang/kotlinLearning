package com.braveryhuang.kotlinlearning.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR


class LoginData(name: String, age: Int) : BaseObservable() {

    @get:Bindable
    var name: String = name
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    val age: Int = age
}