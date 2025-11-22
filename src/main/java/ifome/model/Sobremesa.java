package ifome.model;

public class Sobremesa extends Produto {
    
    // (Pode adicionar atributos específicos, ex: boolean contemLactose)
    
    // Construtor
    public Sobremesa(String nome, String desc, double preco) {
        this.nome = nome;
        this.descricao = desc;
        this.preco = preco;
        this.categoria = "Sobremesa";
    }
}