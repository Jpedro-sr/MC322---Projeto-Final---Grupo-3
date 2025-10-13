package fases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

import fases.TipoCenario;
import fases.FaseDeCombate;
import exceptions.LootIndisponivelException;
import personagens.monstros.Monstro;
import config.Dificuldade;

class FaseDeCombateTest {

    @Test
    void coletarLoot_semLoot_deveLancarExcecao() {
        ArrayList<Monstro> lista = new ArrayList<>();
        FaseDeCombate fase = new FaseDeCombate(TipoCenario.PRAIA_ASSOMBRADA, lista, Dificuldade.NORMAL);

        Assertions.assertThrows(LootIndisponivelException.class, fase::coletarLoot);
    }
}