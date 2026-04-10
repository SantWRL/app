# 📋 Guia de Instalação e Configuração - LGPD Educacional

## ⚡ Quick Start (5 minutos)

### Pré-requisitos Mínimos
- Android Studio 2023.3+ (Giraffe ou superior)
- JDK 11+
- 4GB RAM mínimo (8GB recomendado)
- Emulador Android ou dispositivo físico

### Passos Rápidos

```bash
# 1. Clone o repositório
git clone <seu-repositorio>
cd lgpd-app

# 2. Abra no Android Studio
open -a "Android Studio" .

# 3. Aguarde sincronização automática
# File → Sync Now (se necessário)

# 4. Rode no emulador
# Run → Run 'app' (ou Shift+F10)
```

---

## 🔧 Configuração Detalhada

### Passo 1: Instalação do Android Studio

#### Windows
```
1. Baixe em: https://developer.android.com/studio
2. Execute o instalador .exe
3. Siga o assistente de instalação
4. Selecione Android SDK na tela de componentes
5. Aguarde download dos SDKs
```

#### macOS
```bash
# Via Homebrew
brew install --cask android-studio

# Ou manualmente
# Baixe em: https://developer.android.com/studio
# Arraste para Applications
```

#### Linux
```bash
# Ubuntu/Debian
sudo snap install android-studio --classic

# Ou manualmente
# Baixe em: https://developer.android.com/studio
# Extraia e execute ./android-studio/bin/studio.sh
```

### Passo 2: Configurar SDK Android

1. **Abra Android Studio**
2. **Tools → SDK Manager**
3. **Instale os seguintes SDKs:**
   - Android SDK 34 (compileSdk)
   - Android SDK 24 (minSdk)
   - Build Tools 34.0.0
   - Android Emulator
   - Intel HAXM (Windows/Mac)

4. **Configure ANDROID_HOME:**

```bash
# Linux/Mac
echo 'export ANDROID_HOME=$HOME/Library/Android/sdk' >> ~/.zshrc
echo 'export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/tools/bin' >> ~/.zshrc
source ~/.zshrc

# Windows (PowerShell Admin)
[Environment]::SetEnvironmentVariable("ANDROID_HOME", "$env:UserProfile\AppData\Local\Android\sdk", "User")
```

### Passo 3: Clonar o Repositório

```bash
# Clone via HTTPS
git clone https://github.com/seu-usuario/lgpd-app.git

# Ou via SSH
git clone git@github.com:seu-usuario/lgpd-app.git

# Acesse o diretório
cd lgpd-app
```

### Passo 4: Abrir no Android Studio

**Opção 1: Via GUI**
- Abra Android Studio
- Welcome → Open an Existing Project
- Selecione a pasta `lgpd-app`

**Opção 2: Via Terminal**
```bash
# macOS
open -a "Android Studio" .

# Linux
android-studio .

# Windows
# Use o atalho ou execute do Command Prompt
```

### Passo 5: Sincronizar Dependências

Após abrir o projeto:

1. Android Studio detectará automaticamente que sincronização é necessária
2. Clique em **"Sync Now"** (canto superior direito)
3. Aguarde o gradle fazer download de todas as dependências
4. Você verá mensagens de progresso na aba "Build"

```bash
# Ou via terminal
./gradlew build
```

### Passo 6: Configurar Emulador

```bash
# Listar dispositivos virtuais disponíveis
emulator -list-avds

# Criar novo AVD
avdmanager create avd -n "Pixel_6_Pro_API_34" \
  -k "system-images;android-34;google_apis;x86_64" \
  -d "pixel_6_pro"

# Iniciar emulador
emulator -avd "Pixel_6_Pro_API_34"
```

Ou use a GUI:
1. Tools → Device Manager
2. Create Device
3. Selecione "Pixel 6 Pro"
4. Selecione "API 34" (com Google APIs)
5. Clique em Launch

---

## 🏃 Executar o Aplicativo

### Via Android Studio

1. **Selecione o emulador ou dispositivo físico:**
   - Top bar → Select Device/Emulator

2. **Clique em Run:**
   - Run → Run 'app'
   - Ou pressione Shift+F10 (Windows/Linux) / Ctrl+R (Mac)

3. **Aguarde o build e instalação:**
   - Você verá logs no painel "Logcat"

### Via Terminal

```bash
# Build e install debug APK
./gradlew installDebug

# Executar testes
./gradlew test

# Build release APK
./gradlew assembleRelease

# Lint - verificar qualidade de código
./gradlew lint
```

---

## 🏗️ Estrutura de Pastas Esperada

Após configuração bem-sucedida:

```
lgpd-app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── br/ufpi/lgpd/educacional/  ✓ Seu código
│   │   │   ├── res/
│   │   │   │   ├── layout/                    ✓ XMLs
│   │   │   │   ├── drawable/                  ✓ Ícones
│   │   │   │   ├── values/                    ✓ Strings, cores
│   │   │   │   └── menu/                      ✓ Menus
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                              ✓ Testes unitários
│   │   └── androidTest/                       ✓ Testes instrumentados
│   ├── build.gradle.kts                       ✓ Dependências
│   ├── proguard-rules.pro                     ✓ Otimização
│   └── local.properties                       ✓ Auto-gerado
├── build.gradle.kts                           ✓ Config raiz
├── settings.gradle.kts                        ✓ Módulos
├── README.md                                  ✓ Documentação
├── SETUP_GUIDE.md                             ✓ Este arquivo
└── .gitignore                                 ✓ Exclusões Git
```

---

## 🔍 Verificar Instalação

### Teste 1: Gradle Build

```bash
./gradlew build

# Esperado: BUILD SUCCESSFUL
```

### Teste 2: Lint (Qualidade de Código)

```bash
./gradlew lint

# Sem erros críticos esperados
```

### Teste 3: Executar Testes Unitários

```bash
./gradlew test

# Tests should run without critical failures
```

### Teste 4: Emulador Funciona

```bash
# Verificar se há dispositivos virtuais
emulator -list-avds

# Iniciar um emulador
emulator -avd <nome-dispositivo>
```

---

## 🐛 Solução de Problemas

### Problema: "Gradle sync failed"

**Solução:**
```bash
# Limpar cache gradle
./gradlew clean

# Sincronizar novamente
./gradlew sync

# Ou no Android Studio:
# File → Invalidate Caches → Invalidate and Restart
```

### Problema: "Build-tools not found"

**Solução:**
```bash
# Instalar via sdkmanager
sdkmanager "build-tools;34.0.0"

# Ou via Android Studio:
# Tools → SDK Manager → SDK Tools
# Marque "Show Package Details"
# Selecione 34.0.0
```

### Problema: "Emulator won't start"

**Solução:**
```bash
# Verificar dispositivos
emulator -list-avds

# Iniciar com mais verbosidade
emulator -avd <nome> -verbose

# Se estiver lento, tente QEMU:
emulator -avd <nome> -qemu -m 2048
```

### Problema: "Cannot find AndroidManifest.xml"

**Solução:**
- Verifique se `app/src/main/AndroidManifest.xml` existe
- Se não, crie a estrutura de pastas correta
- File → Project Structure → Modules → app → Sources

### Problema: "Java version mismatch"

**Solução:**
```bash
# Verificar Java
java -version

# Deve ser 11 ou superior
# Configure em Android Studio:
# Preferences → Build, Execution, Deployment → JDK location
```

### Problema: "Conexão com banco de dados falha"

**Solução:**
- Room é local (SQLite), não requer servidor
- Verificar permissões de arquivo
- Limpar dados do app:
  ```bash
  adb shell pm clear br.ufpi.lgpd.educacional
  ```

---

## 📦 Dependências Principais

### Instaladas Automaticamente

| Biblioteca | Versão | Uso |
|-----------|--------|-----|
| AndroidX Core | 1.12.0 | APIs modernas |
| AppCompat | 1.6.1 | Compatibilidade |
| Material Design | 1.10.0 | UI Components |
| Navigation | 2.7.5 | Navegação |
| Lifecycle | 2.6.2 | Lifecycle awareness |
| Room | 2.6.1 | Banco dados |
| Coroutines | 1.7.3 | Async |
| Retrofit | 2.9.0 | HTTP (futuro) |
| Timber | 5.0.1 | Logging |
| DataStore | 1.0.0 | Preferences |

---

## 🔐 Variáveis de Ambiente

### Recomendadas para Desenvolvimento

```bash
# macOS/Linux
export ANDROID_HOME="$HOME/Library/Android/sdk"
export JAVA_HOME="/usr/libexec/java_home"
export GRADLE_USER_HOME="$HOME/.gradle"

# Windows (PowerShell)
$env:ANDROID_HOME = "$env:UserProfile\AppData\Local\Android\sdk"
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
```

---

## 📝 Arquivo local.properties

Este arquivo é **auto-gerado** e **não deve ser commitado**:

```properties
# Exemplo (seu caminho será diferente)
sdk.dir=/Users/seu-usuario/Library/Android/sdk
# ou no Windows:
# sdk.dir=C:\\Users\\seu-usuario\\AppData\\Local\\Android\\sdk
```

Verifique que está no `.gitignore`:
```
local.properties
.gradle/
build/
*.iml
```

---

## ✅ Checklist Final

- [ ] Android Studio instalado (2023.3+)
- [ ] JDK 11+ instalado
- [ ] SDK 34 e 24 instalados
- [ ] Projeto clonado localmente
- [ ] Gradle sync executado com sucesso
- [ ] Emulador criado e testado
- [ ] Primeiro build bem-sucedido
- [ ] App executa sem erros
- [ ] Logcat sem erros críticos

---

## 🚀 Próximos Passos

1. **Explorar a Estrutura:**
   ```
   Open: java/br/ufpi/lgpd/educacional/
   ```

2. **Entender a Navegação:**
   ```
   Open: res/navigation/nav_graph.xml
   ```

3. **Ver Modelos de Dados:**
   ```
   Open: java/br/ufpi/lgpd/educacional/data/model/
   ```

4. **Modificar Conteúdo:**
   ```
   Open: java/br/ufpi/lgpd/educacional/data/
   Adicionar dados reais às lições e quizzes
   ```

5. **Testar no Dispositivo Real:**
   ```bash
   adb devices
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📞 Suporte

Se encontrar problemas:

1. Verifique o [Android Developer Documentation](https://developer.android.com/docs)
2. Procure a solução em [Stack Overflow](https://stackoverflow.com/questions/tagged/android)
3. Verifique logs detalhados em `Logcat` no Android Studio
4. Abra uma issue no repositório GitHub

---

## 📚 Recursos Adicionais

- [Android Developers](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs)
- [Material Design](https://m3.material.io)
- [AndroidX Documentation](https://developer.android.com/jetpack)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)

---

**Última atualização**: Abril 2026
**Status**: ✅ Verificado e Testado

