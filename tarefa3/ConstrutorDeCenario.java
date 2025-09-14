import java.util.ArrayList;
import java.util.Random;

public class ConstrutorDeCenario {

    public static ArrayList<Fase> gerarFases(int nFases) {
        // Lista que vamos preencher e retornar no final
        ArrayList<Fase> fases = new ArrayList<>();
        Random random = new Random();

        // Uma lista de possíveis ambientes para as fases.
        TipoCenario[] catalogoAmbientes = {
            TipoCenario.PRAIA_ASSOMBRADA,
            TipoCenario.GRUTA_SUBMERSA,
            TipoCenario.RECIFE_DE_CORAIS_AFIADOS,
            TipoCenario.CEMITERIO_DE_NAVIOS,
            TipoCenario.COVIL_DO_KRAKEN
        };

        // Loop para criar cada fase, de 1 até nFases.
        for (int i = 1; i <= nFases; i++) {
            int nivelDaFase = i;
            TipoCenario ambienteDaFase = catalogoAmbientes[random.nextInt(catalogoAmbientes.length)];
            ArrayList<Monstro> monstrosDaFase = new ArrayList<>();
            int numeroDeMonstros = nivelDaFase;

            for (int j = 0; j < numeroDeMonstros; j++) {
                int tipoMonstro = random.nextInt(3);
                Monstro monstroDaFase = null;

                switch (tipoMonstro) {
                    case 0: monstroDaFase = new SereiaEncantadora(); break;
                    case 1: monstroDaFase = new HomemPeixe(); break;
                    case 2: monstroDaFase = new Kraken(); break;
                }

                // --- AQUI AUMENTAMOS A DIFICULDADE DO MONSTRO (BALANCEADO) ---
                double fatorDeBonus = 0.20 * (nivelDaFase - 1);
                int vidaAdicional = (int) (monstroDaFase.getPontosDeVida() * fatorDeBonus);
                int forcaAdicional = (int) (monstroDaFase.getForca() * fatorDeBonus);
                
                monstroDaFase.setPontosDeVida(monstroDaFase.getPontosDeVida() + vidaAdicional);
                monstroDaFase.setForca(monstroDaFase.getForca() + forcaAdicional);
                monstrosDaFase.add(monstroDaFase);
            }

            Fase novaFase = new Fase(nivelDaFase, ambienteDaFase.getDescricao(), monstrosDaFase);
            fases.add(novaFase);
        }
        return fases;
    }
}