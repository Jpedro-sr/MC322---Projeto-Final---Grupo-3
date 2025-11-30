package ifome;

import ifome.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//testes gerais
public class ValidacaoTest {

    // cnpj
    
    @Test
    public void testCNPJ14Digitos() {
     
        assertDoesNotThrow(() -> {
            new Restaurante("test@email.com", "123", "Restaurante", "12345678901234");
        });
    }
    
    @Test
    public void testCNPJComFormatacao() {
       
        assertDoesNotThrow(() -> {
            new Restaurante("test@email.com", "123", "Restaurante", "12.345.678/9012-34");
        });
    }
    
    @Test
    public void testCNPJMuitoCurto() {
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Restaurante("test@email.com", "123", "Restaurante", "123456");
        });
    }
    
    @Test
    public void testCNPJMuitoLongo() {
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Restaurante("test@email.com", "123", "Restaurante", "123456789012345");
        });
    }
    
    //telefon
    
    @Test
    public void testTelefone10Digitos() {
        assertDoesNotThrow(() -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "1199999999");
        });
    }
    
    @Test
    public void testTelefone11Digitos() {
        assertDoesNotThrow(() -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "11999999999");
        });
    }
    
    @Test
    public void testTelefoneComFormatacao() {
        assertDoesNotThrow(() -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "(11) 99999-9999");
        });
    }
    
    @Test
    public void testTelefoneMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "999999");
        });
    }
    
    @Test
    public void testTelefoneMuitoLongo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "1234567890123456");
        });
    }
    
    // valida nome
    
    @Test
    public void testNomeClienteValido() {
        assertDoesNotThrow(() -> {
            new Cliente("test@email.com", "123", "João da Silva", "11999999999");
        });
    }
    
    @Test
    public void testNomeClienteMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", "J", "11999999999");
        });
    }
    
    @Test
    public void testNomeClienteMuitoLongo() {
        String nomeLongo = "A".repeat(150);
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", nomeLongo, "11999999999");
        });
    }
    
    @Test
    public void testNomeComCaracteresEspeciais() {
        Cliente cliente = new Cliente("test@email.com", "123", "João<script>", "11999999999");
        assertFalse(cliente.getNome().contains("<"));
        assertFalse(cliente.getNome().contains(">"));
    }
    
    @Test
    public void testNomeRestauranteValido() {
        assertDoesNotThrow(() -> {
            new Restaurante("test@email.com", "123", "Restaurante Bom", "12345678901234");
        });
    }
    
    @Test
    public void testNomeRestauranteMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Restaurante("test@email.com", "123", "AB", "12345678901234");
        });
    }
    
    // =cupom
    
    @Test
    public void testCupomUsoUnico() {
        Cliente cliente = new Cliente("test@email.com", "123", "Cliente Teste", "11999999999");
        cliente.registrarUsoCupom("DESC10");
        assertTrue(cliente.jaUsouCupom("DESC10"));
        assertTrue(cliente.jaUsouCupom("desc10")); // Case-insensitive
    }
    
    // preço
    
    @Test
    public void testPrecoNegativoRejeitado() {
        Produto produto = new Comida("Pizza", "Deliciosa", 50.0, false);
        produto.setPreco(-10.0);
        assertEquals(50.0, produto.getPreco(), 0.01);
    }
    
    @Test
    public void testPrecoZeroRejeitado() {
        Produto produto = new Comida("Pizza", "Deliciosa", 50.0, false);
        produto.setPreco(0.0);
        assertEquals(50.0, produto.getPreco(), 0.01);
    }
    
    @Test
    public void testPrecoPositivoAceito() {
        Produto produto = new Comida("Pizza", "Deliciosa", 50.0, false);
        produto.setPreco(75.50);
        assertEquals(75.50, produto.getPreco(), 0.01);
    }
}