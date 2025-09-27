// Evento que pode ocorrer no Covil do Kraken.

public class ElevacaoNivelAgua implements Evento {

    @Override
    public boolean verificarGatilho(Heroi heroi) {
        // 50% de chance do evento ocorrer (mais comum no covil)
        return Math.random() < 0.5;
    }

    @Override
    public void executar(Heroi heroi) {
        System.out.println("\n🌊 ========== EVENTO: ELEVAÇÃO DO NÍVEL DA ÁGUA! ========== 🌊");
        System.out.println("As águas do covil começam a subir rapidamente!");
        System.out.println("Você precisa nadar para se manter à tona, perdendo energia!");

        // Dano moderado pela luta contra a correnteza
        int dano = 10 + (int)(Math.random() * 6); // 10-15 de dano
        heroi.receberDano(dano);

        // Redução temporária de força (simulação - poderia ser mais complexo)
        int forcaOriginal = heroi.getForca();
        int reducao = 2 + (int)(Math.random() * 3); // 2-4 de redução
        heroi.setForca(Math.max(1, forcaOriginal - reducao)); // Não deixa força ir para 0

        System.out.println("Seus movimentos ficam mais lentos na água! Força reduzida temporariamente!");
        System.out.println("Força: " + forcaOriginal + " → " + heroi.getForca());
        System.out.println("══════════════════════════════════════════════════════════════");
    }
}