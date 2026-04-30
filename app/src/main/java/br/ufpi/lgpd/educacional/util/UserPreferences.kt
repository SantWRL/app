package br.ufpi.lgpd.educacional.util

import android.content.Context

class UserPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    var userName: String
        get() = preferences.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME
        set(value) {
            preferences.edit()
                .putString(KEY_USER_NAME, value.trim().takeIf { it.isNotBlank() } ?: DEFAULT_NAME)
                .apply()
        }

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

    companion object {
        private const val PREFS_FILE = "lgpd_educacional_prefs"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_COMPLETED_QUIZZES = "completed_quizzes"
        private const val DEFAULT_NAME = "Usuário"
        private const val POINTS_MULTIPLIER = 5
    }
}
