package nb_.mbti_talk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import nb_.mbti_talk.databinding.ActivitySplashBinding

private lateinit var binding: ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(R.raw.splashlogo3).into(binding.splashImage)

        Handler().postDelayed({
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
            finish()
        },DURATION)

    }
    companion object {
        private const val DURATION : Long = 3000
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}