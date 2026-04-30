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
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufpi.lgpd.educacional.R
import br.ufpi.lgpd.educacional.databinding.FragmentProfileBinding
import br.ufpi.lgpd.educacional.ui.adapter.AchievementAdapter
import br.ufpi.lgpd.educacional.util.UserPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * ProfileFragment - Tela de perfil e progresso do usuário
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences
    private lateinit var achievementAdapter: AchievementAdapter

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

        userPreferences = UserPreferences(requireContext())
        setupAchievements()
        setupProfileEditor()
        observeData()
        loadProfile()
    }

    private fun setupAchievements() {
        achievementAdapter = AchievementAdapter()
        binding.achievementsRecyclerView.apply {
            adapter = achievementAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProfileEditor() {
        binding.nameInput.setText(userPreferences.userName)
        binding.saveNameButton.setOnClickListener {
            val typedName = binding.nameInput.text?.toString().orEmpty().trim()
            if (typedName.isNotBlank()) {
                userPreferences.userName = typedName
                loadProfile()
                Snackbar.make(binding.root, getString(R.string.profile_saved_name), Snackbar.LENGTH_SHORT).show()
            } else {
                binding.nameInput.error = getString(R.string.profile_name_hint)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfile.collect { profile ->
                    updateUI(profile)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.achievements.collect { achievements ->
                    achievementAdapter.submitList(achievements)
                }
            }
        }
    }

    private fun loadProfile() {
        viewModel.loadUserProfile(
            savedName = userPreferences.userName,
            quizzesCompleted = userPreferences.quizzesCompleted,
            averageScore = userPreferences.averageQuizScore,
            totalPoints = userPreferences.totalPoints
        )
    }

    private fun updateUI(profile: UserProfile) {
        binding.apply {
            userName.text = profile.name
            userLevel.text = "Nível ${profile.level} - trilha LGPD"
            userPoints.text = "${profile.totalPoints} pontos"
            lessonsCompleted.text = profile.lessonsCompleted.toString()
            quizzesCompleted.text = profile.quizzesCompleted.toString()
            averageScore.text = "%.1f%%".format(profile.averageScore)
            avatarInitials.text = getInitials(profile.name)
            if (nameInput.text.isNullOrBlank()) {
                nameInput.setText(profile.name)
            }
        }
    }

    private fun getInitials(name: String): String {
        val words = name.trim().split(" ").filter { it.isNotBlank() }
        return when {
            words.isEmpty() -> "U"
            words.size == 1 -> words.first().take(2).uppercase()
            else -> (words.first().first().toString() + words.last().first().toString()).uppercase()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (::userPreferences.isInitialized) {
            loadProfile()
        }
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
