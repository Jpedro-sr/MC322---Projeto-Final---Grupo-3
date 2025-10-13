package fases;

import java.util.ArrayList;

import combate.AcaoDeCombate;
import config.Dificuldade;
import exceptions.LootIndisponivelException;
import itens.armas.Arma;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import personagens.Lootavel;
import personagens.heroi.Heroi;
import personagens.monstros.Monstro;

/**
 * Implementação concreta de uma fase de combate.
 * Coordena a batalha entre o herói e os monstros da fase.
 * Anotada com JAXB para permitir serialização.
 */
@XmlRootElement
public class FaseDeCombate implements Fase {
    
    @XmlTransient // Loot pendente não deve ser salvo
    private Arma lootPendente;
    
    @XmlElement
    private Dificuldade dificuldade;
    
    @XmlElement
    private TipoCenario ambiente;
    
    @XmlElement
    private ArrayList<Monstro> monstros;
    
    @XmlElement
    private boolean concluida;

    // Construtor padrão necessário para JAXB
    public FaseDeCombate() {
        this.concluida = false;
    }

    /**
     * Construtor para criar uma fase de combate.
     * @param ambiente Tipo de cenário da fase
     * @param monstros Lista de monstros a enfrentar
     * @param dificuldade Dificuldade que afeta os monstros
     */
    public FaseDeCombate(TipoCenario ambiente, ArrayList<Monstro> monstros, Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
        this.ambiente = ambiente;
        this.monstros = monstros;
        this.concluida = false;
    }

    /**
     * Inicia a fase de combate.
     * Aplica efeitos do cenário e processa os combates sequencialmente.
     */
    @Override
    public void iniciar(Heroi heroi) {
        // Aplica efeitos do cenário (eventos, etc)
        this.ambiente.aplicarEfeitos(heroi);

        // Processa cada monstro da fase
        for (Monstro monstroAtual : this.monstros) {
            // Ajusta força do monstro baseado na dificuldade
            int novaForca = (int)(monstroAtual.getForca() * dificuldade.getMultiplicadorForcaMonstro());
            monstroAtual.setForca(novaForca);

            System.out.println("\nUm(a) " + monstroAtual.getNome() + " selvagem surge das sombras!");

            // ===== EXIBE STATUS INICIAL DO COMBATE =====
            System.out.println("\n" + "=".repeat(50));
            System.out.println("        STATUS INICIAL DO COMBATE");
            System.out.println("=".repeat(50));
            
            System.out.println("\n  HERÓI:");
            heroi.exibirStatus();
            
            System.out.println("\n  INIMIGO:");
            monstroAtual.exibirStatus();
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           INICIANDO BATALHA!");
            System.out.println("=".repeat(50));

            // ===== LOOP DE COMBATE =====
            while (heroi.estaVivo() && monstroAtual.estaVivo()) {
                // Turno do herói
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~ Ação do Herói ~~~~~~~~~~~~~~~~~~~~~");
                AcaoDeCombate acaoHeroi = heroi.escolherAcao(monstroAtual);
                if (acaoHeroi != null) {
                    acaoHeroi.executar(heroi, monstroAtual);
                }

                // Verifica se monstro foi derrotado
                if (!monstroAtual.estaVivo()) {
                    break;
                }

                // Turno do monstro
                System.out.println("\n~~~~~~~~~~~~~~~~~~~ Ação do Monstro ~~~~~~~~~~~~~~~~~~~");
                AcaoDeCombate acaoMonstro = monstroAtual.escolherAcao(heroi);
                if (acaoMonstro != null) {
                    acaoMonstro.executar(monstroAtual, heroi);
                }
            }

            // ===== VERIFICA RESULTADO DO COMBATE =====
            if (!heroi.estaVivo()) {
                // DERROTA - Herói morreu
                exibirStatusFinalDerrota(heroi, monstroAtual);
                this.concluida = false;
                return;
            }

            // VITÓRIA - Monstro derrotado
            exibirStatusFinalVitoria(heroi, monstroAtual);
            
            // Processa XP e Loot
            processarRecompensas(heroi, monstroAtual);
        }
        
        // Todas as batalhas foram vencidas
        this.concluida = true;
    }

    /**
     * Exibe o status final quando o herói é derrotado.
     */
    private void exibirStatusFinalDerrota(Heroi heroi, Monstro monstro) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           DERROTA! STATUS FINAL");
        System.out.println("=".repeat(50));
        
        System.out.println("\n  HERÓI (DERROTADO):");
        heroi.exibirStatus();
        
        System.out.println("\n  INIMIGO (VENCEDOR):");
        monstro.exibirStatus();
    }

    /**
     * Exibe o status final quando o herói vence.
     */
    private void exibirStatusFinalVitoria(Heroi heroi, Monstro monstro) {
        System.out.println("\n*** VITÓRIA! O(A) " + monstro.getNome() + " foi derrotado(a)! ***");
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          VITÓRIA! STATUS FINAL");
        System.out.println("=".repeat(50));
        
        System.out.println("\n  HERÓI (VENCEDOR):");
        heroi.exibirStatus();
        
        System.out.println("\n  INIMIGO (DERROTADO):");
        monstro.exibirStatus();
        
        System.out.println("=".repeat(50));
    }

    /**
     * Processa as recompensas (XP e loot) após vitória.
     */
    private void processarRecompensas(Heroi heroi, Monstro monstro) {
        // Concede XP ajustado pela dificuldade
        int xpGanho = (int)(monstro.getXpConcedido() * dificuldade.getMultiplicadorXp());
        heroi.ganharExperiencia(xpGanho);

        // Sistema de loot
        Arma itemDropado = null;
        if (monstro instanceof Lootavel) {
            itemDropado = ((Lootavel) monstro).droparLoot();
        }

        if (itemDropado == null) {
            System.out.println("[x] O monstro não largou nenhum item.");
        } else {
            System.out.println("[!] O monstro largou um item: " + itemDropado.getNome());
            this.lootPendente = itemDropado;
            System.out.println("    (Você poderá interagir com esse loot no menu pós-turno.)");
        }
    }

    /**
     * Verifica se a fase foi concluída com sucesso.
     */
    @Override
    public boolean isConcluida() {
        return this.concluida;
    }

    /**
     * Retorna o tipo de cenário da fase.
     */
    @Override
    public TipoCenario getTipoDeCenario() {
        return this.ambiente;
    }

    /**
     * Permite coletar o loot pendente da fase.
     * Após coletar, o loot é consumido (não pode ser coletado novamente).
     * 
     * @return A arma dropada
     * @throws LootIndisponivelException se não houver loot pendente
     */
    public Arma coletarLoot() throws LootIndisponivelException {
        if (this.lootPendente == null) {
            throw new LootIndisponivelException("Não há loot pendente nesta fase.");
        }
        
        Arma drop = this.lootPendente;
        this.lootPendente = null; // Consome o loot
        return drop;
    }
}