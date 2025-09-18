import java.util.ArrayList;

/**
 * Interface que representa um objeto capaz de criar uma sequência de fases para o jogo.
 * (Conforme Tarefa 3, Seção 3.1.2)
 */
public interface GeradorDeFases {

    /**
     * Gera uma lista de fases para a campanha do jogo.
     * @param quantidadeDeFases O número de fases a serem criadas.
     * @return um ArrayList de objetos que implementam a interface Fase.
     */
    ArrayList<Fase> gerar(int quantidadeDeFases);
}