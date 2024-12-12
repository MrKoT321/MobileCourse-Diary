package com.example.dictionary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dictionary.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private var password = ""
    private var passwordToComplete = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        initButtonEvents()

        lifecycleScope.launch {
            if (!StorageApp.passwordStorage.getPin().isNullOrEmpty())
            {
                binding.title.text = "Введите PIN-код"
            }
            passwordToComplete = StorageApp.passwordStorage.getPin() ?: ""
        }
    }

    private fun updatePasswordViewOnEnter() {
        when (password.length) {
            1 -> {
                Glide.with(this).load(R.drawable.circle_filled).into(binding.passwordIcon1)
            }

            2 -> {
                Glide.with(this).load(R.drawable.circle_filled).into(binding.passwordIcon2)
            }

            3 -> {
                Glide.with(this).load(R.drawable.circle_filled).into(binding.passwordIcon3)
            }

            4 -> {
                Glide.with(this).load(R.drawable.circle_filled).into(binding.passwordIcon4)
                checkPasswordOnComplete()
                resetPasswordView()
            }
        }
    }

    private fun resetPasswordView() {
        listOf(
            binding.passwordIcon1,
            binding.passwordIcon2,
            binding.passwordIcon3,
            binding.passwordIcon4
        ).forEach { Glide.with(this).load(R.drawable.circle_empty).into(it) }
    }

    private fun initButtonEvents() {
        listOf(
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9,
            binding.btn0,
        ).forEach { button ->
            button.setOnClickListener {
                password += button.text.toString()
                updatePasswordViewOnEnter()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkPasswordOnComplete() {
        if (passwordToComplete.isEmpty()) {
            passwordToComplete = password.also { password = passwordToComplete }
            binding.title.text = "Повторите PIN-код"
        } else {
            if (passwordToComplete == password) {
                savePassword()
                findNavController().navigate(R.id.action_loginFragment_to_diaryEntriesFragment)
            } else {
                password = ""
                showInvalidPasswordToast()
            }
        }
    }

    private fun showInvalidPasswordToast() {
        Toast.makeText(
            binding.root.context,
            "Неверный PIN-код",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun savePassword() {
        lifecycleScope.launch {
            StorageApp.passwordStorage.savePin(password)
        }
    }
}