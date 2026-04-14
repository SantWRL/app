package br.ufpi.lgpd.educacional.ui.quizzes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufpi.lgpd.educacional.data.model.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para a tela de quizzes.
 */
class QuizzesViewModel : ViewModel() {

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes.asStateFlow()

    private val _selectedQuiz = MutableStateFlow<Quiz?>(null)
    val selectedQuiz: StateFlow<Quiz?> = _selectedQuiz.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadQuizzes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _quizzes.value = getMockQuizzes()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectQuiz(quiz: Quiz) {
        _selectedQuiz.value = quiz
    }

    private fun getMockQuizzes(): List<Quiz> {
        return listOf(
            Quiz(1, "Quiz Fundamentos", "Teste seus conhecimentos basicos", "Fundamentos", "BEGINNER", 5),
            Quiz(2, "Quiz Principios", "Avalie sua compreensao dos principios", "Fundamentos", "BEGINNER", 8),
            Quiz(3, "Quiz Direitos", "Teste conhecimento sobre direitos", "Direitos", "INTERMEDIATE", 10),
            Quiz(4, "Quiz Atores", "Desafio sobre os atores da LGPD", "Atores", "INTERMEDIATE", 7),
            Quiz(5, "Quiz Seguranca", "Questoes sobre seguranca de dados", "Seguranca", "INTERMEDIATE", 12),
            Quiz(6, "Quiz Final", "Teste abrangente sobre LGPD", "Aplicacao", "ADVANCED", 20)
        )
    }
}
