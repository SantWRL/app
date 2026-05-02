package br.ufpi.lgpd.educacional.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Preferências SharedPreferences para dados de uso rápido (nome, onboarding, etc.).
 * Dados de progresso persistidos devem ser consultados via [UserRepository] + Room.
 */
class UserPreferences(context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // ─── Nome ──────────────────────────────────────────────────────────────────

    var userName: String
        get() = preferences.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
        set(value) {
            preferences.edit()
                .putString(KEY_USER_NAME, value.trim().takeIf { it.isNotBlank() } ?: DEFAULT_NAME)
                .apply()
        }

    // ─── Bio ───────────────────────────────────────────────────────────────────

    var userBio: String
        get() = preferences.getString(KEY_USER_BIO, "") ?: ""
        set(value) {
            preferences.edit().putString(KEY_USER_BIO, value.trim()).apply()
        }

    // ─── Tipo de perfil ────────────────────────────────────────────────────────

    var profileType: String
        get() = preferences.getString(KEY_PROFILE_TYPE, "student") ?: "student"
        set(value) {
            preferences.edit().putString(KEY_PROFILE_TYPE, value).apply()
        }

    // ─── Cor do avatar ─────────────────────────────────────────────────────────

    var avatarColorIndex: Int
        get() = preferences.getInt(KEY_AVATAR_COLOR, 0)
        set(value) {
            preferences.edit().putInt(KEY_AVATAR_COLOR, value).apply()
        }

    // ─── Onboarding ────────────────────────────────────────────────────────────

    var onboardingCompleted: Boolean
        get() = preferences.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) {
            preferences.edit().putBoolean(KEY_ONBOARDING_DONE, value).apply()
        }

    // ─── Quizzes (legado – mantido para compatibilidade) ──────────────────────

    val quizzesCompleted: Int
        get() = completedQuizIds.size

    val averageQuizScore: Double
        get() {
            val ids = completedQuizIds
            if (ids.isEmpty()) return 0.0
            return ids.sumOf { quizId ->
                preferences.getInt(scoreKey(quizId.toInt()), 0)
            }.toDouble() / ids.size
        }

    val totalPoints: Int
        get() = completedQuizIds.sumOf { quizId ->
            preferences.getInt(scoreKey(quizId.toInt()), 0)
        } * POINTS_MULTIPLIER

    fun saveQuizResult(quizId: Int, score: Int) {
        val updatedIds = completedQuizIds.toMutableSet().apply {
            add(quizId.toString())
        }
        preferences.edit()
            .putStringSet(KEY_COMPLETED_QUIZZES, updatedIds)
            .putInt(scoreKey(quizId), score)
            .apply()
    }

    fun getQuizScore(quizId: Int): Int {
        return preferences.getInt(scoreKey(quizId), -1)
    }

    // ─── Auxiliares ────────────────────────────────────────────────────────────

    fun getInitials(): String {
        val words = userName.trim().split(" ").filter { it.isNotBlank() }
        return when {
            words.isEmpty() -> "U"
            words.size == 1 -> words.first().take(2).uppercase()
            else -> (words.first().first().toString() + words.last().first().toString()).uppercase()
        }
    }

    private val completedQuizIds: Set<String>
        get() = preferences.getStringSet(KEY_COMPLETED_QUIZZES, emptySet()).orEmpty()

    private fun scoreKey(quizId: Int) = "quiz_score_$quizId"

    fun clearAll() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val PREFS_FILE = "lgpd_educacional_prefs"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_BIO = "user_bio"
        private const val KEY_PROFILE_TYPE = "profile_type"
        private const val KEY_AVATAR_COLOR = "avatar_color_index"
        private const val KEY_ONBOARDING_DONE = "onboarding_completed"
        private const val KEY_COMPLETED_QUIZZES = "completed_quizzes"
        private const val DEFAULT_NAME = "Usuário"
        private const val POINTS_MULTIPLIER = 5
    }
}
