public abstract class Arma {

    // Atributos de Arma
    protected int dano;
    protected int minNivel;

    // Construtor de Arma
    public Arma(int dano, int minNivel) {
        this.dano = dano;
        this.minNivel = minNivel;
    }

    // Getters
    public int getDano() {
        return dano;
    }
    
    public int getMinNivel() {
        return minNivel;
    }
}