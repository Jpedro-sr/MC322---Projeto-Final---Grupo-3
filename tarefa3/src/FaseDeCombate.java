import java.util.ArrayList;

public class FaseDeCombate implements Fase {
    private TipoCenario ambiente;
    private ArrayList<Monstro> monstros;
    private boolean concluida;

    public FaseDeCombate(TipoCenario ambiente, ArrayList<Monstro> monstros) {
        this.ambiente = ambiente;
        this.monstros = monstros;
        this.concluida = false;
    }

    @Override
    public void iniciar(Heroi heroi) {
        this.ambiente.aplicarEfeitos(heroi);

        for (Monstro monstroAtual : this.monstros) {
            System.out.println("\nUm(a) " + monstroAtual.getNome() + " selvagem surge das sombras!");

            // ADICIONADO: Exibir status no início do combate
            System.out.println("\n" + "=".repeat(50));
            System.out.println("        STATUS INICIAL DO COMBATE");
            System.out.println("=".repeat(50));

            System.out.println("\n🗡️  HERÓI:");
            heroi.exibirStatus();

            System.out.println("\n👹  INIMIGO:");
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

                System.out.println("\n💀  HERÓI (DERROTADO):");
                heroi.exibirStatus();

                System.out.println("\n👹  INIMIGO (VENCEDOR):");
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

            System.out.println("\n🏆  HERÓI (VENCEDOR):");
            heroi.exibirStatus();

            System.out.println("\n💀  INIMIGO (DERROTADO):");
            monstroAtual.exibirStatus();

            System.out.println("=".repeat(50));

            // Adiciona XP ao herói
            heroi.ganharExperiencia(monstroAtual.getXpConcedido());

            // Sistema de loot
            Item itemDropado = monstroAtual.droparLoot();
            if (itemDropado instanceof Arma) {
                System.out.println("[!] O monstro largou um item: " + itemDropado.getNome());
                heroi.equiparArma((Arma) itemDropado);
            } else if (itemDropado == null) {
                System.out.println("[!] O monstro não largou nenhum item.");
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
}