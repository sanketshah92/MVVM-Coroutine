package com.example.samplemvvmkotlin.dashboardmodule.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samplemvvmkotlin.dashboardmodule.data.DetailsData
import com.example.samplemvvmkotlin.dashboardmodule.datasource.DashboardRepository
import com.example.samplemvvmkotlin.network.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DashboardViewModel(private val repository: DashboardRepository) : ViewModel() {
    private var lastFetchedBatchId = -1
    lateinit var batches: HashMap<Int, List<Long>>
    val idDetails: MutableLiveData<Resource<ArrayList<DetailsData>>> = MutableLiveData()
    private val tmp: ArrayList<DetailsData> = ArrayList()

    init {
        getIDs()
    }

    private fun getIDs() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val ids = repository.getIds()
                batches = HashMap(createBatches(ids))
                Log.e("Batches", "$batches")
            }
        }.invokeOnCompletion {
            Log.e("LOAD NEXT", "::" + batches[lastFetchedBatchId + 1]!![0].toString())
            loadNextBatches()
        }
    }

    fun loadNextBatches() {
        idDetails.value = Resource.loading(data = null)

        viewModelScope.launch {
            batches[lastFetchedBatchId + 1]?.forEach {
                async {
                    Log.e("IDS:::", "$it")
                    tmp.add(repository.getDetail(it.toString()))
                }.await()
            }
        }.invokeOnCompletion {
            Log.e("IDS:::", "COMPLETED" + Gson().toJson(tmp))
            idDetails.value = Resource.success(data = tmp)
        }

    }

    private fun createBatches(ids: List<Long>): HashMap<Int, List<Long>> {
        return chopped(ids, 20)!!
    }

    private fun <T> chopped(list: List<T>, L: Int): HashMap<Int, List<T>>? {
        val parts: HashMap<Int, List<T>> = HashMap()
        val N = list.size
        var i = 0
        var key = 0
        while (i < N) {
            parts[key] = ArrayList(
                list.subList(i, Math.min(N, i + L)))
            i += L
            key++
        }
        return parts
    }
}