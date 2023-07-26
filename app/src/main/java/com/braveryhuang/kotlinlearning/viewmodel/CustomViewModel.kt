package com.braveryhuang.kotlinlearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CustomViewModel(application: Application, val repository: Repository) : AndroidViewModel(application) {

    fun test() {

    }
}