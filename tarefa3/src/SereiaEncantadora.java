public class SereiaEncantadora extends Monstro {

    // Atributo específico da Sereia Encantadora: Canto Divino
    private int cantoDivino;

    // Construtor da classe SereiaEncantadora
    public SereiaEncantadora() {
        super("Sereia Encantadora", 75, 7, 25);
        this.cantoDivino = 10;
        listaDeArmasParaLargar.add(new Cutelo());
    }

    // Implementação do método abstrato atacar na classe concreta SereiaEncantadora
    @Override
    public void atacar(Personagem alvo) {
        System.out.println("A " + this.nome + " inicia sua melodia mortal...");
        int tipoAtaque = (int) (Math.random() * 10) + 1;

        if (tipoAtaque <= 3) {
            System.out.println("\t*** Ela entoa seu Canto Divino! ***");
            int dano = this.forca + this.cantoDivino;
            System.out.println("\t> A magia causa " + dano + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(dano);
        } else {
            System.out.println("\t> Ela ataca rapidamente com um Golpe de Cauda!");
            int dano = this.forca;
            System.out.println("\t> O golpe causa " + dano + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(dano);
        }
    }
}