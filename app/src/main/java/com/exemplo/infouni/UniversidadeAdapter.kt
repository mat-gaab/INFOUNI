package com.exemplo.infouni

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UniversidadeAdapter(private var lista: List<Universidade>) :
    RecyclerView.Adapter<UniversidadeAdapter.ViewHolder>() {

    private lateinit var userEmail: String
    private lateinit var context: Context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.txtNomeUni)
        val cidade: TextView = view.findViewById(R.id.txtCidadeUni)
        val btnDetalhes: Button = view.findViewById(R.id.btnVerDetalhes)
        val imgUni: ImageView = view.findViewById(R.id.imgUniItem)
        val btnFav: ImageButton = view.findViewById(R.id.btnFavItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_universidade, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uni = lista[position]
        context = holder.itemView.context
        
        // Recupera o usuário logado
        val pref = context.getSharedPreferences("INFOUNI_PREFS", Context.MODE_PRIVATE)
        userEmail = pref.getString("USER_EMAIL", "") ?: ""

        holder.nome.text = uni.nome
        holder.cidade.text = uni.cidade

        // Verifica se é favorita
        val favoritos = pref.getStringSet("FAVS_$userEmail", mutableSetOf()) ?: mutableSetOf()
        val isFav = favoritos.contains(uni.nome)

        if (isFav) {
            holder.btnFav.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.btnFav.setImageResource(R.drawable.ic_favorite_border)
        }

        // Clique no Favorito
        holder.btnFav.setOnClickListener {
            val listaFavs = pref.getStringSet("FAVS_$userEmail", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
            
            if (listaFavs.contains(uni.nome)) {
                listaFavs.remove(uni.nome)
                holder.btnFav.setImageResource(R.drawable.ic_favorite_border)
            } else {
                listaFavs.add(uni.nome)
                holder.btnFav.setImageResource(R.drawable.ic_favorite)
            }
            
            pref.edit().putStringSet("FAVS_$userEmail", listaFavs).apply()
        }

        // Carrega a imagem usando Glide
        Glide.with(holder.itemView.context)
            .load(uni.foto)
            .placeholder(R.drawable.default_uni)
            .error(R.drawable.default_uni)
            .into(holder.imgUni)

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