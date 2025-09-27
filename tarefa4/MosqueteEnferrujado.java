
public class MosqueteEnferrujado extends Arma {
    // Construtor sem parâmetros (mantém o original)
    public MosqueteEnferrujado() {
        super("Mosquete Enferrujado", 14, 2);
    }

    // Construtor com parâmetros (para compatibilidade)
    public MosqueteEnferrujado(String nome, int dano, int minNivel) {
        super(nome, dano, minNivel);
    }
}