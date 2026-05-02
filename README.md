# LGPD Educacional - Aplicativo Android 🦉🛡️

## 📱 Sobre o Projeto

**LGPD Educacional** é um aplicativo mobile educacional de alta performance desenvolvido para Android, com o objetivo de capacitar cidadãos sobre a **Lei Geral de Proteção de Dados (LGPD)**. 

Diferente de apps convencionais, utilizei uma abordagem mais simples para transformar o aprendizado jurídico em uma experiência interativa, lúdica e visualmente rica. O projeto é fruto de um Trabalho de Conclusão de Curso (TCC) na UFPI, unindo rigor acadêmico com as melhores práticas de desenvolvimento mobile moderno.

---

## 🎨 Design e Experiência (Style: Duolingo-Inspired)

O app foi totalmente redesenhado para focar no engajamento do usuário:
- **Interface Gamificada:** Uso de botões 3D, cores vibrantes e cantos arredondados para uma navegação amigável.
- **Feedback Visual Instantâneo:** Painéis de correção de quiz que mudam de cor (Verde/Vermelho) e fornecem explicações detalhadas imediatamente após a resposta.
- **Sistema de Trilha:** Progresso visual claro através de barras de progresso espessas e cards de lições interativos.

---

## 🛡️ Segurança e Privacidade (Privacy by Design)

Como um app sobre LGPD, a proteção de dados é o coração da aplicação:
- **Criptografia Local:** Todos os dados do usuário (nome, bio, progresso) são armazenados usando **EncryptedSharedPreferences** (AES-256).
- **Banco de Dados Privado:** Persistência robusta com Room Database, isolada do acesso externo.
- **Anti-Leakage:** Configurações de manifesto que impedem backups automáticos não autorizados e tráfego de dados inseguro (cleartext desativado).

---

## 🎯 Funcionalidades Atuais

- **Módulo de Vídeo-Aulas:** Integração com YouTube via player nativo (sem WebView instável), garantindo reprodução fluida de conteúdos educativos da ENAP e ANPD.
- **Quizzes Dinâmicos:** Testes de conhecimento com lógica de pontuação, feedback imediato e suporte a formatação Markdown para explicações ricas.
- **Dicionário Jurídico:** Consulta rápida a termos complexos da lei.
- **Perfil e Conquistas:** Sistema de níveis e badges baseados no progresso real do aluno.
- **Streak System:** Incentivo ao estudo diário com contador de dias seguidos.

---

## 🛠️ Tecnologias e Arquitetura

O projeto segue os padrões de engenharia recomendados pelo Google:

- **Linguagem:** Kotlin + Coroutines
- **Arquitetura:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Banco de Dados:** Room Persistence Library
- **Segurança:** Jetpack Security (Crypto)
- **UI:** Material Design 3 + Custom 3D Components
- **Vídeo:** Android YouTube Player (Pierfrancesco Soffritti)
- **Navegação:** Jetpack Navigation Component

---

## 🏗️ Estrutura do Projeto (Atualizada)

```
app/src/main/java/br/ufpi/lgpd/educacional/
├── data/
│   ├── dao/            # Interfaces de acesso ao banco Room
│   ├── database/       # Configuração e Singleton do AppDatabase
│   ├── model/          # Entidades (User, Lesson, QuizResult)
│   ├── repository/     # Camada de abstração de dados (UserRepository)
│   └── LgpdContent     # Conteúdo estático das aulas e vídeos
├── ui/
│   ├── home/           # Tela inicial com progresso e destaques
│   ├── lessons/        # Detalhes da lição e player de vídeo
│   ├── quizzes/        # Motor de quiz e feedback duo-style
│   └── profile/        # Gestão de perfil e conquistas
└── util/
    └── UserPreferences # Acesso seguro a dados via Encrypted SharedPreferences
```

---

## 🚀 Como Executar

1. Clone o repositório: `git clone https://github.com/SantWRL/app_lgpd.git`
2. Abra no **Android Studio Hedgehog** ou superior.
3. Sincronize o Gradle e as dependências.
4. Execute via USB ou Emulador (API 24+):
   ```bash
   ./gradlew installDebug
   ```

---

## 👥 Autores e Instituição

- **Desenvolvedor**: Patrick Do Nascimento Santos
- **Orientadora**: Patricia Vieira da Silva Barros
- **Instituição**: Universidade Federal do Piauí (UFPI) - Campus Picos

---

## 📄 Licença

Este projeto é de autoria de Patrick Do Nascimento Santos.

**Status do Projeto**: Em desenvolvimento...
