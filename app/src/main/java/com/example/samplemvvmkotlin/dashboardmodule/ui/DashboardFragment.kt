package com.example.samplemvvmkotlin.dashboardmodule.ui

import android.app.AlertDialog
import android.database.DatabaseUtils
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samplemvvmkotlin.EndlessRecyclerViewScrollListener
import com.example.samplemvvmkotlin.R
import com.example.samplemvvmkotlin.ViewModelFactory
import com.example.samplemvvmkotlin.dashboardmodule.data.DetailsData
import com.example.samplemvvmkotlin.dashboardmodule.viewmodel.DashboardViewModel
import com.example.samplemvvmkotlin.databinding.FragmentDashboardBinding
import com.example.samplemvvmkotlin.loginmodule.viewmodel.LoginViewModel
import com.example.samplemvvmkotlin.network.NetworkHelper
import com.example.samplemvvmkotlin.network.RetrofitBuilder
import com.example.samplemvvmkotlin.network.Status
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    lateinit var viewModel: DashboardViewModel
    lateinit var detailsData: ArrayList<DetailsData>
    lateinit var dialog: AlertDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        : View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(NetworkHelper(RetrofitBuilder.apiService))).get(DashboardViewModel::class.java)
        detailsData = ArrayList()
        dialog = SpotsDialog.Builder().setContext(context).setMessage("Loading...").setCancelable(false).build()
        dialog.show()
        val adapter = DashboardAdapter(detailsData = detailsData, itemListener = object : DashboardAdapter.OnTitleSelection {
            override fun onTitleSelected(data: DetailsData) {
                val action = DashboardFragmentDirections.actionDashboardFragmentToDetailsFragment(data)
                binding.root.findNavController().navigate(action)
            }
        })
        recyler_dashboard.adapter = adapter
        recyler_dashboard.addOnScrollListener(object : EndlessRecyclerViewScrollListener(recyler_dashboard.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                dialog.show()
                viewModel.loadNextBatches()
                Log.e("PAGE", "" + page)
            }

            override fun doPullToRefresh() {

            }

        })


        viewModel.idDetails.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                adapter.addData(it.data!!)
                adapter.notifyDataSetChanged()
            } else if (it.status == Status.ERROR) {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
            if (it.status != Status.LOADING)
                dialog.dismiss()
        })
    }

}