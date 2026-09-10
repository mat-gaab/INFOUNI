package com.exemplo.infouni

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.ai
import kotlinx.coroutines.launch

class PesquisaIAActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesquisa_ia)

        // Vinculação dos componentes
        val editBusca = findViewById<EditText>(R.id.editBuscaIA)
        val btnIA = findViewById<Button>(R.id.btnPesquisarIA)
        val progress = findViewById<ProgressBar>(R.id.progressIA)
        val txtResultado = findViewById<TextView>(R.id.txtResultadoIA)

        // Clicar no Botão de Pesquisa
        btnIA.setOnClickListener {
            val universidadeDigitada = editBusca.text.toString().trim()

            // Validação de campo vazio
            if (universidadeDigitada.isEmpty()) {
                Toast.makeText(this, "Por favor, digite o nome de uma universidade", Toast.LENGTH_SHORT).show()
            } else {
                // Inicia a busca com IA
                executarPesquisaGemini(universidadeDigitada, progress, txtResultado)
            }
        }
    }

    private fun executarPesquisaGemini(nomeUni: String, pb: ProgressBar, resultado: TextView) {
        pb.visibility = View.VISIBLE
        resultado.text = "A IA está buscando informações na web..."

        // Configuração do Modelo gemini
        val modelo = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-3.6-flash")
        // Prompt
        val prompt = """
            Você é um assistente acadêmico brasileiro. 
            Pesquise sobre a universidade: $nomeUni.
            Retorne as informações EXATAMENTE neste formato de tópicos, sem textos extras:
            NOME: (nome completo da instituição)
            
            CIDADE: (cidade e estado)
            
            INFRAESTRUTURA: (resumo da qualidade de laboratórios e área da faculdade)
            
            CURSOS: (todos os cursos disponíveis e duração média)
            
            FOTO: (escreva apenas 'default_uni')
        """.trimIndent()

        // Corrotina
        lifecycleScope.launch {
            try {
                val response = modelo.generateContent(prompt)
                val textoResposta = response.text

                if (!textoResposta.isNullOrEmpty()) {
                    resultado.text = textoResposta // Exibe na tela

                    val objetoUni = extrairObjetoDaIA(textoResposta)
                    if (objetoUni != null) {
                        salvarNovaUniversidade(objetoUni)
                        android.widget.Toast.makeText(this@PesquisaIAActivity, "Salvo na lista local!", android.widget.Toast.LENGTH_SHORT).show()
                    }

                } else {
                    resultado.text = "A IA não encontrou dados sobre esta instituição."
                }
            } catch (e: Exception) {
                resultado.text = "Erro ao conectar com a IA: ${e.message}"
            } finally {
                pb.visibility = View.GONE
            }
        }

    }

    private fun extrairObjetoDaIA(textoIA: String): Universidade? {
        return try {
            val linhas = textoIA.lines()
            var nome = "Não informado"
            var cidade = "Não informada"
            var infra = "Não informada"
            var cursos = "Não informados"

            for (linha in linhas) {
                when {
                    linha.startsWith("NOME:", true) -> nome = linha.substringAfter(":").trim()
                    linha.startsWith("CIDADE:", true) -> cidade = linha.substringAfter(":").trim()
                    linha.startsWith("INFRAESTRUTURA:", true) -> infra = linha.substringAfter(":").trim()
                    linha.startsWith("CURSOS:", true) -> cursos = linha.substringAfter(":").trim()
                }
            }
            // Foto padrão para universidades novas pesquisadas pela web
            Universidade(nome, cidade, infra, cursos, "default_uni")
        } catch (e: Exception) { null }
    }

    private fun salvarNovaUniversidade(novaUni: Universidade) {
        try {
            val arquivo = java.io.File(filesDir, "universidades_dinamico.json")

            // Garante que o arquivo exista
            if (!arquivo.exists()) {
                val jsonInicial = assets.open("universidades.json").bufferedReader().use { it.readText() }
                arquivo.writeText(jsonInicial)
            }

            val jsonString = arquivo.readText()
            val jsonArray = org.json.JSONArray(jsonString)

            // Cria o novo objeto JSON
            val novoObj = org.json.JSONObject().apply {
                put("nome", novaUni.nome)
                put("cidade", novaUni.cidade)
                put("infraestrutura", novaUni.infraestrutura)
                put("cursos", novaUni.cursos)
                put("foto", novaUni.foto)
            }

            jsonArray.put(novoObj) // Adiciona à lista
            arquivo.writeText(jsonArray.toString(4)) // Salva com recuo de 4 espaços (identado)
        } catch (e: Exception) {
            // Erro silencioso ou log para debug
        }
    }

}