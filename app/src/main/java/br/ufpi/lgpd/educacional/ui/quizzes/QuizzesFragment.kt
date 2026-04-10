package br.ufpi.lgpd.educacional.ui.quizzes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpi.lgpd.educacional.databinding.FragmentQuizzesBinding
import br.ufpi.lgpd.educacional.ui.adapter.QuizzesListAdapter
import kotlinx.coroutines.launch

/**
 * QuizzesFragment - Tela listando todos os quizzes disponíveis
 */
class QuizzesFragment : Fragment() {

    private var _binding: FragmentQuizzesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuizzesViewModel by viewModels()

    private lateinit var adapter: QuizzesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizzesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        viewModel.loadQuizzes()
    }

    private fun setupRecyclerView() {
        adapter = QuizzesListAdapter { quiz ->
            viewModel.selectQuiz(quiz)
            // Navegar para o quiz
        }

        binding.quizzesRecyclerView.apply {
            adapter = this@QuizzesFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.quizzes.collect { quizzes ->
                    adapter.submitList(quizzes)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
