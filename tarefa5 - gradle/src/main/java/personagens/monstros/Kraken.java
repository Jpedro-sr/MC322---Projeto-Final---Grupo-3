package personagens.monstros;

import combate.AcaoDeCombate;
import combate.Combatente;
import combate.GolpeDeTentaculoAcao;
import itens.armas.PistolaDoKraken;

public class Kraken extends Monstro {

    // Atributo específico do Kraken: Afogamento relâmpago
    private int danoDoAfogamentoRelampago;
    private boolean heroiAgarrado;

    // Construtor da classe Kraken
    public Kraken() {
        super("Kraken", 130, 10, 100);
        this.danoDoAfogamentoRelampago = 30;
        this.heroiAgarrado = false;

        this.arma = new PistolaDoKraken("Pistola do Kraken", 22, 3);

        // OPCIONAL: Mantém na lista para casos especiais
        this.listaDeArmasParaLargar.add(new PistolaDoKraken("Pistola do Kraken", 22, 3));

        // Define as ações que o Kraken pode fazer.
        this.acoes.add(new GolpeDeTentaculoAcao(this)); // Ação no índice 0
        this.acoes.add(new AfogamentoAcao(this));       // Ação no índice 1
    }

    // A IA do Kraken: sua decisão de ataque é baseada no estado 'heroiAgarrado'.
    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        // Se o herói já está agarrado, a única ação possível é o afogamento.
        if (this.heroiAgarrado) {
            return acoes.get(1); // Retorna AfogamentoAcao
        } else {
            // Caso contrário, ele tentará o ataque normal com chance de agarrar.
            return acoes.get(0); // Retorna GolpeDeTentaculoAcao
        }
    }

    // Métodos para que as Ações possam interagir com o estado do Kraken.
    public int getDanoDoAfogamento() {
        return this.danoDoAfogamentoRelampago;
    }

    public void setHeroiAgarrado(boolean status) {
        this.heroiAgarrado = status;
    }

    public boolean isHeroiAgarrado() {
        return this.heroiAgarrado;
    }
}