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

    // Dividir o método atacar em dois métodos: atacarFisico e atacarDistancia
    // O ataque físico usa um d5 e o ataque à distância usa um d3, mas no ataque físico o Capitão pode tropeçar e se machucar.
    @Override
    public void atacarFisico(Combatente alvo) {
        // Para variar a narração, sorteamos uma frase de ataque
        String[] frasesDeAtaque = {
            "O temido " + nome + " saca sua cimitarra enferrujada!",
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
            System.out.println("\t> Ele tentará um ataque físico normal!");
            int multiplicador = (int) (Math.random() * 5);
            int danoBase  = this.forca * multiplicador;
            
            // Se o dano base for 0, o Capitão tropeça e erra o ataque
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
        // O Capitão pode tropeçar e se machucar ao atacar fisicamente
        int danoTropeco = (int)(pontosDeVida * 0.05); // Capitão perde 5% de sua vida ao tropeçar
        if (Math.random() < 1 - this.sorte) { // Chance de tropeçar é inversamente proporcional à sorte
        pontosDeVida -= danoTropeco;
        System.out.println("\t> O capitão tropeça e se machuca, perdendo " + danoTropeco + " pontos de vida no processo.");
        }
    }
    
    @Override
    // Método de ataque à distância do Capitão Cabeçudo
    public void atacarDistancia(Combatente alvo) {
        
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
            System.out.println("\t> Ele tentará um ataque físico normal!");
            int multiplicador = (int) (Math.random() * 3);
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
    public void usarHabilidadeEspecial(Combatente alvo) {
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
    // Método para verificar se o Capitão Cabeçudo está vivo
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