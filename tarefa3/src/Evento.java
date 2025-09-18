/**
 * Interface que representa um evento que pode ser disparado dentro de uma fase
 * (ex: uma emboscada, encontrar um tesouro).
 * (Conforme Tarefa 3, Seção 3.1.2)
 */
public interface Evento {

    /**
     * Verifica se as condições para o evento acontecer foram atendidas.
     * @param heroi O herói atual, para o caso de o gatilho depender de seus status.
     * @return true se o evento deve ser disparado, false caso contrário.
     */
    boolean verificarGatilho(Heroi heroi);

    /**
     * Executa a lógica do evento.
     * @param heroi O herói que será afetado pelo evento.
     */
    void executar(Heroi heroi);
}