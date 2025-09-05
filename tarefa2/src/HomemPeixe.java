public class HomemPeixe extends Monstro {

    // Atributo específico do Homem-Peixe: Jato de Amônia
    private int danoDoJatoDeAmonia;

    // Novo atributo para controlar o ciclo de ataque com um contador (bonus)
    private int contadorDeRaiva;
    private static final int ATAQUES_PARA_ENFURECER = 3; // Convenção de constante

    // Construtor da classe HomemPeixe
    public HomemPeixe() {
        super("Homem-Peixe", 85, 8, 40);
        this.danoDoJatoDeAmonia = 25;
        this.contadorDeRaiva = 0; // Ele sempre começa com o contador em 0

        // Define a arma que o Homem-Peixe pode dropar
        listaDeArmasParaLargar.add(new MosqueteEnferrujado());
    }

    // Implementação do método abstrato atacar com a nova lógica de contador de raiva
    @Override
    public void atacar(Personagem alvo) {

        // Verifica se o contador atingiu o limite para o ataque furioso
        if (this.contadorDeRaiva == ATAQUES_PARA_ENFURECER) {

            // --- ATAQUE FURIOSO ---
            System.out.println("\t*** A raiva do " + this.nome + " chega ao auge! Ele libera um poderoso JATO DE AMÔNIA! ***");
            int danoTotal = this.danoDoJatoDeAmonia;
            System.out.println("\t> O jato tóxico causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(danoTotal);

            // Após usar o ataque forte, ele se acalma e o contador é resetado.
            System.out.println("\t> ... a fúria do Homem-Peixe se dissipa.");
            this.contadorDeRaiva = 0;

        } else {

            // --- ATAQUE NORMAL (ACUMULANDO RAIVA) ---
            System.out.println("\t> O " + this.nome + " ataca com um golpe de tridente!");
            int danoTotal = this.forca; // Dano baseado apenas na força base
            System.out.println("\t> O golpe causa " + danoTotal + " pontos de dano.");
            alvo.receberDano(danoTotal);
            
            // Após o ataque normal, sua raiva aumenta.
            this.contadorDeRaiva++;
            System.out.println("\t> ... sua raiva aumenta! (" + this.contadorDeRaiva + "/" + ATAQUES_PARA_ENFURECER + ")");
        }
    }
}