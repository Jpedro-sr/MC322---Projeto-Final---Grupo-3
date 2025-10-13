package personagens.heroi;

import java.util.ArrayList;
import java.util.List;

import combate.AcaoDeCombate;
import exceptions.NivelInsuficienteException;
import itens.armas.Arma;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlTransient;
import personagens.Personagem;

/**
 * Classe base para todos os heróis.
 * Anotada com JAXB para permitir serialização.
 */
@XmlRootElement
@XmlSeeAlso({CapitaoCabecudo.class, CorsarioSedentario.class})
public abstract class Heroi extends Personagem {
    
    @XmlElement
    protected int maxPontosDeVida;
    
    @XmlElement
    protected int nivel;
    
    @XmlElement
    protected int XP;
    
    @XmlElement
    protected int expProximoNivel;
    
    @XmlElement
    protected double sorte;
    
    @XmlTransient // Não serializar ações (serão recriadas no construtor)
    protected List<AcaoDeCombate> acoes;
    
    @XmlElement
    protected int pontosDeEstudo;

    // Construtor padrão para JAXB
    public Heroi() {
        super();
        this.acoes = new ArrayList<>();
    }

    public Heroi(String nome, int pontosDeVida, int forca, int nivel, int XP) {
        super(nome, pontosDeVida, forca);
        this.maxPontosDeVida = pontosDeVida;
        this.nivel = nivel;
        this.XP = XP;
        this.expProximoNivel = 80;
        this.sorte = 0.2;
        this.acoes = new ArrayList<>();
        this.pontosDeEstudo = 0;
    }

    private void subirDeNivel() {
        this.nivel++;
        this.XP -= this.expProximoNivel;
        this.expProximoNivel *= 1.5;
        this.maxPontosDeVida += 25;
        this.forca += 5;
        this.sorte += 0.05;
        this.pontosDeVida = this.maxPontosDeVida;
        System.out.println("\t*** LEVEL UP! " + this.nome + " subiu para o nível " + this.nivel + "! ***");
    }

    public void ganharExperiencia(int XPrecebido) {
        this.XP += XPrecebido;
        System.out.println("[+] Seu heroi " + nome + " ganhou " + XPrecebido + " pontos de XP!" );
        while (this.XP >= this.expProximoNivel) {
            subirDeNivel();
        }
    }

    public void equiparArma(Arma novaArma) throws NivelInsuficienteException {
        if (this.nivel >= novaArma.getMinNivel()) {
            this.arma = novaArma;
            System.out.println("[+] " + this.nome + " equipou a arma: " + novaArma.getNome());
        } else {
            throw new NivelInsuficienteException(
                "Nível insuficiente para equipar " + novaArma.getNome() +
                " (requer nível " + novaArma.getMinNivel() + ", atual: " + this.nivel + ")"
            );
        }
    }

    public void adicionarPontosDeEstudo(int pontos) {
        this.pontosDeEstudo += pontos;
    }

    public int getPontosDeEstudo() {
        return this.pontosDeEstudo;
    }

    public void zerarPontosDeEstudo() {
        this.pontosDeEstudo = 0;
    }

    public void exibirStatus() {
        System.out.println("------------------------");
        System.out.println("~ " + nome);
        System.out.println(" HP: " + pontosDeVida + "/" + maxPontosDeVida);
        System.out.println(" Pontos de Força: " + forca);
        System.out.println(" Nível: " + nivel);
        System.out.println(" XP: " + XP + "/" + expProximoNivel);
        System.out.println(" Sorte: " + (int)(sorte * 100) + "%");
        System.out.println(" Pontos de Estudo: " + pontosDeEstudo);
        if (this.arma != null) {
            System.out.println(" Arma: " + this.arma.getNome() + " (+" + this.arma.getDano() + " Dano)");
        } else {
            System.out.println(" Arma: Desarmado");
        }
        System.out.println("------------------------");
    }

    @Override
    public void setPontosDeVida(int pontosDeVida) {
        if (pontosDeVida > this.maxPontosDeVida) {
            this.pontosDeVida = this.maxPontosDeVida;
        } else {
            this.pontosDeVida = pontosDeVida;
        }
    }

    public int getNivel() { return nivel; }
    public double getSorte(){ return this.sorte; }
}