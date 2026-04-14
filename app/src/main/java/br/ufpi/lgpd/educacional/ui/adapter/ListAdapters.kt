package br.ufpi.lgpd.educacional.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.ufpi.lgpd.educacional.data.model.Lesson
import br.ufpi.lgpd.educacional.data.model.Quiz
import br.ufpi.lgpd.educacional.databinding.ItemLessonListBinding
import br.ufpi.lgpd.educacional.databinding.ItemQuizListBinding

/**
 * Adapter para lista completa de lições
 */
class LessonsListAdapter(
    private val onLessonClick: (Lesson) -> Unit
) : ListAdapter<Lesson, LessonsListAdapter.LessonViewHolder>(LessonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemLessonListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LessonViewHolder(binding, onLessonClick)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LessonViewHolder(
        private val binding: ItemLessonListBinding,
        private val onLessonClick: (Lesson) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson) {
            binding.apply {
                lessonNumber.text = lesson.orderIndex.toString()
                lessonTitle.text = lesson.title
                lessonDescription.text = lesson.description
                lessonTime.text = "${lesson.estimatedTime} min"
                
                val difficultyColor = when (lesson.difficulty) {
                    "BEGINNER" -> android.graphics.Color.parseColor("#10B981")
                    "INTERMEDIATE" -> android.graphics.Color.parseColor("#F59E0B")
                    "ADVANCED" -> android.graphics.Color.parseColor("#EF4444")
                    else -> android.graphics.Color.parseColor("#6B7280")
                }
                difficultyBadge.setBackgroundColor(difficultyColor)
                difficultyBadge.text = lesson.difficulty
                
                // Mostrar status de conclusão
                if (lesson.isCompleted) {
                    completedIcon.setImageResource(android.R.drawable.ic_dialog_info)
                }
                
                root.setOnClickListener {
                    onLessonClick(lesson)
                }
            }
        }
    }

    private class LessonDiffCallback : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson) = oldItem == newItem
    }
}

/**
 * Adapter para lista completa de quizzes
 */
class QuizzesListAdapter(
    private val onQuizClick: (Quiz) -> Unit
) : ListAdapter<Quiz, QuizzesListAdapter.QuizViewHolder>(QuizDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizListBinding.inflate(
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
        private val binding: ItemQuizListBinding,
        private val onQuizClick: (Quiz) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.apply {
                quizTitle.text = quiz.title
                quizQuestions.text = "${quiz.totalQuestions} perguntas"
                quizCategory.text = quiz.category
                
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
