package personagens.monstros;

import combate.AcaoDeCombate;
import combate.Combatente;
import combate.GolpeDeTentaculoAcao;
import itens.armas.PistolaDoKraken;

public class Kraken extends Monstro {
    private int danoDoAfogamentoRelampago;
    private boolean heroiAgarrado;

    public Kraken() {
        super("Kraken", 130, 10, 100);
        this.danoDoAfogamentoRelampago = 30;
        this.heroiAgarrado = false;
        
        // Equipa arma diretamente
        this.arma = new PistolaDoKraken();
        
        // REFATORADO: Guarda CLASSES, não instâncias (AGREGAÇÃO)
        this.classesDeArmasParaLargar.add(PistolaDoKraken.class);
        
        // Ações
        this.acoes.add(new GolpeDeTentaculoAcao(this));
        this.acoes.add(new AfogamentoAcao(this));
    }

    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        if (this.heroiAgarrado) {
            return acoes.get(1); // AfogamentoAcao
        } else {
            return acoes.get(0); // GolpeDeTentaculoAcao
        }
    }

    public int getDanoDoAfogamento() { return this.danoDoAfogamentoRelampago; }
    public void setHeroiAgarrado(boolean status) { this.heroiAgarrado = status; }
    public boolean isHeroiAgarrado() { return this.heroiAgarrado; }
}