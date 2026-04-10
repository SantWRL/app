# LGPD Educacional - Aplicativo Android

## 📱 Sobre o Projeto

**LGPD Educacional** é um aplicativo mobile educacional desenvolvido em Android para capacitar usuários sobre a Lei Geral de Proteção de Dados (LGPD). O projeto foi concebido como parte de um Trabalho de Conclusão de Curso (TCC) na UFPI, com foco em formar usuários digitais conscientes sobre seus direitos e responsabilidades na proteção de dados pessoais.

### Objetivo Principal

Desenvolver uma estrutura metodológica para formação de usuários digitais conscientes, materializada em uma aplicação educacional prática que articule conhecimento jurídico da LGPD com situações cotidianas do ambiente universitário e digital.

---

## 🎯 Funcionalidades Principais

### 1. **Onboarding Interativo**
- Boas-vindas personalizadas ao novo usuário
- Apresentação dos conceitos fundamentais da LGPD
- Navegação por 3 telas de orientação inicial

### 2. **Tela Inicial (Home)**
- Visualização do progresso geral do usuário
- Cards com lições recomendadas
- Cards com quizzes disponíveis
- Estatísticas de aprendizado em tempo real

### 3. **Módulo de Lições**
- 6 lições principais sobre LGPD:
  - Introdução à LGPD
  - 10 Princípios Fundamentais
  - Direitos dos Titulares
  - Atores da LGPD
  - Segurança de Dados
  - LGPD no Contexto Acadêmico
- Dificuldade variável (Iniciante, Intermediário, Avançado)
- Tempo estimado por lição
- Conteúdo estruturado e progressivo

### 4. **Módulo de Quizzes**
- Testes avaliativos após cada lição
- Feedback imediato
- Explicações das respostas corretas
- Rastreamento de desempenho

### 5. **Cenários Educacionais**
- Situações reais baseadas no contexto acadêmico
- Avaliação de conformidade com LGPD
- Identificação de violações e riscos
- Propostas de correção

### 6. **Perfil do Usuário**
- Visualização de progresso
- Estatísticas de aprendizado
- Nível e pontuação
- Conquistas desbloqueadas
- Histórico de atividades

---

## 🏗️ Arquitetura do Projeto

### Estrutura de Diretórios

```
lgpd-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/ufpi/lgpd/educacional/
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/          # Modelos de dados
│   │   │   │   │   ├── repository/     # Repositórios (implementar)
│   │   │   │   │   └── database/       # Room Database (implementar)
│   │   │   │   ├── ui/
│   │   │   │   │   ├── MainActivity    # Atividade principal
│   │   │   │   │   ├── onboarding/    # Fluxo de onboarding
│   │   │   │   │   ├── home/          # Tela inicial
│   │   │   │   │   ├── lessons/       # Módulo de lições
│   │   │   │   │   ├── quizzes/       # Módulo de quizzes
│   │   │   │   │   ├── profile/       # Perfil do usuário
│   │   │   │   │   └── adapter/       # Adapters RecyclerView
│   │   │   │   └── utils/             # Utilitários
│   │   │   ├── res/
│   │   │   │   ├── layout/            # Arquivos de layout XML
│   │   │   │   ├── drawable/          # Recursos visuais
│   │   │   │   ├── menu/              # Menus
│   │   │   │   ├── navigation/        # Gráfico de navegação
│   │   │   │   ├── values/            # Strings, cores, estilos
│   │   │   │   └── mipmap/            # Ícones do app
│   │   │   └── AndroidManifest.xml
│   │   └── test/                       # Testes
│   └── build.gradle.kts
├── build.gradle.kts
└── README.md
```

### Camadas da Arquitetura

#### 1. **Camada de Apresentação (UI)**
- Activities e Fragments
- ViewModels com Coroutines
- Binding View
- Adapters para RecyclerView

#### 2. **Camada de Dados**
- Modelos de domínio (Lesson, Quiz, User, etc.)
- Room Database para persistência local
- Repositórios para acesso a dados

#### 3. **Camada de Utilidades**
- Logging com Timber
- Gerenciamento de preferências (DataStore)
- Utilitários gerais

---

## 💾 Modelos de Dados

### Lesson (Lição)
```kotlin
@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val category: String,
    val orderIndex: Int,
    val estimatedTime: Int,
    val difficulty: String,
    var isCompleted: Boolean = false
)
```

### Quiz
```kotlin
@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val totalQuestions: Int,
    val passingScore: Int = 70,
    val timeLimit: Int = 0
)
```

### Question
```kotlin
@Entity(tableName = "questions")
data class Question(
    @PrimaryKey val id: Int,
    val quizId: Int,
    val question: String,
    val options: String, // JSON array
    val correctAnswer: Int,
    val explanation: String,
    val orderIndex: Int
)
```

### User
```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "default_user",
    val name: String = "Usuário",
    val email: String = "",
    val profileType: String = "student",
    val totalPoints: Int = 0,
    val level: Int = 1,
    val lessonsCompleted: Int = 0,
    val quizzesCompleted: Int = 0,
    val averageScore: Double = 0.0,
    val joinDate: Long = System.currentTimeMillis(),
    val lastAccessDate: Long = System.currentTimeMillis()
)
```

### Scenario (Cenário)
```kotlin
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
```

---

## 🎨 Design e Cores

### Paleta de Cores
- **Primária**: #2563EB (Azul)
- **Secundária**: #10B981 (Verde)
- **Destaque**: #F59E0B (Laranja)
- **Erro**: #EF4444 (Vermelho)
- **Sucesso**: #22C55E (Verde escuro)

### Tipografia
- **Large Title**: 32sp, Bold
- **Title**: 24sp, Bold
- **Subtitle**: 18sp
- **Body**: 16sp
- **Body Small**: 14sp, Secondary

---

## 🛠️ Tecnologias Utilizadas

### Frameworks e Bibliotecas

| Componente | Versão | Propósito |
|-----------|--------|----------|
| Kotlin | 1.9.0 | Linguagem principal |
| AndroidX Core | 1.12.0 | APIs modernas do Android |
| AppCompat | 1.6.1 | Compatibilidade reversa |
| Material Design 3 | 1.10.0 | Componentes UI |
| Navigation | 2.7.5 | Navegação entre fragmentos |
| Lifecycle | 2.6.2 | Gerenciamento de lifecycle |
| Room | 2.6.1 | Banco de dados local |
| Coroutines | 1.7.3 | Programação assíncrona |
| Retrofit2 | 2.9.0 | Requisições HTTP (futuro) |
| Timber | 5.0.1 | Logging |
| DataStore | 1.0.0 | Preferências |

---

## 📋 Pré-requisitos

- Android Studio Giraffe (2023.3) ou superior
- JDK 11+
- SDK compileSdk 34
- minSdk 24 (Android 7.0)
- targetSdk 34

---

## 🚀 Como Configurar e Executar

### 1. Clonar o Repositório
```bash
git clone <repository-url>
cd lgpd-app
```

### 2. Abrir no Android Studio
```bash
# Abrir o Android Studio
open -a "Android Studio" .
```

### 3. Sincronizar Dependências
- File → Sync Now

### 4. Executar o App
```bash
# Via Android Studio
# Run → Run 'app'

# Via linha de comando
./gradlew installDebug
```

### 5. Estrutura do Projeto

O projeto está dividido em módulos bem definidos:

```
data/
├── model/          # Classes de dados
├── repository/     # Acesso a dados (implementar)
└── database/       # Room Database (implementar)

ui/
├── MainActivity              # Tela principal
├── onboarding/              # Fluxo inicial
│   ├── OnboardingActivity
│   └── OnboardingPageFragment
├── home/                    # Tela inicial
│   ├── HomeFragment
│   └── HomeViewModel
├── lessons/                 # Lições
│   ├── LessonsFragment
│   └── LessonsViewModel
├── quizzes/                 # Quizzes
│   ├── QuizzesFragment
│   └── QuizzesViewModel
├── profile/                 # Perfil
│   ├── ProfileFragment
│   └── ProfileViewModel
└── adapter/                 # Adapters
    ├── LessonCardAdapter
    ├── LessonsListAdapter
    ├── QuizCardAdapter
    └── QuizzesListAdapter
```

---

## 📚 Conteúdo Educacional

### Lições Implementadas

#### 1. Introdução à LGPD (15 min - Iniciante)
- O que é LGPD?
- Contexto histórico
- Importância da proteção de dados
- Alinhamento com GDPR

#### 2. 10 Princípios Fundamentais (20 min - Iniciante)
1. Finalidade
2. Adequação
3. Necessidade
4. Transparência
5. Segurança
6. Prestação de Contas
7. Livre Acesso
8. Qualidade dos Dados
9. Não Discriminação
10. Prevenção

#### 3. Direitos dos Titulares (25 min - Intermediário)
- Confirmação de existência
- Acesso aos dados
- Correção de dados
- Anonimização
- Bloqueio ou eliminação
- Portabilidade
- Eliminação
- Informação sobre compartilhamento
- Direito de não consentir
- Revogação de consentimento

#### 4. Atores da LGPD (20 min - Intermediário)
- Titular de dados
- Controlador
- Operador
- Encarregado (DPO)

#### 5. Segurança de Dados (30 min - Intermediário)
- Medidas técnicas
- Medidas administrativas
- Boas práticas
- Conformidade

#### 6. LGPD no Contexto Acadêmico (25 min - Avançado)
- Dados no PET
- Conformidade na universidade
- Casos reais
- Implementação prática

---

## 🎮 Recursos Gamificados

### Sistema de Pontos
- Lições concluídas: +50 pontos
- Quiz completado: +25 pontos
- Quiz com 100%: +50 pontos
- Resposta rápida: +10 pontos bônus

### Níveis
1. Iniciante (0-500 pontos)
2. Aprendiz (500-1500 pontos)
3. Proficiente (1500-3000 pontos)
4. Especialista (3000+ pontos)

### Conquistas
- 🎓 Primeiro Passo: Completar primeira lição
- ⭐ Estrela em Ascensão: Completar 3 lições
- 🎯 Quiz Master: Pontuar 100% em um quiz
- 🔒 Guardião de Dados: Completar todas as lições
- 🏆 Especialista em LGPD: Atingir nível Especialista

---

## 📝 Implementações Pendentes

### Funcionalidades a Completar

1. **Room Database**
   - Implementar DAOs
   - Migrations
   - Seed data

2. **Repositórios**
   - LessonRepository
   - QuizRepository
   - UserRepository
   - ScenarioRepository

3. **API Integration** (Opcional)
   - Sincronização com servidor
   - Backup online
   - Analytics

4. **Telas Detalhadas**
   - LessonDetailActivity
   - QuizActivity (com temporizador)
   - ScenarioActivity

5. **Notificações**
   - Push notifications
   - Lembretes de atividades

6. **Testes**
   - Unit tests
   - UI tests
   - Integration tests

---

## 🔍 Fluxo de Navegação

```
OnboardingActivity
        ↓
    MainActivity
    /   |   \   \
   /    |    \   \
Home  Lições Quiz Perfil
  |      |      |     |
  └──────┴──────┴─────┘
         |
    Detalhes/Ação
```

---

## 📱 Compatibilidade

- **Android Mínimo**: 7.0 (API 24)
- **Android Alvo**: 14 (API 34)
- **Orientação**: Portrait (vertical)
- **Modo Escuro**: Suportado

---

## 🔐 Segurança

- Nenhuma chave de API sensível no código
- Dados locais criptografados (Room)
- Validação de entrada em formulários
- Proteção contra SQL injection
- HTTPS apenas para requisições de rede

---

## 📊 Estrutura de Dados - Exemplo JSON

### Lição
```json
{
  "id": 1,
  "title": "Introdução à LGPD",
  "description": "Aprenda os conceitos básicos",
  "content": "...",
  "category": "Fundamentos",
  "estimatedTime": 15,
  "difficulty": "BEGINNER"
}
```

### Quiz
```json
{
  "id": 1,
  "title": "Quiz Fundamentos",
  "description": "Teste seus conhecimentos",
  "totalQuestions": 5,
  "passingScore": 70,
  "timeLimit": 600
}
```

### Pergunta
```json
{
  "id": 1,
  "quizId": 1,
  "question": "O que é LGPD?",
  "options": ["Opção A", "Opção B", "Opção C", "Opção D"],
  "correctAnswer": 0,
  "explanation": "A resposta correta é..."
}
```

---

## 🤝 Contribuindo

Para contribuir com o projeto:

1. Fork o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto é licenciado sob a MIT License - veja o arquivo LICENSE para detalhes.

---

## 👥 Autores

- **Aluno**: Patrick Do Nascimento Santos
- **Matrícula**: 20219046182
- **Orientadora**: Patricia Vieira da Silva Barros
- **Instituição**: UFPI - Universidade Federal do Piauí

---

## 📞 Contato

Para dúvidas ou sugestões sobre o projeto:
- Email: patricklx77@gmail.com
- GitHub: [seu-github]

---

## 📚 Referências

- Lei nº 13.709/2018 - LGPD
- GDPR - European General Data Protection Regulation
- Android Documentation: https://developer.android.com
- Material Design 3: https://m3.material.io
- Kotlin Documentation: https://kotlinlang.org

---

## 🎓 Trabalho de Conclusão de Curso

Este projeto foi desenvolvido como parte de um TCC no programa de Educação Tutorial (PET) da UFPI, com foco em:

✅ Educação digital consciente
✅ Proteção de dados pessoais
✅ Formação de usuários críticos
✅ Aplicação prática da LGPD
✅ Desenvolvimento técnico e pedagógico

---

## 📅 Data de Criação

Teresina, Piauí - Abril de 2026

**Status**: Em desenvolvimento ✨

