/**
 * Evento que pode acontecer na Gruta Submersa.
 * Um cardume de peixes descontrolados pode atacar o herói, causando dano, ou surpreender com uma energia mágica que cura.
 */
public class CardumePeixesDescontrolados implements Evento {
    
    @Override
    public boolean verificarGatilho(Heroi heroi) {
        // 35% de chance do evento ser ativado ao entrar na gruta.
        return Math.random() < 0.35;
    }

    @Override
    public void executar(Heroi heroi) {
        System.out.println("\n🐠 ======== EVENTO: CARDUME DE PEIXES DESCONTROLADOS! ======== 🐠");
        System.out.println("Um enorme cardume de peixes luminosos invade a gruta!");
        
        // 60% de chance de causar dano, 40% de chance de curar.
        if (Math.random() < 0.6) {
            System.out.println("Os peixes estão em frenesi e mordem você repetidamente!");
            int dano = 6 + (int)(Math.random() * 4); // Dano entre 6 e 9
            heroi.receberDano(dano);
        } else {
            System.out.println("Os peixes mágicos liberam uma energia restauradora!");
            int cura = 10 + (int)(Math.random() * 6); // Cura entre 10 e 15
            heroi.receberCura(cura);
        }
        
        System.out.println("O cardume desaparece nas profundezas escuras da gruta...");
        System.out.println("═══════════════════════════════════════════════════════════════");
    }
}