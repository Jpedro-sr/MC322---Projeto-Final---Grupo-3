# Projeto de RPG: A Lenda da Ilha Perdida

**Tarefa 1 da disciplina MC322 - Programação Orientada a Objetos**
**Universidade Estadual de Campinas (UNICAMP)**

## 1. Descrição do Projeto

Este projeto implementa um jogo de RPG narrativo em Java, desenvolvido para a Tarefa 1 de MC322. O objetivo foi aplicar e solidificar conceitos de Programação Orientada a Objetos (POO), como Herança, Classes Abstratas e Polimorfismo, para criar um cenário de batalha dinâmico e imersivo.

A narrativa do jogo segue a aventura de um herói pirata que desembarca na amaldiçoada Ilha Perdida em busca de um tesouro lendário. Para alcançá-lo, ele deve sobreviver a um desafio de resistência, enfrentando três guardiões místicos em sequência.

## 2. Decisões de Design e Implementações Bônus

Além dos requisitos mínimos, foram tomadas decisões de design para enriquecer a experiência do jogo e aprofundar a aplicação dos conceitos de POO.

* [cite_start]**Mecânicas de Combate Únicas:** Em vez de ataques genéricos, cada personagem concreto possui um sistema de combate exclusivo, o que torna cada confronto uma experiência diferente[cite: 121, 122]. Isso inclui sistemas de sorte com dados, ataques probabilísticos, contadores de fúria, gerenciamento de estado e mecânicas de "carregamento" de poder.

* **Balanceamento e Dinâmica de Jogo:** Os atributos de todos os personagens (vida, força, etc.) foram cuidadosamente balanceados para criar uma curva de dificuldade desafiadora, mas justa. [cite_start]Foi implementada uma mecânica de **recuperação de vida** entre as batalhas (a "Garrafa de Rum"), essencial para a viabilidade do cenário de sobrevivência proposto[cite: 98].

* **Apresentação e Imersão no Console:** Um grande foco foi dado à experiência narrativa. [cite_start]A saída do console foi aprimorada com formatação especial, separadores visuais, indentação e **frases de ação randomizadas** para o herói, tornando o log de combate menos repetitivo e muito mais imersivo[cite: 133, 134].

## 3. Estrutura das Classes

O projeto utiliza uma hierarquia de herança para modelar os personagens:

* [cite_start]`Personagem` **(Abstrata):** A classe base para todas as entidades vivas, definindo atributos essenciais e o contrato para o método `atacar()`[cite: 58, 68].
* [cite_start]`Heroi` **(Abstrata):** Herda de `Personagem` e serve como molde para os heróis jogáveis, adicionando atributos de progressão e o contrato para `usarHabilidadeEspecial()`[cite: 70, 79].
* [cite_start]`Monstro` **(Abstrata):** Herda de `Personagem` e serve como molde para os inimigos, adicionando o atributo `xpConcedido`[cite: 87, 89].

### Os Personagens

Foram implementadas **duas classes concretas de herói** e três de monstros, cada uma com personalidade e mecânicas distintas.

#### Heróis

1.  **Capitão Cabeçudo:** Um pirata teimoso que confia na sorte. Seu ataque é definido por uma **rolagem de dados** que pode resultar em um acerto crítico, um acerto normal (com dano variável) ou um erro cômico.
2.  **Corsário Sedentário:** Um gênio tático e preguiçoso. Ele **estuda** o inimigo por vários turnos (causando dano mínimo) para acumular `pontosDeEstudo`, que são então consumidos em um único e devastador **"Golpe do Mínimo Esforço"**.

#### Monstros

1.  **Sereia Encantadora:** Uma criatura mística cujo ataque é **probabilístico**, com 30% de chance de usar seu poderoso Canto Divino e 70% de desferir um golpe físico mais fraco.
2.  **Homem-Peixe:** Um guerreiro bruto que acumula fúria. Ele utiliza um **contador de raiva**, desferindo 3 ataques normais antes de liberar um ataque especial devastador no quarto turno.
3.  **Kraken:** O chefe final, com uma mecânica de **dois estágios**. Primeiro, ele ataca com seus tentáculos com uma chance de agarrar o herói. Se conseguir, no turno seguinte ele executa um ataque de afogamento de dano massivo.

## 4. Como Compilar e Executar

**Pré-requisitos:**
* Java JDK 21 instalado.
* [cite_start]Estrutura de pastas conforme especificado[cite: 125, 126]:
    ```
    tarefa1/
    ├── src/  (contendo todos os 9 arquivos .java)
    └── bin/  (inicialmente vazia)
    ```

**Comandos (executados via terminal):**

1.  Navegue até a pasta `tarefa1`:
    ```bash
    cd tarefa1
    ```

2.  Compile todos os arquivos-fonte:
    ```bash
    javac -d bin $(find src -name "*.java")
    ```
    [cite_start][cite: 151]

3.  Execute o programa:
    ```bash
    java -cp bin Main
    ```
    [cite_start][cite: 151]

## 5. Autores

* **Rafael Rodrigues Pimentel de Melo** (RA 239356)
* **Matheus Boazão Silveira** (RA 220080)