package uz.authentication.texyreaderml1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslateActivity : AppCompatActivity() {
    lateinit var edt:EditText
    lateinit var tvResult:TextView
    lateinit var btn:Button
    lateinit var engRusTranslator:Translator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)
        btn = findViewById(R.id.btnTranslate)
        tvResult = findViewById(R.id.resultTrans)
        edt = findViewById(R.id.edtTrans)

        btn.setOnClickListener {
            prepareTranslateModel()
        }

    }

    private fun prepareTranslateModel() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.RUSSIAN)
            .build()
        engRusTranslator = Translation.getClient(options)

        engRusTranslator.downloadModelIfNeeded().addOnSuccessListener {
            transalateText()
        }
            .addOnFailureListener {
                tvResult.text = "${it.message}"
            }

    }

    private fun transalateText() {
        engRusTranslator.translate(edt.text.toString()).addOnSuccessListener {
            tvResult.text = it
        }
    }
}