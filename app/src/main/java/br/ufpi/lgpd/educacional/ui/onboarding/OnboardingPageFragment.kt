package br.ufpi.lgpd.educacional.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.ufpi.lgpd.educacional.R
import br.ufpi.lgpd.educacional.databinding.FragmentOnboardingPageBinding

/**
 * Fragment para exibir uma página do onboarding
 */
class OnboardingPageFragment : Fragment() {

    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var subtitle: String? = null
    private var iconResId: Int = R.drawable.ic_shield_lock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            subtitle = it.getString(ARG_SUBTITLE)
            iconResId = it.getInt(ARG_ICON, R.drawable.ic_shield_lock)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.apply {
            pageTitle.text = title
            pageSubtitle.text = subtitle
            pageIcon.setImageResource(iconResId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_SUBTITLE = "subtitle"
        private const val ARG_ICON = "icon"

        fun newInstance(title: String, subtitle: String, iconResId: Int = R.drawable.ic_shield_lock) =
            OnboardingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_SUBTITLE, subtitle)
                    putInt(ARG_ICON, iconResId)
                }
            }
    }
}
