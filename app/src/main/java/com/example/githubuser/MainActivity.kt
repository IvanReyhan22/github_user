package com.example.githubuser

<<<<<<< Updated upstream
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.HomeActivity
=======
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.HomeActivity
import com.example.githubuser.ui.model.SettingViewModel
import com.example.githubuser.ui.model.SettingViewModelFactory
import com.example.githubuser.utils.SettingPreferences
>>>>>>> Stashed changes

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
<<<<<<< Updated upstream
=======
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
>>>>>>> Stashed changes

    companion object {
        const val SPLASH_TIME_OUT = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< Updated upstream
        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }, SPLASH_TIME_OUT)

    }
=======
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            controlTheme(isDarkModeActive)
        }

        supportActionBar?.hide()

        handler.postDelayed({
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun controlTheme(darkModeActive: Boolean) {
        if(darkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.githubLogo.setImageResource(R.drawable.github_mark_white)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.githubLogo.setImageResource(R.drawable.github_mark)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

>>>>>>> Stashed changes
}