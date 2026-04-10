package br.ufpi.lgpd.educacional.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.ufpi.lgpd.educacional.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

/**
 * ProfileFragment - Tela de perfil e progresso do usuário
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        viewModel.loadUserProfile()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfile.collect { profile ->
                    updateUI(profile)
                }
            }
        }
    }

    private fun updateUI(profile: UserProfile) {
        binding.apply {
            userName.text = profile.name
            userLevel.text = "Nível ${profile.level}"
            userPoints.text = "${profile.totalPoints} pontos"
            lessonsCompleted.text = "${profile.lessonsCompleted} concluídas"
            quizzesCompleted.text = "${profile.quizzesCompleted} resolvidos"
            averageScore.text = "%.1f%%".format(profile.averageScore)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class UserProfile(
    val name: String,
    val email: String,
    val level: Int,
    val totalPoints: Int,
    val lessonsCompleted: Int,
    val quizzesCompleted: Int,
    val averageScore: Double
)
