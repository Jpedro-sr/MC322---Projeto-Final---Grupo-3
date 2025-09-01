public class Kraken extends Monstro {

    // Atributo específico do Kraken: Afogamento relâmpago
    private int danoDoAfogamentoRelampago;
    private boolean heroiAgarrado;

    // Construtor da classe Kraken
    public Kraken() {
        super("Kraken", 120, 8, 100);

        this.danoDoAfogamentoRelampago = 50;
        this.heroiAgarrado = false;
    }

    // O Kraken ataca com uma lógica condicional
    // Caso o Kraken tenha conseguido agarrar o alvo, ele usa o ataque especial de afogamento relâmpago
    // Caso contrário, ele usa um ataque normal com seus tentáculos
    // Iremos sortear um número para decidir se ele conseguiu agarrar o alvo ou não
    // Usaremos um booleano para representar se ele conseguiu agarrar ou não.
    @Override
    public void atacar(Personagem alvo) {

        if (this.heroiAgarrado){

            // --- ATAQUE ESPECIAL: AFOGAMENTO RELÂMPAGO ---
            System.out.println("O " + this.nome + " te puxa para as profundezas da água, te afogando!");
            int danoTotal = this.danoDoAfogamentoRelampago;
            System.out.println("O afogamento relâmpago causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(danoTotal);

            System.out.println("Após receber o dano, o " + alvo.nome + "consegue se libertar do Kraken!");
            this.heroiAgarrado = false;
        } else {

            // --- ATAQUE NORMAL ---
            System.out.println("O " + this.nome + " ataca com seus tentáculos!");
            int danoTotal = this.forca;
            System.out.println("O golpe causa " + danoTotal + " pontos de dano.");
            alvo.receberDano(danoTotal);

            // Sorteia se o Kraken conseguiu agarrar o alvo ou não (50% de chance)
            this.heroiAgarrado = Math.random() < 0.2;
            if (this.heroiAgarrado) {
                System.out.println("O " + this.nome + " conseguiu agarrar " + alvo.getNome() + "!");
            } else {
                System.out.println("O " + this.nome + " não conseguiu agarrar " + alvo.getNome() + ".");
            }
        }
    }
}