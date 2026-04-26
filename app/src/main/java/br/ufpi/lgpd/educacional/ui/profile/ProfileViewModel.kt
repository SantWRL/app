package br.ufpi.lgpd.educacional.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para o perfil do usuario.
 */
class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(
        UserProfile("Usuario", "", 1, 0, 0, 0, 0.0)
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _achievements = MutableStateFlow<List<String>>(emptyList())
    val achievements: StateFlow<List<String>> = _achievements.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadUserProfile(savedName: String = "Joao Silva") {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _userProfile.value = UserProfile(
                    name = savedName.ifBlank { "Joao Silva" },
                    email = "joao@ufpi.br",
                    level = 2,
                    totalPoints = 350,
                    lessonsCompleted = 3,
                    quizzesCompleted = 2,
                    averageScore = 78.5
                )
                _achievements.value = listOf(
                    "Primeiro Passo",
                    "Estrela em Ascensao"
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
