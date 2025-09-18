/**
 * Interface que representa qualquer entidade que pode participar de uma batalha.
 * (Conforme Tarefa 3, Seção 3.1.1)
 */
public interface Combatente {

    /**
     * Retorna o nome do combatente.
     */
    String getNome();

    /**
     * Verifica se o combatente ainda tem pontos de vida.
     * @return true se os pontos de vida forem maiores que zero.
     */
    boolean estaVivo();

    /**
     * Aplica uma quantidade de dano ao combatente.
     * @param dano A quantidade de dano a ser subtraída da vida.
     */
    void receberDano(int dano);

    /**
     * Aplica uma quantidade de cura ao combatente.
     * @param cura A quantidade de vida a ser restaurada.
     */
    void receberCura(int cura);

    /**
     * Define a "IA" do combatente. Este método decide qual ação será tomada.
     * @param alvo O combatente que será o alvo da ação.
     * @return um objeto AcaoDeCombate para ser executado.
     */
    AcaoDeCombate escolherAcao(Combatente alvo);
}