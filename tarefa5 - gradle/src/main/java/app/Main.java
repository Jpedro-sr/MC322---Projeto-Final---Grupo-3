package app;
import config.Dificuldade;
import exceptions.LootIndisponivelException;
import exceptions.NivelInsuficienteException;
import fases.ConstrutorDeCenarioFixo;
import fases.Fase;
import fases.FaseDeCombate;
import fases.GeradorDeFases;
import itens.armas.Arma;
import java.util.ArrayList;
import personagens.heroi.CapitaoCabecudo;
import personagens.heroi.Heroi;
import util.InputManager;

public class Main {

    /* =========================== MENUS AUXILIARES =========================== */

    private static void exibirMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1) Novo jogo");
        System.out.println("2) Ver informações");
        System.out.println("3) Sair");
    }

    private static void exibirInfo() {
        System.out.println("\n=== INFORMAÇÕES ===");
        System.out.println("- Herói: Capitão Cabeçudo (versátil; golpes físicos + armas)");
        System.out.println("- Monstros: Sereia Encantadora, Homem-Peixe, Kraken");
        System.out.println("- Dificuldade altera força dos monstros e XP ganho.");
        InputManager.esperarEnter("Pressione ENTER para voltar...");
    }

    private static Dificuldade escolherDificuldade() {
        System.out.println("\n=== DIFICULDADE ===");
        System.out.println("1) Fácil");
        System.out.println("2) Normal");
        System.out.println("3) Difícil");
        int op = InputManager.lerInteiro("Opção", 1, 3);
        return (op == 1) ? Dificuldade.FACIL
             : (op == 2) ? Dificuldade.NORMAL
                         : Dificuldade.DIFICIL;
    }

    /** Menu exibido entre as fases para o jogador decidir ações. */
    private static boolean menuPosTurno(Heroi heroi, FaseDeCombate fase) {
        while (true) {
            System.out.println("\n--- PÓS-TURNO ------------------------------------");
            System.out.println("1) Interagir com o loot");
            System.out.println("2) Ver status do herói");
            System.out.println("3) Continuar");
            System.out.println("4) Desistir");

            int op = InputManager.lerInteiro("Escolha", 1, 4);

            if (op == 1) {
                try {
                    // coleta e zera o loot pendente (lança LootIndisponivelException se não houver)
                    Arma loot = fase.coletarLoot();

                    if (loot instanceof Arma) {
                        Arma arma = (Arma) loot; // se seu Java < 16, use cast tradicional
                        System.out.println("Arma: " + arma.getNome() + " (+" + arma.getDano() + " de dano)");
                        if (InputManager.lerSimNao("Deseja equipar agora?")) {
                            try {
                                heroi.equiparArma(arma); // pode lançar NivelInsuficienteException
                            } catch (NivelInsuficienteException e) {
                                System.out.println("[!] " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("Você coletou: " + loot.getNome() + " (sem ação especial por enquanto)");
                    }

                } catch (LootIndisponivelException e) {
                    System.out.println("[x] " + e.getMessage());
                }

            } else if (op == 2) {
                heroi.exibirStatus();

            } else if (op == 3) {
                return true;  // segue campanha

            } else { // 4
                boolean confirmar = InputManager.lerSimNao("Confirmar desistência?");
                return !confirmar;
            }
        }
    }

    /* ============================== LOOP PRINCIPAL ========================== */

    public static void main(String[] args) {

        while (true) {
            exibirMenuPrincipal();
            int op = InputManager.lerInteiro("Opção", 1, 3);

            if (op == 1) {
                // 1) Dificuldade
                Dificuldade dif = escolherDificuldade();

                // 2) Gerar fases (se seu gerador ainda não aceita dificuldade, troque por: gerar(3);)
                GeradorDeFases gerador = new ConstrutorDeCenarioFixo();
                ArrayList<Fase> fases = gerador.gerar(3, dif);

                // 3) Herói
                Heroi heroi = new CapitaoCabecudo();

                // 4) Campanha
                System.out.println("\nA AVENTURA COMEÇA!");
                boolean heroiVenceuTudo = true;

                for (int i = 0; i < fases.size(); i++) {
                    Fase fase = fases.get(i);

                    System.out.println("\n+--------------------------------------------------------+");
                    System.out.println("|                    INÍCIO DA FASE " + (i + 1) + "                     |");
                    System.out.println("+--------------------------------------------------------+");

                    fase.iniciar(heroi);

                    if (heroi.getPontosDeVida() <= 0 || !fase.isConcluida()) {
                        heroiVenceuTudo = false;
                        break;
                    }

                    if (fase instanceof FaseDeCombate) {
                        FaseDeCombate fc = (FaseDeCombate) fase;
                        if (!menuPosTurno(heroi, fc)) {
                            heroiVenceuTudo = false; // desistiu
                            break;
                        }
                    }
                }

                System.out.println("\n+--------------------------------------------------------+");
                System.out.println("|                      FIM DE JOGO                       |");
                System.out.println("+--------------------------------------------------------+");

                if (heroiVenceuTudo) {
                    System.out.println("\nVOCÊ VENCEU! O tesouro da Ilha Perdida é seu!");
                } else {
                    System.out.println("\nGAME OVER! A ilha cobrou seu preço.");
                }
            }
            else if (op == 2) {
                exibirInfo();
            }
            else { // 3
                System.out.println("Saindo...");
                InputManager.fecharScanner(); // encerra o Scanner
                return;
            }
        }
    }
}
