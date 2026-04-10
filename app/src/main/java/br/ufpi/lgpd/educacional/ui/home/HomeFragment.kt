package br.ufpi.lgpd.educacional.ui.home

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
import br.ufpi.lgpd.educacional.databinding.FragmentHomeBinding
import br.ufpi.lgpd.educacional.ui.adapter.LessonCardAdapter
import br.ufpi.lgpd.educacional.ui.adapter.QuizCardAdapter
import kotlinx.coroutines.launch

/**
 * HomeFragment - Tela inicial do app
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var lessonAdapter: LessonCardAdapter
    private lateinit var quizAdapter: QuizCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeData()
        loadContent()
    }

    private fun setupRecyclerViews() {
        lessonAdapter = LessonCardAdapter { lesson ->
            // Navegar para detalhe da lição
            viewModel.selectLesson(lesson)
        }

        quizAdapter = QuizCardAdapter { quiz ->
            // Navegar para quiz
            viewModel.selectQuiz(quiz)
        }

        binding.lessonsRecyclerView.apply {
            adapter = lessonAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.quizzesRecyclerView.apply {
            adapter = quizAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                
                // Observar lições
                launch {
                    viewModel.lessons.collect { lessons ->
                        lessonAdapter.submitList(lessons)
                    }
                }

                // Observar quizzes
                launch {
                    viewModel.quizzes.collect { quizzes ->
                        quizAdapter.submitList(quizzes)
                    }
                }

                // Observar progresso do usuário
                launch {
                    viewModel.userProgress.collect { stats ->
                        updateProgressUI(stats)
                    }
                }
            }
        }
    }

    private fun updateProgressUI(stats: UserProgressStats) {
        binding.apply {
            progressPercentage.text = "${stats.completionPercentage}%"
            progressBar.progress = stats.completionPercentage
            lessonsCompletedText.text = "${stats.lessonsCompleted}/${stats.totalLessons}"
            pointsText.text = "${stats.totalPoints} pontos"
        }
    }

    private fun loadContent() {
        viewModel.loadLessons()
        viewModel.loadQuizzes()
        viewModel.loadUserProgress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**
 * Classe para estatísticas do usuário
 */
data class UserProgressStats(
    val lessonsCompleted: Int,
    val totalLessons: Int,
    val completionPercentage: Int,
    val totalPoints: Int,
    val currentLevel: Int
)
