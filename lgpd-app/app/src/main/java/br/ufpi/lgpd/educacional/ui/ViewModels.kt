package br.ufpi.lgpd.educacional.ui.quizzes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufpi.lgpd.educacional.data.model.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para a tela de quizzes
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
            Quiz(1, "Quiz Fundamentos", "Teste seus conhecimentos básicos", "Fundamentos", "BEGINNER", 5),
            Quiz(2, "Quiz Princípios", "Avalie sua compreensão dos princípios", "Fundamentos", "BEGINNER", 8),
            Quiz(3, "Quiz Direitos", "Teste conhecimento sobre direitos", "Direitos", "INTERMEDIATE", 10),
            Quiz(4, "Quiz Atores", "Desafio sobre os atores da LGPD", "Atores", "INTERMEDIATE", 7),
            Quiz(5, "Quiz Segurança", "Questões sobre segurança de dados", "Segurança", "INTERMEDIATE", 12),
            Quiz(6, "Quiz Final", "Teste abrangente sobre LGPD", "Aplicação", "ADVANCED", 20)
        )
    }
}

---

package br.ufpi.lgpd.educacional.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para o perfil do usuário
 */
class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile>(
        UserProfile("Usuário", "", 1, 0, 0, 0, 0.0)
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _achievements = MutableStateFlow<List<String>>(emptyList())
    val achievements: StateFlow<List<String>> = _achievements.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Simular carregamento
                _userProfile.value = UserProfile(
                    name = "João Silva",
                    email = "joao@ufpi.br",
                    level = 2,
                    totalPoints = 350,
                    lessonsCompleted = 3,
                    quizzesCompleted = 2,
                    averageScore = 78.5
                )
                _achievements.value = listOf(
                    "Primeiro Passo",
                    "Estrela em Ascensão"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        _userProfile.value = profile
    }
}
