package com.kursatmemis.burc_rehberi_app

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.kursatmemis.burc_rehberi_app.adapters.PreviewsOfHoroscopesAdapter
import com.kursatmemis.burc_rehberi_app.databinding.ActivityMainBinding
import com.kursatmemis.burc_rehberi_app.models.Horoscope


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var previewsOfHoroscopesAdapter: PreviewsOfHoroscopesAdapter?  = null
    private var previewsOfHoroscopes: ArrayList<Horoscope?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        previewsOfHoroscopes =
            getParcelableArrayListExtra(intent, "previewsOfHoroscopes", Horoscope::class.java)

        if (previewsOfHoroscopes != null) {
            setAdapter()
        } else {
            showToastMessage("Error!")
        }

        binding.previewsOfHoroscopesListView.setOnItemClickListener { parent, view, position, id ->
            val horoscope = previewsOfHoroscopes?.get(position)
            goToDetailActivity(horoscope)
        }

    }

    private fun goToDetailActivity(horoscope: Horoscope?) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("horoscope", horoscope)
        startActivity(intent)
    }

    private fun setAdapter() {
        previewsOfHoroscopesAdapter =
            PreviewsOfHoroscopesAdapter(this, previewsOfHoroscopes!!.toList())
        binding.previewsOfHoroscopesListView.adapter = previewsOfHoroscopesAdapter
    }

    private fun showToastMessage(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun <T : Parcelable?> getParcelableArrayListExtra(
        intent: Intent,
        key: String,
        m_class: Class<T>
    ): ArrayList<T?>? {
        if (Build.VERSION.SDK_INT >= 33) {
            return intent.getParcelableArrayListExtra(key, m_class)
        } else {
            @Suppress("DEPRECATION") return intent.getParcelableArrayListExtra<T>(key)
        }
    }

}