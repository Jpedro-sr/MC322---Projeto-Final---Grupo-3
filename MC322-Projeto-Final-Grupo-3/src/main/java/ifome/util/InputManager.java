package ifome.util;

import java.util.Scanner;

/**
 * Centraliza todas as entradas do usuário com validações básicas.
 */
public class InputManager {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Solicita um inteiro ao usuário com validação.
     */
    public static int lerInteiro(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("❌ Entrada inválida. Digite um número.");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    /**
     * Solicita um texto ao usuário.
     */
    public static String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Solicita um texto não-vazio.
     */
    public static String lerTextoObrigatorio(String prompt) {
        while (true) {
            String input = lerTexto(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❌ Este campo é obrigatório.");
        }
    }

    /**
     * Solicita uma confirmação (S/N).
     */
    public static boolean lerConfirmacao(String prompt) {
        while (true) {
            String input = lerTexto(prompt + " (S/N): ").toUpperCase();
            if (input.equals("S") || input.equals("SIM")) {
                return true;
            } else if (input.equals("N") || input.equals("NÃO")) {
                return false;
            }
            System.out.println("❌ Digite S para Sim ou N para Não.");
        }
    }

    /**
     * Solicita um email com validação básica.
     */
    public static String lerEmail(String prompt) {
        while (true) {
            String email = lerTexto(prompt);
            if (email.contains("@") && email.contains(".")) {
                return email;
            }
            System.out.println("❌ Email inválido. Digite um email válido (ex: usuario@email.com).");
        }
    }

    /**
     * Solicita um telefone com validação básica.
     */
    public static String lerTelefone(String prompt) {
        while (true) {
            String telefone = lerTexto(prompt);
            String limpo = telefone.replaceAll("[^0-9]", "");
            if (limpo.length() >= 10) {
                return telefone;
            }
            System.out.println("❌ Telefone inválido. Digite um telefone com pelo menos 10 dígitos.");
        }
    }

    /**
     * Pausa a execução até o usuário pressionar Enter.
     */
    public static void pausar(String mensagem) {
        System.out.print("\n" + mensagem + " Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Limpa a tela (funciona em Linux/Mac, parcialmente em Windows).
     */
    public static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Se não conseguir limpar, continua mesmo assim
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Exibe uma linha separadora.
     */
    public static void linha() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Fecha o scanner (deve ser chamado antes de encerrar).
     */
    public static void fechar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}