package com.example.samplemvvmkotlin.dashboardmodule.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.samplemvvmkotlin.dashboardmodule.data.DetailsData
import com.example.samplemvvmkotlin.databinding.ItemDashboardBinding

class DashboardAdapter(private val detailsData: ArrayList<DetailsData>, private val itemListener: OnTitleSelection) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {
    fun addData(newData: ArrayList<DetailsData>) {
        detailsData.addAll(newData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDashboardBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = detailsData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(detailsData[position])
        holder.itemTitle.setOnClickListener { itemListener.onTitleSelected(detailsData[position]) }
    }

    class ViewHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var itemTitle: RelativeLayout
        fun bind(data: DetailsData) {
            binding.data = data
            itemTitle = binding.itemTitle
        }
    }

    interface OnTitleSelection {
        fun onTitleSelected(data: DetailsData)
    }
}