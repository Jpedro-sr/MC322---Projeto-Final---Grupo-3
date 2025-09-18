/**
 * Interface que representa uma área ou nível do jogo com um objetivo.
 * (Conforme Tarefa 3, Seção 3.1.2)
 */
public interface Fase {

    /**
     * Inicia a lógica principal da fase.
     * @param heroi O herói que está participando da fase.
     */
    void iniciar(Heroi heroi);

    /**
     * Verifica se a fase foi concluída com sucesso.
     * @return true se o objetivo da fase foi alcançado.
     */
    boolean isConcluida();

    /**
     * Retorna o tipo de cenário da fase.
     * @return um valor do Enum TipoCenario.
     */
    TipoCenario getTipoDeCenario();
}