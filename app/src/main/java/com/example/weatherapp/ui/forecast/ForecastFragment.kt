package com.example.weatherapp.ui.forecast

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.API_KEY
import com.example.weatherapp.databinding.ForecastFragmentBinding
import com.example.weatherapp.ui.forecast.adapter.ForecastAdapter

class ForecastFragment : Fragment() {

    private val viewModel: ForecastViewModel by viewModels()
    private lateinit var binding: ForecastFragmentBinding
    private val adapter = ForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ForecastFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        setFragmentResultListener("request_key_lon") { requestKey, bundle ->
            val coord = bundle.get("coor") as Pair<Double, Double>
            Log.d("latt", coord.first.toString())
            Log.d("lon", coord.second.toString())
            with(binding) {
                viewModel.getForecastOfCountry(coord, API_KEY)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = layoutManager
                viewModel.forecastInfo.observe(viewLifecycleOwner) { forecasts ->
                    adapter.submitList(forecasts)
                    progressBarLoading.isVisible = false

                }
            }

        }
    }

    companion object {
        fun newInstance() = ForecastFragment()
    }

}