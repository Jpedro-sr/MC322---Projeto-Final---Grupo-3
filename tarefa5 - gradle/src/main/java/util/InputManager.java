package util;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputManager {
    private static Scanner scanner = new Scanner(System.in);
    private InputManager() {}
    public static int lerInteiro (String mensagem, int min, int max) {
        while (true) {
            System.out.print(mensagem + " (" + min + " - " + max + "): ");
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor < min || valor > max) {
                    System.out.println("Valor fora do intervalo!");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, digite um número inteiro.");
            } catch (NoSuchElementException e) {
                throw new RuntimeException("Entrada não disponível.", e);
            }
        }
    }

    public static String lerString (String mensagem) {
        System.out.print(mensagem + ": ");
        try {
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Entrada não disponível.", e);
        }
    }
    
    public static boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem + " (s/n): ");
            String s = scanner.nextLine().trim().toLowerCase();
            if (s.equals("s")) return true;
            if (s.equals("n")) return false;
            System.out.println("Opção inválida. Digite apenas 'Sim' ou 'Nao'.");
        }
    }

    public static void esperarEnter(String mensagem) {
        System.out.print(mensagem);
        scanner.nextLine();
    }

    public static void fecharScanner() {
        scanner.close();
    }
}
