package com.exemplo.infouni

import java.io.Serializable

data class Universidade(
    val nome: String,
    val cidade: String,
    val infraestrutura: String,
    val cursos: String,
    val foto: String // Conterá uma URL, ex: "https://site.com/foto.jpg"
) : Serializable