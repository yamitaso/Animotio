package ru.hatchup.animotio


import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.ProgressDialog.show
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.activity_draw.*
import kotlinx.android.synthetic.main.dialogfragment.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class DrawActivity : AppCompatActivity() {
    var dVs = arrayOfNulls<Bitmap>(256)
    var delay : Int = 10
    var repeat : Int = 0
    val saveDialog : dialog = dialog()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)
        textView6.setText("Толщина линии: "+seekBar3.progress.toString())
        dView.setStroke(seekBar3.progress.toFloat())
        seekBar3.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView6.setText("Толщина линии: "+seekBar3.progress.toString())
                dView.setStroke(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                return
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                return
            }

        })
        tabLayout.addTab(tabLayout.newTab().setText((tabLayout.tabCount+1).toString()),true)
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) dVs[tab.position]= Bitmap.createBitmap(dView.getBitmap())
            }

            override fun onTabSelected(tabb: TabLayout.Tab?) {
                if (tabb != null) {
                    if(dVs[tabb.position]!=null){
                        dView.settBitmap(dVs[tabb.position])

                    }
                    else dView.whiteList()
                }
            }

        })
        val actionbar = supportActionBar
        actionbar!!.title = "Рисование анимации"
        actionbar.setDisplayHomeAsUpEnabled(true)


    }
    fun addPage(view:View){
        if(tabLayout.tabCount<255){
            tabLayout.addTab(tabLayout.newTab().setText((tabLayout.tabCount+1).toString()),true)
        }
    }
    fun delPage(view:View){
        if(tabLayout.selectedTabPosition!=0){
            tabLayout.selectTab(tabLayout.getTabAt(tabLayout.selectedTabPosition-1))
            tabLayout.removeTabAt(tabLayout.selectedTabPosition+1)
            dVs[tabLayout.selectedTabPosition+1]=null
        }
    }
    fun SwitchColor(view : View){
        ColorPickerDialog.Builder(this)
            .setTitle("Выбор цвета")
            .setPreferenceName("colorPick")
            .setPositiveButton("Выбрать",
                ColorEnvelopeListener { envelope, fromUser -> dView.setColor(envelope.color)
                divider2.setBackgroundColor(envelope.color)
                })
            .setNegativeButton(
                "Отмена"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(false)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }
    public fun setDelays(d : Int){
        delay = d
    }
    public fun setRepeats(r : Int){
        repeat = r
    }
    fun viewDialog(view: View){
        isStoragePermissionGranted()
        saveDialog.show(supportFragmentManager,"Сохранение анимации")

    }
    fun closeDialog(view: View){
        saveDialog.dismiss()

    }
    fun saveGif(view : View) {
        dVs[tabLayout.selectedTabPosition]= Bitmap.createBitmap(dView.getBitmap())
        try {
            var agw : AnimatedGIFWriter = AnimatedGIFWriter(true)
            var dest = File(getGalleryPath() + "Animotio/")
            dest.mkdirs()
            dest =
                File(getGalleryPath() + "Animotio/" + Calendar.getInstance().time +".gif")
            val out = FileOutputStream(dest)
            agw.prepareForWrite(out,-1,-1)
            if (repeat!=0){
                agw.setLoopCount(repeat)
            }
            for (index in 0..dVs.size-1){
                if (dVs[index]!=null){
                    agw.writeFrame(out,dVs[index],(1000/delay).toInt())
                }
            }
            agw.finishWrite(out)
            Toast.makeText(this, "Анимация сохранена в \n"+dest.canonicalPath, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {

            Log.d("MyLog", e.toString())
        }
        saveDialog.dismiss()
    }


    private fun getGalleryPath(): String {
        return Environment.getExternalStorageDirectory().toString() + "/"
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {

                true
            } else {

                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.

                    }

                    else -> {
                        // You can directly ask for the permission.
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            12)
                    }
                }

                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            true
        }
    }

}