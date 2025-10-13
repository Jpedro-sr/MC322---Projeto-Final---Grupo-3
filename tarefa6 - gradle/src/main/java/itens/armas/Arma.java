package itens.armas;

import itens.Item;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlSeeAlso({Cutelo.class, MosqueteEnferrujado.class, PistolaDoKraken.class, ArmaSimples.class})
public abstract class Arma implements Item {
    
    @XmlElement
    protected String nome;
    
    @XmlElement
    protected int dano;
    
    @XmlElement
    protected int minNivel;

    // Construtor padrão para JAXB
    public Arma() {}

    public Arma(String nome, int dano, int minNivel) {
        this.nome = nome;
        this.dano = dano;
        this.minNivel = minNivel;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public int getDano() {
        return dano;
    }

    public int getMinNivel() {
        return minNivel;
    }
}