public abstract class Arma implements Item{

    // Atributos que toda arma terá.
    protected String nome;
    protected int dano;
    protected int minNivel;

    public Arma(int dano, int minNivel) {
        this.dano = dano;
        this.minNivel = minNivel;
    }

    // --- Getters ---
    public int getDano() {
        return dano;
    }

    public int getMinNivel() {
        return minNivel;
    }

    @Override
    public String getNome() {
        return nome;
    }
}