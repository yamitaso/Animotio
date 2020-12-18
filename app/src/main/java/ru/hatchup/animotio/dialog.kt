package ru.hatchup.animotio

import android.app.ActionBar
import android.content.DialogInterface
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialogfragment.*

class dialog : DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner)

        return inflater.inflate(R.layout.dialogfragment,container,false)

    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels*0.90).toInt()
        val height = (resources.displayMetrics.heightPixels*0.70).toInt()
        dialog!!.window?.setLayout(width,height)
        textView3.setText(seekBar.progress.toString()+" FPS")
        if (seekBar2.progress==0)
            textView5.setText("бесконечно")
        else
            textView5.setText(seekBar2.progress.toString())
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView3.setText(progress.toString()+" FPS")
                val callingAct : DrawActivity = activity as DrawActivity
                callingAct.setDelays(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                return
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                return
            }

        })
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress==0)
                    textView5.setText("бесконечно")
                else
                    textView5.setText(progress.toString())
                val callingAct : DrawActivity = activity as DrawActivity
                callingAct.setRepeats(progress)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                return
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                return
            }

        })

    }

}