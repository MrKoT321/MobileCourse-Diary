package com.example.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class DiaryViewModel(
    private val dao: RecordDao
) : ViewModel() {

    val records = MutableStateFlow<List<Record>>(emptyList())
    private var currRecord : MutableStateFlow<Record?> = MutableStateFlow(null)

    init {
        loadRecords()
    }

    fun addRecord(record: Record) {
        viewModelScope.launch {
            dao.insertAll(record)
            loadRecords()
        }
    }

    fun saveRecord(title: String, content: String) {
        if (currRecord.value == null)
        {
            currRecord.value = Record(UUID.randomUUID().toString(), title, content, Date().time)
        }
        else
        {
            currRecord.value = currRecord.value!!.copy(
                title = title,
                content = content
            )
        }
        viewModelScope.launch {
            dao.insertAll(currRecord.value!!)
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
                currRecord.value = it.copy()
                return it
            }
        }
        currRecord.value = null
        return currRecord.value
    }

    private fun loadRecords() {
        viewModelScope.launch {
            records.value = dao.getAll()
        }
    }
}