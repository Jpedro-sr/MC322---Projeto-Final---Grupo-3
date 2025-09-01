// Nesse código criamos a classe abstrata Heroi que herda de Personagem. :)

public abstract class Heroi extends Personagem {
    
    // Atributos adicionais de Heroi:
    protected int nivel;
    protected int XP;

    // Construtor da classe Heroi
    public Heroi(String nome, int pontosDeVida, int forca, int nivel, int XP) {

        // Veja que aqui usamos o super para chamar apenas o construtor da classe Personagem
        super(nome, pontosDeVida, forca);

        // Inicializamos os atributos adicionais de Heroi
        this.nivel = nivel;
        this.XP = XP;
    }

    // Método que adiciona XP ao herói:
    public void ganharExperiencia(int XPrecebido) {
        this.XP = XP + XPrecebido;
        System.out.println("Seu heroi " + nome + "ganhou " + XPrecebido + "pontos de XP!" );
    }

    @Override
    public void exibirStatus() {
        System.out.println("------------------------");
        System.out.println("~ " + nome);
        System.out.println(" HP: " + pontosDeVida);
        System.out.println(" Pontos de Força: " + forca);
        System.out.println(" Nível: " + nivel);
        System.out.println(" XP: " + XP);
        System.out.println("------------------------");
    }

    // Métodos getters e setters para os atributos adicionais
    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getXP() {
        return XP;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }

    // Vamos obrigar a implementação do método abstrato usarHabilidadeEspecial
    // em todas as classes filhas de Heroi.
    public abstract void usarHabilidadeEspecial(Personagem alvo);
}