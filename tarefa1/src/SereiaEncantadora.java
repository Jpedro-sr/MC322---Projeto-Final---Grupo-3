public class SereiaEncantadora extends Monstro {

    // Atributo específico da Sereia Encantadora: Canto Divino
    private int cantoDivino;

    // Construtor da classe SereiaEncantadora
    public SereiaEncantadora() {
        // Chamamos o construtor da superclasse Monstro com os valores específicos da Sereia Encantadora
        super("Sereia Encantadora", 80, 8, 25);

        this.cantoDivino = 15; // A Sereia terá 15 pontos de dano extra em seus ataques.
    }

    // Implementação do método abstrato atacar na classe concreta SereiaEncantadora
    @Override
    public void atacar(Personagem alvo) {

        // A sereia tem chance de usar seu ataque físico (Golpe de cauda -> mais fraco) ou ataque magico (canto divino -> mais forte).
        // Vamos sortear um valor entre 1 e 10
        // Se o valor estiver entre 1 e 3, a sereia usará o canto divino (ataque mágico)
        // Se o valor estiver entre 4 e 10, a sereia usará o golpe de cauda (ataque físico)
        int tipoAtaque = (int) (Math.random() * 10) + 1;

        if (tipoAtaque <= 3) {
            System.out.println("Ela entoa seu canto divino...");
            System.out.println("Causando " + (this.forca + this.cantoDivino) + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(this.forca + this.cantoDivino);
        } else {
            System.out.println("Ela ataca rapidamente com um golpe de cauda!");
            System.out.println("Causando " + this.forca + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(this.forca);
        }
    }

}