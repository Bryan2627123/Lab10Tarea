package com.example.lab10_tarea.data

data class RegistroFuncionamiento(
    val id: Int,
    val electrodomestico: String,
    val temperatura: Float,
    val fecha: String,
    val desconectado: Boolean
)

