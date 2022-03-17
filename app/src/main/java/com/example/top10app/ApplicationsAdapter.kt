package com.example.top10app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ApplicationsAdapter(context: Context, feedEntries: ArrayList<FeedEntry>): RecyclerView.Adapter<ApplicationsAdapter.ViewHolder>() {
    private var localContext: Context? = null
    private var localFeedEntries: ArrayList<FeedEntry>? = null

    init {
        localContext = context
        localFeedEntries = feedEntries
    }

    //Inflar el layout
    //Infla una vista dentro de este mainActivity
    //Se busca inflar n cantdiad de vistas necesarias
    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ApplicationsAdapter.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(localContext)
        val view: View = layoutInflater.inflate(R.layout.cards, parent, false)
        return ViewHolder(view)
    }

    //Asignar valores a las filas cuando son reintroducidas a la oantalla basadas en la posicion que tienen en la visya
    //Reciclar las cards
    //Se recibe la referencia a una vista que ya no se ve en pantalla
    override fun onBindViewHolder(holder: ApplicationsAdapter.ViewHolder, position: Int) {
        //Creeme 100% que este objeto no es nulo -> !!, ignora que te dije que es nuleable
        val currentFeedEntry: FeedEntry = localFeedEntries!![position]
        holder.textArtist.text = currentFeedEntry.artist
        holder.textSummary.text = currentFeedEntry.summary.take(800).plus("...")
        holder.textName.text = currentFeedEntry.name

    }

    override fun getItemCount(): Int {
        //Si existe hazlo, si no, devuelve 0 como tamaño
        return localFeedEntries?.size ?:0
    }

    //Declaramos esta clase. Queremos recibir una vista
    //Extendemos la clase ViewHolder
    //La vista v que recibimos mandala como par[ametro a la clase que estamos extendiendo
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {

        val textName: TextView = v.findViewById(R.id.textViewName)
        val textArtist: TextView = v.findViewById(R.id.textViewArtist)
        val textSummary: TextView = v.findViewById(R.id.textViewSummary)

    }
}