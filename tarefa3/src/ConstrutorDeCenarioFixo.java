import java.util.ArrayList;

public class ConstrutorDeCenarioFixo implements GeradorDeFases {

    @Override
    public ArrayList<Fase> gerar(int quantidadeDeFases) {
        ArrayList<Fase> fases = new ArrayList<>();
        
        // FASE 1
        ArrayList<Monstro> monstrosFase1 = new ArrayList<>();
        SereiaEncantadora sereia = new SereiaEncantadora();
        // REMOVIDO: Ações duplicadas - já são definidas no construtor
        monstrosFase1.add(sereia);
        fases.add(new FaseDeCombate(TipoCenario.PRAIA_ASSOMBRADA, monstrosFase1));

        // FASE 2
        ArrayList<Monstro> monstrosFase2 = new ArrayList<>();
        HomemPeixe homemPeixe = new HomemPeixe();
        // REMOVIDO: Ações duplicadas - já são definidas no construtor
        monstrosFase2.add(homemPeixe);
        fases.add(new FaseDeCombate(TipoCenario.GRUTA_SUBMERSA, monstrosFase2));

        // FASE 3
        ArrayList<Monstro> monstrosFase3 = new ArrayList<>();
        Kraken kraken = new Kraken();
        // REMOVIDO: Ações duplicadas - já são definidas no construtor
        monstrosFase3.add(kraken);
        fases.add(new FaseDeCombate(TipoCenario.COVIL_DO_KRAKEN, monstrosFase3));

        return fases;
    }
}