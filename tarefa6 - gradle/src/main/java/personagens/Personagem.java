package personagens;

import combate.AcaoDeCombate;
import combate.Combatente;
import itens.armas.Arma;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import personagens.heroi.Heroi;
import personagens.monstros.Monstro;

@XmlSeeAlso({Heroi.class, Monstro.class})
public abstract class Personagem implements Combatente {
    
    @XmlElement
    protected String nome;
    
    @XmlElement
    protected int pontosDeVida;
    
    @XmlElement
    protected int forca;
    
    @XmlElement
    protected Arma arma;

    // Construtor padrão para JAXB
    public Personagem() {
        this.arma = null;
    }

    public Personagem(String nome, int pontosDeVida, int forca) {
        this.nome = nome;
        this.pontosDeVida = pontosDeVida;
        this.forca = forca;
        this.arma = null;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public boolean estaVivo() {
        return this.pontosDeVida > 0;
    }

    @Override
    public void receberDano(int dano) {
        int vidaAntes = this.pontosDeVida;
        this.pontosDeVida -= dano;
        if (this.pontosDeVida < 0) {
            this.pontosDeVida = 0;
        }
        System.out.println("\t>> Dano sofrido! HP de " + this.nome + ": " + vidaAntes + " -> " + this.pontosDeVida);
    }

    @Override
    public void receberCura(int cura) {
        int vidaAntes = this.pontosDeVida;
        this.pontosDeVida += cura;
        System.out.println("\t>> Cura recebida! HP de " + this.nome + ": " + vidaAntes + " -> " + this.pontosDeVida);
    }

    @Override
    public abstract AcaoDeCombate escolherAcao(Combatente alvo);

    public void exibirStatus() {
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│ " + String.format("%-31s", nome) + " │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│ HP: " + String.format("%-27s", pontosDeVida) + " │");
        System.out.println("│ Força: " + String.format("%-24s", forca) + " │");
        if (arma != null) {
            System.out.println("│ Arma: " + String.format("%-25s", arma.getNome()) + " │");
            System.out.println("│ Dano da Arma: " + String.format("%-17s", "+" + arma.getDano()) + " │");
        } else {
            System.out.println("│ Arma: " + String.format("%-25s", "Desarmado") + " │");
        }
        System.out.println("└─────────────────────────────────┘");
    }

    public int getPontosDeVida() { return pontosDeVida; }
    public void setPontosDeVida(int pontosDeVida) { this.pontosDeVida = pontosDeVida; }
    public int getForca() { return forca; }
    public void setForca(int forca) { this.forca = forca; }
    public Arma getArma() { return arma; }
    public void setArma(Arma arma) { this.arma = arma; }
}