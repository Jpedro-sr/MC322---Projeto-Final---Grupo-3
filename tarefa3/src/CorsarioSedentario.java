// Classe concreta que representa o herói "Corsário Sedentário".
public class CorsarioSedentario extends Heroi {

    // Atributo específico do Corsário Sedentário.
    private int pontosDeEstudo; // Representa o quanto ele já analisou os pontos fracos do inimigo.

    // Construtor da classe CorsarioSedentario
    public CorsarioSedentario() {
        // Ele não é forte, mas precisa ser resistente para aguentar o combate enquanto descansa.
        // BALANCEAMENTO: HP aumentado para 120
        super("Corsário Sedentário", 120, 2, 1, 0);

        // Começa a luta sem nenhum conhecimento, afinal, acabou de acordar.
        this.pontosDeEstudo = 0;
        this.sorte = 0.3; // Ele tem 30% de sorte de ter uma ideia brilhante.
    }

    // Lógica do ataque do CorsarioSedentario:
    // Este método simula um turno de "estudo" e preparação.
    @Override
    public void atacar(Personagem alvo) {
        
        System.out.println("O " + nome + " se recusa a levantar. Do chão mesmo, ele observa " + alvo.getNome() + "...");

        // ETAPA 1: ATAQUE MÍNIMO E DISTRATIVO
        int danoMinimo = this.forca + (this.arma != null ? this.arma.getDano() : 0);
        System.out.println("\t> Ele atira uma concha que estava ao seu alcance. Um esforço tremendo.");
        alvo.receberDano(danoMinimo);

        // ETAPA 2: ACUMULANDO CONHECIMENTO (PONTOS DE ESTUDO)
        if (Math.random() < this.sorte) {
            System.out.println("\t> Por um golpe de sorte, ele nota um grande ponto fraco!");
            this.pontosDeEstudo += 2; // Ganha 2 pontos de estudo
        } else {
            this.pontosDeEstudo++; // Ganha 1 ponto de estudo
        }
        System.out.println("\t> (Pontos de Estudo acumulados: " + this.pontosDeEstudo + ")");
    }

    // A Habilidade Especial é o momento em que ele usa todo o conhecimento acumulado.
    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        
        // ETAPA 1: VERIFICAÇÃO DE RECURSO
        if (this.pontosDeEstudo > 0) {
            
            System.out.println("'Argh, já chega! Isso está atrapalhando minha soneca!', resmunga o " + nome + ".");
            
            // ETAPA 2: CÁLCULO DO DANO MASSIVO
            int bonusDeDano = this.pontosDeEstudo * 15;
            int danoArma = (this.arma != null ? this.arma.getDano() : 0);
            int danoTotal = this.forca + bonusDeDano + danoArma;

            System.out.println("\t*** Com um movimento rápido e surpreendentemente preciso, ele executa o GOLPE DO MÍNIMO ESFORÇO! ***");
            if(this.arma != null){
                System.out.println("\t> Sua arma, " + this.arma.getClass().getSimpleName() + ", adiciona +" + danoArma + " de dano!");
            }
            System.out.println("\t> Explorando um ponto fraco, ele causa " + danoTotal + " de dano preciso!");
            alvo.receberDano(danoTotal);

            // ETAPA 3: RESET DO CONTADOR
            System.out.println("\t> (Pontos de Estudo zerados)");
            this.pontosDeEstudo = 0;

        } else {
            // Mensagem cômica caso ele tente usar a habilidade sem ter feito o esforço de observar.
            System.out.println("O " + nome + " tenta lembrar de um ponto fraco, mas a preguiça é grande demais. Nada acontece.");
        }
    }

    @Override
    public boolean estaVivo() {
        return pontosDeVida > 0;
    }

    @Override
    public void receberCura(int cura) {
        pontosDeVida += cura;
        if (pontosDeVida > maxPontosDeVida) {
            pontosDeVida = maxPontosDeVida;
        }
    }
}