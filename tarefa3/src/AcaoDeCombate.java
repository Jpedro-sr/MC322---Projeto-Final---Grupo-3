/**
 * Interface que representa uma ação que um Combatente pode executar (atacar,
 * usar habilidade, etc.).
 * (Conforme Tarefa 3, Seção 3.1.1)
 */
public interface AcaoDeCombate {

    /**
     * Executa a lógica da ação de combate.
     * @param usuario O Combatente que está realizando a ação.
     * @param alvo O Combatente que está sofrendo a ação.
     */
    void executar(Combatente usuario, Combatente alvo);
}