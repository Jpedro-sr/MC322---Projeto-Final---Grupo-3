package personagens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import combate.AcaoDeCombate;
import itens.armas.Arma;
import personagens.heroi.CapitaoCabecudo;
import personagens.heroi.Heroi;
import personagens.monstros.Kraken;
import personagens.monstros.Monstro;

class CombatenteTest {

    // ========== TESTES DE HERÓI ==========
    
    @Test
    void heroi_receberDano_reduzVida() {
        Heroi heroi = new CapitaoCabecudo();
        int vidaInicial = heroi.getPontosDeVida();
        
        heroi.receberDano(50);
        
        assertTrue(heroi.getPontosDeVida() < vidaInicial);
        assertEquals(vidaInicial - 50, heroi.getPontosDeVida());
    }

    @Test
    void heroi_receberDanoMortal_ficaComVidaZeroOuMenor() {
        Heroi heroi = new CapitaoCabecudo();
        
        heroi.receberDano(9999);
        
        assertFalse(heroi.estaVivo());
        assertTrue(heroi.getPontosDeVida() <= 0);
    }

    @Test
    void heroi_atacaAlvo_causaDano() {
        Heroi heroi = new CapitaoCabecudo();
        Monstro monstro = new Kraken();
        int vidaInicialMonstro = monstro.getPontosDeVida();
        
        // Herói escolhe uma ação e executa
        AcaoDeCombate acao = heroi.escolherAcao(monstro);
        if (acao != null) {
            acao.executar(heroi, monstro);
        }
        
        // Verifica que o monstro sofreu algum dano
        assertTrue(monstro.getPontosDeVida() < vidaInicialMonstro);
    }

    // ========== TESTES DE MONSTRO ==========
    
    @Test
    void monstro_receberDano_reduzVida() {
        Monstro monstro = new Kraken();
        int vidaInicial = monstro.getPontosDeVida();
        
        monstro.receberDano(30);
        
        assertTrue(monstro.getPontosDeVida() < vidaInicial);
        assertEquals(vidaInicial - 30, monstro.getPontosDeVida());
    }

    @Test
    void monstro_receberDanoMortal_ficaComVidaZeroOuMenor() {
        Monstro monstro = new Kraken();
        
        monstro.receberDano(9999);
        
        assertFalse(monstro.estaVivo());
        assertTrue(monstro.getPontosDeVida() <= 0);
    }

    @Test
    void monstro_atacaAlvo_causaDano() {
        Monstro monstro = new Kraken();
        Heroi heroi = new CapitaoCabecudo();
        int vidaInicialHeroi = heroi.getPontosDeVida();
        
        // Monstro escolhe uma ação e executa
        AcaoDeCombate acao = monstro.escolherAcao(heroi);
        if (acao != null) {
            acao.executar(monstro, heroi);
        }
        
        // Verifica que o herói sofreu algum dano
        assertTrue(heroi.getPontosDeVida() < vidaInicialHeroi);
    }

    // ========== TESTE DE LOOTAVEL ==========
    
    @Test
    void monstro_implementaLootavel() {
        Monstro monstro = new Kraken();
        
        // Verifica que Monstro implementa a interface Lootavel
        assertTrue(monstro instanceof Lootavel);
    }

    @Test
    void monstro_dropaLoot_retornaArmaOuNull() {
        Monstro monstro = new Kraken();
        
        Arma loot = monstro.droparLoot();
        
        // Loot pode ser null ou uma Arma válida
        assertTrue(loot == null || loot instanceof Arma);
    }
}