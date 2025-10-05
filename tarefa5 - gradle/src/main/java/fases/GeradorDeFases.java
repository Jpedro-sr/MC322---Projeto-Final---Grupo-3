package fases;
import java.util.ArrayList;

import config.Dificuldade;

/**
 * Interface que representa um objeto capaz de criar uma sequência de fases para o jogo.
 */
public interface GeradorDeFases {

    /**
     * Gera uma lista de fases para a campanha do jogo baseada na dificuldade escolhida.
     * A dificuldade afeta a força dos monstros e a quantidade de XP concedida.
     * 
     * @param quantidadeDeFases número de fases a serem geradas
     * @param dificuldade nível de dificuldade que afeta os parâmetros do jogo
     * @return lista de fases configuradas
     */
    ArrayList<Fase> gerar(int quantidadeDeFases, Dificuldade dificuldade);
}