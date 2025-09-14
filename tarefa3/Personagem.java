// Essa é a classe abstrata Personagem que servirá como base para os seres vivos do jogo.
public abstract class Personagem {
    // Atributos:
    // Tornaremos protected para que herói e monstro possam acessar diretamente.
    protected String nome;
    protected int pontosDeVida;
    protected int forca;
    protected Arma arma; // Novo atributo da Tarefa 2 para a arma equipada

    // Construtor
    public Personagem(String nome, int pontosDeVida, int forca) {
        this.nome = nome;
        this.pontosDeVida = pontosDeVida;
        this.forca = forca;
        this.arma = null; // Todo personagem começa desarmado por padrão
    }

    // Esse é o método que exibe o status do Personagem.
    public void exibirStatus() {
        System.out.println("------------------------");
        System.out.println("~ " + nome);
        System.out.println(" HP: " + pontosDeVida);
        System.out.println(" Pontos de Força: " + forca);
        System.out.println("------------------------");
    }

    // Método que faz um personagem receber dano.
    public void receberDano(int dano) {
        int vidaAntes = this.pontosDeVida;
        this.pontosDeVida -= dano; 

        if(this.pontosDeVida < 0) {
            this.pontosDeVida = 0;
        }
        // Exibe o status atualizado do alvo do ataque após receber dano
        System.out.println("\t>> Dano sofrido! HP de " + this.nome + ": " + vidaAntes + " -> " + this.pontosDeVida);
    }

    // Aqui iremos forçar a implementação do método atacar em todas as classes filhas.
    public abstract void atacar(Personagem alvo);

    // Fazendo os getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontosDeVida() {
        return pontosDeVida;
    }

    public void setPontosDeVida(int pontosDeVida) {
        this.pontosDeVida = pontosDeVida;
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    // Getters e Setters para a nova arma
    public Arma getArma() {
        return arma;
    }

    public void setArma(Arma arma) {
        this.arma = arma;
    }
}