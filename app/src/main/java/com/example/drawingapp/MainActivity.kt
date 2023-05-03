package com.example.drawingapp

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView :DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(5.3f) // размер щетки

        val linearLayout = findViewById<LinearLayout>(R.id.colors)
        mImageButtonCurrentPaint = linearLayout[2] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.palette_pressed))

        val ib_brush : ImageButton = findViewById(R.id.image)
        ib_brush.setOnClickListener{
            showBrushSizeDialog()
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