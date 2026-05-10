package com.alejandromartinez.sistemagestionrecursos.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alejandromartinez.sistemagestionrecursos.EditarRecursoActivity
import com.alejandromartinez.sistemagestionrecursos.R
import com.alejandromartinez.sistemagestionrecursos.model.Recurso

class RecursoAdapter(
    private val listaRecursos: List<Recurso>,
    private val rol: String,
    private val onEliminarClick: (Recurso) -> Unit,
    private val onRatingChange: (Recurso, Float) -> Unit,
    private val onFavoritoClick: (Recurso) -> Unit
) : RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val txtTipo: TextView = itemView.findViewById(R.id.txtTipo)
        val txtRating: TextView = itemView.findViewById(R.id.txtRating)

        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
        val btnFavorito: Button = itemView.findViewById(R.id.btnFavorito)

        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {

        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recurso, parent, false)

        return RecursoViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {

        val recurso = listaRecursos[position]

        holder.txtTitulo.text = recurso.titulo
        holder.txtDescripcion.text = recurso.descripcion
        holder.txtTipo.text = recurso.tipo

        // =========================
        // ⭐ RATING (1–10 LÓGICO, 1–5 VISUAL)
        // =========================

        val rating10 = recurso.rating.coerceIn(0f, 10f)

        val ratingVisual = (rating10 / 2f).coerceIn(0f, 5f)

        holder.txtRating.text = "⭐ ${"%.1f".format(rating10)}/10"

        // IMPORTANTE: evitar callback fantasma
        holder.ratingBar.setOnRatingBarChangeListener(null)
        holder.ratingBar.rating = ratingVisual

        // =========================
        // ROLES
        // =========================

        if (rol == "docente") {

            holder.btnEliminar.visibility = View.VISIBLE
            holder.btnFavorito.visibility = View.GONE

        } else {

            holder.btnEliminar.visibility = View.GONE
            holder.btnFavorito.visibility = View.VISIBLE
        }

        // =========================
        // CLICK DOCENTE (EDITAR)
        // =========================

        holder.itemView.setOnClickListener {
            if (rol == "docente") {

                val intent = Intent(
                    holder.itemView.context,
                    EditarRecursoActivity::class.java
                )

                intent.putExtra("ID", recurso.id)
                intent.putExtra("TITULO", recurso.titulo)
                intent.putExtra("DESCRIPCION", recurso.descripcion)
                intent.putExtra("TIPO", recurso.tipo)

                holder.itemView.context.startActivity(intent)
            }
        }

        // =========================
        // ELIMINAR
        // =========================

        holder.btnEliminar.setOnClickListener {
            onEliminarClick(recurso)
        }

        // =========================
        // FAVORITOS
        // =========================

        holder.btnFavorito.setOnClickListener {

            val prefs = holder.itemView.context.getSharedPreferences(
                "SesionUsuario",
                android.content.Context.MODE_PRIVATE
            )

            val userId = prefs.getString("id", "") ?: ""
            val key = "favoritos_$userId"

            val favoritos = prefs.getStringSet(key, mutableSetOf())?.toMutableSet()
                ?: mutableSetOf()

            val recursoId = recurso.id.toString()

            if (favoritos.contains(recursoId)) {
                favoritos.remove(recursoId)
                Toast.makeText(holder.itemView.context, "Quitado de favoritos", Toast.LENGTH_SHORT).show()
            } else {
                favoritos.add(recursoId)
                Toast.makeText(holder.itemView.context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
            }

            prefs.edit().putStringSet(key, favoritos).apply()

            onFavoritoClick(recurso)
        }

        // =========================
        // ⭐ RATING CHANGE (1–10)
        // =========================

        holder.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->

            if (!fromUser) return@setOnRatingBarChangeListener

            val rating10 = (rating * 2f).coerceIn(1f, 10f)

            holder.txtRating.text = "⭐ ${"%.1f".format(rating10)}/10"

            onRatingChange(recurso, rating10)

            Toast.makeText(
                holder.itemView.context,
                "Calificaste con $rating10 / 10",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = listaRecursos.size
}