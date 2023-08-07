package com.kursatmemis.burc_rehberi_app

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.android.material.snackbar.Snackbar
import com.kursatmemis.burc_rehberi_app.databinding.ActivitySplashBinding
import com.kursatmemis.burc_rehberi_app.models.Horoscope

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val previewsOfHoroscopes = ArrayList<Horoscope>()
    private var isDataReceived = false
    private var isTimeOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.rootElement)

        startAnimation()

        if (checkInternetConnection()) {
            startTimer()
            getDataFromUrl()
        } else {
            val snackbar = Snackbar.make(
                binding.rootElement,
                getString(R.string.explain_internet_connection),
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.show()
        }

    }

    /**
     * startTimer() - getDataFromUrl() Methodları Çalışma Mantığı:
     * Uygulama en az 2 saniye boyunca splash ekranında kalır. 2 saniye bittiğinde eğer veriler
     * internet sitesinden henüz alınmamışsa uygulama splash activity'de kalmaya devam eder ve
     * veriler internet sitesinden geldiğinde MainActivity'e geçiş yapar. Eğer veriler 2 saniye
     * bittiğinde internet sitesinden alınmış ise uygulama MainActivity'e geçiş yapar.
     */
    private fun startTimer() {
        val countDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Bir şey yapmaya gerek yok.
            }

            override fun onFinish() {
                isTimeOver = true
                if (isDataReceived)
                    goToMainActivity()
            }
        }
        countDownTimer.start()
    }

    private fun getDataFromUrl() {
        val result = Result()
        val run = Runnable {
            val data = result.horoscopePreview()
            previewsOfHoroscopes.addAll(data)
            isDataReceived = true
            if (isTimeOver)
                goToMainActivity()
        }
        Thread(run).start()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putParcelableArrayListExtra("previewsOfHoroscopes", previewsOfHoroscopes)
        startActivity(intent)
        finish()
    }

    private fun startAnimation() {
        val rotateAnimation =
            ObjectAnimator.ofFloat(binding.horoscopesSplashImageView, "rotation", 0f, 360f)
        rotateAnimation.duration = 9000
        rotateAnimation.repeatCount = ObjectAnimator.INFINITE
        rotateAnimation.repeatMode = ObjectAnimator.REVERSE
        rotateAnimation.start()
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}