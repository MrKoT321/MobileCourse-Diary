package com.example.dictionary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.dictionary.databinding.FragmentRecordBinding
import java.util.Date
import java.util.UUID

class RecordFragment : Fragment(R.layout.fragment_record) {
    private lateinit var binding: FragmentRecordBinding
    private val viewModel: DiaryViewModel by activityViewModels()
    private var uid: String = ""

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
                saveRecord(createRecordFromFields())
                findNavController().navigate(R.id.action_recordFragment_to_diaryEntriesFragment)
            }
        }
    }

    private fun createRecordFromFields(): Record {
        return Record(
            uid.ifEmpty { UUID.randomUUID().toString() },
            binding.recordTitle.text.toString(),
            binding.recordContent.text.toString(),
            Date().time
        )
    }

    private fun saveRecord(record: Record) {
        viewModel.addRecord(record)
    }

    private fun render() {
        val receivedUid = arguments?.getString(ArgumentFields.UID.value)
        if (!receivedUid.isNullOrEmpty()) {
            uid = receivedUid
            val record = viewModel.findRecord(receivedUid)
            if (record != null) {
                binding.recordTitle.setText(record.title)
                binding.recordContent.setText(record.content)
            }
        } else {
            uid = ""
        }
    }
}