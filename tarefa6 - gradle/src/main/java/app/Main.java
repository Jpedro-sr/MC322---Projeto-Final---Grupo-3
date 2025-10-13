package app;

import config.Dificuldade;
import fases.ConstrutorDeCenarioFixo;
import fases.Fase;
import fases.GeradorDeFases;
import personagens.heroi.CapitaoCabecudo;
import personagens.heroi.Heroi;
import util.InputManager;

import java.util.ArrayList;

/**
 * Classe principal do jogo, responsável apenas pela orquestração do menu
 * e pela criação/carregamento de batalhas.
 * A lógica da campanha foi movida para a classe Batalha (composição).
 * (Refatorado para Tarefa 6)
 */
public class Main {

    /* =========================== MENUS AUXILIARES =========================== */

    /**
     * Exibe o menu principal do jogo.
     * Agora inclui a opção "Carregar Jogo" se houver saves disponíveis.
     */
    private static void exibirMenuPrincipal(boolean existemSaves) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     TERRAS SOMBRIAS - RPG          ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println("[1] Iniciar Novo Jogo");
        if (existemSaves) {
            System.out.println("[2] Carregar Jogo");
            System.out.println("[3] Ver Informações");
            System.out.println("[4] Sair do Jogo");
        } else {
            System.out.println("[2] Ver Informações");
            System.out.println("[3] Sair do Jogo");
        }
        System.out.println("════════════════════════════════════");
    }

    /**
     * Exibe informações sobre o jogo.
     */
    private static void exibirInfo() {
        System.out.println("\n════════ INFORMAÇÕES DO JOGO ════════");
        System.out.println("• Herói: Capitão Cabeçudo");
        System.out.println("  - Pirata versátil com golpes físicos");
        System.out.println("  - Pode equipar diversas armas");
        System.out.println();
        System.out.println("• Monstros:");
        System.out.println("  - Sereia Encantadora (Canto mágico)");
        System.out.println("  - Homem-Peixe (Jato de amônia)");
        System.out.println("  - Kraken (Tentáculos mortais)");
        System.out.println();
        System.out.println("• Dificuldade:");
        System.out.println("  - Altera força dos monstros");
        System.out.println("  - Modifica XP ganho");
        System.out.println("════════════════════════════════════");
        InputManager.esperarEnter("\nPressione ENTER para voltar...");
    }

    /**
     * Menu para escolher a dificuldade do jogo.
     */
    private static Dificuldade escolherDificuldade() {
        System.out.println("\n════════ ESCOLHA A DIFICULDADE ════════");
        System.out.println("[1] Fácil   - Monstros mais fracos, mais XP");
        System.out.println("[2] Normal  - Balanceamento padrão");
        System.out.println("[3] Difícil - Monstros mais fortes, menos XP");
        System.out.println("═══════════════════════════════════════");
        
        int op = InputManager.lerInteiro("Opção", 1, 3);
        return (op == 1) ? Dificuldade.FACIL
             : (op == 2) ? Dificuldade.NORMAL
                         : Dificuldade.DIFICIL;
    }

    /**
     * Menu para selecionar qual save carregar.
     */
    private static String selecionarSave() {
        String[] saves = GerenciadorDePersistencia.listarSaves();
        
        if (saves.length == 0) {
            System.out.println("[!] Nenhum save encontrado.");
            return null;
        }
        
        System.out.println("\n════════ JOGOS SALVOS ════════");
        for (int i = 0; i < saves.length; i++) {
            System.out.println("[" + (i + 1) + "] " + saves[i]);
        }
        System.out.println("[0] Cancelar");
        System.out.println("══════════════════════════════");
        
        int opcao = InputManager.lerInteiro("Escolha um save", 0, saves.length);
        
        if (opcao == 0) {
            return null;
        }
        
        return saves[opcao - 1];
    }

    /**
     * Cria uma nova batalha do zero.
     */
    private static Batalha criarNovaBatalha() {
        // 1. Escolhe dificuldade
        Dificuldade dificuldade = escolherDificuldade();
        
        // 2. Gera fases
        GeradorDeFases gerador = new ConstrutorDeCenarioFixo();
        ArrayList<Fase> fases = gerador.gerar(3, dificuldade);
        
        // 3. Cria herói (COMPOSIÇÃO: o herói só existe dentro da Batalha)
        Heroi heroi = new CapitaoCabecudo();
        
        // 4. Cria batalha
        return new Batalha(heroi, fases, dificuldade);
    }

    /**
     * Executa o loop principal de uma batalha.
     */
    private static void executarBatalha(Batalha batalha) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      A AVENTURA COMEÇA!            ║");
        System.out.println("╚════════════════════════════════════╝");
        
        // Loop principal: executa fases até o fim ou desistência
        while (batalha.heroiEstaVivo() && !batalha.isConcluida()) {
            boolean continuar = batalha.executarProxFase();
            
            if (!continuar) {
                break; // Jogador desistiu ou herói morreu
            }
        }
        
        // Mensagem final
        exibirMensagemFinal(batalha);
    }

    /**
     * Exibe a mensagem final baseada no resultado da batalha.
     */
    private static void exibirMensagemFinal(Batalha batalha) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║          FIM DE JOGO               ║");
        System.out.println("╚════════════════════════════════════╝");
        
        if (batalha.isConcluida() && batalha.heroiEstaVivo()) {
            System.out.println("\n🎉 VITÓRIA! 🎉");
            System.out.println("Você conquistou o tesouro da Ilha Perdida!");
            System.out.println("Sua bravura será lembrada para sempre!");
        } else if (!batalha.heroiEstaVivo()) {
            System.out.println("\n💀 GAME OVER 💀");
            System.out.println("A ilha cobrou seu preço...");
            System.out.println("Seu herói caiu em combate.");
        } else {
            System.out.println("\n🏳️ DESISTÊNCIA 🏳️");
            System.out.println("Você decidiu abandonar a aventura.");
            System.out.println("Talvez em outra ocasião...");
        }
        
        InputManager.esperarEnter("\nPressione ENTER para voltar ao menu...");
    }

    /* ============================== LOOP PRINCIPAL ========================== */

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║   BEM-VINDO AO TERRAS SOMBRIAS     ║");
        System.out.println("║         RPG NARRATIVO              ║");
        System.out.println("╚════════════════════════════════════╝");
        
        while (true) {
            // Verifica se existem saves
            boolean existemSaves = GerenciadorDePersistencia.existemSaves();
            
            // Exibe menu principal
            exibirMenuPrincipal(existemSaves);
            
            // Ajusta limites de opção baseado em saves disponíveis
            int maxOpcao = existemSaves ? 4 : 3;
            int op = InputManager.lerInteiro("Opção", 1, maxOpcao);
            
            // Processa opção escolhida
            if (op == 1) {
                // ===== NOVO JOGO =====
                Batalha batalha = criarNovaBatalha();
                executarBatalha(batalha);
                
            } else if (op == 2 && existemSaves) {
                // ===== CARREGAR JOGO =====
                String nomeSave = selecionarSave();
                
                if (nomeSave != null) {
                    Batalha batalha = GerenciadorDePersistencia.carregarBatalha(nomeSave);
                    
                    if (batalha != null) {
                        System.out.println("[✓] Jogo carregado com sucesso!");
                        executarBatalha(batalha);
                    } else {
                        System.out.println("[x] Erro ao carregar o jogo.");
                        InputManager.esperarEnter("Pressione ENTER para continuar...");
                    }
                }
                
            } else if ((op == 2 && !existemSaves) || (op == 3 && existemSaves)) {
                // ===== INFORMAÇÕES =====
                exibirInfo();
                
            } else {
                // ===== SAIR =====
                System.out.println("\n╔════════════════════════════════════╗");
                System.out.println("║   Obrigado por jogar!              ║");
                System.out.println("║   Até a próxima aventura!          ║");
                System.out.println("╚════════════════════════════════════╝");
                InputManager.fecharScanner();
                return;
            }
        }
    }
}