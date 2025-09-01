public class HomemPeixe extends Monstro {

    // Atributo específico do Homem-Peixe: Jato de Amônia
    private int danoDoJatoDeAmonia;

    // Novo atributo para controlar o ciclo de ataque com um contador (bonus)
    private int contadorDeRaiva;
    private static final int ataqueFurioso = 3; // Uma constante para deixar o código mais claro

    // Construtor da classe HomemPeixe
    public HomemPeixe() {
        super("Homem-Peixe", 80, 5, 40);

        this.danoDoJatoDeAmonia = 30;
        this.contadorDeRaiva = 0; // Ele sempre começa com o contador em 0
    }

    // Implementação do método abstrato atacar com a nova lógica de contador de raiva
    @Override
    public void atacar(Personagem alvo) {

        // Verifica se o contador atingiu o limite para o ataque furioso
        if (this.contadorDeRaiva == ataqueFurioso) {

            // --- ATAQUE FURIOSO ---
            System.out.println("A raiva do " + this.nome + " chega ao auge! Ele libera um poderoso JATO DE AMÔNIA!");
            
            int danoTotal = this.danoDoJatoDeAmonia;
            
            System.out.println("O jato tóxico causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(danoTotal);

            // Após usar o ataque forte, ele se acalma e o contador é resetado.
            System.out.println("... a fúria do Homem-Peixe se dissipa.");
            this.contadorDeRaiva = 0;

        } else {

            // --- ATAQUE NORMAL (ACUMULANDO RAIVA) ---
            System.out.println("O " + this.nome + " ataca com um golpe de tridente!");
            
            int danoTotal = this.forca; // Dano baseado apenas na força base

            System.out.println("O golpe causa " + danoTotal + " pontos de dano.");
            alvo.receberDano(danoTotal);
            
            // Após o ataque normal, sua raiva aumenta.
            this.contadorDeRaiva++;
            System.out.println("... sua raiva aumenta! (" + this.contadorDeRaiva + "/" + ataqueFurioso + ")");
        }
    }
}