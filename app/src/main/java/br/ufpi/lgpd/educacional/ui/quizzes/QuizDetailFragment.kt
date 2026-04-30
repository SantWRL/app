package br.ufpi.lgpd.educacional.ui.quizzes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.ufpi.lgpd.educacional.R
import br.ufpi.lgpd.educacional.data.LgpdContent
import br.ufpi.lgpd.educacional.data.QuizQuestionContent
import br.ufpi.lgpd.educacional.databinding.FragmentQuizDetailBinding
import br.ufpi.lgpd.educacional.util.UserPreferences
import com.google.android.material.snackbar.Snackbar

class QuizDetailFragment : Fragment() {

    private var _binding: FragmentQuizDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var userPreferences: UserPreferences
    private lateinit var questions: List<QuizQuestionContent>

    private var quizId: Int = 1
    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private var hasAnsweredCurrentQuestion = false

    private val optionButtons: List<RadioButton>
        get() = listOf(binding.optionA, binding.optionB, binding.optionC, binding.optionD)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())
        quizId = arguments?.getInt(ARG_QUIZ_ID) ?: 1
        questions = LgpdContent.questionsForQuiz(quizId)

        binding.quizTitle.text = LgpdContent.quizzes.firstOrNull { it.id == quizId }?.title ?: "Teste LGPD"
        binding.primaryActionButton.setOnClickListener {
            if (binding.resultCard.visibility == View.VISIBLE) {
                findNavController().popBackStack()
            } else if (hasAnsweredCurrentQuestion) {
                goToNextQuestion()
            } else {
                submitAnswer()
            }
        }

        showQuestion()
    }

    private fun showQuestion() {
        val question = questions[currentQuestionIndex]
        hasAnsweredCurrentQuestion = false

        binding.questionCounter.text = getString(
            R.string.quiz_question,
            currentQuestionIndex + 1,
            questions.size
        )
        binding.quizProgress.max = questions.size
        binding.quizProgress.progress = currentQuestionIndex + 1
        binding.questionText.text = question.question
        binding.explanationCard.visibility = View.GONE
        binding.optionsGroup.clearCheck()
        binding.primaryActionButton.text = getString(R.string.button_submit)

        optionButtons.forEachIndexed { index, radioButton ->
            radioButton.text = question.options[index]
            radioButton.isEnabled = true
        }
    }

    private fun submitAnswer() {
        val selectedIndex = optionButtons.indexOfFirst { it.id == binding.optionsGroup.checkedRadioButtonId }
        if (selectedIndex == -1) {
            Snackbar.make(binding.root, "Escolha uma alternativa para continuar.", Snackbar.LENGTH_SHORT).show()
            return
        }

        val question = questions[currentQuestionIndex]
        val isCorrect = selectedIndex == question.correctAnswer
        if (isCorrect) correctAnswers++

        hasAnsweredCurrentQuestion = true
        optionButtons.forEach { it.isEnabled = false }
        binding.answerStatus.text = if (isCorrect) getString(R.string.quiz_correct) else getString(R.string.quiz_incorrect)
        binding.answerStatus.setTextColor(
            requireContext().getColor(if (isCorrect) R.color.success else R.color.error)
        )
        binding.explanationText.text = question.explanation
        binding.explanationCard.visibility = View.VISIBLE
        binding.primaryActionButton.text =
            if (currentQuestionIndex == questions.lastIndex) getString(R.string.quiz_finish) else getString(R.string.quiz_next)
    }

    private fun goToNextQuestion() {
        if (currentQuestionIndex == questions.lastIndex) {
            showResult()
            return
        }

        currentQuestionIndex++
        showQuestion()
    }

    private fun showResult() {
        val score = ((correctAnswers.toDouble() / questions.size) * 100).toInt()
        userPreferences.saveQuizResult(quizId, score)

        binding.questionCard.visibility = View.GONE
        binding.explanationCard.visibility = View.GONE
        binding.resultCard.visibility = View.VISIBLE
        binding.questionCounter.text = "Teste concluído"
        binding.quizProgress.progress = questions.size
        binding.resultScore.text = "$score%"
        binding.resultMessage.text = if (score >= PASSING_SCORE) {
            "Muito bom. Seu resultado foi salvo no perfil."
        } else {
            "Resultado salvo. Revise a trilha e tente novamente para fixar melhor."
        }
        binding.primaryActionButton.text = getString(R.string.button_back)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_QUIZ_ID = "quizId"
        private const val PASSING_SCORE = 70
    }
}
