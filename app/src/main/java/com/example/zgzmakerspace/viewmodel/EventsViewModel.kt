package com.example.zgzmakerspace.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zgzmakerspace.core.GetDataWebCodeBeers
import com.example.zgzmakerspace.core.GetDataWebLaboratories
import com.example.zgzmakerspace.model.Events
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventsViewModel: ViewModel() {
    private var _listEvents = MutableStateFlow<List<Events>>(emptyList())
    val listEvent: StateFlow<List<Events>> = _listEvents
    private var _loading= MutableStateFlow<Boolean>(true)
    val loading:StateFlow<Boolean> = _loading

    fun loadHome(){
        _loading.value=true
        viewModelScope.launch(Dispatchers.IO){
            var data=GetDataWebCodeBeers()
            data.getEvents()
            _listEvents.value=data.returnEvents()
            _loading.value=false
        }
    }
    fun readWeb(){
        _loading.value=true
        viewModelScope.launch(Dispatchers.IO){
            var data=GetDataWebCodeBeers()
            data.getEvents()
            _listEvents.value=data.returnEvents()
            _loading.value=false
        }
    }

    fun readWebLab(){
        _loading.value=true
        viewModelScope.launch(Dispatchers.IO){
            var data=GetDataWebLaboratories()
            data.getEvents()
            _listEvents.value=data.returnEvents()
            _loading.value=false

        }
    }


}