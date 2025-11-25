package ifome.model;

/**
 * ✅ CORRIGIDO: Adicionado getter para volumeML
 */
public class Bebida extends Produto {

    private int volumeML;

    // Construtor
    public Bebida(String nome, String desc, double preco, int ml) {
        this.nome = nome;
        this.descricao = desc;
        this.preco = preco;
        this.categoria = "Bebida";
        this.volumeML = ml;
    }

    /**
     * ✅ NOVO: Getter para volume em mililitros
     */
    public int getVolumeML() {
        return volumeML;
    }

    /**
     * ✅ NOVO: Setter para volume em mililitros
     */
    public void setVolumeML(int volumeML) {
        this.volumeML = volumeML > 0 ? volumeML : this.volumeML;
    }
}