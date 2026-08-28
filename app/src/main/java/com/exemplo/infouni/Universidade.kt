package com.exemplo.infouni

import java.io.Serializable

data class Universidade(
    val nome: String,
    val cidade: String,
    val infraestrutura: String,
    val cursos: String,
    val foto: String // As imagens não foram adicionadas ainda
) : Serializable