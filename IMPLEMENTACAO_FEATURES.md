# 🚀 Guia de Implementação das Features Pendentes

Este documento fornece um roadmap detalhado para completar as features pendentes do aplicativo LGPD Educacional.

---

## 📋 Índice
1. [Room Database](#room-database)
2. [Repositórios](#repositórios)
3. [Telas Detalhadas](#telas-detalhadas)
4. [Conteúdo Educacional](#conteúdo-educacional)
5. [Notificações](#notificações)
6. [Testes](#testes)

---

## 🗄️ Room Database

### Passo 1: Criar Classes DAO

Arquivo: `app/src/main/java/br/ufpi/lgpd/educacional/data/database/LessonDao.kt`

```kotlin
package br.ufpi.lgpd.educacional.data.database

import androidx.room.*
import br.ufpi.lgpd.educacional.data.model.Lesson
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons ORDER BY orderIndex ASC")
    fun getAllLessons(): Flow<List<Lesson>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    suspend fun getLessonById(lessonId: Int): Lesson

    @Query("SELECT * FROM lessons WHERE difficulty = :difficulty")
    fun getLessonsByDifficulty(difficulty: String): Flow<List<Lesson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLessons(lessons: List<Lesson>)

    @Update
    suspend fun updateLesson(lesson: Lesson)

    @Delete
    suspend fun deleteLesson(lesson: Lesson)

    @Query("SELECT COUNT(*) FROM lessons WHERE isCompleted = 1")
    suspend fun getCompletedLessonsCount(): Int

    @Query("SELECT COUNT(*) FROM lessons")
    suspend fun getTotalLessonsCount(): Int
}
```

### Passo 2: Criar Classe Database

Arquivo: `app/src/main/java/br/ufpi/lgpd/educacional/data/database/LgpdDatabase.kt`

```kotlin
package br.ufpi.lgpd.educacional.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.ufpi.lgpd.educacional.data.model.*

@Database(
    entities = [
        Lesson::class,
        Quiz::class,
        Question::class,
        User::class,
        UserAnswer::class,
        LessonProgress::class,
        Achievement::class,
        UserAchievement::class,
        Scenario::class,
        ScenarioAnswer::class
    ],
    version = 1,
    exportSchema = false
)
abstract class LgpdDatabase : RoomDatabase() {
    
    abstract fun lessonDao(): LessonDao
    abstract fun quizDao(): QuizDao
    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao
    abstract fun scenarioDao(): ScenarioDao

    companion object {
        @Volatile
        private var INSTANCE: LgpdDatabase? = null

        fun getDatabase(context: Context): LgpdDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LgpdDatabase::class.java,
                    "lgpd_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

### Passo 3: Criar DAOs para outros modelos

Crie arquivos similares para:
- `QuizDao.kt`
- `QuestionDao.kt`
- `UserDao.kt`
- `ScenarioDao.kt`

---

## 📦 Repositórios

### Passo 1: Criar LessonRepository

Arquivo: `app/src/main/java/br/ufpi/lgpd/educacional/data/repository/LessonRepository.kt`

```kotlin
package br.ufpi.lgpd.educacional.data.repository

import br.ufpi.lgpd.educacional.data.database.LessonDao
import br.ufpi.lgpd.educacional.data.model.Lesson
import kotlinx.coroutines.flow.Flow

class LessonRepository(private val lessonDao: LessonDao) {

    fun getAllLessons(): Flow<List<Lesson>> = lessonDao.getAllLessons()

    suspend fun getLessonById(lessonId: Int): Lesson = lessonDao.getLessonById(lessonId)

    fun getLessonsByDifficulty(difficulty: String): Flow<List<Lesson>> = 
        lessonDao.getLessonsByDifficulty(difficulty)

    suspend fun updateLesson(lesson: Lesson) = lessonDao.updateLesson(lesson)

    suspend fun insertAllLessons(lessons: List<Lesson>) = lessonDao.insertAllLessons(lessons)

    suspend fun getProgress(): ProgressData {
        val completed = lessonDao.getCompletedLessonsCount()
        val total = lessonDao.getTotalLessonsCount()
        return ProgressData(completed, total)
    }

    data class ProgressData(
        val completedLessons: Int,
        val totalLessons: Int,
        val percentage: Int = if (totalLessons > 0) 
            (completedLessons * 100) / totalLessons else 0
    )
}
```

### Passo 2: Criar QuizRepository

```kotlin
package br.ufpi.lgpd.educacional.data.repository

import br.ufpi.lgpd.educacional.data.database.QuizDao
import br.ufpi.lgpd.educacional.data.database.QuestionDao
import br.ufpi.lgpd.educacional.data.model.Quiz
import br.ufpi.lgpd.educacional.data.model.Question
import br.ufpi.lgpd.educacional.data.model.QuizResult
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val quizDao: QuizDao,
    private val questionDao: QuestionDao
) {

    fun getAllQuizzes(): Flow<List<Quiz>> = quizDao.getAllQuizzes()

    suspend fun getQuizById(quizId: Int): Quiz = quizDao.getQuizById(quizId)

    suspend fun getQuestionsByQuizId(quizId: Int): List<Question> = 
        questionDao.getQuestionsByQuizId(quizId)

    suspend fun saveQuizResult(result: QuizResult) = quizDao.saveQuizResult(result)

    suspend fun getQuizResults(quizId: Int): List<QuizResult> = 
        quizDao.getQuizResults(quizId)
}
```

---

## 🎯 Telas Detalhadas

### Implementar LessonDetailActivity

Arquivo: `app/src/main/java/br/ufpi/lgpd/educacional/ui/lesson/LessonDetailActivity.kt`

```kotlin
package br.ufpi.lgpd.educacional.ui.lesson

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.ufpi.lgpd.educacional.data.model.Lesson
import br.ufpi.lgpd.educacional.databinding.ActivityLessonDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class LessonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonDetailBinding
    private lateinit var viewModel: LessonDetailViewModel
    private var lesson: Lesson? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LessonDetailViewModel::class.java)

        lesson = intent.getParcelableExtra("lesson")
        lesson?.let { setupUI(it) }

        setupViewPager()
    }

    private fun setupUI(lesson: Lesson) {
        binding.lessonTitle.text = lesson.title
        binding.lessonDescription.text = lesson.description
        binding.estimatedTime.text = "${lesson.estimatedTime} minutos"
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = LessonSectionAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Conteúdo"
                1 -> "Conceitos"
                2 -> "Referências"
                else -> ""
            }
        }.attach()
    }

    inner class LessonSectionAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int) = when (position) {
            0 -> LessonContentFragment.newInstance(lesson?.id ?: 0)
            1 -> LessonConceptsFragment.newInstance(lesson?.id ?: 0)
            2 -> LessonReferencesFragment.newInstance(lesson?.id ?: 0)
            else -> LessonContentFragment.newInstance(0)
        }
    }
}
```

### Implementar QuizActivity

Arquivo: `app/src/main/java/br/ufpi/lgpd/educacional/ui/quiz/QuizActivity.kt`

```kotlin
package br.ufpi.lgpd.educacional.ui.quiz

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.ufpi.lgpd.educacional.data.model.Quiz
import br.ufpi.lgpd.educacional.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var viewModel: QuizViewModel
    private var quiz: Quiz? = null
    private var countDownTimer: CountDownTimer? = null
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        quiz = intent.getParcelableExtra("quiz")
        quiz?.let { 
            setupUI(it)
            startQuiz()
        }

        setupListeners()
    }

    private fun setupUI(quiz: Quiz) {
        binding.quizTitle.text = quiz.title
        binding.totalQuestions.text = "0/${quiz.totalQuestions}"
        
        if (quiz.timeLimit > 0) {
            startTimer(quiz.timeLimit)
        }
    }

    private fun setupListeners() {
        binding.nextButton.setOnClickListener { nextQuestion() }
        binding.submitButton.setOnClickListener { submitAnswer() }
    }

    private fun startTimer(timeInSeconds: Int) {
        countDownTimer = object : CountDownTimer((timeInSeconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                binding.timer.text = "$minutes:${String.format("%02d", seconds)}"
            }

            override fun onFinish() {
                finishQuiz()
            }
        }.start()
    }

    private fun startQuiz() {
        viewModel.loadQuizQuestions(quiz?.id ?: 0)
    }

    private fun nextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < (quiz?.totalQuestions ?: 0)) {
            updateQuestion()
        } else {
            finishQuiz()
        }
    }

    private fun submitAnswer() {
        // Gravar resposta do usuário
        viewModel.saveUserAnswer(currentQuestionIndex)
        nextQuestion()
    }

    private fun updateQuestion() {
        binding.questionNumber.text = "${currentQuestionIndex + 1}/${quiz?.totalQuestions}"
    }

    private fun finishQuiz() {
        countDownTimer?.cancel()
        viewModel.calculateResults()
        // Navegar para tela de resultados
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
```

---

## 📚 Conteúdo Educacional

### Arquivo de Dados: content_data.json

Crie: `app/src/main/assets/content_data.json`

```json
{
  "lessons": [
    {
      "id": 1,
      "title": "Introdução à LGPD",
      "description": "Aprenda os conceitos básicos da Lei Geral de Proteção de Dados",
      "category": "Fundamentos",
      "difficulty": "BEGINNER",
      "estimatedTime": 15,
      "orderIndex": 1,
      "sections": [
        {
          "id": "intro_1",
          "title": "O que é LGPD?",
          "content": "A Lei Geral de Proteção de Dados (LGPD) é uma lei brasileira que regula como as informações pessoais são coletadas, processadas e armazenadas...",
          "type": "TEXT"
        },
        {
          "id": "intro_2",
          "title": "Por que é importante?",
          "content": "A LGPD protege direitos fundamentais de privacidade e segurança dos cidadãos brasileiros...",
          "type": "TEXT"
        }
      ],
      "keyPoints": [
        "LGPD foi instituída pela Lei nº 13.709/2018",
        "Entrou em vigor em agosto de 2020",
        "Alinha-se com o GDPR europeu",
        "Protege dados pessoais de cidadãos brasileiros"
      ],
      "references": [
        "Lei nº 13.709/2018",
        "GDPR - European Data Protection Regulation",
        "Autoridade Nacional de Proteção de Dados"
      ]
    },
    {
      "id": 2,
      "title": "10 Princípios Fundamentais",
      "description": "Conheça os 10 princípios que guiam a LGPD",
      "category": "Fundamentos",
      "difficulty": "BEGINNER",
      "estimatedTime": 20,
      "orderIndex": 2,
      "sections": [
        {
          "id": "principles_1",
          "title": "Finalidade",
          "content": "Tratamento de dados para propósitos legítimos, específicos e explícitos...",
          "type": "TEXT"
        },
        {
          "id": "principles_2",
          "title": "Adequação",
          "content": "Compatibilidade do tratamento com as finalidades informadas...",
          "type": "TEXT"
        }
      ],
      "keyPoints": [
        "1. Finalidade - propósitos explícitos",
        "2. Adequação - compatibilidade",
        "3. Necessidade - minimalismo",
        "4. Transparência - clareza",
        "5. Segurança - proteção",
        "6. Prestação de Contas - responsabilidade",
        "7. Livre Acesso - consulta gratuita",
        "8. Qualidade dos Dados - exatidão",
        "9. Não Discriminação - equidade",
        "10. Prevenção - proteção de riscos"
      ],
      "references": [
        "Art. 6º da LGPD"
      ]
    },
    {
      "id": 3,
      "title": "Direitos dos Titulares",
      "description": "Explore todos os direitos como titular de dados",
      "category": "Direitos",
      "difficulty": "INTERMEDIATE",
      "estimatedTime": 25,
      "orderIndex": 3,
      "sections": [
        {
          "id": "rights_1",
          "title": "10 Direitos Fundamentais",
          "content": "A LGPD garante diversos direitos aos titulares de dados...",
          "type": "TEXT"
        }
      ],
      "keyPoints": [
        "Confirmação de existência de tratamento",
        "Acesso aos dados coletados",
        "Correção de dados incompletos ou desatualizados",
        "Anonimização de dados",
        "Bloqueio ou eliminação de dados",
        "Portabilidade para outro fornecedor",
        "Eliminação de dados coletados com consentimento",
        "Informação sobre compartilhamento de dados",
        "Direito de não consentir",
        "Revogação de consentimento"
      ],
      "references": [
        "Art. 18º da LGPD"
      ]
    }
  ],
  "quizzes": [
    {
      "id": 1,
      "title": "Quiz Fundamentos",
      "description": "Teste seus conhecimentos sobre introdução e princípios da LGPD",
      "category": "Fundamentos",
      "difficulty": "BEGINNER",
      "totalQuestions": 5,
      "passingScore": 70,
      "timeLimit": 300
    },
    {
      "id": 2,
      "title": "Quiz Direitos",
      "description": "Avalie seu conhecimento sobre direitos dos titulares",
      "category": "Direitos",
      "difficulty": "INTERMEDIATE",
      "totalQuestions": 10,
      "passingScore": 70,
      "timeLimit": 600
    }
  ],
  "questions": [
    {
      "id": 1,
      "quizId": 1,
      "question": "Em que ano a LGPD foi instituída?",
      "options": [
        "2016",
        "2018",
        "2020",
        "2022"
      ],
      "correctAnswer": 1,
      "explanation": "A LGPD foi instituída pela Lei nº 13.709 em 2018, entrando em vigor em agosto de 2020.",
      "orderIndex": 1
    },
    {
      "id": 2,
      "quizId": 1,
      "question": "Qual princípio se refere ao direito de consultar dados de forma gratuita?",
      "options": [
        "Finalidade",
        "Livre Acesso",
        "Necessidade",
        "Transparência"
      ],
      "correctAnswer": 1,
      "explanation": "O princípio do Livre Acesso garante consulta facilitada e gratuita sobre o tratamento dos dados.",
      "orderIndex": 2
    }
  ]
}
```

### Loader de Dados

Arquivo: `app/src/main/java/br/ufpi/lgpd/educacional/data/ContentLoader.kt`

```kotlin
package br.ufpi.lgpd.educacional.data

import android.content.Context
import com.google.gson.Gson
import br.ufpi.lgpd.educacional.data.model.Lesson
import br.ufpi.lgpd.educacional.data.model.Quiz
import br.ufpi.lgpd.educacional.data.model.Question

class ContentLoader(private val context: Context) {

    private val gson = Gson()

    fun loadLessons(): List<Lesson> {
        val json = context.assets.open("content_data.json").bufferedReader().use { it.readText() }
        val data = gson.fromJson(json, ContentData::class.java)
        return data.lessons
    }

    fun loadQuizzes(): List<Quiz> {
        val json = context.assets.open("content_data.json").bufferedReader().use { it.readText() }
        val data = gson.fromJson(json, ContentData::class.java)
        return data.quizzes
    }

    fun loadQuestions(): List<Question> {
        val json = context.assets.open("content_data.json").bufferedReader().use { it.readText() }
        val data = gson.fromJson(json, ContentData::class.java)
        return data.questions
    }

    data class ContentData(
        val lessons: List<Lesson>,
        val quizzes: List<Quiz>,
        val questions: List<Question>
    )
}
```

---

## 🔔 Notificações

### Criar Channel de Notificação

Arquivo: `app/src/main/java/br/ufpi/lgpd/educacional/util/NotificationUtils.kt`

```kotlin
package br.ufpi.lgpd.educacional.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import br.ufpi.lgpd.educacional.R

object NotificationUtils {

    private const val CHANNEL_ID = "lgpd_channel"
    private const val CHANNEL_NAME = "LGPD Notificações"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notificações do aplicativo LGPD Educacional"
            }
            
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int = 1
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    fun sendLessonReminderNotification(context: Context, lessonTitle: String) {
        sendNotification(
            context,
            "Continue aprendendo!",
            "Você não terminou a lição: $lessonTitle"
        )
    }

    fun sendAchievementNotification(context: Context, achievementName: String) {
        sendNotification(
            context,
            "🏆 Conquista Desbloqueada!",
            "Parabéns! Você desbloqueou: $achievementName"
        )
    }
}
```

---

## ✅ Testes

### Testes Unitários

Arquivo: `app/src/test/java/br/ufpi/lgpd/educacional/ui/home/HomeViewModelTest.kt`

```kotlin
package br.ufpi.lgpd.educacional.ui.home

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadLessons() {
        viewModel.loadLessons()
        assertEquals(6, viewModel.lessons.value.size)
    }

    @Test
    fun testLoadQuizzes() {
        viewModel.loadQuizzes()
        assertEquals(4, viewModel.quizzes.value.size)
    }

    @Test
    fun testUserProgress() {
        viewModel.loadUserProgress()
        assertEquals(50, viewModel.userProgress.value.completionPercentage)
    }
}
```

---

## 🎬 Próximos Passos

1. **Semana 1-2**: Implementar Room Database
2. **Semana 2-3**: Criar Repositórios
3. **Semana 3-4**: Implementar Telas Detalhadas
4. **Semana 4-5**: Integrar Conteúdo Educacional
5. **Semana 5-6**: Notificações e Gamificação
6. **Semana 6-7**: Testes Completos
7. **Semana 7-8**: Polimento e Deploy

---

## 📞 Recursos

- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [ViewModel Best Practices](https://developer.android.com/topic/architecture/data-layer)
- [Coroutines Documentation](https://kotlinlang.org/docs/coroutines-overview.html)
- [Testing Guide](https://developer.android.com/training/testing)

