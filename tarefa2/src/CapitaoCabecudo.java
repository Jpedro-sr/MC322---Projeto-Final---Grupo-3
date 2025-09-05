public class CapitaoCabecudo extends Heroi {
    
    // Atributo específico do Capitão Cabeçudo: Coragem Líquida (Rum)
    private int coragemLiquida; // Quantidade de Rum que o Capitão bebeu (quanto mais melhor!)   :)

    // Construtor da classe CapitaoCabecudo
    public CapitaoCabecudo() {
        // Chamamos o construtor da superclasse Heroi com os valores ESPECÍFICOS E BALANCEADOS do Capitão Cabeçudo
        super("Capitão Cabeçudo", 120, 6, 1, 0);
        this.coragemLiquida = 4;
        this.sorte = 0.25; // Sorte inicial do Capitão Cabeçudo é 25%
    }

    // Lógica do ataque do CapitaoCabecudo:
    @Override
    public void atacar(Personagem alvo) {
        
        // Para variar a narração, sorteamos uma frase de ataque
        String[] frasesDeAtaque = {
            "O temido " + nome + " saca seu mosquete enferrujado!",
            "Com um grito de 'Pela minha honra!', " + nome + " avança contra o inimigo!",
            nome + " aproveita uma brecha na defesa adversária e parte para o ataque!",
            "'Você vai virar comida de peixe!', brada " + nome + " ao atacar!",
            "Arrr! " + nome + " mostra por que é o terror dos sete mares!"
        };
        String fraseDoTurno = frasesDeAtaque[(int)(Math.random() * frasesDeAtaque.length)];
        System.out.println(fraseDoTurno);

        // --- LÓGICA DE COMBATE: ATAQUE BASEADO EM SORTE ---

        // ETAPA 1: O TESTE DE SORTE PARA O ACERTO CRÍTICO
        // Geramos um número aleatório entre 0.0 e 1.0. Se for menor que a 'sorte' do herói,
        // ele desfere um ataque crítico usando sua habilidade especial.
        if (Math.random() < this.sorte) {
            System.out.println("\t*** Sorte de pirata! Um golpe de mestre! ***");
            usarHabilidadeEspecial(alvo);

        } else {
            // ETAPA 2: O ATAQUE NORMAL (CASO NÃO TENHA SORTE)
            System.out.println("\t> Ele tentará um ataque normal!");
            
            // 2.1: Sorteamos um multiplicador de dano (de 0 a 3) para simular a variação de um golpe comum.
            int multiplicador = (int) (Math.random() * 4);
            int danoBase  = this.forca * multiplicador;
            
            // 2.2: Verificamos se o ataque errou (se o multiplicador sorteado foi 0).
            if (danoBase == 0) {
                System.out.println("\t(Fracasso) O capitão tropeça numa garrafa de Rum e erra o ataque!!!");
            } else {
                // 2.3: Se acertou, calculamos o dano total somando todos os bônus.
                System.out.println("\t> Pelas barbas de Netuno! O multiplicador de dano foi: " + multiplicador);
                
                // O dano total começa com o dano base (força * multiplicador) + o bônus da coragem líquida.
                int danoTotal = danoBase + this.coragemLiquida; 
                System.out.println("\t> Com a ajuda de sua coragem líquida (Rum), seu ataque ganha " + this.coragemLiquida + " pontos de dano extra.");

                // Verificamos se há uma arma equipada e somamos seu dano.
                if (this.arma != null) {
                    danoTotal += this.arma.getDano();
                    System.out.println("\t> Sua arma, " + this.arma.getClass().getSimpleName() + ", adiciona +" + this.arma.getDano() + " de dano!");
                }

                System.out.println("\t> Ele atira seu mosquete causando " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
                alvo.receberDano(danoTotal);
            }
        }
    }

    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        // Mensagem lúdica de uso da habilidade especial
        System.out.println("\t> O capitão usa sua habilidade: Tiro Caolho!");
        
        // --- LÓGICA DE DANO DA HABILIDADE ESPECIAL ---
        // 1. O dano base da habilidade é a força do herói multiplicada por 6 (dano crítico).
        int danoHabilidade = this.forca * 6;
        int danoTotal = danoHabilidade;

        // 2. Somamos o dano da arma equipada, se houver.
        if (this.arma != null) {
            danoTotal += this.arma.getDano();
            System.out.println("\t> Sua arma, " + this.arma.getClass().getSimpleName() + ", adiciona +" + this.arma.getDano() + " ao tiro!");
        }

        // Mensagem de ataque crítico
        System.out.println("\t> Dano CRÍTICO de " + danoTotal + " pontos!");
        
        // De fato, aplicamos o dano ao alvo
        alvo.receberDano(danoTotal);
    }
}