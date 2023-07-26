package com.braveryhuang.kotlinlearning.generic

open class ClassRoom<S: Student, T: Teacher> {
    private lateinit var student: S
    private lateinit var teacher: T
}