package com.example.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DiaryViewModel(private val dao: RecordDao) : ViewModel() {
    val records = MutableStateFlow<List<Record>>(emptyList())

    init {
        loadRecords()
    }

    fun addRecord(record: Record) {
        viewModelScope.launch {
            dao.insertAll(record)
            loadRecords()
        }
    }

    fun deleteRecord(record: Record) {
        viewModelScope.launch {
            dao.delete(record)
            loadRecords()
        }
    }

    fun findRecord(uid: String): Record?
    {
        records.value.forEach{
            if (it.uid == uid)
            {
                return it
            }
        }
        return null
    }

    private fun loadRecords() {
        viewModelScope.launch {
            records.value = dao.getAll()
        }
    }
}