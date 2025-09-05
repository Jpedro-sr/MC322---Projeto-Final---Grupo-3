import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

        // (1) SETUP INICIAL DO JOGO
        // 1.1) Usando o ConstrutorDeCenario para gerar 3 fases com dificuldade crescente.
        ArrayList<Fase> fases = ConstrutorDeCenario.gerarFases(3);

        // 1.2) Criando uma instância do Herói.
        // Para testar o outro herói, basta trocar a linha abaixo por: Heroi heroi = new CorsarioSedentario();
        Heroi heroi = new CapitaoCabecudo();

        // 1.3) Apresentação inicial do Jogo
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~               A LENDA DA ILHA PERDIDA                ~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("O " + heroi.getNome() + " retorna à ilha, agora mais experiente, em busca da verdade por trás do tesouro amaldiçoado...");
        System.out.println();

        // Variável para controlar o estado do jogo
        boolean heroiFoiDerrotado = false;

        // (2) LOOP PRINCIPAL DAS FASES
        // Este loop percorre cada fase que foi gerada.
        for (Fase faseAtual : fases) {
            
            System.out.println("\n+--------------------------------------------------------+");
            System.out.println("|        INICIANDO FASE " + faseAtual.getNivel() + ": " + faseAtual.getAmbiente().toUpperCase() + "       |");
            System.out.println("+--------------------------------------------------------+");
            System.out.println("Seu herói avalia seus equipamentos antes de prosseguir...");
            heroi.exibirStatus();

            // (3) LOOP DOS MONSTROS DA FASE
            // Este loop percorre cada monstro dentro da fase atual.
            for (Monstro monstroAtual : faseAtual.getMonstros()) {
                System.out.println("\nUm(a) " + monstroAtual.getNome() + " (Nível " + faseAtual.getNivel() + ") surge das sombras!");
                monstroAtual.exibirStatus();

                // (4) LOOP DE COMBATE
                // A batalha continua enquanto ambos estiverem vivos.
                while (heroi.getPontosDeVida() > 0 && monstroAtual.getPontosDeVida() > 0) {
                    
                    System.out.println("\n~~~~~~~~~~~~~~~~~~~~~ Ação do Herói ~~~~~~~~~~~~~~~~~~~~~");
                    heroi.atacar(monstroAtual);

                    // Verifica se o monstro foi derrotado
                    if (monstroAtual.getPontosDeVida() <= 0) {
                        System.out.println("\n*** VITÓRIA! O(A) " + monstroAtual.getNome() + " foi derrotado(a)! ***");
                        heroi.ganharExperiencia(monstroAtual.getXpConcedido());

                        // Lógica de Drop de Arma baseada na Sorte
                        if (Math.random() < heroi.sorte) {
                            Arma armaDropada = monstroAtual.largaArma();
                            if (armaDropada != null) {
                                System.out.println("[!] Sorte de pirata! O monstro largou um item: " + armaDropada.getClass().getSimpleName() + " (Dano: +" + armaDropada.getDano() + ")");
                                
                                // Lógica para Equipar a nova arma
                                Arma armaAtual = heroi.getArma();
                                if (armaAtual == null || armaDropada.getDano() > armaAtual.getDano()) {
                                    heroi.equiparArma(armaDropada);
                                } else {
                                    System.out.println("[!] A arma encontrada não é melhor que a atual.");
                                }
                            }
                        }
                        break; // Sai do loop de combate
                    }

                    System.out.println("\n~~~~~~~~~~~~~~~~~~~ Ação do Monstro ~~~~~~~~~~~~~~~~~~~");
                    monstroAtual.atacar(heroi);

                    if (heroi.getPontosDeVida() <= 0) {
                        heroiFoiDerrotado = true;
                        break; 
                    }
                } // Fim do loop de combate

                if (heroiFoiDerrotado) {
                    break; // Sai do loop de monstros se o herói foi derrotado
                }
            } // Fim do loop de monstros

            if (heroiFoiDerrotado) {
                break; // Sai do loop de fases se o herói foi derrotado
            }

            System.out.println("\n--- FASE " + faseAtual.getNivel() + " CONCLUÍDA! ---");

        } // Fim do loop de fases

        // (5) CONCLUSÃO DO JOGO
        System.out.println("\n+--------------------------------------------------------+");
        System.out.println("|                      FIM DE JOGO                     |");
        System.out.println("+--------------------------------------------------------+");
        
        if (!heroiFoiDerrotado) {
            // Mensagem de vitória 
            System.out.println("\nCom todos os guardiões derrotados, você finalmente purifica o tesouro de sua maldição.");
            System.out.println("A lenda era real! Você se torna o protetor da ilha e o pirata mais respeitado dos sete mares!");
            System.out.println("\n                  *** VOCÊ VENCEU! ***");
        } else {
            // Mensagem de GAME OVER
            System.out.println("\nSeu corpo tomba nas rochas frias da ilha.");
            System.out.println("A Ilha Perdida cobra seu preço, e você se torna apenas mais uma história de fantasma contada nas tavernas...");
            System.out.println("\n                  --- GAME OVER ---");
        }
    }
}