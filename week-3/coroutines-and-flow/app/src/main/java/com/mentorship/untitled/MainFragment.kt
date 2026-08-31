package com.mentorship.untitled

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.mentorship.untitled.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadScreenData() // винести в ViewModel().init {} напевно? мені тут не подобається, може випадково перезавантажити при зміні конфігу
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pressLike.setOnClickListener { viewModel.pressLike() } // нам не потрібно реасайнити кожен раз в колекті
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { // стартуємо/кенселимо, коли лайфсайкл стартує/ендиться
                viewModel.uiState.collectLatest { uiState -> // теж можна лейтест, думаю нам не важливо кожну отримувати, а тільки останню
                    binding.textView.text = uiState.chatName
                }
            }
        }
        lifecycleScope.launch { // інший скоуп, коллект це саспенд тому без цього ніколи не стартане цей колект
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvents.collectLatest { event ->
                    when (event) {
                        UiAction.OnLikePressed -> Snackbar.make(
                            binding.root,
                            "Liked",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
