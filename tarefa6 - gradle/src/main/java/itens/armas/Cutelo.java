package itens.armas;

public class Cutelo extends Arma {
    // Construtor sem parâmetros (mantém o original)
    public Cutelo() {
        super("Cutelo", 10, 1);
    }

    // Construtor com parâmetros (para compatibilidade)
    public Cutelo(String nome, int dano, int minNivel) {
        super(nome, dano, minNivel);
    }
}