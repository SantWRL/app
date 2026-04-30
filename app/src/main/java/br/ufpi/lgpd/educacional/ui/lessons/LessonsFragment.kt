package br.ufpi.lgpd.educacional.ui.lessons

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
import br.ufpi.lgpd.educacional.data.model.Lesson
import br.ufpi.lgpd.educacional.databinding.FragmentLessonsBinding
import br.ufpi.lgpd.educacional.ui.adapter.LessonsListAdapter
import kotlinx.coroutines.launch

/**
 * LessonsFragment - Tela listando todas as lições disponíveis
 */
class LessonsFragment : Fragment() {

    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LessonsViewModel by viewModels()

    private lateinit var adapter: LessonsListAdapter
    private var allLessons: List<Lesson> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFilters()
        observeData()
        viewModel.loadLessons()
    }

    private fun setupRecyclerView() {
        adapter = LessonsListAdapter { lesson ->
            viewModel.selectLesson(lesson)
            // Navegar para detalhes da lição
        }

        binding.lessonsRecyclerView.apply {
            adapter = this@LessonsFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupFilters() {
        binding.filterAll.setOnClickListener { adapter.submitList(allLessons) }
        binding.filterBeginner.setOnClickListener { filterByDifficulty("BEGINNER") }
        binding.filterIntermediate.setOnClickListener { filterByDifficulty("INTERMEDIATE") }
        binding.filterAdvanced.setOnClickListener { filterByDifficulty("ADVANCED") }
    }

    private fun filterByDifficulty(difficulty: String) {
        adapter.submitList(allLessons.filter { it.difficulty == difficulty })
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lessons.collect { lessons ->
                    allLessons = lessons
                    adapter.submitList(lessons)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
