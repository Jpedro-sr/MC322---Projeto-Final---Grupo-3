import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

        // (1) SETUP INICIAL DO JOGO
        ArrayList<FaseDeCombate> fases = ConstrutorDeCenarioFixo.gerarFases(3);
        Heroi heroi = new CapitaoCabecudo();

        // (2) APRESENTAÇÃO DO DESAFIO
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~                A LENDA DA ILHA PERDIDA                 ~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("O " + heroi.getNome() + " retorna à ilha, agora mais experiente, em busca da verdade por trás do tesouro amaldiçoado...");
        System.out.println();
        
        boolean heroiFoiDerrotado = false;

        // (3) LOOP PRINCIPAL DAS FASES
        for (FaseDeCombate faseAtual : fases) {
            
            System.out.println("\n+--------------------------------------------------------+");
            System.out.println("|        INICIANDO FASE " + faseAtual.getNivel() + ": " + faseAtual.getAmbiente().toUpperCase() + "           |");
            System.out.println("|        Monstros nesta fase: " + faseAtual.getMonstros().size() + "                          |");
            System.out.println("+--------------------------------------------------------+");
            heroi.exibirStatus();

            // (3.1) LOOP DOS MONSTROS DA FASE
            for (Monstro monstroAtual : faseAtual.getMonstros()) {
                System.out.println("\nUm(a) " + monstroAtual.getNome() + " (Nível " + faseAtual.getNivel() + ") surge das sombras!");
                monstroAtual.exibirStatus();

                // (3.2) LOOP DE COMBATE
                while (heroi.getPontosDeVida() > 0 && monstroAtual.getPontosDeVida() > 0) {
                    
                    System.out.println("\n~~~~~~~~~~~~~~~~~~~~~ Ação do Herói ~~~~~~~~~~~~~~~~~~~~~");
                    heroi.atacar(monstroAtual);

                    if (monstroAtual.getPontosDeVida() <= 0) {
                        System.out.println("\n*** VITÓRIA! O(A) " + monstroAtual.getNome() + " foi derrotado(a)! ***");
                        
                        int nivelAntes = heroi.getNivel();
                        heroi.ganharExperiencia(monstroAtual.getXpConcedido());

                        if (heroi.getNivel() == nivelAntes) {
                            int vidaRecuperada = 40;
                            heroi.setPontosDeVida(heroi.getPontosDeVida() + vidaRecuperada);
                            System.out.println("[+] O herói encontra uma garrafa de rum e recupera " + vidaRecuperada + " de HP!");
                        }
                        
                        if (Math.random() < heroi.getSorte()) {
                            Arma armaDropada = monstroAtual.largaArma();
                            if (armaDropada != null) {
                                System.out.println();
                                System.out.println("\t--- TESOURO ENCONTRADO! ---");
                                System.out.println("\t> Graças à sua sorte, o " + monstroAtual.getNome() + " largou uma arma!");
                                System.out.println("\t> Você encontrou: " + armaDropada.getClass().getSimpleName() + " (Dano: +" + armaDropada.getDano() + ")");
                                
                                Arma armaAtual = heroi.getArma();
                                if (armaAtual == null || armaDropada.getDano() > armaAtual.getDano()) {
                                    heroi.equiparArma(armaDropada);
                                } else {
                                    System.out.println("\t> [!] A arma encontrada não é melhor que a atual.");
                                }
                            }
                        }
                        System.out.println();
                        break;
                    }

                    System.out.println("\n~~~~~~~~~~~~~~~~~~~ Ação do Monstro ~~~~~~~~~~~~~~~~~~~");
                    monstroAtual.atacar(heroi);

                    if (heroi.getPontosDeVida() <= 0) {
                        heroiFoiDerrotado = true;
                        break; 
                    }
                }

                if (heroiFoiDerrotado) break;
            }

            if (heroiFoiDerrotado) break;
            System.out.println("\n--- FASE " + faseAtual.getNivel() + " CONCLUÍDA! ---");
        }

        // (4) CONCLUSÃO DO JOGO
        System.out.println("\n+--------------------------------------------------------+");
        System.out.println("|                      FIM DE JOGO                     |");
        System.out.println("+--------------------------------------------------------+");
        
        if (!heroiFoiDerrotado) {
            System.out.println("\nCom todos os guardiões derrotados, você finalmente purifica o tesouro de sua maldição.");
            System.out.println("A lenda era real! Você se torna o protetor da ilha e o pirata mais respeitado dos sete mares!");
            System.out.println("\n                  *** VOCÊ VENCEU! ***");
        } else {
            System.out.println("\nSeu corpo tomba nas rochas frias da ilha.");
            System.out.println("A Ilha Perdida cobra seu preço, e você se torna apenas mais uma história de fantasma contada nas tavernas...");
            System.out.println("\n                  --- GAME OVER ---");
        }
    }
}