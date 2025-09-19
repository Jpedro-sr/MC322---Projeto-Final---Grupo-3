# Projeto de RPG: A Lenda da Ilha Perdida (Tarefa 2)

**Tarefa 2 da disciplina MC322 - Programação Orientada a Objetos**
**Universidade Estadual de Campinas (UNICAMP)**

## 1. Visão Geral do Projeto

Este projeto implementa um jogo de RPG narrativo em Java, focado na aplicação de conceitos avançados de Programação Orientada a Objetos. A aplicação simula a jornada de um herói pirata em uma ilha amaldiçoada, onde ele deve enfrentar hordas de monstros em diferentes fases para alcançar um tesouro lendário.

O sistema foi projetado para ser dinâmico e expansível, utilizando uma arquitetura de classes que permite a fácil adição de novos heróis, monstros, itens e cenários.

## 2. Evolução do Projeto: Da Tarefa 1 para a Tarefa 2

A Tarefa 2 expandiu significativamente a base da Tarefa 1, transformando uma simulação linear em um sistema de RPG mais robusto e dinâmico. As principais evoluções foram:

* [cite_start]**Geração Dinâmica de Mundo:** O roteiro fixo da Tarefa 1 foi substituído por um sistema de `Fase` e um `ConstrutorDeCenario`. [cite_start]O `ConstrutorDeCenario` agora gera uma aventura com um número configurável de fases, cada uma com ambientes e monstros sorteados aleatoriamente e com **dificuldade crescente**[cite: 46], garantindo alta rejogabilidade.

* [cite_start]**Sistema de Progressão do Herói:** O herói agora possui um sistema de progressão completo[cite: 13]. [cite_start]Ele ganha experiência (XP) ao derrotar inimigos e pode **subir de nível** (`subirDeNivel`), o que aumenta seus atributos e o cura completamente, criando um ciclo de recompensa e fortalecimento.

* [cite_start]**Sistema de Itens (Armas):** Foi introduzido um sistema de itens através da classe abstrata `Arma` e suas implementações concretas[cite: 48, 54]. [cite_start]Os monstros agora podem carregar e **largar armas** (`largaArma`) [cite: 105][cite_start], e o herói pode **equipá-las** (`equiparArma`)  para aumentar seu poder de ataque, adicionando uma camada estratégica de customização.

* [cite_start]**Atributo `sorte`:** A sorte deixou de ser um conceito abstrato e se tornou um atributo central do herói. [cite_start]Ela agora influencia diretamente a chance de usar **habilidades especiais**  [cite_start]e de obter **recompensas valiosas** dos monstros.

## 3. Estrutura das Classes

O projeto utiliza uma hierarquia de herança para modelar o universo do jogo:

* [cite_start]`Personagem` (Abstrata): A base para todas as entidades vivas, definindo atributos essenciais (`nome`, `pontosDeVida`, `forca`) e a capacidade de segurar uma `arma`[cite: 63].
* [cite_start]`Heroi` (Abstrata): Herda de `Personagem` e adiciona atributos de progressão (`nivel`, `XP`, `sorte`) [cite: 74, 75, 76, 78] [cite_start]e métodos para `subirDeNivel`  [cite_start]e `equiparArma`.
* [cite_start]`Monstro` (Abstrata): Herda de `Personagem` e adiciona atributos de recompensa, como `xpConcedido` [cite: 100] [cite_start]e uma lista de armas que pode largar (`listaDeArmasParaLargar`)[cite: 101].
* [cite_start]`Arma` (Abstrata): A base para todos os itens equipáveis, definindo `dano` e `minNivel`[cite: 49, 50, 51].
* [cite_start]`Fase`: Um objeto que representa um "capítulo" da aventura, contendo um `ambiente` e uma lista de `monstros`[cite: 36, 37, 38, 39].

### Personagens e Mecânicas Únicas

[cite_start]Foram implementadas duas classes de herói e três de monstros, cada uma com um comportamento único em combate, um dos focos do critério de **Diferenciação e Criatividade**[cite: 143].

#### Heróis
1.  **Capitão Cabeçudo:** Um pirata que confia na sorte. Sua chance de usar a habilidade especial "Tiro Caolho" é determinada diretamente por seu atributo `sorte`.
2.  **Corsário Sedentário:** Um gênio tático e preguiçoso. Sua `sorte` influencia a velocidade com que ele acumula "Pontos de Estudo", o recurso para seu devastador "Golpe do Mínimo Esforço".

#### Monstros
1.  **Sereia Encantadora:** Possui um ataque probabilístico, com chance de usar um canto mágico poderoso ou um golpe físico mais fraco.
2.  **Homem-Peixe:** Utiliza um contador de raiva, tornando-se mais perigoso a cada turno e liberando um ataque especial após um número fixo de ações.
3.  **Kraken:** Um chefe com uma mecânica de dois estágios, que primeiro tenta agarrar o herói para depois executar um ataque de afogamento devastador.

## 4. Como Compilar e Executar

**Pré-requisitos:**
* Java JDK 21 instalado.
* Estrutura de pastas `tarefa2/src` e `tarefa2/bin`.

**Comandos (executados via terminal):**

1.  Navegue até a pasta `tarefa2`:
    ```bash
    cd tarefa2
    ```
2.  Compile todos os arquivos-fonte:
    ```bash
    javac -d bin $(find src -name "*.java")
    ```
3.  Execute o programa:
    ```bash
    java -cp bin Main
    ```

## 5. Autores

* **Rafael Rodrigues Pimentel de Melo** (RA 239356)
* **Matheus Boazão Silveira** (RA 220080)