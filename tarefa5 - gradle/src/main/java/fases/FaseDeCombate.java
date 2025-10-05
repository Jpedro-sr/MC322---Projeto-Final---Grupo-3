package fases;
import java.util.ArrayList;

import combate.AcaoDeCombate;
import config.Dificuldade;
import exceptions.LootIndisponivelException;
import itens.armas.Arma;
import personagens.Lootavel;
import personagens.heroi.Heroi;
import personagens.monstros.Monstro;

public class FaseDeCombate implements Fase {
    private Arma lootPendente;
    private Dificuldade dificuldade;
    private TipoCenario ambiente;
    private ArrayList<Monstro> monstros;
    private boolean concluida;

    public FaseDeCombate(TipoCenario ambiente, ArrayList<Monstro> monstros, Dificuldade dificuldade) {
        this.dificuldade = dificuldade;
        this.ambiente = ambiente;
        this.monstros = monstros;
        this.concluida = false;
    }

    /**
     * Inicia a fase de combate entre o herói e os monstros.
     * Aplica os efeitos do cenário e processa os combates sequencialmente.
     * A força dos monstros é ajustada pela dificuldade escolhida.
     * 
     * @param heroi o herói que participará dos combates
     */
    @Override
    public void iniciar(Heroi heroi) {
        this.ambiente.aplicarEfeitos(heroi);

        for (Monstro monstroAtual : this.monstros) {
            int novaForca = (int)(monstroAtual.getForca() * dificuldade.getMultiplicadorForcaMonstro());
            monstroAtual.setForca(novaForca);

            System.out.println("\nUm(a) " + monstroAtual.getNome() + " selvagem surge das sombras!");

            // ADICIONADO: Exibir status no início do combate
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

            while (heroi.estaVivo() && monstroAtual.estaVivo()) {
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~ Ação do Herói ~~~~~~~~~~~~~~~~~~~~~");
                AcaoDeCombate acaoHeroi = heroi.escolherAcao(monstroAtual);
                if (acaoHeroi != null) acaoHeroi.executar(heroi, monstroAtual);

                if (!monstroAtual.estaVivo()) break;

                System.out.println("\n~~~~~~~~~~~~~~~~~~~ Ação do Monstro ~~~~~~~~~~~~~~~~~~~");
                AcaoDeCombate acaoMonstro = monstroAtual.escolherAcao(heroi);
                if (acaoMonstro != null) acaoMonstro.executar(monstroAtual, heroi);
            }

            if (!heroi.estaVivo()) {
                // ADICIONADO: Status final em caso de derrota
                System.out.println("\n" + "=".repeat(50));
                System.out.println("           DERROTA! STATUS FINAL");
                System.out.println("=".repeat(50));

                System.out.println("\n  HERÓI (DERROTADO):");
                heroi.exibirStatus();

                System.out.println("\n  INIMIGO (VENCEDOR):");
                monstroAtual.exibirStatus();

                this.concluida = false;
                return;
            }

            // VITÓRIA DO HERÓI
            System.out.println("\n*** VITÓRIA! O(A) " + monstroAtual.getNome() + " foi derrotado(a)! ***");

            // ADICIONADO: Status final após vitória
            System.out.println("\n" + "=".repeat(50));
            System.out.println("          VITÓRIA! STATUS FINAL");
            System.out.println("=".repeat(50));

            System.out.println("\n  HERÓI (VENCEDOR):");
            heroi.exibirStatus();

            System.out.println("\n  INIMIGO (DERROTADO):");
            monstroAtual.exibirStatus();

            System.out.println("=".repeat(50));

            // Adiciona XP ao herói
            heroi.ganharExperiencia(monstroAtual.getXpConcedido() * (int)dificuldade.getMultiplicadorXp());

            // Sistema de loot
            Arma itemDropado = null;
            if (monstroAtual instanceof Lootavel) {
                itemDropado = ((Lootavel) monstroAtual).droparLoot();
            }

            if (itemDropado == null) {
                System.out.println("[x] O monstro não largou nenhum item.");
            } else {
                System.out.println("[!] O monstro largou um item: " + itemDropado.getNome());
                // guarda para o menu pós-turno decidir o que fazer
                this.lootPendente = itemDropado;

                // Se quiser já dar uma dica ao jogador:
                System.out.println("    (Você poderá interagir com esse loot no menu pós-turno.)");
            }
        }
        this.concluida = true;
    }

    @Override
    public boolean isConcluida() {
        return this.concluida;
    }

    @Override
    public TipoCenario getTipoDeCenario() {
        return this.ambiente;
    }

    public Arma coletarLoot() throws LootIndisponivelException {
    if (this.lootPendente == null) {
        throw new LootIndisponivelException("Não há loot pendente nesta fase.");
    }
    Arma drop = this.lootPendente;
    this.lootPendente = null;
    return drop;
}

}