package personagens.monstros;

import combate.AcaoDeCombate;
import combate.Combatente;
import combate.GolpeDeCaudaAcao;
import itens.armas.Cutelo;

public class SereiaEncantadora extends Monstro {
    private int cantoDivino;

    public SereiaEncantadora() {
        super("Sereia Encantadora", 75, 7, 25);
        this.cantoDivino = 10;
        
        // Equipa arma diretamente
        this.arma = new Cutelo();
        
        // REFATORADO: Guarda CLASSES, não instâncias (AGREGAÇÃO)
        this.classesDeArmasParaLargar.add(Cutelo.class);
        
        // Ações
        this.acoes.add(new GolpeDeCaudaAcao(this));
        this.acoes.add(new CantoDivinoAcao(this));
    }

    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        System.out.println("A " + this.nome + " inicia sua melodia mortal...");
        if (Math.random() < 0.3) {
            return acoes.get(1); // CantoDivinoAcao
        } else {
            return acoes.get(0); // GolpeDeCaudaAcao
        }
    }

    public int getCantoDivino() { return this.cantoDivino; }
}