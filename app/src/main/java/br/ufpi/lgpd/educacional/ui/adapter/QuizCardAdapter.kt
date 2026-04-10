package br.ufpi.lgpd.educacional.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.ufpi.lgpd.educacional.data.model.Quiz
import br.ufpi.lgpd.educacional.databinding.ItemQuizCardBinding

/**
 * Adapter para lista de quizzes em formato de card
 */
class QuizCardAdapter(
    private val onQuizClick: (Quiz) -> Unit
) : ListAdapter<Quiz, QuizCardAdapter.QuizViewHolder>(QuizDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuizViewHolder(binding, onQuizClick)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class QuizViewHolder(
        private val binding: ItemQuizCardBinding,
        private val onQuizClick: (Quiz) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.apply {
                quizTitle.text = quiz.title
                quizDescription.text = quiz.description
                quizQuestions.text = "${quiz.totalQuestions} perguntas"
                
                // Definir cor baseado em dificuldade
                val difficultyColor = when (quiz.difficulty) {
                    "BEGINNER" -> android.graphics.Color.parseColor("#10B981")
                    "INTERMEDIATE" -> android.graphics.Color.parseColor("#F59E0B")
                    "ADVANCED" -> android.graphics.Color.parseColor("#EF4444")
                    else -> android.graphics.Color.parseColor("#6B7280")
                }
                difficultyBadge.setBackgroundColor(difficultyColor)
                difficultyBadge.text = quiz.difficulty
                
                root.setOnClickListener {
                    onQuizClick(quiz)
                }
            }
        }
    }

    private class QuizDiffCallback : DiffUtil.ItemCallback<Quiz>() {
        override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz) = oldItem == newItem
    }
}
