package com.example.top10app

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

//Clase a la que le vamos a pasar un String grande, el cual nos
//va a decir si lo pudo parsear
class ParseApplication {
    private val TAG = "ParseApplication"
    val applications = ArrayList<FeedEntry>()

    fun parse( xmlData: String ): Boolean {
        var status = true
        var tagInEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val pullParser = factory.newPullParser()
            //https://developer.android.com/reference/org/xmlpull/v1/XmlPullParser

            pullParser.setInput(xmlData.reader())
            var eventType = pullParser.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = pullParser.name?.lowercase()
                //Cuando se esté al inicio de un Tag
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for: $tagName")
                        if(tagName == "entry") {
                            tagInEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> {
                        textValue = pullParser.text
                    }
                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for: $tagName")
                        if(tagInEntry) {
                            when(tagName) {
                                "entry" -> {
                                    applications.add(currentRecord)
                                    tagInEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "summary" -> currentRecord.summary = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "image" -> currentRecord.imageUrl = textValue
                            }
                        }
                    }
                }
                //Revisa que primero este en un Start Tag (tag para abrir), luego revisa el contenido y luego el end tag
                eventType = pullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            status = false
        }

        return true
    }
}