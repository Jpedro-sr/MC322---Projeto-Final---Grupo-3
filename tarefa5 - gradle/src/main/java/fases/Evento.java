package fases;

import personagens.heroi.Heroi;
// Interface que representa um evento que pode ser disparado dentro de uma fase (ex: uma emboscada, encontrar um tesouro).

public interface Evento {

    // Verifica se as condições para o evento acontecer foram atendidas.

    boolean verificarGatilho(Heroi heroi);

    // Executa a lógica do evento.

    void executar(Heroi heroi);
}