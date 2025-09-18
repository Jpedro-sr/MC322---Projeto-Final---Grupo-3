/**
 * Evento que pode ocorrer na Gruta Submersa.
 * Um cardume de peixes descontrolados ataca, mas também pode curar.
 */
public class CardumePeixesDescontrolados implements Evento {
    
    @Override
    public boolean verificarGatilho(Heroi heroi) {
        // 35% de chance do evento ocorrer
        return Math.random() < 0.35;
    }

    @Override
    public void executar(Heroi heroi) {
        System.out.println("\n🐠 ======== EVENTO: CARDUME DE PEIXES DESCONTROLADOS! ======== 🐠");
        System.out.println("Um enorme cardume de peixes luminosos invade a gruta!");
        
        if (Math.random() < 0.6) {
            // 60% chance de dano
            System.out.println("Os peixes estão em frenesi e mordem você repetidamente!");
            int dano = 6 + (int)(Math.random() * 4); // 6-9 de dano
            heroi.receberDano(dano);
        } else {
            // 40% chance de cura (peixes mágicos)
            System.out.println("Os peixes mágicos liberam uma energia restauradora!");
            int cura = 10 + (int)(Math.random() * 6); // 10-15 de cura
            heroi.receberCura(cura);
        }
        
        System.out.println("O cardume desaparece nas profundezas escuras da gruta...");
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}