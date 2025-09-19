import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

        // 1. Construção do Cenário
        GeradorDeFases gerador = new ConstrutorDeCenarioFixo();
        ArrayList<Fase> fases = gerador.gerar(3);

        // 2. Criação do Herói
        Heroi heroi = new CapitaoCabecudo();

        // 3. Início do Jogo
        System.out.println("A AVENTURA COMEÇA!");
        boolean heroiVenceuTudo = true;

        for (Fase fase : fases) {
            fase.iniciar(heroi);

            if (!fase.isConcluida()) {
                heroiVenceuTudo = false;
                break; // Encerra a campanha.
            }
        }

        // 4. Conclusão
        System.out.println("\n+--------------------------------------------------------+");
        System.out.println("|                      FIM DE JOGO                     |");
        System.out.println("+--------------------------------------------------------+");

        if (heroiVenceuTudo) {
            System.out.println("\nVOCÊ VENCEU! O tesouro da Ilha Perdida é seu!");
        } else {
            System.out.println("\nGAME OVER! A ilha cobrou seu preço.");
        }
    }
}