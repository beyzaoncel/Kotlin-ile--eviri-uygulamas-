@file:Suppress("DEPRECATION")

package com.example.languagetranslator

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions


class MainActivity : AppCompatActivity() {
    private lateinit var fromSpinner: Spinner
    private lateinit var sourceEdt: TextInputEditText
    private lateinit var toSpinner: Spinner
    private lateinit var translateBtn: MaterialButton
    private lateinit var translatedTV: TextView
    val fromLanguage = arrayOf(
        "Dil Seçiniz",
        "Türkçe",
        "English",
        "Bulgarian",
        "Slovak",
        "Macedonian",
        "Swahili",
        "Japanese",
        "Hindu",
        "Korean",
        "Malay",
        "Belgium",
        "Portuguese",
        "Russian",
        "Spanish",
        "French",
        "German",
        "Arabic"
    )

    val toLanguage = arrayOf(
        "Dil Seçiniz",
        "Türkçe",
        "English",
        "Bulgarian",
        "Slovak",
        "Macedonian",
        "Swahili",
        "Japanese",
        "Hindu",
        "Korean",
        "Malay",
        "Belgium",
        "Portuguese",
        "Russian",
        "Spanish",
        "French",
        "German",
        "Arabic"
    )

    private val REQUEST_PERMISSION_CODE = 1
    var fromLanguageCode: Int = 0
    var toLanguageCode: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fromSpinner = findViewById(R.id.idFromSpinner)
        toSpinner = findViewById(R.id.idToSpinner)
        sourceEdt = findViewById(R.id.idEdtSource)
        translateBtn = findViewById(R.id.idBtnTranslate)
        translatedTV = findViewById(R.id.idTranslatedTV)

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                fromLanguageCode = getlanguageCode(fromLanguage[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        val fromAdapter = ArrayAdapter(this, R.layout.spinner_item, fromLanguage)
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromSpinner.adapter = fromAdapter

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                toLanguageCode = getlanguageCode(toLanguage[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }
        val toAdapter = ArrayAdapter(this, R.layout.spinner_item, toLanguage)
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        toSpinner.adapter = toAdapter

        translateBtn.setOnClickListener {
            translatedTV.text = ""
            if (sourceEdt.text.toString().isEmpty()) {
                Log.d(TAG, "Çevirmek istediğiniz metni giriniz. ")
            } else if (fromLanguageCode == 0) {
                Log.d(TAG, "Lütfen dili seçiniz. ")
            } else if (toLanguageCode == 0) {
                Log.d(TAG, "Lütfen çevirmek istediğiniz dili seçiniz. ")
            } else {
                translateText(fromLanguageCode, toLanguageCode, sourceEdt.text.toString())
            }
        }

    }//val fromLanguage = arrayOf("From", "English", "Spanish", "French", "German", "Arabic")

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val result: ArrayList<String>? =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                sourceEdt.setText(result?.get(0))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun translateText(fromLanguageCode: Int, toLanguageCode: Int, source: String) {
        translatedTV.text = "Çeviriliyor"
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(fromLanguageCode)
            .setTargetLanguage(toLanguageCode)
            .build()
        val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
        val conditions = FirebaseModelDownloadConditions.Builder().build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translatedTV.text = "Çeviriliyor"
                translator.translate(source)
                    .addOnSuccessListener { translatedText ->
                        translatedTV.text = translatedText
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Çevirilemedi ")
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Dil Algılanamadı")
                    }
            }
    }

    private fun getlanguageCode(language: String): Int {
        var languageCode = 0
        when (language) {
            "Türkçe" -> languageCode = FirebaseTranslateLanguage.TR
            "English" -> languageCode = FirebaseTranslateLanguage.EN
            "Bulgarian" -> languageCode = FirebaseTranslateLanguage.BG
            "Slovak" -> languageCode = FirebaseTranslateLanguage.SK
            "Macedonian" -> languageCode = FirebaseTranslateLanguage.MK
            "Swahili" -> languageCode = FirebaseTranslateLanguage.SW
            "Japanese" -> languageCode = FirebaseTranslateLanguage.JA
            "Hindu" -> languageCode = FirebaseTranslateLanguage.HI
            "Korean" -> languageCode = FirebaseTranslateLanguage.KO
            "Malay" -> languageCode = FirebaseTranslateLanguage.MS
            "Belgium" -> languageCode = FirebaseTranslateLanguage.BE
            "Portuguese" -> languageCode = FirebaseTranslateLanguage.PT
            "Russian" -> languageCode = FirebaseTranslateLanguage.RU
            "Spanish" -> languageCode = FirebaseTranslateLanguage.ES
            "French" -> languageCode = FirebaseTranslateLanguage.FR
            "German" -> languageCode = FirebaseTranslateLanguage.DE
            "Arabic" -> languageCode = FirebaseTranslateLanguage.AR
        }
        return languageCode
    }

}