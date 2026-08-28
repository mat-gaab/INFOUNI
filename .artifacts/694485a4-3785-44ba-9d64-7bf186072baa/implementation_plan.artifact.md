# Plano de Correção: Navegação de Detalhes e Atualização da IA

Este plano visa resolver a falha na navegação para os detalhes das universidades e atualizar o modelo da IA Gemini que foi descontinuado.

## User Review Required

> [!IMPORTANT]
> A tela de Detalhes não existia no projeto. Vou criar uma nova Activity (`DetalhesActivity`) para exibir as informações completas da universidade.

> [!WARNING]
> O modelo `gemini-1.5-flash` foi desativado em 2025 (no contexto do projeto que está em 2026). Vou atualizar para o `gemini-2.0-flash`.

## Proposed Changes

### Source Code

#### [MODIFY] [Universidade.kt](file:///home/gabriel/AndroidStudioProjects/INFOUNI/app/src/main/java/com/exemplo/infouni/Universidade.kt)
- Implementar a interface `java.io.Serializable` para permitir o envio do objeto entre telas.

#### [NEW] [DetalhesActivity.kt](file:///home/gabriel/AndroidStudioProjects/INFOUNI/app/src/main/java/com/exemplo/infouni/DetalhesActivity.kt)
- Nova tela que recebe os dados da universidade e os exibe.

#### [NEW] [activity_detalhes.xml](file:///home/gabriel/AndroidStudioProjects/INFOUNI/app/src/main/res/layout/activity_detalhes.xml)
- Layout da tela de detalhes com imagem, nome, cidade, infraestrutura e cursos.

#### [MODIFY] [UniversidadeAdapter.kt](file:///home/gabriel/AndroidStudioProjects/INFOUNI/app/src/main/java/com/exemplo/infouni/UniversidadeAdapter.kt)
- Adicionar o clique no botão `btnVerDetalhes` para abrir a `DetalhesActivity`.

#### [MODIFY] [PesquisaIAActivity.kt](file:///home/gabriel/AndroidStudioProjects/INFOUNI/app/src/main/java/com/exemplo/infouni/PesquisaIAActivity.kt)
- Atualizar o nome do modelo de `gemini-1.5-flash` para `gemini-2.0-flash`.

#### [MODIFY] [AndroidManifest.xml](file:///home/gabriel/AndroidStudioProjects/INFOUNI/app/src/main/AndroidManifest.xml)
- Registrar a `DetalhesActivity`.

### Resources

#### [MODIFY] [strings.xml](file:///home/gabriel/AndroidStudioProjects/INFOUNI/app/src/main/res/values/strings.xml)
- Adicionar strings para a tela de detalhes.

## Verification Plan

### Automated Tests
- Executar `./gradlew app:assembleDebug` para garantir a compilação.

### Manual Verification
- Testar o clique no botão "Ver Detalhes" na lista da `MainActivity`.
- Realizar uma pesquisa na `PesquisaIAActivity` e verificar se a resposta da IA é exibida sem erro de modelo expirado.
