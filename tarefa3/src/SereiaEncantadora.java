public class SereiaEncantadora extends Monstro {

    // Atributo específico da Sereia Encantadora: Canto Divino
    private int cantoDivino;

    // Construtor da classe SereiaEncantadora
    public SereiaEncantadora() {
        super("Sereia Encantadora", 75, 7, 25);
        this.cantoDivino = 10;
        listaDeArmasParaLargar.add(new Cutelo());
    }

    // O ataque a distância da Sereia é seu Canto Divino, que sempre aumenta seu poder
    @Override
    public void atacarDistancia(Personagem alvo) {
        System.out.println("A " + this.nome + " inicia sua melodia mortal...");
            System.out.println("\t*** Ela entoa seu Canto Divino! ***");
            int dano = this.forca + this.cantoDivino;
            System.out.println("\t> A magia causa " + dano + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(dano);
    }
    
    //O ataque físico é um golpe de cauda simples, que aumenta o canto divino
    @Override
    public void atacarFisico(Personagem alvo) {
            System.out.println("\t> Ela ataca rapidamente com um Golpe de Cauda!");
            int dano = this.forca;
            System.out.println("\t> O golpe causa " + dano + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(dano);
            this.cantoDivino += 2;
        }

    @Override
    public boolean estaVivo() {
        return pontosDeVida > 0;
    }

    @Override
    public void receberCura(int cura) {
        pontosDeVida += cura;
    }
}