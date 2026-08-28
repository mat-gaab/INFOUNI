package com.exemplo.infouni

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = FirebaseDatabase.getInstance().getReference("usuarios")

        val editEmail = findViewById<EditText>(R.id.editEmailLogin)
        val editSenha = findViewById<EditText>(R.id.editSenhaLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtCadastro = findViewById<TextView>(R.id.txtIrParaCadastro)
        val progressBar = findViewById<ProgressBar>(R.id.progressLogin)

        // Botao de login
        btnLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val senha = editSenha.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                realizarLogin(email, senha, progressBar, btnLogin)
            }
        }

        // Botão para fazer cadastro
        txtCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
    }

    private fun realizarLogin(email: String, pass: String, pb: ProgressBar, btn: Button) {
        pb.visibility = View.VISIBLE
        btn.isEnabled = false

        // Consulta o Firebase
        database.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pb.visibility = View.GONE
                    btn.isEnabled = true

                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val usuario = userSnapshot.getValue(Usuario::class.java)
                            if (usuario?.senha == pass) {
                                // Login Sucesso
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish() // Fecha a tela de login
                            } else {
                                Toast.makeText(this@LoginActivity, "Senha incorreta!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Usuário não encontrado!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    pb.visibility = View.GONE
                    btn.isEnabled = true
                    Toast.makeText(this@LoginActivity, "Erro no banco: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}
