// Interface que representa uma área ou nível do jogo com um objetivo.

public interface Fase {

    // Inicia a lógica principal da fase.

    void iniciar(Heroi heroi);

    boolean isConcluida();

    // Retorna o tipo de cenário da fase.

    TipoCenario getTipoDeCenario();
}