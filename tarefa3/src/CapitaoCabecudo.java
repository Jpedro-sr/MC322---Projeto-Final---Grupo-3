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
    // 1) A chance de usar a habilidade especial (ataque crítico) agora é baseada na SORTE do herói.
    // 2) Caso contrário, o ataque normal será a força do personagem * multiplicador (d4).
    // 3) O dano da ARMA equipada é somado a todos os acertos.
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

        // Tarefa 2: Agora a chance de acerto crítico depende da sorte do herói
        if (Math.random() < this.sorte) {
            System.out.println("\t*** Sorte de pirata! Um golpe de mestre! ***");
            usarHabilidadeEspecial(alvo);

        } else {
            System.out.println("\t> Ele tentará um ataque normal!");
            int multiplicador = (int) (Math.random() * 4);
            int danoBase  = this.forca * multiplicador;
            
            if (danoBase == 0) {
                System.out.println("\t(Fracasso) O capitão tropeça numa garrafa de Rum e erra o ataque!!!");
            } else {
                System.out.println("\t> Pelas barbas de Netuno! O multiplicador de dano foi: " + multiplicador);
                
                int danoTotal = danoBase + this.coragemLiquida; 
                System.out.println("\t> Com a ajuda de sua coragem líquida (Rum), seu ataque ganha " + this.coragemLiquida + " pontos de dano extra.");

                // Adicionamos o dano da arma, se houver uma equipada.
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
        
        // Cálculo do dano da habilidade especial
        int danoHabilidade = this.forca * 6;
        int danoTotal = danoHabilidade;

        // Adicionamos o dano da arma, se houver uma equipada.
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