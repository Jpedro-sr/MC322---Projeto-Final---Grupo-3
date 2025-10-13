package personagens.monstros;

import combate.AcaoDeCombate;
import combate.Combatente;
import itens.armas.MosqueteEnferrujado;
import personagens.AtaqueTridenteAcao;

public class HomemPeixe extends Monstro {
    private int danoDoJatoDeAmonia;
    private int contadorDeRaiva;
    private static final int ATAQUES_PARA_ENFURECER = 3;

    public HomemPeixe() {
        super("Homem-Peixe", 50, 5, 40);
        this.danoDoJatoDeAmonia = 25;
        this.contadorDeRaiva = 0;
        
        // Equipa arma diretamente
        this.arma = new MosqueteEnferrujado();
        
        // REFATORADO: Guarda CLASSES, não instâncias (AGREGAÇÃO)
        this.classesDeArmasParaLargar.add(MosqueteEnferrujado.class);
        
        // Ações
        this.acoes.add(new AtaqueTridenteAcao(this));
        this.acoes.add(new JatoDeAmoniaAcao(this));
    }

    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        if (this.contadorDeRaiva >= ATAQUES_PARA_ENFURECER) {
            return acoes.get(1); // JatoDeAmoniaAcao
        } else {
            return acoes.get(0); // AtaqueTridenteAcao
        }
    }

    public int getDanoDoJatoDeAmonia() { return this.danoDoJatoDeAmonia; }
    public int getContadorDeRaiva() { return this.contadorDeRaiva; }
    public int getAtaquesParaEnfurecer() { return ATAQUES_PARA_ENFURECER; }
    public void incrementarRaiva() { this.contadorDeRaiva++; }
    public void zerarRaiva() { this.contadorDeRaiva = 0; }
}