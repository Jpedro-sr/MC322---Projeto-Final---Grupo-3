public abstract class Arma implements Item {
    protected String nome;
    protected int dano;
    protected int minNivel;

    public Arma(String nome, int dano, int minNivel) {
        this.nome = nome;
        this.dano = dano;
        this.minNivel = minNivel;
    }

    // Implementação da interface Item
    @Override
    public String getNome() {
        return nome;
    }

    // Getters específicos da arma
    public int getDano() {
        return dano;
    }

    public int getMinNivel() {
        return minNivel;
    }
}