package com.example.pogodynka

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial

class HomeFragment : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false)






    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button=view.findViewById<Button>(R.id.buttonstart)

        var switch=view.findViewById<Switch>(R.id.switch2)
        var nrekranu=1


        button.setOnClickListener {


            if(!switch.isChecked) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_basicFragment)
            } else {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_forolderFragment)
            }
        }
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked

                nrekranu=2
                // Change the app background color

            } else {
                // The switch is disabled
                nrekranu=1

                // Set the app background color to light gray

            }
        }

    }


}