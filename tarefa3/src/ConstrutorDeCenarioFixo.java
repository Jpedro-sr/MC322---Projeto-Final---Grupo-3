import java.util.ArrayList;
public class ConstrutorDeCenarioFixo implements GeradorDeFases {

    @Override
    public ArrayList<Fase> gerar(int quantidadeDeFases) {
        ArrayList<Fase> fases = new ArrayList<>(); // quantidadeDeFases é ignorado

        // Fase 1: Praia Assombrada — Sereia
        ArrayList<Monstro> monstrosFase1 = new ArrayList<>();
        monstrosFase1.add(new SereiaEncantadora());
        fases.add(new FaseDeCombate(TipoCenario.PRAIA_ASSOMBRADA, monstrosFase1));

        // Fase 2: Gruta Submersa — Homem-Peixe
        ArrayList<Monstro> monstrosFase2 = new ArrayList<>();
        monstrosFase2.add(new HomemPeixe());
        fases.add(new FaseDeCombate(TipoCenario.GRUTA_SUBMERSA, monstrosFase2));

        // Fase 3: Covil do Kraken — Kraken
        ArrayList<Monstro> monstrosFase3 = new ArrayList<>();
        monstrosFase3.add(new Kraken());
        fases.add(new FaseDeCombate(TipoCenario.COVIL_DO_KRAKEN, monstrosFase3));

        return fases;
    }
}
