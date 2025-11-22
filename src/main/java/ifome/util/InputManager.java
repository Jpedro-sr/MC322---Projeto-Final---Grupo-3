package ifome.util;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Centraliza todas as entradas do usuário com validações.
 * VERSÃO DEFINITIVA - Remove completamente problemas de buffer
 */
public class InputManager {

    private static BufferedReader reader;
    private static boolean isWindows;

    static {
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        // Usa BufferedReader para evitar problemas com o Scanner e Gradle
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private static String lerLinha() {
        try {
            System.out.flush(); // Força o flush antes de ler
            String linha = reader.readLine();
            return linha != null ? linha.trim() : "";
        } catch (Exception e) {
            return "";
        }
    }

    public static Integer lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt + " (ENTER para cancelar): ");
            System.out.flush();
            
            String input = lerLinha();

            if (input.isEmpty()) {
                return null;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[X] Entrada invalida. Digite um numero inteiro.");
            }
        }
    }

    public static Integer lerInteiroPositivo(String prompt) {
        while (true) {
            Integer valor = lerInteiro(prompt);
            if (valor == null) return null;
            if (valor > 0) return valor;
            System.out.println("[X] Digite um numero maior que zero.");
        }
    }

    public static Double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt + " (ENTER para cancelar): ");
            System.out.flush();
            
            String input = lerLinha();

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
                System.out.println("[X] Valor invalido. Digite um numero decimal.");
            }
        }
    }

    public static String lerTexto(String prompt) {
        System.out.print(prompt + ": ");
        System.out.flush();
        return lerLinha();
    }

    public static String lerTextoObrigatorio(String prompt) {
        while (true) {
            System.out.print(prompt + " (ENTER para cancelar): ");
            System.out.flush();
            
            String input = lerLinha();
            if (input.isEmpty()) {
                return null;
            }
            return input;
        }
    }

    public static boolean lerConfirmacao(String prompt) {
        while (true) {
            System.out.print(prompt + " (S/N): ");
            System.out.flush();
            
            String input = lerLinha().toUpperCase();
            if (input.equals("S") || input.equals("SIM")) {
                return true;
            } else if (input.equals("N") || input.equals("NAO") || input.equals("NÃO")) {
                return false;
            }
            System.out.println("[X] Digite S para Sim ou N para Nao.");
        }
    }

    public static String lerEmail(String prompt) {
        while (true) {
            System.out.print(prompt + " (ENTER para cancelar): ");
            System.out.flush();
            
            String email = lerLinha();

            if (email.isEmpty()) {
                return null;
            }

            if (email.contains("@") && email.contains(".")) {
                return email;
            }

            System.out.println("[X] Email invalido. Tente novamente.");
        }
    }

    public static String lerTelefone(String prompt) {
        while (true) {
            System.out.print(prompt + " (ENTER para cancelar): ");
            System.out.flush();
            
            String telefone = lerLinha();

            if (telefone.isEmpty()) {
                return null;
            }

            String limpo = telefone.replaceAll("[^0-9]", "");
            if (limpo.length() >= 10) {
                return telefone;
            }

            System.out.println("[X] Telefone invalido. Digite pelo menos 10 digitos.");
        }
    }

    public static void pausar(String mensagem) {
        if (mensagem != null && !mensagem.isEmpty()) {
            System.out.println("\n" + mensagem);
        }
        System.out.print("\nPressione ENTER para continuar...");
        System.out.flush();
        lerLinha();
    }

    public static void limparTela() {
        try {
            if (isWindows) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public static void linha() {
        System.out.println("==================================================");
    }

    public static void fechar() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            // Ignora
        }
    }
}