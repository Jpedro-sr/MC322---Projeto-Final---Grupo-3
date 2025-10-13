package personagens;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import personagens.heroi.Heroi;
import personagens.heroi.CapitaoCabecudo;
import exceptions.NivelInsuficienteException;
import itens.armas.ArmaSimples;

class HeroiTest {

    @Test
    void equiparArma_quandoNivelInsuficiente_deveLancar() {
        Heroi h = new CapitaoCabecudo();
        ArmaSimples armaPesada = new ArmaSimples("Espada Gigante", 50, 99);
        Assertions.assertThrows(NivelInsuficienteException.class, () -> {
            h.equiparArma(armaPesada);
        });
    }

    @Test
    void equiparArma_quandoNivelSuficiente_deveEquiparSemExcecao() {
        Heroi h = new CapitaoCabecudo();
        ArmaSimples armaLeve = new ArmaSimples("Punhal", 5, 1);
        Assertions.assertDoesNotThrow(() -> {
            h.equiparArma(armaLeve);
        });
    }
}