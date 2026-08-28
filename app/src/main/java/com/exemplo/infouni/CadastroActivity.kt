package com.exemplo.infouni

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CadastroActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        database = FirebaseDatabase.getInstance().getReference("usuarios")

        // Mapeamento dos componentes
        val editNome = findViewById<EditText>(R.id.editNome)
        val editDataNasc = findViewById<EditText>(R.id.editDataNasc)
        val spinnerGenero = findViewById<Spinner>(R.id.spinnerGenero)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editSenha = findViewById<EditText>(R.id.editSenha)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)
        val progressBar = findViewById<ProgressBar>(R.id.progressCadastro)

        btnCadastrar.setOnClickListener {
            val nome = editNome.text.toString().trim()
            val dataNasc = editDataNasc.text.toString().trim()
            val genero = spinnerGenero.selectedItem.toString()
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()

            // Validação nos campos obrigatórios
            if (nome.isEmpty() || dataNasc.isEmpty() || email.isEmpty() || senha.isEmpty() || genero == "Selecione o Gênero") {
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Barra de carregamento
            progressBar.visibility = View.VISIBLE
            btnCadastrar.isEnabled = false

            salvarUsuarioNoFirebase(nome, dataNasc, genero, email, senha, progressBar, btnCadastrar)
        }
    }

    private fun salvarUsuarioNoFirebase(nome: String, data: String, gen: String, email: String, pass: String, pb: ProgressBar, btn: Button) {
        val id = database.push().key // Gera um ID

        // Objeto Usuario
        val novoUsuario = Usuario(id, nome, data, gen, email, pass)

        if (id != null) {
            database.child(id).setValue(novoUsuario)
                .addOnSuccessListener {
                    pb.visibility = View.GONE
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Volta para a tela de login
                }
                .addOnFailureListener {
                    pb.visibility = View.GONE
                    btn.isEnabled = true
                    Toast.makeText(this, "Erro ao cadastrar: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}

data class Usuario(
    val id: String? = null,
    val nome: String? = null,
    val dataNasc: String? = null,
    val genero: String? = null,
    val email: String? = null,
    val senha: String? = null
)