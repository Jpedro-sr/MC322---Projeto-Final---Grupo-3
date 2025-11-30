package ifome;

import ifome.model.*;
import ifome.exceptions.PagamentoRecusadoException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FormaPagamentoTest {

    @Test
    public void testPagamentoPIXSucesso() {
        PIX pix = new PIX();
        boolean resultado = pix.processarPagamento(50.0);
        
        assertTrue(resultado);
        assertTrue(pix.isPagamentoProcessado());
    }

    @Test
    public void testPagamentoCartaoValido() {
        CartaoCredito cartao = new CartaoCredito(
            "1234567890123456", 
            "João Silva", 
            "123", 
            "12/25"
        );
        
        boolean resultado = cartao.processarPagamento(100.0);
        assertTrue(resultado);
    }

    @Test
    public void testPagamentoCartaoInvalido() {
        CartaoCredito cartao = new CartaoCredito(
            "1234567890123456", 
            "João Silva", 
            "000",  // CVV inválido (simulação)
            "12/25"
        );
        
        boolean resultado = cartao.processarPagamento(100.0);
        assertFalse(resultado);
    }

    @Test
    public void testPagamentoDinheiroSuficiente() {
        Dinheiro dinheiro = new Dinheiro(100.0);
        boolean resultado = dinheiro.processarPagamento(80.0);
        
        assertTrue(resultado);
        assertEquals(20.0, dinheiro.getTroco(), 0.01);
    }

    @Test
    public void testPagamentoDinheiroInsuficiente() {
        Dinheiro dinheiro = new Dinheiro(50.0);
        boolean resultado = dinheiro.processarPagamento(80.0);
        
        assertFalse(resultado);
    }
}