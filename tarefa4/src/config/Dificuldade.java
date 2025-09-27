package config;
public enum Dificuldade {
    FACIL (0.75, 1.25),
    NORMAL (1.0, 1.0),
    DIFICIL (1.5, 0.8);

    private final double multiplicadorForcaMonstro;
    private final double multiplicadorXp;

    Dificuldade(double multForca, double multXp) {
        this.multiplicadorForcaMonstro = multForca;
        this.multiplicadorXp = multXp;
    }

    public double getMultiplicadorForcaMonstro() {
        return multiplicadorForcaMonstro;
    }

    public double getMultiplicadorXp() {
        return multiplicadorXp;
    }
    }