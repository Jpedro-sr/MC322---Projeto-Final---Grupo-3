/**
 * Evento que pode ocorrer na Praia Assombrada.
 * Carangueijos atacam o herói, causando dano leve.
 */
public class EmboscadaCarangueijos implements Evento {
    
    @Override
    public boolean verificarGatilho(Heroi heroi) {
        // 40% de chance do evento ocorrer
        return Math.random() < 0.4;
    }

    @Override
    public void executar(Heroi heroi) {
        System.out.println("\n🦀 ========== EVENTO: EMBOSCADA DE CARANGUEIJOS! ========== 🦀");
        System.out.println("Dezenas de carangueijos fantasma emergem da areia!");
        System.out.println("Suas garras afiadas cortam suas pernas e braços!");
        
        int dano = 8 + (int)(Math.random() * 5); // 8-12 de dano
        heroi.receberDano(dano);
        
        System.out.println("Os carangueijos desaparecem de volta para a areia...");
        System.out.println("═══════════════════════════════════════════════════════════");
    }
}