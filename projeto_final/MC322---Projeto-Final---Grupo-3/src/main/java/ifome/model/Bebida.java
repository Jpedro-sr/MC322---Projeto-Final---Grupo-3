package ifome.model;

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
}