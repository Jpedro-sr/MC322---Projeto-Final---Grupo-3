package ifome.model;
// essa versao 
//add getter do volume
//parece correto, precisa de testes
public class Bebida extends Produto {

    private int volumeML;


    public Bebida(String nome, String desc, double preco, int ml) {
        this.nome = nome;
        this.descricao = desc;
        this.preco = preco;
        this.categoria = "Bebida";
        this.volumeML = ml;
    }

    
    public int getVolumeML() {
        return volumeML;
    }

   // setter ml
    public void setVolumeML(int volumeML) {
        this.volumeML = volumeML > 0 ? volumeML : this.volumeML;
    }
}