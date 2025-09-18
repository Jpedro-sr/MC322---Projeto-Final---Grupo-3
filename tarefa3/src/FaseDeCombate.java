import java.util.ArrayList;

public class FaseDeCombate implements Fase {

    // Atributos da Fase
    private int nivel;
    private String ambiente;
    private ArrayList<Monstro> monstros;

    public FaseDeCombate(int nivel, String ambiente, ArrayList<Monstro> monstros) {
        this.nivel = nivel;
        this.ambiente = ambiente;
        this.monstros = monstros;
    }

    // --- Getters ---
    public int getNivel() {
        return nivel;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public ArrayList<Monstro> getMonstros() {
        return monstros;
    }

    @Override
    public boolean isConcluida(Heroi heroi, Monstro monstro) {
        if (heroi.getPontosDeVida() <= 0 && heroi.getPontosDeVida() <= 0) {
            return true; // A fase não está concluída, o combate continua
        } else {
            return false; // A fase está concluída, o combate terminou
        }
}
}