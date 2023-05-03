package com.example.drawingapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView :DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null


    val openGalleryLauncher:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == RESULT_OK && result.data != null){
            val imageBackground: ImageView = findViewById(R.id.iv_background)
            imageBackground.setImageURI(result.data?.data)
        }
    }

    val requestPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if(isGranted){
                    Toast.makeText(this, "Permisson granted", Toast.LENGTH_SHORT).show()
                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                }else{
                    if(permissionName ==android.Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this, "Opps", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(5.3f) // размер щетки

        val linearLayout = findViewById<LinearLayout>(R.id.colors)
        mImageButtonCurrentPaint = linearLayout[2] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.palette_pressed))

        val ib_brush : ImageButton = findViewById(R.id.pal)
        ib_brush.setOnClickListener{
            showBrushSizeDialog()
        }

        val delete : ImageButton = findViewById(R.id.delete)
        delete.setOnClickListener{
            drawingView?.onClickUndo()
        }

        val galleryBtn :ImageButton = findViewById(R.id.gallery)
        galleryBtn.setOnClickListener {
            requestStoragePermission()
        }

    }
    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
        ){

        }
    }

    fun paintClicked(view : View){
        if(view!= mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.palette_pressed)
            )
            mImageButtonCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.palette_pressed)
            )
            mImageButtonCurrentPaint = view
        }
    }

    @SuppressLint("CutPasteId")
    private fun showBrushSizeDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush size: ")

        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small)
        smallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(7.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()

        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium)
        mediumBtn.setOnClickListener {
            drawingView?.setSizeForBrush(12.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()

        val bigBtn = brushDialog.findViewById<ImageButton>(R.id.ib_big)
        bigBtn.setOnClickListener {
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }
}