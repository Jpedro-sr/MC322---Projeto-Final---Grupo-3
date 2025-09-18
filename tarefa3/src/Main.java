import java.util.ArrayList;

// A Main agora é muito mais simples e apenas orquestra o jogo.
public class Main {
    public static void main(String[] args){

        // 1. Construção do Cenário
        GeradorDeFases gerador = new ConstrutorDeCenarioFixo();
        ArrayList<Fase> fases = gerador.gerar(3);

        // 2. Criação do Herói
        Heroi heroi = new CapitaoCabecudo(); // Você pode trocar para new CorsarioSedentario() para testar

        // 3. Início do Jogo
        System.out.println("A AVENTURA COMEÇA!");
        boolean heroiVenceuTudo = true;

        for (Fase fase : fases) {
            fase.iniciar(heroi);

            // Se a fase não foi concluída, significa que o herói foi derrotado.
            if (!fase.isConcluida()) {
                heroiVenceuTudo = false;
                break; // Encerra a campanha.
            }
             System.out.println("\n--- FASE CONCLUÍDA! ---");
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