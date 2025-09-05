import java.util.ArrayList;
import java.util.Random;

public class ConstrutorDeCenario {

    public static ArrayList<Fase> gerarFases(int nFases) {
        // Lista que vamos preencher e retornar no final
        ArrayList<Fase> fases = new ArrayList<>();
        Random random = new Random();

        // Uma lista de possíveis ambientes para as fases.
        String[] catalogoAmbientes = {
            "Praia Assombrada",
            "Gruta Submersa",
            "Recife de Corais Afiados",
            "Cemitério de Navios",
            "Covil do Kraken"
        };

        // Loop para criar cada fase, de 1 até nFases.
        for (int i = 1; i <= nFases; i++) {
            int nivelDaFase = i;
            
            // Sorteia um ambiente para a fase atual.
            String ambienteDaFase = catalogoAmbientes[random.nextInt(catalogoAmbientes.length)];
            
            // Prepara a lista de monstros que estarão na fase.
            ArrayList<Monstro> monstrosDaFase = new ArrayList<>();
            // A quantidade de monstros será igual ao nível da fase.
            int numeroDeMonstros = nivelDaFase;

            // Loop para criar cada monstro da fase.
            for (int j = 0; j < numeroDeMonstros; j++) {
                
                // Sorteia um número de 0 a 2 para decidir qual monstro criar.
                int tipoMonstro = random.nextInt(3);
                Monstro monstroDaFase = null;

                // Um switch-case para criar o monstro certo baseado no número sorteado.
                switch (tipoMonstro) {
                    case 0:
                        monstroDaFase = new SereiaEncantadora();
                        break;
                    case 1:
                        monstroDaFase = new HomemPeixe();
                        break;
                    case 2:
                        monstroDaFase = new Kraken();
                        break;
                }

                // --- AQUI AUMENTAMOS A DIFICULDADE DO MONSTRO ---
                // Pegamos a vida e força base do monstro e multiplicamos pelo nível da fase.
                int novaVida = monstroDaFase.getPontosDeVida() * nivelDaFase;
                int novaForca = monstroDaFase.getForca() * nivelDaFase;
                
                // Atualizamos o monstro com os novos atributos mais fortes.
                monstroDaFase.setPontosDeVida(novaVida);
                monstroDaFase.setForca(novaForca);

                // Adicionamos o monstro fortalecido na lista da fase.
                monstrosDaFase.add(monstroDaFase);
            }

            // Com tudo pronto, criamos o objeto Fase e adicionamos na lista principal.
            Fase novaFase = new Fase(nivelDaFase, ambienteDaFase, monstrosDaFase);
            fases.add(novaFase);
        }

        return fases;
    }
}