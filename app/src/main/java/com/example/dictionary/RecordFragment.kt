package com.example.dictionary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentRecordBinding

// TODO: Добавить во viewModel текущую запись
// onViewStateRestored - фрагмент "хочет быть умным" и поэтому сохраняет все свои элементы: https://stackoverflow.com/questions/17792132/how-does-onviewstaterestored-from-fragments-work
class RecordFragment : Fragment(R.layout.fragment_record) {
    private lateinit var binding: FragmentRecordBinding
    private val viewModel: DiaryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRecordBinding.bind(view)

        render()

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.completeButton.setOnClickListener {
            if (binding.recordTitle.text.toString().isEmpty()) {
                binding.recordTitle.error = "Can not create without title"
            } else {
                saveRecord()
                findNavController().navigate(R.id.action_recordFragment_to_diaryEntriesFragment)
            }
        }
    }

    private fun saveRecord() {
        viewModel.saveRecord(
            binding.recordTitle.text.toString(),
            binding.recordContent.text.toString()
        )
    }

    private fun render() {
        val receivedUid = arguments?.getString(ArgumentFields.UID.value)
        if (!receivedUid.isNullOrEmpty()) {
            val record = viewModel.findRecord(receivedUid)
            if (record != null) {
                binding.recordTitle.setText(record.title)
                binding.recordContent.setText(record.content)
            }
        }
    }
}