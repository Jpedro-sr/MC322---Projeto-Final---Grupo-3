package ifome;

import ifome.model.Cupom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CupomTest {

    @Test
    public void testCupomPercentual() {
        Cupom cupom = Cupom.criarCupomPercentual("DESC20", 20);
        
        double valorOriginal = 100.0;
        double valorComDesconto = cupom.aplicarDesconto(valorOriginal);
        
        assertEquals(80.0, valorComDesconto, 0.01);
    }

    @Test
    public void testCupomValorFixo() {
        Cupom cupom = Cupom.criarCupomFixo("DESC10", 10.0);
        
        double valorOriginal = 50.0;
        double valorComDesconto = cupom.aplicarDesconto(valorOriginal);
        
        assertEquals(40.0, valorComDesconto, 0.01);
    }

    @Test
    public void testCupomValorFixoMaiorQueTotal() {
        Cupom cupom = Cupom.criarCupomFixo("DESC50", 50.0);
        
        double valorOriginal = 30.0;
        double valorComDesconto = cupom.aplicarDesconto(valorOriginal);
        
       //limite de desconto
        assertEquals(0.0, valorComDesconto, 0.01);
    }

    @Test
    public void testCupomInvalido() {
        Cupom cupom = Cupom.criarCupomPercentual("EXPIRADO", 10);
        cupom.setAtivo(false);
        
        assertFalse(cupom.estaValido());
    }
}