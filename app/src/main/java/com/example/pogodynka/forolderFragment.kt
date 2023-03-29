package com.example.pogodynka

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.pogodynka.databinding.FragmentBasicBinding
import com.example.pogodynka.databinding.FragmentForolderBinding
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [forolderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class forolderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentForolderBinding? = null
    var defaultcity:String="Gliwice"
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForolderBinding.inflate(inflater, container, false)
            val args=this.arguments
        val cit=args?.getString("miasto")
        if(!cit.isNullOrEmpty())
            defaultcity=cit.toString()


        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        load(view,defaultcity,1)
        val buttonback = view.findViewById<ImageButton>(R.id.back2)

        buttonback.setOnClickListener() {


            Navigation.findNavController(view)
                .navigate(R.id.action_forolderFragment_to_homeFragment)

        }

        val buttonFindWeather = view.findViewById<Button>(R.id.button)
        buttonFindWeather.setOnClickListener { load(view,"Gliwice",2) }
        var buttto2=view.findViewById<ImageButton>(R.id.imageButton)
        buttto2.setOnClickListener(){


            val b=Bundle()
            b.putString("miasto",binding.textView4.text.toString())
            val fragment=BasicFragment()
            fragment.arguments=b
            fragmentManager?.beginTransaction()?.replace(R.id.fragment,fragment)?.commit()
          //  Navigation.findNavController(view)
             //   .navigate(R.id.action_forolderFragment_to_basicFragment)
        }

    }
private fun load(view:View,city:String,nr:Int){
        val okHttpClient = OkHttpClient()

        var jsonObject: JSONObject? = null



            if(binding.textView4.text.toString() != "" || nr==1) {
               var place:String
                if(nr==1)
                {

                    place=city
                }
                else
                {
                    place=binding.inputNameOfCityField.text.toString()
                }

                val request = Request.Builder()
                    .url("https://api.openweathermap.org/data/2.5/weather?q=${place}&APPID=fcdfeb407dcbd28e29ff4e5e5fa0f259&units=metric&lang=pl")
                    .build()

                var isResponse = false

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        Handler(Looper.getMainLooper()).post(Runnable {
                            Toast.makeText(
                                view.context,
                                "Nieprawidłowe dane!\nNie prawidłowa lokalizacja!",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.inputNameOfCityField.setText("")
                        })
                    }

                    @SuppressLint("SetTextI18n", "SimpleDateFormat", "ResourceAsColor")
                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) {
                                Handler(Looper.getMainLooper()).post(Runnable {
                                    Toast.makeText(
                                        view.context,
                                        "Wprowadź poprawną lokalizację!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.inputNameOfCityField.setText("")
                                })
                                throw IOException("Unexpected code $response")
                            }

                            jsonObject = response.body?.string()?.let { it1 -> JSONObject(it1) }

                            val main = jsonObject?.getJSONObject("main")
                            val sys = jsonObject?.getJSONObject("sys")
                            val weather = jsonObject?.getJSONArray("weather")
                            val wind = jsonObject?.getJSONObject("wind")

                            Handler(Looper.getMainLooper()).post(Runnable {
                                binding.inputNameOfCityField.text?.clear()

                                val simpleDate = SimpleDateFormat("HH:mm:ss")
                                val longDate = SimpleDateFormat("dd.M.yyyy HH:mm:ss")

                                val timezone = jsonObject?.getLong("timezone")

                                binding.textView4.text = jsonObject?.getString("name").toString()
                                binding.textView15.text =
                                    longDate.format(jsonObject?.getLong("dt")!! * 1000 + timezone!! * 1000)
                                        .toString()

                                val temp = main?.getInt("temp")
                                var color: Int = 111111

                                //binding.temperatureField.text = temp.toString() + "\u00B0C"
                                binding.textView.setText("Temperatura:\n"+temp.toString() + "\u00B0C")

//                                if (temp != null) {
//                                    if(temp < -10) {
//                                        color = R.color.low_low_temp
//                                    } else if(temp in -10..0) {
//                                        color = R.color.low_temp
//                                    } else if(temp in 1..9) {
//                                        color = R.color.high_low_temp
//                                    } else if(temp in 10..15) {
//                                        color = R.color.low_medium_temp
//                                    } else if(temp in 16..21) {
//                                        color = R.color.medium_temp
//                                    } else if(temp in 22..26) {
//                                        color = R.color.high_medium_temp
//                                    } else if(temp in 27..31) {
//                                        color = R.color.low_high_temp
//                                    } else if(temp in 32..39) {
//                                        color = R.color.high_temp
//                                    } else if(temp > 40) {
//                                        color = R.color.high_high_temp
//                                    }

                                Picasso.get().load(
                                    "https://openweathermap.org/img/wn/${
                                        weather?.getJSONObject(0)?.getString("icon")
                                    }@2x.png"
                                )
                                    .fit()
                                    .into(binding.imageView)
                                binding.textView6.setText("Ciśnienie:\n"+  main?.getInt("pressure").toString() + " hPa")


                                binding.textView5.setText("Opis:\n"+weather?.getJSONObject(0)?.getString("description").toString()+ ", Wilgotność:"+ main?.getInt("humidity").toString()+"%"+", Wiatr:"+wind?.getInt("speed").toString() + " m/s")
                                binding.textView13.setText(
                                    "Wschód słońca\n"+simpleDate.format(+sys?.getLong("sunrise")!! * 1000 + timezone!! * 1000)
                                        .toString())
                                binding.textView14.setText("Zachód słońca\n"+
                                    simpleDate.format(sys?.getLong("sunset")!! * 1000 + timezone!! * 1000)
                                        .toString())


                            })
                        }
                    }
                })

            }}




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment forolderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            forolderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}