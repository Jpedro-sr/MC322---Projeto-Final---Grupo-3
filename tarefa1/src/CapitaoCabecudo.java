public class CapitaoCabecudo extends Heroi {
    
    // Atributo específico do Capitão Cabeçudo: Coragem Líquida (Rum)
    private int coragemLiquida; // Quantidade de Rum que o Capitão bebeu (quanto mais melhor!)   :)

    // Construtor da classe CapitaoCabecudo
    public CapitaoCabecudo() {
        // Chamamos o construtor da superclasse Heroi com os valores ESPECÍFICOS E BALANCEADOS do Capitão Cabeçudo
        super("Capitão Cabeçudo", 120, 6, 1, 0);
        this.coragemLiquida = 4;
    }

    // Lógica do ataque do CapitaoCabecudo:
    // 1) Jogamos um d6 e, caso caia em 6, usaremos a habilidade especial (ataque critico)
    // 2) Caso contrário, o ataque normal será a força do personagem * multiplicador
    // O multiplicador é um valor de 0 a 3 sorteado aleatoriamente por outro dado lançado.
    // Só faremos o ataque criítico se o d6 cair em 6.
    @Override
    public void atacar(Personagem alvo){
        
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

        // Rolamos um dado que vai de 1 a 6
        int d6 = (int) (Math.random() * 6) + 1;
        System.out.println("\t> Ele lança um d6 e tira: " + d6 + "!");

        if (d6 == 6){
            System.out.println("\t*** Sorte de pirata! Ele usará sua habilidade especial: Tiro Caolho! ***");
            // Aqui devemos fazer a lógica do ataque critico
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
                System.out.println("\t> Ele atira seu mosquete causando " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
                alvo.receberDano(danoTotal);
            }
        }
    }

    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        // Mensagem lúdica de uso da habilidade especial
        System.out.println("\t> O capitão mira com seu único olho e dispara um tiro que acerta em cheio!");
        // Cálculo do dano da habilidade especial
        int danoTotal = this.forca * 6;
        // Mensagem de ataque crítico
        System.out.println("\t> Dano CRÍTICO de " + danoTotal + " pontos!");
        // De fato, aplicamos o dano ao alvo
        alvo.receberDano(danoTotal);
    }
}