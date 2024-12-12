package com.example.dictionary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.databinding.RecordCardBinding
import java.text.SimpleDateFormat
import java.util.Date

class DiaryEntriesViewHolder(view: View) : RecyclerView.ViewHolder(view)

class DiaryEntriesAdapter(
    private var recordList: MutableList<Record>,
    private val onOpenRecord: (Record) -> Unit,
    private val onDeleteRecord: (Record) -> Unit
) : RecyclerView.Adapter<DiaryEntriesViewHolder>() {
    override fun getItemCount(): Int = recordList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryEntriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = RecordCardBinding.inflate(inflater, parent, false)

        return DiaryEntriesViewHolder(view.root)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: DiaryEntriesViewHolder, position: Int) {
        val recordBinding = RecordCardBinding.bind(holder.itemView)
        val record = recordList[position]

        recordBinding.recordTitle.text = record.title
        recordBinding.recordContent.text = record.content
        recordBinding.recordDate.text =
            SimpleDateFormat("dd.MM.yyyy").format(Date(record.createdAt))
        recordBinding.itemBlock.contentDescription = record.uid

        recordBinding.root.setOnClickListener { onOpenRecord(record) }
        recordBinding.deleteBtn.setOnClickListener { onDeleteRecord(record) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Record>) {
        if (newData.isNotEmpty()) {
            recordList = newData.toMutableList()
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterDataByTimestamp(timestamp: Long) {
        val millisecondsToDays = 1000 * 60 * 60 * 24
        recordList =
            recordList.filter { (it.createdAt / millisecondsToDays).toInt() == (timestamp / millisecondsToDays).toInt() }
                .toMutableList()
        notifyDataSetChanged()
    }
}