package app;

import config.Dificuldade;
import exceptions.LootIndisponivelException;
import exceptions.NivelInsuficienteException;
import fases.Fase;
import fases.FaseDeCombate;
import fases.GeradorDeFases;
import itens.armas.Arma;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import personagens.heroi.Heroi;
import util.InputManager;

import java.util.ArrayList;

/**
 * Classe que coordena uma batalha completa no RPG.
 * Responsável por gerenciar o herói, as fases e o progresso do jogo.
 * Esta classe implementa COMPOSIÇÃO: o herói só existe dentro de uma Batalha.
 * (Conforme Tarefa 6)
 */
@XmlRootElement(name = "batalha")
public class Batalha {
    
    @XmlElement
    private Heroi heroi;
    
    @XmlElement
    private ArrayList<Fase> fases;
    
    @XmlElement
    private Dificuldade dificuldade;
    
    @XmlElement
    private int faseAtual;
    
    @XmlElement
    private boolean concluida;
    
    // Construtor padrão necessário para JAXB
    public Batalha() {
        this.faseAtual = 0;
        this.concluida = false;
    }
    
    /**
     * Construtor para criar uma nova batalha.
     * @param heroi O herói que participará da batalha
     * @param fases Lista de fases a serem enfrentadas
     * @param dificuldade Nível de dificuldade escolhido
     */
    public Batalha(Heroi heroi, ArrayList<Fase> fases, Dificuldade dificuldade) {
        this.heroi = heroi;
        this.fases = fases;
        this.dificuldade = dificuldade;
        this.faseAtual = 0;
        this.concluida = false;
    }
    
    /**
     * Executa a próxima fase incompleta da batalha.
     * Após a fase, exibe o menu pós-combate para o jogador decidir ações.
     * @return true se o jogo deve continuar, false se o herói desistiu ou morreu
     */
    public boolean executarProxFase() {
        if (faseAtual >= fases.size()) {
            concluida = true;
            return false; // Todas as fases foram completadas
        }
        
        Fase fase = fases.get(faseAtual);
        
        System.out.println("\n+--------------------------------------------------------+");
        System.out.println("|                    INÍCIO DA FASE " + (faseAtual + 1) + "                     |");
        System.out.println("+--------------------------------------------------------+");
        
        // Executa a fase
        fase.iniciar(heroi);
        
        // Verifica se o herói morreu
        if (!heroi.estaVivo() || !fase.isConcluida()) {
            concluida = false;
            return false;
        }
        
        // Menu pós-combate
        boolean continuar = menuPosTurno((FaseDeCombate) fase);
        
        if (continuar) {
            faseAtual++; // Avança para próxima fase
        }
        
        return continuar;
    }
    
    /**
     * Menu exibido após cada fase para o jogador decidir ações.
     */
    private boolean menuPosTurno(FaseDeCombate fase) {
        while (true) {
            System.out.println("\n--- PÓS-TURNO ------------------------------------");
            System.out.println("1) Interagir com o loot");
            System.out.println("2) Ver status do herói");
            System.out.println("3) Salvar jogo");
            System.out.println("4) Continuar");
            System.out.println("5) Desistir");

            int op = InputManager.lerInteiro("Escolha", 1, 5);

            if (op == 1) {
                try {
                    Arma loot = fase.coletarLoot();
                    System.out.println("Arma: " + loot.getNome() + " (+" + loot.getDano() + " de dano)");
                    if (InputManager.lerSimNao("Deseja equipar agora?")) {
                        try {
                            heroi.equiparArma(loot);
                        } catch (NivelInsuficienteException e) {
                            System.out.println("[!] " + e.getMessage());
                        }
                    }
                } catch (LootIndisponivelException e) {
                    System.out.println("[x] " + e.getMessage());
                }

            } else if (op == 2) {
                heroi.exibirStatus();

            } else if (op == 3) {
                String nomeSave = "save_" + System.currentTimeMillis();
                if (GerenciadorDePersistencia.salvarBatalha(this, nomeSave)) {
                    System.out.println("[✓] Jogo salvo com sucesso!");
                } else {
                    System.out.println("[x] Erro ao salvar o jogo.");
                }

            } else if (op == 4) {
                return true; // Continua para próxima fase

            } else { // 5
                boolean confirmar = InputManager.lerSimNao("Confirmar desistência?");
                return !confirmar;
            }
        }
    }
    
    /**
     * Verifica se a batalha foi completada com sucesso.
     */
    public boolean isConcluida() {
        return concluida && faseAtual >= fases.size();
    }
    
    /**
     * Verifica se o herói ainda está vivo.
     */
    public boolean heroiEstaVivo() {
        return heroi != null && heroi.estaVivo();
    }
    
    // Getters necessários para serialização
    public Heroi getHeroi() { return heroi; }
    public ArrayList<Fase> getFases() { return fases; }
    public Dificuldade getDificuldade() { return dificuldade; }
    public int getFaseAtual() { return faseAtual; }
}