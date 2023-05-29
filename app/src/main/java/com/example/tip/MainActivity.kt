package com.example.tip

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat


private const val TAG="MainActivity"
private const val INITIAL_TIP_PERCENT=15
class MainActivity : AppCompatActivity() {
    private lateinit var tvBaseAmount: EditText
    private lateinit var seekBarTip:SeekBar
    private lateinit var tvTipPercentLable:TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmmount:TextView
    private lateinit var tvTipDiscreption:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvBaseAmount=findViewById(R.id.tvBasedAmmount)
       seekBarTip=findViewById(R.id.seekBarTip)
        tvTipAmount=findViewById(R.id.tvTipAmmount)
        tvTotalAmmount=findViewById(R.id.tvTotalAmmount)
        tvTipPercentLable=findViewById(R.id.tvTipPercentLable)
        tvTipDiscreption=findViewById(R.id.tvTipDiscreption)



        seekBarTip.progress=INITIAL_TIP_PERCENT
        updatetvTipDiscreption(INITIAL_TIP_PERCENT)
        tvTipPercentLable.text= "$INITIAL_TIP_PERCENT"
        seekBarTip.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG,"onProcressChanging$progress")
                tvTipPercentLable.text="$progress%"
                complitTipAndTotal()
                updatetvTipDiscreption(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        tvBaseAmount.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
               Log.i(TAG,"afterTextCgange $s")
                complitTipAndTotal()
            }

        })
    }

    private fun updatetvTipDiscreption(progress: Int) {
         val tipDiscreption=when(progress){
           in  0..9->"poor"
           in  10..14->"Acceptable"
           in  15..19->"Good"
           in  20..24->"Great"

             else -> "Amazing"
         }
        tvTipDiscreption.text=tipDiscreption
        //update the color based on the tipPercent
        val color=ArgbEvaluator().evaluate(
            progress.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this,R.color.worst),
            ContextCompat.getColor(this,R.color.best),
        )as Int
        tvTipDiscreption.setTextColor(color)
    }

    private fun complitTipAndTotal() {
        if (tvBaseAmount.text.isEmpty()){
            tvTotalAmmount.text=""
            tvTipAmount.text=""
            return
        }
        //1.get the value of Tip persent and base
       val BaseAmount= tvBaseAmount.text.toString().toDouble()
        val tipPersent=seekBarTip.progress
        //2.compute the tip and total base
       val tipAmount= BaseAmount*tipPersent/100
        val totalAmount=BaseAmount+tipAmount
        //3.update the ui
        tvTipAmount.text="%.2f".format(tipAmount)
        tvTotalAmmount.text="%.2f".format(totalAmount)
    }
}