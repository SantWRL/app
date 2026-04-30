package br.ufpi.lgpd.educacional.data

import br.ufpi.lgpd.educacional.data.model.Quiz

object LgpdContent {
    val quizzes = listOf(
        Quiz(1, "Fundamentos da LGPD", "Conceitos essenciais, objetivo da lei e tipos de dados.", "Fundamentos", "BEGINNER", 8),
        Quiz(2, "Princípios e bases legais", "Finalidade, necessidade, transparência e hipóteses de tratamento.", "Conformidade", "BEGINNER", 10),
        Quiz(3, "Direitos dos titulares", "Pedidos de acesso, correção, eliminação, portabilidade e revisão.", "Direitos", "INTERMEDIATE", 10),
        Quiz(4, "Atores e responsabilidades", "Titular, controlador, operador, encarregado e ANPD.", "Atores", "INTERMEDIATE", 8),
        Quiz(5, "Segurança e incidentes", "Boas práticas, prevenção, resposta e comunicação de incidentes.", "Segurança", "INTERMEDIATE", 12),
        Quiz(6, "LGPD na universidade", "Situações acadêmicas, pesquisa, matrícula e eventos.", "Aplicação", "ADVANCED", 12),
        Quiz(7, "Revisão geral", "Um simulado com temas essenciais para fixação.", "Revisão", "ADVANCED", 20)
    )

    val quizQuestions = mapOf(
        1 to listOf(
            QuizQuestionContent("Qual é o principal objetivo da LGPD?", listOf("Eliminar o uso de dados pessoais", "Proteger direitos fundamentais de liberdade e privacidade", "Permitir venda livre de bases de dados", "Substituir todas as políticas internas"), 1, "A LGPD regula o tratamento de dados pessoais para proteger liberdade, privacidade e desenvolvimento da personalidade."),
            QuizQuestionContent("Um dado pessoal é uma informação que:", listOf("Sempre precisa ser secreta", "Identifica ou pode identificar uma pessoa natural", "Só existe em sistemas digitais", "Pertence apenas a empresas"), 1, "Dado pessoal é qualquer informação relacionada a pessoa natural identificada ou identificável."),
            QuizQuestionContent("Qual exemplo representa dado pessoal sensível?", listOf("Nome completo", "E-mail institucional", "Dado de saúde", "Número de matrícula"), 2, "Dados de saúde são sensíveis e exigem proteção reforçada."),
            QuizQuestionContent("A LGPD se aplica a:", listOf("Somente empresas privadas", "Somente aplicativos móveis", "Tratamento de dados em meios físicos ou digitais", "Apenas dados financeiros"), 2, "A lei alcança tratamento de dados pessoais em meios físicos e digitais, no setor público e privado.")
        ),
        2 to listOf(
            QuizQuestionContent("O princípio da necessidade orienta a organização a:", listOf("Coletar todos os dados possíveis", "Usar apenas dados necessários à finalidade", "Guardar dados para sempre", "Pedir consentimento para tudo"), 1, "A necessidade limita o tratamento ao mínimo necessário para cumprir a finalidade."),
            QuizQuestionContent("Consentimento válido deve ser:", listOf("Livre, informado e inequívoco", "Automático e permanente", "Presumido quando o usuário navega", "Obrigatório em qualquer tratamento"), 0, "Quando usado, o consentimento precisa ser livre, informado e inequívoco."),
            QuizQuestionContent("Qual alternativa é uma base legal da LGPD?", listOf("Curiosidade institucional", "Obrigação legal ou regulatória", "Facilidade operacional", "Interesse sem justificativa"), 1, "Obrigação legal ou regulatória é uma das hipóteses legais para tratamento."),
            QuizQuestionContent("Transparência significa:", listOf("Explicar finalidades e formas de tratamento", "Exibir código-fonte do sistema", "Evitar qualquer coleta", "Publicar dados pessoais"), 0, "O titular deve receber informações claras, precisas e acessíveis.")
        ),
        3 to listOf(
            QuizQuestionContent("Qual é um direito do titular?", listOf("Impedir toda obrigação legal", "Acessar dados pessoais tratados", "Apagar registros públicos obrigatórios sempre", "Exigir senha de outro usuário"), 1, "Acesso aos dados é um direito previsto para o titular."),
            QuizQuestionContent("Se um dado estiver incorreto, o titular pode pedir:", listOf("Correção", "Venda", "Duplicação", "Bloqueio da ANPD"), 0, "A correção de dados incompletos, inexatos ou desatualizados é um direito do titular."),
            QuizQuestionContent("A revogação do consentimento permite:", listOf("Cancelar consentimento antes dado", "Apagar qualquer lei", "Transferir obrigação ao titular", "Autorizar compartilhamento automático"), 0, "O consentimento pode ser revogado mediante manifestação do titular."),
            QuizQuestionContent("Decisões automatizadas podem ser:", listOf("Revisadas quando afetarem interesses do titular", "Sempre secretas", "Usadas sem critério", "Proibidas em todos os casos"), 0, "A LGPD prevê revisão de decisões tomadas unicamente com base em tratamento automatizado em certas situações.")
        ),
        4 to listOf(
            QuizQuestionContent("Quem decide a finalidade e os meios do tratamento?", listOf("Operador", "Controlador", "Titular", "Usuário visitante"), 1, "O controlador toma as principais decisões sobre o tratamento."),
            QuizQuestionContent("O operador atua:", listOf("Em nome do controlador", "Como titular dos dados", "Sempre como ANPD", "Sem seguir instruções"), 0, "O operador realiza tratamento conforme instruções do controlador."),
            QuizQuestionContent("O encarregado tem papel de:", listOf("Ponte entre controlador, titulares e ANPD", "Excluir todos os dados", "Criar multas", "Substituir o titular"), 0, "O encarregado atua como canal de comunicação em temas de proteção de dados."),
            QuizQuestionContent("A ANPD é responsável por:", listOf("Orientar e fiscalizar a aplicação da LGPD", "Guardar senhas dos usuários", "Autorizar todo login", "Coletar dados de alunos"), 0, "A Autoridade Nacional de Proteção de Dados orienta, fiscaliza e pode aplicar sanções.")
        ),
        5 to listOf(
            QuizQuestionContent("Qual prática melhora a segurança de dados?", listOf("Compartilhar senha da equipe", "Usar mínimo privilégio", "Enviar planilhas abertas por grupos", "Guardar documentos sem controle"), 1, "Mínimo privilégio reduz acesso indevido."),
            QuizQuestionContent("Um incidente de segurança pode envolver:", listOf("Acesso não autorizado a dados pessoais", "Troca de tema visual", "Atualização de ícone", "Apenas erro de digitação"), 0, "Acesso indevido, perda ou divulgação não autorizada podem caracterizar incidente."),
            QuizQuestionContent("Ao suspeitar de vazamento, a primeira postura adequada é:", listOf("Ignorar se ninguém perguntou", "Conter, registrar e avaliar riscos", "Publicar os dados", "Apagar evidências"), 1, "A resposta deve conter danos, documentar fatos e avaliar riscos aos titulares."),
            QuizQuestionContent("Backup ajuda porque:", listOf("Substitui transparência", "Permite recuperar dados em falhas", "Dispensa controle de acesso", "Remove obrigação legal"), 1, "Backups bem geridos apoiam continuidade e recuperação.")
        ),
        6 to listOf(
            QuizQuestionContent("Em pesquisa acadêmica, é importante:", listOf("Coletar mais dados por garantia", "Informar finalidade e proteger participantes", "Divulgar respostas individuais", "Ignorar dados sensíveis"), 1, "Projetos acadêmicos devem informar finalidades e proteger participantes."),
            QuizQuestionContent("Lista de presença em evento deve conter:", listOf("Apenas dados necessários à finalidade", "Dados de saúde sem motivo", "Senha dos participantes", "Informações familiares completas"), 0, "A coleta deve respeitar necessidade e finalidade."),
            QuizQuestionContent("Dados de alunos devem ser acessados:", listOf("Por qualquer pessoa curiosa", "Somente por quem precisa para a atividade", "Sempre publicamente", "Sem registro de acesso"), 1, "Controle de acesso é medida básica de segurança."),
            QuizQuestionContent("Ao usar formulário online, a instituição deve:", listOf("Explicar o uso dos dados coletados", "Pedir documentos sem finalidade", "Compartilhar respostas sem aviso", "Desativar toda segurança"), 0, "Transparência sobre finalidade e tratamento é essencial.")
        )
    )

    fun questionsForQuiz(quizId: Int): List<QuizQuestionContent> {
        return quizQuestions[quizId] ?: quizQuestions.values.flatten().take(8)
    }
}

data class QuizQuestionContent(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
)
