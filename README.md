Tarefa 4 – RPG de Batalha Marinha com Interação e Exceções (MC322)

Projeto da disciplina MC322 – Programação Orientada a Objetos, Unicamp.
Evolução do jogo da Tarefa 3, com foco em interação com o jogador, dificuldade configurável e tratamento de exceções.

🗺️ Sumário

Visão Geral

Principais Novidades em Relação à Tarefa 3

Arquitetura do Código

Como Executar

Requisitos Atendidos

Principais Classes e Interfaces

Extensibilidade

Autores

Licença

*Visão Geral*

O jogo mantém o combate em turnos ambientado no mar, com o herói Capitão Cabeçudo enfrentando Sereia Encantadora, Homem-Peixe e Kraken.
Agora, porém, o jogador participa ativamente, tomando decisões após cada fase e durante o uso de loot.

Características principais:

-> Batalhas estratégicas com loot real e armas equipáveis.

-> Sistema de dificuldade: Fácil, Normal ou Difícil, que afeta a força dos monstros e as recompensas.

-> Menus interativos para coleta de itens, visualização de status e escolha de continuar ou desistir.

Tratamento de exceções personalizadas (ex.: loot inexistente, nível insuficiente para equipar armas).

*Principais Novidades em Relação à Tarefa 3*

Recurso	--- O que mudou
Interação com o jogador	--- Inclusão de menus no console (menu principal e menu pós-turno) para coletar loot, ver status, continuar ou desistir.
Sistema de dificuldade --- Novo enum Dificuldade ajusta atributos de monstros e recompensas.
Gerenciador de entradas	--- Classe InputManager centraliza leitura de números e confirmações (sim/não) com validações.
Exceções customizadas --- LootIndisponivelException e NivelInsuficienteException tratam situações de jogo inválidas, evitando travamentos.
Estrutura em pacotes --- Código organizado em pacotes (app, combate, config, exceptions, fases, itens, itens.armas, personagens, personagens.heroi, personagens.monstros, util), facilitando manutenção e evolução.
Classe Main	--- Reescrita para controlar menus, dificuldade, tratamento de exceções e loop completo da campanha.

*Arquitetura do Código*

tarefa4/src/
├─ app/
│   └─ Main.java                  # Ponto de entrada do jogo
├─ util/
│   └─ InputManager.java          # Leitura segura de entradas do usuário
├─ config/
│   └─ Dificuldade.java           # Enum para níveis de dificuldade
├─ exceptions/
│   ├─ LootIndisponivelException.java
│   └─ NivelInsuficienteException.java
├─ combate/
│   └─ AcaoDeCombate.java
├─ itens/
│   ├─ Item.java
│   ├─ Lootavel.java
│   └─ armas/
│       └─ Arma.java
├─ personagens/
│   ├─ Combatente.java
│   ├─ Personagem.java
│   ├─ heroi/
│   │   └─ CapitaoCabecudo.java
│   └─ monstros/
│       ├─ Monstro.java
│       ├─ SereiaEncantadora.java
│       ├─ HomemPeixe.java
│       └─ Kraken.java
└─ fases/
    ├─ Fase.java
    ├─ FaseDeCombate.java
    ├─ TipoCenario.java
    ├─ GeradorDeFases.java
    └─ ConstrutorDeCenarioFixo.java

*Como Executar*

Pré-requisitos

Java 21 ou superior (compila em versões mais novas).

Nenhuma dependência externa.

Passo a passo

No diretório raiz do projeto (onde está a pasta tarefa4):

# Compilar
javac -d bin $(find tarefa4/src -name "*.java")

# Executar
java -cp bin app.Main


(Windows PowerShell)

Get-ChildItem -Recurse -Filter *.java "tarefa4\src" | % { $_.FullName } | javac -d bin @-
java -cp bin app.Main


Durante a execução, o console exibirá:

-> Menu principal (Novo jogo, Ver informações, Sair).

-> Escolha de dificuldade.

-> Combates em turnos, drop de loot e menus pós-turno.

*Requisitos Atendidos*

O projeto implementa todos os itens solicitados no enunciado da Tarefa 4 (Tarefa_MC322_4.pdf):

-> Interação com o jogador via menus.

-> Sistema de dificuldade parametrizável.

-> Gerenciador de entradas robusto.

-> Exceções customizadas e tratamento em todos os pontos críticos.

-> Refatoração em pacotes, melhorando organização e modularidade.

*Principais Classes e Interfaces*
Componente ---	Responsabilidade
InputManager --- Lê e valida números, confirmações e strings.
Dificuldade	--- Define níveis e escalas de atributos do jogo.
LootIndisponivelException / NivelInsuficienteException ---	Exceções para situações específicas do jogo.
FaseDeCombate --- Controla cada batalha e gerencia loot.
ConstrutorDeCenarioFixo ---	Gera as fases e inimigos da campanha.
Heroi / Monstro	--- Personagens jogáveis e inimigos com ações de combate.
Main --- Integra tudo, exibe menus e controla o fluxo completo do jogo.

*Extensibilidade*

O design orientado a objetos permite:

-> Adicionar novos monstros, heróis ou armas.

-> Criar novos cenários e eventos.

-> Ajustar regras de loot ou adicionar novas exceções para lógicas de jogo mais complexas.

-> Implementar IA em monstros.

*Autores*

Rafael Rodrigues Pimentel de Melo (RA 239356)

Matheus Boazão Silveira (RA 220080)