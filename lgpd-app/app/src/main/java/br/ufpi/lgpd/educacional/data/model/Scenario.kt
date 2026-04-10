package br.ufpi.lgpd.educacional.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de dados para Cenários Educacionais
 */
@Entity(tableName = "scenarios")
data class Scenario(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val context: String,
    val situation: String,
    val difficulty: String,
    var isCompleted: Boolean = false
)

/**
 * Opção de resposta para um cenário
 */
data class ScenarioOption(
    val id: String,
    val scenarioId: Int,
    val text: String,
    val explanation: String,
    val isCorrect: Boolean,
    val principles: List<String>, // Princípios LGPD envolvidos
    val rights: List<String> // Direitos dos titulares envolvidos
)

/**
 * Resposta do usuário em um cenário
 */
@Entity(tableName = "scenario_answers")
data class ScenarioAnswer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val scenarioId: Int,
    val selectedOptionId: String,
    val isCorrect: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Exemplo de violação LGPD
 */
data class ViolationExample(
    val id: String,
    val title: String,
    val description: String,
    val principlesViolated: List<String>,
    val consequences: String,
    val correctApproach: String
)
