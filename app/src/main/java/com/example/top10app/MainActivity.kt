package com.example.top10app

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

//name artist releaseDate sumary imageUrl

class FeedEntry {
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var summary: String = ""
    var imageUrl: String = ""

    override fun toString(): String {
        return """"
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageUrl = $imageUrl
            """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.xmlRecyclerView)

        Log.d(TAG, "onCreate")

        val downloadData = DownloadData(this, recyclerView)
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate DONE")
    }

    //Companion - Independiente de main activity
    companion object {
        //Parametros:no c cual era la primera, promesa, tipo de resultado que se espera
        //AsuncTask
        private class DownloadData(context: Context, recyclerView: RecyclerView) : AsyncTask<String, Void, String>(){
            private val TAG = "DownloadData"

            var localContext: Context by Delegates.notNull()
            var localRecyclerView: RecyclerView by Delegates.notNull()

            init {
                localContext = context
                localRecyclerView = recyclerView
            }

            //doInBackground: no actues en el hilo principal. Levanta un segundo hilo de ejecución
            //y manda ahí las tareas que se encuentran aquí. Su resultado se regresa a onPostExecute como un string
            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground")
                val resFeed = downloadXML(url[0])
                if(resFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: failed")
                }
                Log.d(TAG, resFeed)
                return resFeed
            }

            //Después de lo que hace doInBackground
            //Revisar el String del parámetro
            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute")
                super.onPostExecute(result)
                val parsedApplication = ParseApplication()
                parsedApplication.parse(result)

                //Crea el adaptador
                val adapter: ApplicationsAdapter = ApplicationsAdapter(localContext, parsedApplication.applications)
                localRecyclerView.adapter = adapter
                localRecyclerView.layoutManager = LinearLayoutManager(localContext)
//                for (app in parsedApplication.applications) {
//                    Log.d(TAG, app.toString())
//                }
            }

            //Manda un URL
            private fun downloadXML(urlPath: String?): String {
                //El manejo de errores se tiene que implementar de forma separada
                return URL(urlPath).readText()
            }
        }
    }
}