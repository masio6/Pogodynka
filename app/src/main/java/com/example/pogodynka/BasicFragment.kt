package com.example.pogodynka

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.example.pogodynka.databinding.FragmentBasicBinding
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat


class BasicFragment : Fragment() {
    private var _binding: FragmentBasicBinding? = null
    private val binding get() = _binding!!
  var defaultcity:String="Gliwice"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view=inflater.inflate(R.layout.fragment_basic,container,false)
        val view = inflater.inflate(R.layout.fragment_basic, container, false)
        val buttonback = view.findViewById<ImageButton>(R.id.back)
        val args=this.arguments
        val cit=args?.getString("miasto")
        if(!cit.isNullOrEmpty())
            defaultcity=cit.toString()
        _binding = FragmentBasicBinding.inflate(inflater, container, false)

        /*val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getWeather()

        viewModel.myResponse.observe(viewLifecycleOwner, Observer {
            // Do something
        })*/



        return binding.root


        buttonback.setOnClickListener {

            findNavController().navigate(R.id.action_basicFragment_to_homeFragment)
        }

        /*val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.getWeather()

        viewModel.myResponse.observe(viewLifecycleOwner, Observer {
            // Do something
        })*/

        //  binding.cardTopTitle.isVisible = false
        //binding.nestedScroll.isVisible = false

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        load(view,defaultcity,1)


        val buttonFindWeather = view.findViewById<Button>(R.id.findweather)
        buttonFindWeather.setOnClickListener {

            load(view,binding.inputNameOfCityField.text.toString(),2)
        }
    var buttt2=view.findViewById<ImageButton>(R.id.imageButton2)
    buttt2.setOnClickListener(){
          val b=Bundle()
        b.putString("miasto",binding.Cityname.text.toString())
       val fragment=forolderFragment()
        fragment.arguments=b
fragmentManager?.beginTransaction()?.replace(R.id.fragment,fragment)?.commit()
 //      Navigation.findNavController(view)
   //         .navigate(R.id.action_basicFragment_to_forolderFragment)
    }

    }
    private fun load(view:View,city:String,nr:Int){


            if(binding.inputNameOfCityField.text.toString() != "" || nr==1) {
                var place:String
                if(nr==1)
                    place=city
                else
                    place=binding.inputNameOfCityField.text.toString()

                val request = Request.Builder()
                    .url("https://api.openweathermap.org/data/2.5/weather?q=${place}&APPID=fcdfeb407dcbd28e29ff4e5e5fa0f259&units=metric&lang=pl")
                    .build()

                var isResponse = false
                val okHttpClient = OkHttpClient()

                var jsonObject: JSONObject? = null

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        Handler(Looper.getMainLooper()).post(Runnable {
                            Toast.makeText(view.context, "Nieprawidłowe dane!\nNie prawidłowa lokalizacja!", Toast.LENGTH_SHORT).show()
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

                                binding.Cityname.text = jsonObject?.getString("name").toString()
                                binding.data.text = longDate.format(jsonObject?.getLong("dt")!! * 1000 + timezone!! * 1000).toString()

                                val temp = main?.getInt("temp")
                                var color: Int = 111111

                                //binding.temperatureField.text = temp.toString() + "\u00B0C"
                                binding.textView.setText(temp.toString() + "\u00B0C")

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
                              //  }
                             //   binding.temperatureFieldColor.setBackgroundResource(color)

                                /*Picasso.get().load("https://openweathermap.org/img/wn/${weather?.getJSONObject(0)?.getString("icon")}@2x.png")
                                    .fit()
                                    .into(binding.iconOfWeather)*/

                              //  Picasso.get().load("https://openweathermap.org/img/wn/${weather?.getJSONObject(0)?.getString("icon")}@2x.png")
                               //     .fit()
                                 //   .into(binding.iconOfWeather2)

                              //  binding.windField.text = "Wiatr:\n" + wind?.getInt("speed").toString() + " m/s"
                                Picasso.get().load("https://openweathermap.org/img/wn/${weather?.getJSONObject(0)?.getString("icon")}@2x.png")
                                    .fit()
                                    .into(binding.imageView4)

                                binding.textView5.setText(weather?.getJSONObject(0)?.getString("description").toString()+ ", Wilgotność:"+ main?.getInt("humidity").toString()+"%"+", Wiatr:"+wind?.getInt("speed").toString() + " m/s")
                                binding.textView7.setText(  main?.getInt("pressure").toString() + " hPa")
                                binding.textView13.text =  simpleDate.format(sys?.getLong("sunrise")!! * 1000 + timezone!! * 1000).toString()
                                binding.textView14.text = simpleDate.format(sys?.getLong("sunset")!! * 1000 + timezone!! * 1000).toString()


                            })
                        }
                    }
                })
            }
        }


     //  val buttonback1 = view.findViewById<Button>(R.id.back)

       // buttonback1.setOnClickListener() {


         //   Navigation.findNavController(view).navigate(R.id.action_basicFragment_to_homeFragment)

        //}
    }

