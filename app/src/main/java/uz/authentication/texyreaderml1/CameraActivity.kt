package uz.authentication.texyreaderml1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class CameraActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var btnSnap: Button
    lateinit var btnDelect: Button
    lateinit var textView: TextView
    private var imageBitmap: Bitmap? = null
    val REQUENST_IMAGE_CAPTURE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        imageView = findViewById(R.id.imageView)
        btnSnap = findViewById(R.id.btnSnap)
        btnDelect = findViewById(R.id.btnDetec)
        textView = findViewById(R.id.textView)

        btnSnap.setOnClickListener {
            dispatchTakePictureInent()
        }
        /* btnDelect.setOnClickListener {
             delectText()
         }*/
    }

    @SuppressLint("SetTextI18n")
    private fun delectText() {
        val image = FirebaseVisionImage.fromBitmap(imageBitmap!!)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(image)
            .addOnSuccessListener(OnSuccessListener<FirebaseVisionText> { firebaseVisionText ->
                if (firebaseVisionText.text.isEmpty()) {
                    textView.text = "Are you sure there is text in the picture"
                }
                textView.text = firebaseVisionText.text
                // proccesTxt(firebaseVisionText)
            }).addOnFailureListener {
                OnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }

            }
    }

    /*  private fun proccesTxt(text: FirebaseVisionText) {
          val blocks = text.textBlocks
          if (blocks.size==0){
              Toast.makeText(this, "No Text Found", Toast.LENGTH_SHORT).show()
              return
          }
          for (block in text.textBlocks){
              val txt = block.text
              textView!!.textSize = 16f
              textView.setText(txt)
          }
      }*/

    private fun dispatchTakePictureInent() {
        var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUENST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUENST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            imageBitmap = extras!!.get("data") as Bitmap
            imageView!!.setImageBitmap(imageBitmap)
            delectText()
        }
    }
}