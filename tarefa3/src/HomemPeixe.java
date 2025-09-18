public class HomemPeixe extends Monstro {

    // Atributo específico do Homem-Peixe: Jato de Amônia
    private int danoDoJatoDeAmonia;
    private int contadorDeRaiva;
    private static final int ATAQUES_PARA_ENFURECER = 3;

    // Construtor da classe HomemPeixe
    public HomemPeixe() {
        super("Homem-Peixe", 85, 8, 40);
        this.danoDoJatoDeAmonia = 25;
        this.contadorDeRaiva = 0;
        listaDeArmasParaLargar.add(new MosqueteEnferrujado());
    }

    // Implementação do método de ataque físico do Homem-Peixe, que acumula raiva a cada ataque.
    @Override
    public void atacarFisico(Personagem alvo) {
        if (this.contadorDeRaiva == ATAQUES_PARA_ENFURECER) {
            System.out.println("\t*** A raiva do " + this.nome + " chega ao auge! Ele libera um poderoso JATO DE AMÔNIA! ***");
            int danoTotal = this.danoDoJatoDeAmonia;
            System.out.println("\t> O jato tóxico causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
            alvo.receberDano(danoTotal);
            System.out.println("\t> ... a fúria do Homem-Peixe se dissipa.");
            this.contadorDeRaiva = 0;
        } else {
            System.out.println("\t> O " + this.nome + " ataca com um golpe de tridente!");
            int danoTotal = this.forca;
            System.out.println("\t> O golpe causa " + danoTotal + " pontos de dano.");
            alvo.receberDano(danoTotal);
            this.contadorDeRaiva++;
            System.out.println("\t> ... sua raiva aumenta! (" + this.contadorDeRaiva + "/" + ATAQUES_PARA_ENFURECER + ")");
        }
    }

    //O ataque a distância é mais forte, mas não faz o contador de raiva aumentar
    @Override
    public void atacarDistancia(Personagem alvo) {
        System.out.println("\t> O " + this.nome + " arremessa uma bola de algas!");
        int danoTotal = this.forca * 2;
        alvo.receberDano(danoTotal);
        System.out.println("\t> Bem na cabeça! A bola de algas causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
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