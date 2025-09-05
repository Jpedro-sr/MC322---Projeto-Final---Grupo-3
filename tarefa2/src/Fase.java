import java.util.ArrayList;

// Esta classe representa uma fase específica do jogo
// Pense nessa classe como um pacote que guarda todas as informações de uma etapa da jornada do Herói
public class Fase {
    // Atributos da Fase
    private int nivel;
    private String ambiente;
    private ArrayList<Monstro> monstros;

    // Construtor da Fase
    public Fase(int nivel, String ambiente, ArrayList<Monstro> monstros) {
        this.nivel = nivel;
        this.ambiente = ambiente;
        this.monstros = monstros;
    }

    // Getters
    public int getNivel() {
        return nivel;
    }
    
    public String getAmbiente() {
        return ambiente;
    }

    public ArrayList<Monstro> getMonstros() {
        return monstros;
    }
}