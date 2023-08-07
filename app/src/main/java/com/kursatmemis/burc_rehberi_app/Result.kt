package com.kursatmemis.burc_rehberi_app

import android.util.Log
import com.kursatmemis.burc_rehberi_app.models.Horoscope
import org.jsoup.Jsoup

class Result {

    fun horoscopePreview(): MutableList<Horoscope> {
        val horoscopes = mutableListOf<Horoscope>()
        val url = "https://www.elle.com.tr/astroloji/burc-ozellikleri"
        val doc = Jsoup.connect(url).timeout(15000).get()
        val containerDivs = doc.select("div.horoscope-feed--story-container")

        for (containerDiv in containerDivs) {
            val imgDiv = containerDiv.select("div.horoscope-feed--story-image")
            val contentDiv = containerDiv.select("div.horoscope-feed--story-content")

            val detailLink = imgDiv.select("a").attr("abs:href")
            val imgLink = imgDiv.select("a").select("img").attr("src")
            val name = contentDiv.select("h2").text()
            val date = contentDiv.select("h3").select("p").text()

            val horoscope = Horoscope(detailLink, imgLink, name, date)
            horoscopes.add(horoscope)
        }
        return horoscopes
    }

    fun horoscopeDetail(url: String): MutableMap<String, String> {
        val titleAndDescriptions = mutableMapOf<String, String>()
        val doc = Jsoup.connect(url).timeout(60000).get()
        val containerDiv = doc.select("div[class=standard-article-body--text]")
        val elements = containerDiv.select("div[class=body-el-text standard-body-el-text]")
            .select("p:not([href])")
        for (element in elements) {
            val data = element.text()
            if (data.isEmpty() || data.uppercase().contains("UYUMU")) {
                continue
            }
            Log.w("mKm - data", data)
            val title = data.substring(0, data.indexOf(":")).trim()
            val desc = data.substring(data.indexOf(":")+1).trim()
            titleAndDescriptions[title] = desc
        }
        return titleAndDescriptions
    }
}