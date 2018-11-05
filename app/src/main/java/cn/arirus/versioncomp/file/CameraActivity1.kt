package cn.arirus.versioncomp.file

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import cn.arirus.versioncomp.R
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_camera.cam
import kotlinx.android.synthetic.main.activity_camera.img
import java.io.File

class CameraActivity1 : AppCompatActivity() {
  
  
  override fun onCreate(savedInstanceState : Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_camera)
    val permission = RxPermissions(this)
    
    cam.setOnClickListener { _ ->
      permission.request(android.Manifest.permission.CAMERA).subscribe { _ -> takeCam() }
    }
  }
  
  lateinit var path : String
  
  lateinit var outputFile : File
  
  init {
    outputFile = File(getExternalFilesDir(null), "capture.png").apply { if(exists()) delete() }
    path = outputFile.absolutePath
  }
  
  fun takeCam() {
    
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
    }
    
    startActivityForResult(intent, 1);
    
  }
  
  override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?) {
    if(requestCode != 1 || resultCode != Activity.RESULT_OK) return
    img.setImageBitmap(BitmapFactory.decodeFile(path))
  }
  
}
