# 🚀 Quick Start - LGPD Educacional

**Tempo estimado: 5 minutos para rodar o app**

---

## ⚡ Pré-requisitos Mínimos

- Android Studio 2023.3+ (Giraffe)
- JDK 11+
- 4GB RAM
- Android SDK 34 instalado

---

## 📋 Passos Rápidos

### 1️⃣ Clone e Abra (2 min)

```bash
# Clone
git clone <repo-url>
cd lgpd-app

# Abra no Android Studio
open -a "Android Studio" .
# ou no Windows: start android-studio .
```

### 2️⃣ Sincronize (1 min)

Aguarde o Android Studio sincronizar automaticamente. Se não sincronizar:
- **File → Sync Now**

### 3️⃣ Execute (2 min)

```bash
# Via Android Studio:
# Run → Run 'app'
# Ou: Shift+F10 (Windows/Linux) / Ctrl+R (Mac)

# Via Terminal:
./gradlew installDebug
```

---

## 🎯 Primeiro Teste

Após o app instalar e abrir:

1. **Veja o Onboarding** (3 telas)
2. **Clique em "Começar"**
3. **Explore a Home** (progresso, lições, quizzes)
4. **Clique em uma lição** (estrutura pronta)
5. **Veja o Perfil** (estatísticas)

---

## 📂 Estrutura Simplificada

```
lgpd-app/
├── README.md              ← Leia PRIMEIRO (visão geral)
├── SETUP_GUIDE.md        ← Se houver problemas
├── QUICK_START.md        ← Este arquivo
│
└── app/
    └── src/main/
        ├── java/...      ← Código-fonte (28 arquivos)
        └── res/...       ← Layouts e recursos (20+ arquivos)
```

---

## 💡 Entendendo o Código

### Ordem de Leitura Recomendada

1. **MainActivity.kt** (entrada)
   ```kotlin
   // Navegação e setup inicial
   ```

2. **HomeFragment.kt** (tela inicial)
   ```kotlin
   // UI da home
   ```

3. **HomeViewModel.kt** (lógica)
   ```kotlin
   // Dados e estado
   ```

4. **Models** (Lesson.kt, Quiz.kt, etc.)
   ```kotlin
   // Estrutura de dados
   ```

---

## ✅ Testes Rápidos

### Verificar Build

```bash
./gradlew build
# Esperado: BUILD SUCCESSFUL
```

### Verificar Sintaxe

```bash
./gradlew lint
# Sem erros críticos
```

---

## 🐛 Problemas Comuns

### "Gradle sync failed"

```bash
./gradlew clean
./gradlew sync
```

### "Build tools not found"

```bash
sdkmanager "build-tools;34.0.0"
```

### "Emulator won't start"

```bash
emulator -list-avds
emulator -avd <device-name>
```

### "Cannot resolve symbol"

- File → Invalidate Caches → Invalidate and Restart

---

## 🎓 Próximos Passos

Após o app estar rodando:

1. **Leia DEVELOPMENT_GUIDELINES.md** (padrões)
2. **Leia IMPLEMENTACAO_FEATURES.md** (features)
3. **Implemente Room Database** (2-3 horas)
4. **Crie um repositório** (1 hora)
5. **Adicione conteúdo** (2-4 horas)

---

## 📞 Links Úteis

- **Android Docs**: https://developer.android.com
- **Kotlin Docs**: https://kotlinlang.org
- **Material Design**: https://m3.material.io
- **Room Guide**: https://developer.android.com/training/data-storage/room

---

## ✨ Estrutura Confirmada

```
✅ Kotlin compilando
✅ Layouts válidos
✅ Recursos definidos
✅ Navegação funcional
✅ ViewModels prontos
✅ Adapters funcionando
✅ Assets inclusos
```

---

**Status**: 🟢 PRONTO PARA RODAR

Tempo total de setup: **~15 minutos**

