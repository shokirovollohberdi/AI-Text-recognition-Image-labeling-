package uz.authentication.texyreaderml1

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var tvResult:TextView
    lateinit var btnUpload:Button
    lateinit var inputImage: InputImage
    lateinit var textRecognizer:TextRecognizer
    var intentActivityResultLauncher:ActivityResultLauncher<Intent>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvResult = findViewById(R.id.tvResult)
        btnUpload = findViewById(R.id.btnUpload)


        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        intentActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                val date = it.data
                val imageUri = date?.data

                convertImageToText(imageUri!!)
            }
        )
         btnUpload.setOnClickListener {
             val chooseIntent = Intent()
             chooseIntent.type = "image/*"
             chooseIntent.action = Intent.ACTION_GET_CONTENT
             intentActivityResultLauncher?.launch(chooseIntent)
         }
    }

    @SuppressLint("SetTextI18n")
    private fun convertImageToText(imageUri: Uri) {
        try {
            inputImage = InputImage.fromFilePath(applicationContext,imageUri)

            val result:Task<Text> = textRecognizer.process(inputImage)
                .addOnSuccessListener {
                    tvResult.text = it.text
                }.addOnFailureListener {
                    tvResult.text = "Error : ${it.message}"
                }

        }catch (e:Exception){

        }
    }

    override fun onResume() {
        super.onResume()
        myMethod()
    }
    fun myMethod(){
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE){
            //all permissions already granted or just granted

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

    }
}