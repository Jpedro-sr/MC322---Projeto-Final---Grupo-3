public class PistolaDoKraken extends Arma {
    // Construtor sem parâmetros (mantém o original)
    public PistolaDoKraken() {
        super("Pistola do Kraken", 22, 3);
    }
    
    // Construtor com parâmetros (para compatibilidade)
    public PistolaDoKraken(String nome, int dano, int minNivel) {
        super(nome, dano, minNivel);
    }
}