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
                    resultado.text = textoResposta
                } else {
                    resultado.text = "A IA não encontrou dados sobre esta instituição."
                }
            } catch (e: Exception) {
                // Tratamento de erro (falha de conexão)
                resultado.text = "Erro ao conectar com a IA: ${e.message}"
            } finally {
                pb.visibility = View.GONE
            }
        }
    }
}
