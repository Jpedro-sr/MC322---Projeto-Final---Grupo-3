package ifome.model;

public class Adicional extends Produto {

    // Construtor
    public Adicional(String nome, double preco) {
        this.nome = nome;
        this.descricao = "Adicional";
        this.preco = preco;
        this.categoria = "Adicional";
    }
}