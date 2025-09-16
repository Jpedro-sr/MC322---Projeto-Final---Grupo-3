public class Kraken extends Monstro {

    // Atributo específico do Kraken: Afogamento relâmpago
    private int danoDoAfogamentoRelampago;
    private boolean heroiAgarrado;

    // Construtor da classe Kraken
    public Kraken() {
        super("Kraken", 130, 10, 100);
        this.danoDoAfogamentoRelampago = 30;
        this.heroiAgarrado = false;
        listaDeArmasParaLargar.add(new PistolaDoKraken());
    }

    // O Kraken ataca com uma lógica condicional
    @Override
    public void atacar(Personagem alvo) {
        if (this.heroiAgarrado){
            System.out.println("\t*** O " + this.nome + " te puxa para as profundezas da água, te afogando! ***");
            int danoTotal = this.danoDoAfogamentoRelampago;
            System.out.println("\t> O afogamento causa " + danoTotal + " de dano massivo em " + alvo.getNome() + "!");
            alvo.receberDano(danoTotal);
            System.out.println("\t> Após receber o dano, o " + alvo.getNome() + " consegue se libertar do Kraken!");
            this.heroiAgarrado = false;
        } else {
            System.out.println("\t> O " + this.nome + " ataca com seus tentáculos chicoteantes!");
            int danoTotal = this.forca;
            System.out.println("\t> O golpe causa " + danoTotal + " de dano.");
            alvo.receberDano(danoTotal);

            // Sorteia se o Kraken conseguiu agarrar o alvo ou não (20% de chance)
            boolean agarrou = Math.random() < 0.2;
            if (agarrou) {
                System.out.println("\t> (Sucesso) Um dos tentáculos se enrola em você e te agarra firmemente!");
                this.heroiAgarrado = true;
            } else {
                System.out.println("\t> (Falha) Você consegue se esquivar dos tentáculos!");
            }
        }
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