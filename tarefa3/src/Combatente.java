// Interface para qualquer entidade que pode participar de uma batalha.
// Um combatente pode ser um herói, monstro ou chefe, e precisa implementar métodos básicos de combate.
public interface Combatente {

    // Retorna o nome do combatente (usado para exibição e narração).
    String getNome();

    // Indica se o combatente ainda está vivo, ou seja, se possui pontos de vida acima de zero.
    boolean estaVivo();

    // Aplica dano ao combatente, reduzindo seus pontos de vida.
    void receberDano(int dano);

    // Aplica cura ao combatente, aumentando seus pontos de vida.
    void receberCura(int cura);

    // Decide qual ação de combate será tomada no turno, podendo variar conforme a estratégia ou IA.
    AcaoDeCombate escolherAcao(Combatente alvo);
}