package com.example.ecomapp.dialog


import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.ecomapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.zip.Inflater

fun Fragment.setBottomSheetDialog(
    onResetclick:(String)->Unit
){

    val dialog=BottomSheetDialog(requireContext(),R.style.DialogStyle);
    val layoutBottomsheet=layoutInflater.inflate(R.layout.resetpassworddialogue,null)


    dialog.setContentView(layoutBottomsheet)
    dialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
    dialog.show()


    val Email=layoutBottomsheet.findViewById<EditText>(R.id.resetpassedittext)
    val resetbtn=layoutBottomsheet.findViewById<Button>(R.id.resetbtn)
    val cancelbtn=layoutBottomsheet.findViewById<Button>(R.id.cancelbtn)

    resetbtn.setOnClickListener{

        val email=Email.text.toString().trim()
        onResetclick(email)

        dialog.dismiss()
    }

    cancelbtn.setOnClickListener{

      dialog.dismiss()

    }



}