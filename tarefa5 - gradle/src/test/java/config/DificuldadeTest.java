package config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import personagens.monstros.Kraken;
import personagens.monstros.Monstro;

class DificuldadeTest {

    @Test
    void dificuldadeDificil_monstroMaisForteQueFacil() {
        // Cria dois monstros idênticos
        Monstro monstroFacil = new Kraken();
        Monstro monstroDificil = new Kraken();
        
        int forcaOriginal = monstroFacil.getForca();
        
        // Aplica multiplicadores de dificuldade
        int forcaFacil = (int)(forcaOriginal * Dificuldade.FACIL.getMultiplicadorForcaMonstro());
        int forcaDificil = (int)(forcaOriginal * Dificuldade.DIFICIL.getMultiplicadorForcaMonstro());
        
        monstroFacil.setForca(forcaFacil);
        monstroDificil.setForca(forcaDificil);
        
        // Verifica que dificuldade maior resulta em monstro mais forte
        assertTrue(monstroDificil.getForca() > monstroFacil.getForca());
    }

    @Test
    void dificuldadeFacil_maisXpQueNormal() {
        int xpBase = 100;
        
        double xpFacil = xpBase * Dificuldade.FACIL.getMultiplicadorXp();
        double xpNormal = xpBase * Dificuldade.NORMAL.getMultiplicadorXp();
        
        // Verifica que dificuldade fácil dá mais XP
        assertTrue(xpFacil > xpNormal);
    }

    @Test
    void dificuldadeNormal_multiplicadoresIguaisAUm() {
        assertEquals(1.0, Dificuldade.NORMAL.getMultiplicadorForcaMonstro());
        assertEquals(1.0, Dificuldade.NORMAL.getMultiplicadorXp());
    }

    @Test
    void dificuldadeDificil_menosXpQueFacil() {
        int xpBase = 100;
        
        double xpDificil = xpBase * Dificuldade.DIFICIL.getMultiplicadorXp();
        double xpFacil = xpBase * Dificuldade.FACIL.getMultiplicadorXp();
        
        // Verifica que dificuldade difícil dá menos XP que fácil
        assertTrue(xpDificil < xpFacil);
    }
}