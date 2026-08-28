package com.exemplo.infouni

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UniversidadeAdapter(private var lista: List<Universidade>) :
    RecyclerView.Adapter<UniversidadeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.txtNomeUni)
        val cidade: TextView = view.findViewById(R.id.txtCidadeUni)
        val btnDetalhes: Button = view.findViewById(R.id.btnVerDetalhes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_universidade, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uni = lista[position]
        holder.nome.text = uni.nome
        holder.cidade.text = uni.cidade

        holder.btnDetalhes.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetalhesActivity::class.java)
            intent.putExtra("UNIVERSIDADE", uni)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = lista.size

    fun filtrarLista(listaFiltrada: List<Universidade>) {
        this.lista = listaFiltrada
        notifyDataSetChanged()
    }
}