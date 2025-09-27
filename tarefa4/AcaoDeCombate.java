
// Representa uma ação de combate realizada por um combatente, como atacar ou usar uma habilidade.
// O método executar recebe o usuário da ação e o alvo, permitindo aplicar efeitos de combate.
public interface AcaoDeCombate {
    void executar(Combatente usuario, Combatente alvo);
}