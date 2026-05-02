package br.ufpi.lgpd.educacional.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpi.lgpd.educacional.R
import br.ufpi.lgpd.educacional.databinding.FragmentHomeBinding
import br.ufpi.lgpd.educacional.ui.adapter.LessonCardAdapter
import br.ufpi.lgpd.educacional.ui.adapter.QuizCardAdapter
import br.ufpi.lgpd.educacional.ui.lessons.LessonDetailFragment
import br.ufpi.lgpd.educacional.ui.quizzes.QuizDetailFragment
import br.ufpi.lgpd.educacional.util.UserPreferences
import kotlinx.coroutines.launch

/**
 * HomeFragment - Tela inicial do app
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences

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

        userPreferences = UserPreferences(requireContext())
        setupRecyclerViews()
        setupGovLink()
        observeData()
        loadContent()
        updateGreeting()
    }

    private fun setupGovLink() {
        binding.btnGovLink.setOnClickListener {
            val url = "https://www.gov.br/anpd/pt-br"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                android.widget.Toast.makeText(requireContext(), "Não foi possível abrir o navegador.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateGreeting() {
        binding.homeGreeting.text = getString(R.string.home_greeting, userPreferences.userName)
        binding.homeDescription.text = getString(R.string.home_summary)
    }

    private fun setupRecyclerViews() {
        lessonAdapter = LessonCardAdapter { lesson ->
            viewModel.selectLesson(lesson)
            val args = Bundle().apply {
                putInt(LessonDetailFragment.ARG_LESSON_ID, lesson.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_lessonDetailFragment, args)
        }

        quizAdapter = QuizCardAdapter { quiz ->
            viewModel.selectQuiz(quiz)
            val args = Bundle().apply {
                putInt(QuizDetailFragment.ARG_QUIZ_ID, quiz.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_quizDetailFragment, args)
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

    override fun onResume() {
        super.onResume()
        updateGreeting()
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
