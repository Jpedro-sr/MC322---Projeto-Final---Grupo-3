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
    
    // ✅ NOVO: Getters para vegetariano/vegano
    public boolean ehVegetariano() {
        return ehVegetariano;
    }
    
    public boolean ehVegano() {
        return ehVegano;
    }
    
    public void setVegetariano(boolean vegetariano) {
        this.ehVegetariano = vegetariano;
    }
    
    public void setVegano(boolean vegano) {
        this.ehVegano = vegano;
        if (vegano) {
            this.ehVegetariano = true; // Vegano é automaticamente vegetariano
        }
    }
}