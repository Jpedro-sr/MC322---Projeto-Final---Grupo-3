// Classe concreta que representa o herói "Corsário Sedentário".

public class CorsarioSedentario extends Heroi {
    
    // Atributo específico do Corsário Sedentário.
    private int pontosDeEstudo; // Representa o quanto ele já analisou os pontos fracos do inimigo.

    // Construtor da classe CorsarioSedentario
    public CorsarioSedentario() {
        // Ele não é forte, mas precisa ser resistente para aguentar o combate enquanto descansa.
        super("Corsário Sedentário", 110, 2, 1, 0);

        // Começa a luta sem nenhum conhecimento, afinal, acabou de acordar.
        this.pontosDeEstudo = 0;
    }

    // Lógica do ataque do CorsarioSedentario:
    // Ele odeia atacar. Seu "ataque" é, na verdade, um turno de observação.
    // Ele causa um dano mínimo apenas para fingir que está participando.
    // Sua verdadeira força está em sua habilidade especial (Golpe do mínimo esforço), que depende de quantos pontos de estudo ele acumulou.
    @Override
    public void atacar(Personagem alvo){
        
        System.out.println("O " + nome + " se recusa a levantar. Do chão mesmo, ele observa " + alvo.getNome() + "...");

        // Causa um dano irrisório, só para dizer que fez alguma coisa.
        int danoMinimo = this.forca;
        System.out.println("\t> Ele atira uma concha que estava ao seu alcance. Um esforço tremendo.");
        alvo.receberDano(danoMinimo);

        // A cada "ataque", ele acumula conhecimento sobre como terminar a luta mais rápido.
        this.pontosDeEstudo++;
        System.out.println("\t> (Pontos de Estudo acumulados: " + this.pontosDeEstudo + ")");
    }

    // A Habilidade Especial é o momento em que ele se irrita por ter seu descanso interrompido.
    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        
        // Ele só pode usar a habilidade se tiver prestado o mínimo de atenção.
        if (this.pontosDeEstudo > 0) {
            
            System.out.println("'Argh, já chega! Isso está atrapalhando minha soneca!', resmunga o " + nome + ".");
            
            // O dano da habilidade é baseado em quão bem ele estudou o alvo para não errar.
            int bonusDeDano = this.pontosDeEstudo * 15;
            int danoTotal = this.forca + bonusDeDano;

            System.out.println("\t*** Com um movimento rápido e surpreendentemente preciso, ele executa o GOLPE DO MÍNIMO ESFORÇO! ***");
            System.out.println("\t> Explorando um ponto fraco, ele causa " + danoTotal + " de dano preciso!");
            alvo.receberDano(danoTotal);

            // Após o golpe, ele pode voltar a relaxar. O conhecimento já foi usado.
            System.out.println("\t> (Pontos de Estudo zerados)");
            this.pontosDeEstudo = 0;

        } else {
            // Mensagem cômica caso ele tente usar a habilidade sem ter feito o esforço de observar.
            System.out.println("O " + nome + " tenta lembrar de um ponto fraco, mas a preguiça é grande demais. Nada acontece.");
        }
    }
}