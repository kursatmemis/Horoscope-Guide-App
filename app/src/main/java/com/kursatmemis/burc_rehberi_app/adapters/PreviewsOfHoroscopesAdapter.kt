package com.kursatmemis.burc_rehberi_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.kursatmemis.burc_rehberi_app.R
import com.kursatmemis.burc_rehberi_app.models.Horoscope

class PreviewsOfHoroscopesAdapter(context: Context, private val horoscopes: List<Horoscope?>) :
    ArrayAdapter<Horoscope>(context, R.layout.horoscope_item, horoscopes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val horoscopeItemView: View
        val viewHolder: ViewHolder
        val layoutInflater = LayoutInflater.from(context)

        if (convertView == null) {
            horoscopeItemView = layoutInflater.inflate(R.layout.horoscope_item, parent, false)
            val horoscopeImageImageView =
                horoscopeItemView.findViewById<ImageView>(R.id.horoscopeImageImageView)
            val horoscopeNameTextView =
                horoscopeItemView.findViewById<TextView>(R.id.horoscopeNameTextView)
            val horoscopeDateTextView =
                horoscopeItemView.findViewById<TextView>(R.id.horoscopeDateTextView)
            viewHolder =
                ViewHolder(horoscopeImageImageView, horoscopeNameTextView, horoscopeDateTextView)
            horoscopeItemView.tag = viewHolder
        } else {
            horoscopeItemView = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        Glide.with(context).load(horoscopes[position]?.imgLink)
            .into(viewHolder.horoscopeImageImageView)
        viewHolder.horoscopeNameTextView.text = horoscopes[position]?.name
        viewHolder.horoscopeDateTextView.text = horoscopes[position]?.date

        return horoscopeItemView
    }

    data class ViewHolder(
        val horoscopeImageImageView: ImageView,
        val horoscopeNameTextView: TextView,
        val horoscopeDateTextView: TextView
    )
}