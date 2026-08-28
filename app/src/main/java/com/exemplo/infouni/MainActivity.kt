package com.exemplo.infouni

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    // Referências para a lista e o adapter
    private lateinit var adapter: UniversidadeAdapter
    private var listaCompleta = mutableListOf<Universidade>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        carregarDadosDoJson()

        // Recycler view
        val rvUniversidades = findViewById<RecyclerView>(R.id.rvUniversidades)
        rvUniversidades.layoutManager = LinearLayoutManager(this)
        adapter = UniversidadeAdapter(listaCompleta)
        rvUniversidades.adapter = adapter

        val editBusca = findViewById<EditText>(R.id.editBusca)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)

        btnBuscar.setOnClickListener {
            val termoBusca = editBusca.text.toString().trim().lowercase()
            filtrarUniversidades(termoBusca)
        }

        // Botao "Sair": Redireciona para o Login
        val btnSair = findViewById<Button>(R.id.btnSair)
        btnSair.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Botao da pesquisa por IA
        val btnIrParaIA = findViewById<Button>(R.id.btnPesquisaWeb)
        btnIrParaIA.setOnClickListener {
            startActivity(Intent(this, PesquisaIAActivity::class.java))
        }
    }

    private fun carregarDadosDoJson() {
        try {
            // Abre o arquivo json
            val jsonString = assets.open("universidades.json").bufferedReader().use { it.readText() }

            // Converte o texto em um Array Json
            val jsonArray = JSONArray(jsonString)

            listaCompleta.clear()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val uni = Universidade(
                    obj.getString("nome"),
                    obj.getString("cidade"),
                    obj.getString("infraestrutura"),
                    obj.getString("cursos"),
                    obj.getString("foto")
                )
                listaCompleta.add(uni)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao carregar dados locais: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filtrarUniversidades(termo: String) {
        val listaFiltrada = listaCompleta.filter {
            it.nome.lowercase().contains(termo) || it.cursos.lowercase().contains(termo)
        }

        if (listaFiltrada.isEmpty()) {
            Toast.makeText(this, "Nenhuma faculdade encontrada no JSON.", Toast.LENGTH_SHORT).show()
        }

        adapter.filtrarLista(listaFiltrada)
    }
}