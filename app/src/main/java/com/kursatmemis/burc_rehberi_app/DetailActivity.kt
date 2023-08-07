package com.kursatmemis.burc_rehberi_app

import android.R
import android.content.Intent

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.kursatmemis.burc_rehberi_app.databinding.ActivityDetailBinding
import com.kursatmemis.burc_rehberi_app.models.Horoscope
import android.graphics.Bitmap
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val horoscope = getParcelableExtra(intent, "horoscope", Horoscope::class.java)
        val result = Result()
        val run = Runnable {
            val titlesAndDescriptions = result.horoscopeDetail(horoscope!!.detailLink)
            val formattedTitlesAndDescritions = formatTitlesAndDescriptions(titlesAndDescriptions)
            runOnUiThread {
                binding.horoscopeDetailTextView.text = formattedTitlesAndDescritions
            }
        }
        Thread(run).start()



        setSupportActionBar(binding.animToolbar)
        binding.collapsingToolbar.title = horoscope?.name

        loadImageAsBitmap(horoscope?.imgLink!!) { bitmap ->
            if (bitmap != null) {
                // Bitmap başarıyla elde edildi, işlemlerinizi yapabilirsiniz
                Palette.from(bitmap).generate { palette ->
                    val vibrantColor = palette!!.getDominantColor(R.attr.colorPrimary)
                    binding.collapsingToolbar.setContentScrimColor(vibrantColor)
                    window.statusBarColor = vibrantColor
                }
            } else {
                // Bitmap yüklenemedi, hata durumunu işleyebilirsiniz
            }
        }


    }

    // Title'ları bold tipine çevirir.
    private fun formatTitlesAndDescriptions(titlesAndDescriptions: MutableMap<String, String>): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder()

        for ((title, description) in titlesAndDescriptions) {
            val titleText = "$title: "
            val descriptionText = "$description\n\n"

            // Başlık metnini bold yapmak için SpannableString oluşturun
            val titleSpannable = SpannableString(titleText)
            titleSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, titleText.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Oluşturulan SpannableString'leri SpannableStringBuilder'a ekleyin
            spannableStringBuilder.append(titleSpannable)
            spannableStringBuilder.append(descriptionText)
            Log.w("mKm-xx", spannableStringBuilder.toString())
        }

        return spannableStringBuilder
    }

    private fun loadImageAsBitmap(url: String, callback: (Bitmap?) -> Unit) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Fotoğraf başarıyla yüklendiğinde bitmap olarak elde edin
                    binding.header.setImageBitmap(resource)
                    callback(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    // Yükleme başarısız olduğunda callback ile null değeri döndürün
                    callback(null)
                }
            })
    }

    private fun <T : Parcelable?> getParcelableExtra(
        intent: Intent,
        key: String,
        m_class: Class<T>
    ): T? {
        if (Build.VERSION.SDK_INT >= 33) {
            return intent.getParcelableExtra(key, m_class)
        } else {
            @Suppress("DEPRECATION") return intent.getParcelableExtra(key)
        }
    }

}
