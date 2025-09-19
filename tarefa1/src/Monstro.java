public abstract class Monstro extends Personagem{

    // Atributo adicional de Monstro:
    protected int xpConcedido;

    // Construtor da classe Monstro
    public Monstro(String nome, int pontosDeVida, int forca, int xpConcedido) {
        super(nome, pontosDeVida, forca);
        this.xpConcedido = xpConcedido;
    }

    @Override
    public void exibirStatus() {
        System.out.println("------------------------");
        System.out.println("~ " + nome);
        System.out.println(" HP: " + pontosDeVida);
        System.out.println(" Pontos de Força: " + forca);
        System.out.println(" XP Concedido ao ser derrotado: " + xpConcedido);
        System.out.println("------------------------");
    }

    // Métodos getters e setters para o atributo adicional xpConcedido.
    public int getXpConcedido() {
        return xpConcedido;
    }

    public void setXpConcedido(int xpConcedido) {
        this.xpConcedido = xpConcedido;
    }
}