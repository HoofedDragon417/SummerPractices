package com.example.summerpractics.models

class TaskDataModel(
    val id: Int,
    val title: String,
    var note: String,
    val duration: Double,
    val priority: Int,
    var completed: Int,
    val timeOfCreation: Long
)