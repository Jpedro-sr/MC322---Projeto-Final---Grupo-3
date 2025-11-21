package ifome.util;

import java.util.Scanner;
import java.nio.charset.StandardCharsets;

/**
 * Centraliza todas as entradas do usuário com validações.
 * VERSÃO MELHORADA com suporte UTF-8 para Windows.
 */
public class InputManager {

    private static Scanner scanner;
    private static boolean isWindows;

    static {
        // Detecta sistema operacional
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        
        // Configura scanner com UTF-8
        try {
            scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        } catch (Exception e) {
            scanner = new Scanner(System.in);
        }
        
        // Tenta configurar encoding do console no Windows
        if (isWindows) {
            try {
                // Configura UTF-8 no Windows
                new ProcessBuilder("cmd", "/c", "chcp", "65001").inheritIO().start().waitFor();
            } catch (Exception e) {
                // Ignora erro
            }
        }
    }

    /**
     * Solicita um inteiro ao usuário com validação.
     */
    public static Integer lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt + " (ENTER para cancelar): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null; // cancelamento
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[X] Entrada invalida. Digite um numero inteiro ou deixe vazio para cancelar.");
            }
        }
    }

    public static Integer lerInteiroPositivo(String prompt) {
        while (true) {
            Integer valor = lerInteiro(prompt);

            if (valor == null) {
                return null; // cancelamento
            }

            if (valor > 0) {
                return valor;
            }

            System.out.println("[X] Digite um numero maior que zero.");
        }
    }

    public static Double lerDouble(String prompt) {
        while (true) {
            String input = lerTexto(prompt + " (ENTER para cancelar): ").trim();

            if (input.isEmpty()) {
                return null;
            }

            try {
                input = input.replace(",", ".");
                double valor = Double.parseDouble(input);

                if (valor < 0) {
                    System.out.println("[X] Valor nao pode ser negativo.");
                    continue;
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("[X] Valor invalido. Digite um numero decimal ou deixe vazio para cancelar.");
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
            String input = lerTexto(prompt + " (ENTER para cancelar): ");

            if (input.isEmpty()) {
                return null; // cancelamento
            }

            return input;
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
            } else if (input.equals("N") || input.equals("NAO") || input.equals("NÃO")) {
                return false;
            }
            System.out.println("[X] Digite S para Sim ou N para Nao.");
        }
    }

    /**
     * Solicita um email com validação básica.
     */
    public static String lerEmail(String prompt) {
        while (true) {
            String email = lerTexto(prompt + " (ENTER para cancelar): ").trim();

            if (email.isEmpty()) {
                return null;
            }

            if (email.contains("@") && email.contains(".")) {
                return email;
            }

            System.out.println("[X] Email invalido. Tente novamente ou deixe vazio para cancelar.");
        }
    }

    /**
     * Solicita um telefone com validação básica.
     */
    public static String lerTelefone(String prompt) {
        while (true) {
            String telefone = lerTexto(prompt + " (ENTER para cancelar): ").trim();

            if (telefone.isEmpty()) {
                return null;
            }

            String limpo = telefone.replaceAll("[^0-9]", "");

            if (limpo.length() >= 10) {
                return telefone;
            }

            System.out.println("[X] Telefone invalido. Digite pelo menos 10 digitos ou deixe vazio para cancelar.");
        }
    }

    /**
     * Pausa a execução até o usuário pressionar Enter.
     */
    public static void pausar(String mensagem) {
        if (!mensagem.isEmpty()) {
            System.out.print("\n" + mensagem + " ");
        }
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Limpa a tela do console.
     */
    public static void limparTela() {
        try {
            if (isWindows) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Se não conseguir limpar, imprime linhas vazias
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Exibe uma linha separadora (compatível com Windows).
     */
    public static void linha() {
        if (isWindows) {
            // Usa caracteres ASCII simples no Windows
            System.out.println("==================================================");
        } else {
            // Usa caracteres Unicode bonitos em Linux/Mac
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        }
    }

    /**
     * Fecha o scanner (deve ser chamado antes de encerrar).
     */
    public static void fechar() {
        if (scanner != null) {
            scanner.close();
        }
    }

    /**
     * Retorna um símbolo compatível com o sistema.
     */
    public static String simbolo(String unicode, String ascii) {
        return isWindows ? ascii : unicode;
    }

    // Métodos de conveniência para símbolos comuns
    public static String ok() {
        return simbolo("✅", "[OK]");
    }

    public static String erro() {
        return simbolo("❌", "[X]");
    }

    public static String info() {
        return simbolo("ℹ️", "[i]");
    }

    public static String carrinho() {
        return simbolo("🛒", "[Carrinho]");
    }

    public static String restaurante() {
        return simbolo("🏪", "[Rest]");
    }

    public static String comida() {
        return simbolo("🍔", "[Comida]");
    }

    public static String bebida() {
        return simbolo("🥤", "[Bebida]");
    }

    public static String estrela() {
        return simbolo("⭐", "[*]");
    }

    public static String dinheiro() {
        return simbolo("💰", "[R$]");
    }
}