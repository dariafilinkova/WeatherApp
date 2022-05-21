package com.example.weatherapp.ui.forecast

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.weatherapp.API_KEY
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastFragmentBinding

class ForecastFragment : Fragment() {

    private val viewModel: ForecastViewModel by viewModels()
    private lateinit var binding: ForecastFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setFragmentResultListener("request_key_lon") { requestKey, bundle ->
//            val lon = bundle.getDouble("lon")
//            Log.d("lonnn", lon.toString())
//        }
        setFragmentResultListener("request_key_lon") { requestKey, bundle ->
            //val (lat,lon) = bundle.get("coor")
            val coord = bundle.get("coor") as Pair<Double, Double>
            Log.d("latt", coord.first.toString())
            Log.d("lon", coord.second.toString())
            viewModel.getForecastOfCountry(coord, API_KEY)


        }
//        setFragmentResultListener("request_key_lat") { requestKey, bundle ->
//            val lat= bundle.getString("lat")
//            Log.d("lon",lat!!)
//
//        }
    }

    companion object {
        fun newInstance() = ForecastFragment()
    }

}