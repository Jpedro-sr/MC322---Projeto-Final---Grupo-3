package ifome.model;

public class Comida extends Produto {

    private boolean ehVegetariano;
    private boolean ehVegano;

    // Construtor
    public Comida(String nome, String desc, double preco, boolean veg) {
        this.nome = nome;
        this.descricao = desc;
        this.preco = preco;
        this.categoria = "Comida";
        this.ehVegetariano = veg;
    }
}