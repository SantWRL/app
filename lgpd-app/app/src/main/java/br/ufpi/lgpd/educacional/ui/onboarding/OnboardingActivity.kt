package br.ufpi.lgpd.educacional.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.ufpi.lgpd.educacional.R
import br.ufpi.lgpd.educacional.databinding.ActivityOnboardingBinding
import br.ufpi.lgpd.educacional.ui.MainActivity
import com.google.android.material.tabs.TabLayoutMediator

/**
 * OnboardingActivity - Tela de apresentação e orientação inicial
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupButtons()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = OnboardingPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
    }

    private fun setupButtons() {
        binding.btnSkip.setOnClickListener {
            goToHome()
        }

        binding.btnNext.setOnClickListener {
            val nextPage = binding.viewPager.currentItem + 1
            if (nextPage < 3) {
                binding.viewPager.currentItem = nextPage
            } else {
                goToHome()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(
            object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.btnNext.text = if (position == 2) {
                        getString(R.string.onboarding_get_started)
                    } else {
                        getString(R.string.button_continue)
                    }
                }
            }
        )
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    inner class OnboardingPagerAdapter(activity: AppCompatActivity) :
        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int) = when (position) {
            0 -> OnboardingPageFragment.newInstance(
                "Bem-vindo ao LGPD Educacional",
                "Aprenda sobre proteção de dados de forma interativa e divertida"
            )
            1 -> OnboardingPageFragment.newInstance(
                "Proteja Sua Privacidade",
                "Entenda seus direitos e como proteger seus dados pessoais"
            )
            2 -> OnboardingPageFragment.newInstance(
                "10 Direitos Fundamentais",
                "Descubra quais são seus direitos como titular de dados"
            )
            else -> OnboardingPageFragment()
        }
    }
}
