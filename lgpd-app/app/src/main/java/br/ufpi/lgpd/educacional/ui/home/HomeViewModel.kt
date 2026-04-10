package br.ufpi.lgpd.educacional.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufpi.lgpd.educacional.data.model.Lesson
import br.ufpi.lgpd.educacional.data.model.Quiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para a tela inicial
 */
class HomeViewModel : ViewModel() {

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes.asStateFlow()

    private val _userProgress = MutableStateFlow<UserProgressStats>(
        UserProgressStats(0, 0, 0, 0, 1)
    )
    val userProgress: StateFlow<UserProgressStats> = _userProgress.asStateFlow()

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    private val _selectedQuiz = MutableStateFlow<Quiz?>(null)
    val selectedQuiz: StateFlow<Quiz?> = _selectedQuiz.asStateFlow()

    fun loadLessons() {
        viewModelScope.launch {
            // Aqui você carregará as lições do repositório
            _lessons.value = getMockLessons()
        }
    }

    fun loadQuizzes() {
        viewModelScope.launch {
            // Aqui você carregará os quizzes do repositório
            _quizzes.value = getMockQuizzes()
        }
    }

    fun loadUserProgress() {
        viewModelScope.launch {
            // Aqui você carregará o progresso do usuário
            _userProgress.value = UserProgressStats(
                lessonsCompleted = 3,
                totalLessons = 6,
                completionPercentage = 50,
                totalPoints = 250,
                currentLevel = 2
            )
        }
    }

    fun selectLesson(lesson: Lesson) {
        _selectedLesson.value = lesson
    }

    fun selectQuiz(quiz: Quiz) {
        _selectedQuiz.value = quiz
    }

    // Mock data para desenvolvimento
    private fun getMockLessons(): List<Lesson> {
        return listOf(
            Lesson(1, "Introdução à LGPD", "Aprenda os conceitos básicos", "Content", "Fundamentos", 1, 15, "BEGINNER"),
            Lesson(2, "Princípios Fundamentais", "Os 10 princípios da LGPD", "Content", "Fundamentos", 2, 20, "BEGINNER"),
            Lesson(3, "Seus Direitos", "Direitos dos titulares de dados", "Content", "Direitos", 3, 25, "INTERMEDIATE"),
            Lesson(4, "Segurança de Dados", "Medidas de proteção de dados", "Content", "Segurança", 4, 30, "INTERMEDIATE"),
            Lesson(5, "LGPD no Contexto Acadêmico", "Aplicação prática na universidade", "Content", "Aplicação", 5, 20, "ADVANCED"),
            Lesson(6, "Casos de Estudo", "Exemplos reais de conformidade", "Content", "Aplicação", 6, 25, "ADVANCED")
        )
    }

    private fun getMockQuizzes(): List<Quiz> {
        return listOf(
            Quiz(1, "Quiz Fundamentos", "Teste seus conhecimentos básicos", "Fundamentos", "BEGINNER", 5),
            Quiz(2, "Quiz Princípios", "Avalie sua compreensão dos princípios", "Fundamentos", "BEGINNER", 8),
            Quiz(3, "Quiz Direitos", "Teste conhecimento sobre direitos", "Direitos", "INTERMEDIATE", 10),
            Quiz(4, "Quiz Segurança", "Desafio sobre segurança de dados", "Segurança", "INTERMEDIATE", 12)
        )
    }
}
