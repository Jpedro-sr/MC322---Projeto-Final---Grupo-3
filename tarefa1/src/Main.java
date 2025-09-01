public class Main {
    public static void main(String[] args){

        // (1) CRIAÇÃO DOS PERSONAGENS
        // 1.1) Criando o Heroi
        Heroi heroi = new CapitaoCabecudo();

        // 1.2) Criando um array de Monstros
        Monstro[] monstros = {
        new SereiaEncantadora(),
        new HomemPeixe(),
        new Kraken()
        };

        // (2) APRESENTAÇÃO DO DESAFIO
        // 2.1) Apresentação inicial
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~               A LENDA DA ILHA PERDIDA                ~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Após semanas no mar, seu navio ancora na costa da lendária Ilha Perdida.");
        System.out.println("O mapa em suas mãos mostra o caminho para o tesouro escondido,");
        System.out.println("mas presságios sombrios indicam que a ilha não está desabitada…");
        System.out.println();
        System.out.println("Seu herói, o destemido " + heroi.getNome() + ", prepara-se para o desafio!");

        // 2.2) Exibindo o status inicial do herói
        System.out.println("\n--- STATUS INICIAL DO HERÓI ---");
        heroi.exibirStatus();

        // (3) SIMULAÇÃO DOS TURNOS EM LOOP
        // Cria um laço de repetição para os 3 turnos/monstros 
        for (int i = 0; i < monstros.length; i++) {
            Monstro monstroAtual = monstros[i];

            System.out.println("\n+--------------------------------------------------------+");
            System.out.println("|                      TURNO " + (i + 1) + "                         |");
            System.out.println("+--------------------------------------------------------+");
            
            // Anuncia a chegada do monstro 
            System.out.println("\nUm(a) " + monstroAtual.getNome() + " selvagem emerge das sombras!");
            monstroAtual.exibirStatus();

            // Loop de batalha para o turno atual (Herói e Monstro se atacam até um morrer)
            while (heroi.getPontosDeVida() > 0 && monstroAtual.getPontosDeVida() > 0) {
                
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~ Ação do Herói ~~~~~~~~~~~~~~~~~~~~~");
                heroi.atacar(monstroAtual);

                // Verifica se o monstro foi derrotado
                if (monstroAtual.getPontosDeVida() <= 0) {
                    System.out.println("\n*** VITÓRIA! O(A) " + monstroAtual.getNome() + " foi derrotado(a)! ***");
                    heroi.ganharExperiencia(monstroAtual.getXpConcedido());

                    int vidaRecuperada = 35;
                    heroi.setPontosDeVida(heroi.getPontosDeVida() + vidaRecuperada);
                    System.out.println("[+] O herói encontra uma garrafa de rum e recupera " + vidaRecuperada + " de HP!");
                    System.out.println();
                    break;
                }

                System.out.println("\n~~~~~~~~~~~~~~~~~~~ Ação do Monstro ~~~~~~~~~~~~~~~~~~~");
                monstroAtual.atacar(heroi);

                if (heroi.getPontosDeVida() <= 0) {
                    break; 
                }
            }

            System.out.println("\n\n--- FIM DO TURNO " + (i + 1) + ": BALANÇO ---");
            System.out.println("Status do Herói:");
            heroi.exibirStatus();
            System.out.println("Status do Monstro:");
            monstroAtual.exibirStatus();

            if (heroi.getPontosDeVida() <= 0) {
                break;
            }
        }

        System.out.println("\n+--------------------------------------------------------+");
        System.out.println("|                      FIM DE JOGO                     |");
        System.out.println("+--------------------------------------------------------+");
        
        if (heroi.getPontosDeVida() > 0) {
            // Mensagem de vitória 
            System.out.println("\nCom o último guardião derrotado, o caminho está livre!");
            System.out.println("Você abre o baú e uma cascata de ouro e jóias amaldiçoadas reflete em seus olhos.");
            System.out.println("A lenda era real! Você é o pirata mais rico e temido dos sete mares!");
            System.out.println("\n                  *** VOCÊ VENCEU! ***");
        } else {
            // Mensagem de GAME OVER
            System.out.println("\nFerido e exausto, você cai de joelhos na areia.");
            System.out.println("A Ilha Perdida cobra seu preço, e você se torna apenas mais uma história de fantasma contada nas tavernas...");
            System.out.println("\n                  --- GAME OVER ---");
        }
    }
}