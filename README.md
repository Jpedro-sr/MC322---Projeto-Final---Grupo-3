Tarefa 3 – RPG de Batalha Marinha (MC322)

Projeto da disciplina MC322 – Programação Orientada a Objetos, Unicamp.
Um jogo de combate em turnos, ambientado em um mundo marítimo repleto de monstros e eventos.
O herói Capitão Cabeçudo enfrenta sereias, homens-peixe e o temido Kraken, evoluindo de fase em fase.

🗺️ Sumário

Visão Geral

Arquitetura do Código

Como Executar

Requisitos Atendidos

Principais Classes e Interfaces

Extensibilidade

Autores

Licença

Visão Geral

O jogo é totalmente automatizado: não requer entrada do usuário.
O motor principal (Main) cria o herói, gera uma campanha de fases e executa combates turno a turno, exibindo cada ação no console.

Características:

Batalhas táticas com sistema de turnos.

Loot e evolução do herói.

Eventos aleatórios baseados no cenário (p.ex. cardumes e emboscadas).

Cenários marítimos com efeitos próprios (Praia Assombrada, Gruta Submersa, Covil do Kraken).

Arquitetura do Código
src/
├─ Main.java                 # Ponto de entrada do jogo
├─ interfaces/
│   ├─ Combatente.java
│   ├─ AcaoDeCombate.java
│   ├─ Fase.java
│   ├─ GeradorDeFases.java
│   ├─ Evento.java
│   ├─ Item.java
│   └─ Lootavel.java
├─ modelos/
│   ├─ Personagem.java
│   ├─ Heroi.java
│   ├─ Monstro.java
│   ├─ CapitaoCabecudo.java
│   ├─ SereiaEncantadora.java
│   ├─ HomemPeixe.java
│   └─ Kraken.java
├─ fases/
│   ├─ TipoCenario.java
│   ├─ FaseDeCombate.java
│   └─ ConstrutorDeCenarioFixo.java
├─ eventos/
│   ├─ CardumePeixesDescontrolados.java
│   └─ EmboscadaCarangueijos.java
└─ acoes/
    ├─ AtaqueFisico.java
    ├─ CantoDivinoAcao.java
    ├─ JatoDeAmoniaAcao.java
    └─ ...


Interfaces definem contratos (combate, fases, loot, etc.).

Modelos implementam heróis, monstros e seus comportamentos.

Fases controlam a progressão e a geração dos cenários.

Eventos e ações tornam as batalhas dinâmicas e variadas.

Como Executar
Pré-requisitos

Java 21 ou superior (compila e roda em versões mais novas).

Nenhuma dependência externa.

Passo a passo

Clonar o repositório:

git clone <url-do-repo>
cd tarefa3


Compilar:

javac -d bin $(find src -name "*.java")


Executar:

java -cp bin Main


A saída no terminal mostrará:

descrição do cenário,

turnos de combate,

efeitos de eventos,

status de herói e monstros após cada rodada.

Requisitos Atendidos

Este projeto cumpre 100% dos requisitos descritos no enunciado da Tarefa 3 (vide Tarefa_MC322_3.pdf):

Criação das interfaces solicitadas (Combatente, AcaoDeCombate, Fase, GeradorDeFases, Evento, Item, Lootavel).

Enum TipoCenario com ao menos três cenários e efeitos únicos.

Implementação de Personagem, Heroi, Monstro, classes concretas, e ações de combate (mínimo de duas, excedido).

Fase de combate que se conclui quando todos os monstros são derrotados.

Gerador de fases que retorna uma campanha completa.

Main que integra tudo, rodando combates em loop sem input do usuário.

Principais Classes e Interfaces
Componente	Responsabilidade
Combatente	Contrato para qualquer entidade que participa de combates.
AcaoDeCombate	Define ataques, curas e efeitos especiais.
Fase / FaseDeCombate	Representa uma etapa do jogo, controlando combates e eventos.
GeradorDeFases	Cria a sequência de fases da campanha.
Evento	Acionamentos aleatórios com impacto no herói.
Item / Lootavel	Sistema de loot: monstros podem dropar itens.
TipoCenario	Define ambientes (Praia Assombrada, Gruta Submersa, Covil do Kraken) e seus efeitos.
Heroi / Monstro	Personagens jogáveis e inimigos, cada um com suas ações e atributos.
Main	Motor do jogo: executa as fases e imprime o andamento no console.
Extensibilidade

O design orientado a interfaces torna o projeto fácil de evoluir:

Novos monstros: basta criar uma classe que estenda Monstro e implementar suas ações.

Novas ações de combate: crie uma classe que implemente AcaoDeCombate.

Novos eventos ou cenários: adicione no enum TipoCenario e implemente em Evento.

IA mais complexa: ajuste Monstro.escolherAcao para estratégias mais sofisticadas (por exemplo, priorizar curas ou combos).

Dificuldade dinâmica: use o parâmetro nivel de GeradorDeFases.gerar(int) para escalar força, loot e número de inimigos.

Autores

Rafael Rodrigues Pimentel de Melo (RA 239356)
Matheus Boazão Silveira (RA 220080)

Licença

Distribuído sob a licença MIT. Consulte o arquivo LICENSE para mais detalhes.