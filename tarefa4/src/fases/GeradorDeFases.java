package fases;
import java.util.ArrayList;

import config.Dificuldade;

// Interface que representa um objeto capaz de criar uma sequência de fases para o jogo.

public interface GeradorDeFases {

    // Gera uma lista de fases para a campanha do jogo.

    ArrayList<Fase> gerar(int quantidadeDeFases, Dificuldade dificuldade);
}