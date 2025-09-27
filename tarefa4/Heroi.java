import java.util.ArrayList;
import java.util.List;

public abstract class Heroi extends Personagem {
    protected int maxPontosDeVida;
    protected int nivel;
    protected int XP;
    protected int expProximoNivel;
    protected double sorte;
    protected List<AcaoDeCombate> acoes; // Lista de ações disponíveis para o herói
    protected int pontosDeEstudo; // ADICIONADO: Sistema de pontos de estudo

    public Heroi(String nome, int pontosDeVida, int forca, int nivel, int XP) {
        super(nome, pontosDeVida, forca);
        this.maxPontosDeVida = pontosDeVida;
        this.nivel = nivel;
        this.XP = XP;
        this.expProximoNivel = 80;
        this.sorte = 0.2;
        this.acoes = new ArrayList<>();
        this.pontosDeEstudo = 0; // ADICIONADO: Inicializa pontos de estudo
    }

    // Método privado para o herói subir de nível
    private void subirDeNivel() {
        this.nivel++;
        this.XP -= this.expProximoNivel;
        this.expProximoNivel *= 1.5;

        this.maxPontosDeVida += 25;
        this.forca += 5;
        this.sorte += 0.05;
        this.pontosDeVida = this.maxPontosDeVida;

        System.out.println("\t*** LEVEL UP! " + this.nome + " subiu para o nível " + this.nivel + "! ***");
        System.out.println("\t> Vida máxima, Força e Sorte aumentadas! Vida totalmente recuperada!");
    }

    // Método que adiciona XP ao herói e verifica se ele pode subir de nível
    public void ganharExperiencia(int XPrecebido) {
        this.XP += XPrecebido;
        System.out.println("[+] Seu heroi " + nome + " ganhou " + XPrecebido + " pontos de XP!" );

        while (this.XP >= this.expProximoNivel) {
            subirDeNivel();
        }
    }

    // Método para equipar uma nova arma
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

    // MÉTODOS ADICIONADOS: Sistema de pontos de estudo
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
        System.out.println(" Pontos de Estudo: " + pontosDeEstudo); // ADICIONADO

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

    // Getters
    public int getNivel() { return nivel; }
    public double getSorte(){ return this.sorte; }
}