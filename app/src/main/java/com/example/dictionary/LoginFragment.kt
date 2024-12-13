package com.example.dictionary

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dictionary.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

// TODO: Добаваить viewModel для логики приложения
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        initButtonEvents()
        initLifecycleScopes()
    }

    private fun initLifecycleScopes() {
        lifecycleScope.launch {
            viewModel.loginTitleText.collect {
                binding.title.text = it
            }
        }
        lifecycleScope.launch {
            viewModel.password.collect { password ->
                updatePasswordViewOnEnter(password)
            }
        }
        lifecycleScope.launch {
            viewModel.wasIncorrectPassword.collect {
                if (it) {
                    viewModel.wasIncorrectPassword.value = false
                    showInvalidPasswordToast()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.completeLogin.collect {
                if (it) {
                    findNavController().navigate(R.id.action_loginFragment_to_diaryEntriesFragment)
                }
            }
        }
    }

    private fun updatePasswordViewOnEnter(password: String) {
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
        ).forEach {
            Glide.with(this).load(R.drawable.circle_empty).into(it)
        }
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
                viewModel.handle(LoginEvent.EnterEvent(button.text.toString()))
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
}