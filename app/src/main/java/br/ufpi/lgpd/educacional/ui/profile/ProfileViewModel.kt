package br.ufpi.lgpd.educacional.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para o perfil do usuário.
 */
class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(
        UserProfile("Usuário", "", 1, 0, 0, 0, 0.0)
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _achievements = MutableStateFlow<List<AchievementItem>>(emptyList())
    val achievements: StateFlow<List<AchievementItem>> = _achievements.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadUserProfile(
        savedName: String = "João Silva",
        quizzesCompleted: Int = 0,
        averageScore: Double = 0.0,
        totalPoints: Int = 0
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _userProfile.value = UserProfile(
                    name = savedName.ifBlank { "João Silva" },
                    email = "aluno@ufpi.br",
                    level = calculateLevel(totalPoints),
                    totalPoints = totalPoints,
                    lessonsCompleted = 3,
                    quizzesCompleted = quizzesCompleted,
                    averageScore = averageScore
                )
                _achievements.value = listOf(
                    AchievementItem("Primeiro passo", "Concluiu a introdução à LGPD.", true),
                    AchievementItem("Guardião dos dados", "Aprendeu a identificar dados pessoais e sensíveis.", true),
                    AchievementItem("Titular consciente", "Revisou os principais direitos previstos na LGPD.", true),
                    AchievementItem("Rumo à conformidade", "Complete a aula de bases legais para desbloquear.", false),
                    AchievementItem("Resposta a incidentes", "Conclua segurança e incidentes para desbloquear.", false)
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        _userProfile.value = profile
    }

    private fun calculateLevel(totalPoints: Int): Int {
        return when {
            totalPoints >= 1500 -> 5
            totalPoints >= 1000 -> 4
            totalPoints >= 500 -> 3
            totalPoints >= 150 -> 2
            else -> 1
        }
    }
}

data class AchievementItem(
    val title: String,
    val description: String,
    val isUnlocked: Boolean
)
