package uz.authentication.texyreaderml1

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.vision.label.ImageLabeler
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.lang.Exception

class ImageLabelingActivity : AppCompatActivity() {
    lateinit var btnUpload: Button
    lateinit var tvResult: TextView
    lateinit var imageView: ImageView
    private val TAG = "ImageLabelingActivity"
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    lateinit var inputImage: InputImage
    lateinit var imagelabeler: com.google.mlkit.vision.label.ImageLabeler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_labeling)
        btnUpload = findViewById(R.id.btnUploadImageLabel)
        tvResult = findViewById(R.id.tvResultLabel)
        imageView = findViewById(R.id.imageViewLabel)

        imagelabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                /**
                 * Called when result is available
                 */
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        val image = data?.extras?.get("data") as Bitmap
                        imageView.setImageBitmap(image)
                        inputImage = InputImage.fromBitmap(image, 0)
                    } catch (e: Exception) {

                    }
                }
            })
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                object : ActivityResultCallback<ActivityResult> {
                    /**
                     * Called when result is available
                     */
                    override fun onActivityResult(result: ActivityResult?) {
                        val data = result?.data
                        try {
                            inputImage =
                                InputImage.fromFilePath(this@ImageLabelingActivity, data?.data!!)
                            imageView.setImageURI(data.data)
                            proccesImage()
                        } catch (e: Exception) {
                            Log.d(TAG, "onActivityResult: ${e.message}")
                        }
                    }
                })

        btnUpload.setOnClickListener {
            val options = arrayOf("camera", "gallery")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pick Options")
            builder.setItems(options, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)
                } else {
                    val storageIntent = Intent()
                    storageIntent.setType("image/*")
                    storageIntent.setAction(Intent.ACTION_GET_CONTENT)
                    galleryLauncher.launch(storageIntent)
                }
            })
            builder.show()
        }

    }

    private fun proccesImage() {
        imagelabeler.process(inputImage).addOnSuccessListener {
            var result = " "
            for (label in it) {
                result = result + "\n" + label.text
            }
            tvResult.text = result
        }
    }
}