package edu.ap.publictoiletfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beust.klaxon.Parser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun parse(name: String): Any? {
        val cls = Parser::class.java
        return cls.getResourceAsStream(name)?.let { inputStream -> return Parser.default().parse(inputStream) }
    }
}