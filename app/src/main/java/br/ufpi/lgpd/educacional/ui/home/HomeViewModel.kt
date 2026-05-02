package br.ufpi.lgpd.educacional.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.ufpi.lgpd.educacional.data.LgpdContent
import br.ufpi.lgpd.educacional.data.database.AppDatabase
import br.ufpi.lgpd.educacional.data.model.Lesson
import br.ufpi.lgpd.educacional.data.model.Quiz
import br.ufpi.lgpd.educacional.data.repository.UserRepository
import br.ufpi.lgpd.educacional.util.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para a tela inicial.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository by lazy {
        UserRepository(AppDatabase.getInstance(application).userDao())
    }
    private val prefs: UserPreferences by lazy { UserPreferences(application) }

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
    val quizzes: StateFlow<List<Quiz>> = _quizzes.asStateFlow()

    private val _userProgress = MutableStateFlow(
        UserProgressStats(0, 0, 0, 0, 1)
    )
    val userProgress: StateFlow<UserProgressStats> = _userProgress.asStateFlow()

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    private val _selectedQuiz = MutableStateFlow<Quiz?>(null)
    val selectedQuiz: StateFlow<Quiz?> = _selectedQuiz.asStateFlow()

    fun loadLessons() {
        viewModelScope.launch {
            _lessons.value = LgpdContent.lessons.take(5) // Mostra apenas as primeiras na Home
        }
    }

    fun loadQuizzes() {
        viewModelScope.launch {
            _quizzes.value = getMockQuizzes()
        }
    }

    fun loadUserProgress() {
        viewModelScope.launch {
            repository.ensureUserExists()
            repository.updateStreak()
            val user = repository.getUser()
            val completed = user?.lessonsCompleted ?: 0
            val points = user?.totalPoints ?: 0
            val total = 10
            val pct = if (total == 0) 0 else ((completed.toDouble() / total) * 100).toInt()
            _userProgress.value = UserProgressStats(
                lessonsCompleted = completed,
                totalLessons = total,
                completionPercentage = pct,
                totalPoints = points,
                currentLevel = user?.level ?: 1
            )
        }
    }

    fun selectLesson(lesson: Lesson) {
        _selectedLesson.value = lesson
    }

    fun selectQuiz(quiz: Quiz) {
        _selectedQuiz.value = quiz
    }


    private fun getMockQuizzes(): List<Quiz> {
        return LgpdContent.quizzes
    }
}
