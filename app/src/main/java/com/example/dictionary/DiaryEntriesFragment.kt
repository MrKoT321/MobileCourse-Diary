package com.example.dictionary

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.dictionary.databinding.FragmentDiaryEntriesBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class DiaryEntriesFragment : Fragment(R.layout.fragment_diary_entries) {

    private lateinit var binding: FragmentDiaryEntriesBinding
    private lateinit var adapter: DiaryEntriesAdapter
    var isFiltered = false

    private val viewModel: DiaryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDiaryEntriesBinding.bind(view)

        initAvatarImage()
        initRecyclerView()
        initNavigationEvents()
        initFilterEvent()

        lifecycleScope.launch {
            viewModel.records.collect { records ->
                render(records.toMutableList())
                adapter.updateData(records)
            }
        }
    }

    private fun render(records: MutableList<Record>) {
        binding.emptyDiaryPage.isInvisible = records.isNotEmpty()
        binding.recyclerView.isInvisible = records.isEmpty()
        binding.dateFilter.isInvisible = records.isEmpty()
        binding.createBtn.isInvisible = records.isEmpty()
    }

    private fun initAvatarImage() {
        Glide.with(this).load(R.drawable.default_avatar_image).into(binding.avatar)
    }

    private fun initRecyclerView() {
        val itemClickCallback: (Record) -> Unit = {
            val arguments = Bundle().apply {
                putString(ArgumentFields.UID.value, it.uid)
            }
            findNavController().navigate(
                R.id.action_diaryEntriesFragment_to_recordFragment,
                arguments
            )
        }
        val itemDeleteCallback: (Record) -> Unit = {
            viewModel.deleteRecord(record = it)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(context, 1)
        binding.recyclerView.addItemDecoration(RecordEntriesDecoration(resources))
        adapter = DiaryEntriesAdapter(
            viewModel.records.value.toMutableList(),
            itemClickCallback,
            itemDeleteCallback
        )
        binding.recyclerView.adapter = adapter
    }

    private fun initNavigationEvents() {
        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_diaryEntriesFragment_to_recordFragment)
        }
        binding.btnCreateRecord.setOnClickListener {
            findNavController().navigate(R.id.action_diaryEntriesFragment_to_recordFragment)
        }
    }

    private fun initFilterEvent() {
        binding.dateFilter.setOnClickListener {
            if (!isFiltered) {
                createDatePicker()
            } else {
                isFiltered = false
                Glide.with(this).load(R.drawable.date_picker).into(binding.dateFilter)
                adapter.updateData(viewModel.records.value)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createDatePicker() {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, yearSelected, monthOfYear, dayOfMonth ->
                adapter.filterDataByTimestamp(SimpleDateFormat("dd-MM-yyyy").parse("$dayOfMonth-${monthOfYear + 1}-$yearSelected").time)
                isFiltered = true
                Glide.with(this).load(R.drawable.date_picker_selected).into(binding.dateFilter)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}