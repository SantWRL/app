package br.ufpi.lgpd.educacional.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * ProfileFragment — tela de perfil, progresso e conquistas do usuário.
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
        setupAvatarColorPicker()
        setupClearSession()
        observeData()
    }

    // ─── Conquistas ───────────────────────────────────────────────────────────

    private fun setupAchievements() {
        achievementAdapter = AchievementAdapter()
        binding.achievementsRecyclerView.apply {
            adapter = achievementAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    // ─── Editor de perfil ─────────────────────────────────────────────────────

    private fun setupProfileEditor() {
        // Nome
        binding.nameInput.setText(userPreferences.userName)
        binding.saveNameButton.setOnClickListener {
            val name = binding.nameInput.text?.toString().orEmpty().trim()
            if (name.isNotBlank()) {
                viewModel.saveName(name)
                Snackbar.make(binding.root, getString(R.string.profile_saved_name), Snackbar.LENGTH_SHORT).show()
            } else {
                binding.nameInput.error = getString(R.string.profile_name_hint)
            }
        }

        // Bio
        binding.bioInput.setText(userPreferences.userBio)
        binding.saveBioButton.setOnClickListener {
            val bio = binding.bioInput.text?.toString().orEmpty().trim()
            viewModel.saveBio(bio)
            Snackbar.make(binding.root, "Bio salva com sucesso", Snackbar.LENGTH_SHORT).show()
        }

        // Tipo de perfil
        val profileTypes = arrayOf("Estudante", "Professor", "Técnico")
        val profileValues = arrayOf("student", "teacher", "technician")
        binding.profileTypeButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Tipo de perfil")
                .setItems(profileTypes) { _, which ->
                    viewModel.saveProfileType(profileValues[which])
                    Snackbar.make(binding.root, "Perfil atualizado para ${profileTypes[which]}", Snackbar.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    // ─── Seletor de cor do avatar ─────────────────────────────────────────────

    private fun setupAvatarColorPicker() {
        val colorViews = listOf(
            binding.color0, binding.color1, binding.color2,
            binding.color3, binding.color4, binding.color5
        )
        colorViews.forEachIndexed { index, view ->
            view.setOnClickListener {
                viewModel.saveAvatarColor(index)
            }
        }
    }

    // ─── Limpar sessão ────────────────────────────────────────────────────────

    private fun setupClearSession() {
        binding.logoutButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Limpar sessão")
                .setMessage("Isso apagará todo o progresso salvo. Deseja continuar?")
                .setPositiveButton("Sim, limpar") { _, _ ->
                    viewModel.clearSession()
                    binding.nameInput.setText("")
                    binding.bioInput.setText("")
                    Snackbar.make(binding.root, "Sessão limpa", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    // ─── Observação de dados ──────────────────────────────────────────────────

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userProfile.collect { profile ->
                        updateUI(profile)
                    }
                }
                launch {
                    viewModel.achievements.collect { list ->
                        achievementAdapter.submitList(list)
                    }
                }
                launch {
                    viewModel.isLoading.collect { loading ->
                        binding.loadingIndicator.isVisible = loading
                    }
                }
            }
        }
    }

    // ─── Atualização da UI ────────────────────────────────────────────────────

    private fun updateUI(profile: UserProfile) {
        binding.apply {
            userName.text = profile.name
            userLevel.text = "Nível ${profile.level} — trilha LGPD"
            userPoints.text = "${profile.totalPoints} pontos"
            lessonsCompleted.text = profile.lessonsCompleted.toString()
            quizzesCompleted.text = profile.quizzesCompleted.toString()
            averageScore.text = "%.1f%%".format(profile.averageScore)
            streakDays.text = "${profile.streakDays}"
            avatarInitials.text = getInitials(profile.name)
            profileTypeLabel.text = profileTypeLabel(profile.profileType)

            if (bioInput.text.isNullOrBlank()) {
                bioInput.setText(profile.bio)
            }
            if (nameInput.text.isNullOrBlank()) {
                nameInput.setText(profile.name)
            }

            // Cor do avatar
            try {
                avatarCircle.setBackgroundColor(Color.parseColor(profile.avatarColor))
                avatarInitials.setTextColor(Color.WHITE)
            } catch (_: Exception) { }

            // Destaca a cor selecionada
            highlightSelectedColor(profile.avatarColorIndex)

            // Barra de progresso de aulas (baseada em 10 aulas no total)
            val pct = ((profile.lessonsCompleted / 10.0) * 100).toInt().coerceIn(0, 100)
            lessonsProgressBar.progress = pct
            lessonsProgressText.text = "${profile.lessonsCompleted}/10 aulas"
        }
    }

    private fun highlightSelectedColor(selectedIndex: Int) {
        val colorViews = listOf(
            binding.color0, binding.color1, binding.color2,
            binding.color3, binding.color4, binding.color5
        )
        colorViews.forEachIndexed { index, view ->
            view.alpha = if (index == selectedIndex) 1f else 0.4f
            view.scaleX = if (index == selectedIndex) 1.25f else 1f
            view.scaleY = if (index == selectedIndex) 1.25f else 1f
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

    private fun profileTypeLabel(type: String) = when (type) {
        "teacher"     -> "Professor(a)"
        "technician"  -> "Técnico(a)"
        else          -> "Estudante"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFromDatabase()
    }
}
