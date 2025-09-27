
// Classe abstrata que representa uma arma no jogo.
// Toda arma tem nome, dano e nível mínimo necessário para ser utilizada.

public abstract class Arma implements Item {
    protected String nome;
    protected int dano;
    protected int minNivel;

    // Cria uma arma com nome, dano e nível mínimo exigido.
    public Arma(String nome, int dano, int minNivel) {
        this.nome = nome;
        this.dano = dano;
        this.minNivel = minNivel;
    }

    // Retorna o nome da arma.
    @Override
    public String getNome() {
        return nome;
    }

    // Retorna o dano que a arma causa.
    public int getDano() {
        return dano;
    }

    // Retorna o nível mínimo necessário para equipar a arma.
    public int getMinNivel() {
        return minNivel;
    }
}