package com.example.customviewanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.customviewanimation.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<StatsView>(R.id.stats_view).data = listOf(
            0.25F,
            0.25F,
            0.25F,
            0.25F
        )
    }
}