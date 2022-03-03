package uz.authentication.texyreaderml1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChooseActivity : AppCompatActivity() {
    lateinit var storage: Button
    lateinit var camera: Button
    lateinit var labeling: Button
    lateinit var trasn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        storage = findViewById(R.id.btnstorage)
        camera = findViewById(R.id.btnCamera)
        labeling = findViewById(R.id.btnImageLabeling)
        trasn = findViewById(R.id.btnTranslateIntent)

        storage.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        camera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        labeling.setOnClickListener {
            startActivity(Intent(this,ImageLabelingActivity::class.java))
        }
        trasn.setOnClickListener {
            startActivity(Intent(this,TranslateActivity::class.java))
        }
    }
}