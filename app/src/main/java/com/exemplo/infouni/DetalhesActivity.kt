package com.exemplo.infouni

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetalhesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        // Recebe o objeto universidade da Intent
        val uni = intent.getSerializableExtra("UNIVERSIDADE") as? Universidade

        if (uni != null) {
            val img = findViewById<ImageView>(R.id.imgDetalhe)
            val nome = findViewById<TextView>(R.id.txtNomeDetalhe)
            val cidade = findViewById<TextView>(R.id.txtCidadeDetalhe)
            val infra = findViewById<TextView>(R.id.txtInfraDetalhe)
            val cursos = findViewById<TextView>(R.id.txtCursosDetalhe)
            val btnVoltar = findViewById<Button>(R.id.btnVoltar)

            // Usa o Glide para carregar a URL na ImageView
            val imgFoto = findViewById<ImageView>(R.id.imgFotoUni) // Localiza a imagem no XML
            val urlFoto = uni.foto // Pega o link que está no objeto

            // Comando do Glide para carregar a imagem da internet
            com.bumptech.glide.Glide.with(this)
                .load(urlFoto)
                .placeholder(R.drawable.ic_launcher_foreground) // imagem temporária enquanto baixa
                .error(R.drawable.default_uni)  // imagem caso o link esteja quebrado
                .into(imgFoto)


            nome.text = uni.nome
            cidade.text = uni.cidade
            infra.text = uni.infraestrutura
            cursos.text = uni.cursos

            // Carregar imagem
            val resId = resources.getIdentifier(uni.foto, "drawable", packageName)
            if (resId != 0) {
                img.setImageResource(resId)
            } else {
                // Imagem padrão caso não encontre
                img.setImageResource(R.drawable.ic_launcher_foreground)
            }

            btnVoltar.setOnClickListener {
                finish()
            }
        } else {
            finish() // Fecha se não houver dados
        }
    }
}