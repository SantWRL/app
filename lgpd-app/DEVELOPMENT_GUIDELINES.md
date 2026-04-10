# 📐 Diretrizes de Desenvolvimento - LGPD Educacional

Este documento estabelece os padrões de código e boas práticas para o projeto.

---

## 📋 Índice

1. [Arquitetura](#arquitetura)
2. [Convenções de Código](#convenções-de-código)
3. [Padrões de Design](#padrões-de-design)
4. [Estrutura de Pacotes](#estrutura-de-pacotes)
5. [Boas Práticas](#boas-práticas)
6. [Performance](#performance)
7. [Segurança](#segurança)

---

## 🏗️ Arquitetura

### MVVM (Model-View-ViewModel)

O projeto segue a arquitetura MVVM com as seguintes camadas:

```
┌─────────────────────────────────────────────────┐
│         UI Layer (Activities/Fragments)         │
│  - Responsável por renderização                 │
│  - Observa dados do ViewModel                   │
└─────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────┐
│      ViewModel Layer (ViewModels)               │
│  - Gerencia estado da UI                        │
│  - Expõe dados via StateFlow/LiveData          │
│  - Coordena com Repository                      │
└─────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────┐
│      Data Layer (Repository/Database)          │
│  - Acesso a dados locais (Room)                │
│  - APIs remotas (Retrofit)                      │
│  - Cache e sincronização                        │
└─────────────────────────────────────────────────┘
```

### Fluxo de Dados

```kotlin
// 1. UI chama ViewModel
homeViewModel.loadLessons()

// 2. ViewModel chama Repository
repository.getAllLessons()

// 3. Repository acessa Database
lessonDao.getAllLessons()

// 4. Dados retornam via Flow
lessonDao.getAllLessons() // Returns Flow<List<Lesson>>
    .collect { lessons ->
        // Update UI
    }
```

---

## 📝 Convenções de Código

### Nomenclatura

#### Classes

```kotlin
// ✅ Correto
class HomeFragment : Fragment()
class LessonViewModel : ViewModel()
class UserRepository(private val userDao: UserDao)

// ❌ Evitar
class home : Fragment()
class LessonVM : ViewModel()
class repository(val dao: UserDao)
```

#### Funções

```kotlin
// ✅ Correto
fun loadLessons() { }
fun calculateProgress(): Int { }
private fun setupRecyclerView() { }

// ❌ Evitar
fun loadlessons() { }
fun calc_prog(): Int { }
fun setup_recycler_view() { }
```

#### Variáveis

```kotlin
// ✅ Correto
private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

private var currentQuestionIndex = 0
val userName: String = "João"

// ❌ Evitar
private val _LESSONS = MutableStateFlow<List<Lesson>>(emptyList())
private var current_question_index = 0
val user_name = "João"
```

#### Constantes

```kotlin
// ✅ Correto
companion object {
    private const val DEFAULT_TIMEOUT = 30000L
    private const val SHARED_PREF_KEY = "app_prefs"
}

// ❌ Evitar
companion object {
    val DEFAULT_TIMEOUT = 30000L
    private const val default_timeout = 30000L
}
```

### Documentação

```kotlin
/**
 * Carrega todas as lições disponíveis.
 *
 * @return Flow contendo lista de lições ordenadas por índice
 * 
 * Exemplo:
 * ```kotlin
 * viewModel.lessons.collect { lessons ->
 *     adapter.submitList(lessons)
 * }
 * ```
 */
fun getAllLessons(): Flow<List<Lesson>>

/**
 * Atualiza o status de conclusão de uma lição.
 *
 * @param lessonId ID da lição a ser atualizada
 * @param isCompleted Novo status de conclusão
 * @throws IllegalArgumentException se lessonId é inválido
 */
suspend fun updateLessonCompletion(lessonId: Int, isCompleted: Boolean)
```

---

## 🎯 Padrões de Design

### 1. Singleton Pattern (Database)

```kotlin
@Database(...)
abstract class LgpdDatabase : RoomDatabase() {
    
    companion object {
        @Volatile
        private var INSTANCE: LgpdDatabase? = null

        fun getDatabase(context: Context): LgpdDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, LgpdDatabase::class.java, "lgpd_db")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
```

### 2. Repository Pattern

```kotlin
class LessonRepository(private val lessonDao: LessonDao) {
    
    fun getAllLessons(): Flow<List<Lesson>> = lessonDao.getAllLessons()
    
    suspend fun getLessonById(id: Int): Lesson = lessonDao.getLessonById(id)
    
    suspend fun updateLesson(lesson: Lesson) = lessonDao.updateLesson(lesson)
}
```

### 3. ViewModel Pattern

```kotlin
class HomeViewModel : ViewModel() {
    
    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    fun loadLessons() {
        viewModelScope.launch {
            try {
                _lessons.value = repository.getAllLessons()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}
```

### 4. Adapter Pattern

```kotlin
class LessonsListAdapter(
    private val onLessonClick: (Lesson) -> Unit
) : ListAdapter<Lesson, LessonsListAdapter.LessonViewHolder>(LessonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(
            ItemLessonListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onLessonClick
        )
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
                lessonTitle.text = lesson.title
                root.setOnClickListener { onLessonClick(lesson) }
            }
        }
    }
}
```

---

## 📦 Estrutura de Pacotes

### Padrão Recomendado

```
br.ufpi.lgpd.educacional/
├── data/
│   ├── database/
│   │   ├── LgpdDatabase.kt
│   │   ├── LessonDao.kt
│   │   ├── QuizDao.kt
│   │   └── ...
│   ├── model/
│   │   ├── Lesson.kt
│   │   ├── Quiz.kt
│   │   ├── User.kt
│   │   └── ...
│   └── repository/
│       ├── LessonRepository.kt
│       ├── QuizRepository.kt
│       └── ...
├── ui/
│   ├── home/
│   │   ├── HomeFragment.kt
│   │   └── HomeViewModel.kt
│   ├── lessons/
│   │   ├── LessonsFragment.kt
│   │   ├── LessonDetailActivity.kt
│   │   └── LessonsViewModel.kt
│   ├── quizzes/
│   │   ├── QuizzesFragment.kt
│   │   ├── QuizActivity.kt
│   │   └── QuizzesViewModel.kt
│   ├── profile/
│   │   ├── ProfileFragment.kt
│   │   └── ProfileViewModel.kt
│   ├── onboarding/
│   │   ├── OnboardingActivity.kt
│   │   └── OnboardingPageFragment.kt
│   ├── adapter/
│   │   ├── LessonsListAdapter.kt
│   │   ├── QuizzesListAdapter.kt
│   │   └── ...
│   └── MainActivity.kt
└── util/
    ├── NotificationUtils.kt
    ├── DateUtils.kt
    └── ...
```

---

## ✅ Boas Práticas

### 1. Usar StateFlow ao invés de LiveData

```kotlin
// ✅ Preferido
private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

// Usar em Composable ou observar em Fragment
viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.lessons.collect { lessons ->
            adapter.submitList(lessons)
        }
    }
}

// ❌ Evitar (legado)
private val _lessons = MutableLiveData<List<Lesson>>()
val lessons: LiveData<List<Lesson>> = _lessons
```

### 2. Usar Coroutines para operações assíncronas

```kotlin
// ✅ Correto
fun loadData() {
    viewModelScope.launch {
        try {
            val data = withContext(Dispatchers.IO) {
                repository.fetchData()
            }
            _uiState.value = UiState.Success(data)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message ?: "Unknown error")
        }
    }
}

// ❌ Evitar (callbacks)
fun loadData() {
    repository.fetchData(object : Callback {
        override fun onSuccess(data: Data) {
            _uiState.value = UiState.Success(data)
        }
        
        override fun onError(error: String) {
            _uiState.value = UiState.Error(error)
        }
    })
}
```

### 3. View Binding ao invés de findViewById

```kotlin
// ✅ Correto
private lateinit var binding: ActivityMainBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    binding.button.setOnClickListener { doSomething() }
}

// ❌ Evitar
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    val button: Button = findViewById(R.id.button)
    button.setOnClickListener { doSomething() }
}
```

### 4. DiffUtil para otimizar RecyclerView

```kotlin
// ✅ Correto
class LessonDiffCallback : DiffUtil.ItemCallback<Lesson>() {
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson) = 
        oldItem.id == newItem.id
    
    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson) = 
        oldItem == newItem
}

class LessonsListAdapter : ListAdapter<Lesson, LessonsListAdapter.ViewHolder>(LessonDiffCallback()) {
    // ...
}

// ❌ Evitar
class LessonsAdapter(private val lessons: MutableList<Lesson>) : 
    RecyclerView.Adapter<LessonsAdapter.ViewHolder>() {
    
    fun updateLessons(newLessons: List<Lesson>) {
        lessons.clear()
        lessons.addAll(newLessons)
        notifyDataSetChanged() // Muito custoso!
    }
}
```

### 5. Tratamento de erros apropriado

```kotlin
// ✅ Correto
fun loadLessons() {
    viewModelScope.launch {
        _isLoading.value = true
        try {
            _lessons.value = repository.getAllLessons()
        } catch (e: IOException) {
            _errorMessage.value = "Erro de conexão. Verifique sua internet."
        } catch (e: Exception) {
            _errorMessage.value = "Erro inesperado. Tente novamente."
            Timber.e(e, "Error loading lessons")
        } finally {
            _isLoading.value = false
        }
    }
}

// ❌ Evitar
fun loadLessons() {
    viewModelScope.launch {
        try {
            _lessons.value = repository.getAllLessons()
        } catch (e: Exception) {
            // Ignorar silenciosamente é perigoso
            println("Error: ${e.message}")
        }
    }
}
```

---

## ⚡ Performance

### 1. Lazy Loading

```kotlin
// Carregar dados sob demanda, não tudo de uma vez
fun loadLessonsByCategory(category: String) {
    viewModelScope.launch {
        repository.getLessonsByCategory(category)
            .collect { lessons ->
                _lessons.value = lessons
            }
    }
}
```

### 2. Paginação

```kotlin
class LessonsViewModel : ViewModel() {
    
    private var currentPage = 1
    private var isLoading = false
    
    fun loadMoreLessons() {
        if (isLoading) return
        
        viewModelScope.launch {
            isLoading = true
            try {
                val newLessons = repository.getLessonsPaginated(currentPage, PAGE_SIZE)
                currentPage++
                val currentList = _lessons.value.toMutableList()
                currentList.addAll(newLessons)
                _lessons.value = currentList
            } finally {
                isLoading = false
            }
        }
    }
    
    companion object {
        private const val PAGE_SIZE = 20
    }
}
```

### 3. Cache de Imagens

```kotlin
// Usar Glide ou Coil para cache automático
Glide.with(this)
    .load(imageUrl)
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .into(imageView)
```

---

## 🔐 Segurança

### 1. Não armazenar dados sensíveis em SharedPreferences

```kotlin
// ❌ Evitar
SharedPreferences.Editor()
    .putString("auth_token", token)
    .apply()

// ✅ Usar EncryptedSharedPreferences
val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val encryptedSharedPreferences = EncryptedSharedPreferences.create(
    context,
    "secret_shared_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)

encryptedSharedPreferences.edit()
    .putString("auth_token", token)
    .apply()
```

### 2. Validar entrada do usuário

```kotlin
// ✅ Validar sempre
fun validateEmail(email: String): Boolean {
    return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"))
}

fun submitForm(email: String, password: String) {
    if (!validateEmail(email)) {
        _errorMessage.value = "Email inválido"
        return
    }
    
    if (password.length < 8) {
        _errorMessage.value = "Senha deve ter pelo menos 8 caracteres"
        return
    }
    
    // Processar formulário
}
```

### 3. HTTPS apenas

```kotlin
// ✅ Correto
val httpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.example.com/") // HTTPS obrigatório
    .addConverterFactory(GsonConverterFactory.create())
    .client(httpClient)
    .build()

// ❌ Evitar
val retrofit = Retrofit.Builder()
    .baseUrl("http://api.example.com/") // Inseguro!
    .build()
```

### 4. Não logar dados sensíveis

```kotlin
// ✅ Correto
Timber.d("User loaded: $userId")

// ❌ Evitar
Timber.d("User token: $authToken")
Timber.d("User data: $userData") // Se userData contém dados sensíveis
```

---

## 🧪 Testes

### Estrutura de Testes

```
app/
├── src/
│   ├── test/                # Testes unitários
│   │   └── java/br/ufpi/...
│   │       ├── ui/
│   │       │   └── home/
│   │       │       └── HomeViewModelTest.kt
│   │       ├── data/
│   │       │   └── repository/
│   │       │       └── LessonRepositoryTest.kt
│   │       └── util/
│   │           └── ValidatorTest.kt
│   │
│   └── androidTest/         # Testes instrumentados
│       └── java/br/ufpi/...
│           ├── ui/
│           │   └── HomeFragmentTest.kt
│           └── data/
│               └── LessonDaoTest.kt
```

### Exemplo de Teste Unitário

```kotlin
class HomeViewModelTest {
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: FakeLessonRepository
    
    @Before
    fun setup() {
        repository = FakeLessonRepository()
        viewModel = HomeViewModel(repository)
    }
    
    @Test
    fun loadLessons_updatesList() {
        viewModel.loadLessons()
        
        assertEquals(6, viewModel.lessons.value.size)
        assertEquals("Introdução à LGPD", viewModel.lessons.value[0].title)
    }
    
    @Test
    fun loadLessons_completionPercentage() {
        viewModel.loadUserProgress()
        
        assertEquals(50, viewModel.userProgress.value.completionPercentage)
    }
}
```

---

## 📋 Checklist de Code Review

- [ ] Segue a estrutura de pacotes recomendada?
- [ ] Usa nomes descritivos para classes, funções e variáveis?
- [ ] Tem documentação em funções públicas?
- [ ] Usa StateFlow ao invés de LiveData?
- [ ] Implementa tratamento de erros apropriado?
- [ ] Usa View Binding?
- [ ] Evita código duplicado?
- [ ] Testes unitários incluídos?
- [ ] Sem dados sensíveis em logs?
- [ ] Performance otimizada (DiffUtil, lazy loading)?

---

**Última atualização**: Abril 2026

