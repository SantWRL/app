package br.ufpi.lgpd.educacional.ui.lessons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.ufpi.lgpd.educacional.data.model.Lesson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para a tela de lições
 */
class LessonsViewModel : ViewModel() {

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadLessons() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Simular carregamento de dados
                _lessons.value = getMockLessons()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectLesson(lesson: Lesson) {
        _selectedLesson.value = lesson
    }

    private fun getMockLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = 1,
                title = "Introdução à LGPD",
                description = "Aprenda os conceitos básicos da Lei Geral de Proteção de Dados",
                content = "Content",
                category = "Fundamentos",
                orderIndex = 1,
                estimatedTime = 15,
                difficulty = "BEGINNER"
            ),
            Lesson(
                id = 2,
                title = "10 Princípios Fundamentais",
                description = "Conheça os 10 princípios que guiam a LGPD",
                content = "Content",
                category = "Fundamentos",
                orderIndex = 2,
                estimatedTime = 20,
                difficulty = "BEGINNER"
            ),
            Lesson(
                id = 3,
                title = "Direitos dos Titulares",
                description = "Explore todos os direitos como titular de dados",
                content = "Content",
                category = "Direitos",
                orderIndex = 3,
                estimatedTime = 25,
                difficulty = "INTERMEDIATE"
            ),
            Lesson(
                id = 4,
                title = "Atores da LGPD",
                description = "Entenda o papel de cada ator na proteção de dados",
                content = "Content",
                category = "Atores",
                orderIndex = 4,
                estimatedTime = 20,
                difficulty = "INTERMEDIATE"
            ),
            Lesson(
                id = 5,
                title = "Segurança de Dados",
                description = "Medidas técnicas e administrativas para proteção",
                content = "Content",
                category = "Segurança",
                orderIndex = 5,
                estimatedTime = 30,
                difficulty = "INTERMEDIATE"
            ),
            Lesson(
                id = 6,
                title = "LGPD no Contexto Acadêmico",
                description = "Aplicação prática da LGPD na universidade",
                content = "Content",
                category = "Aplicação",
                orderIndex = 6,
                estimatedTime = 25,
                difficulty = "ADVANCED"
            )
        )
    }
}
