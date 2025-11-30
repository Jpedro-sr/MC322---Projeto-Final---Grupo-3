package ifome;

import ifome.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para validar as novas funcionalidades de segurança
 */
public class ValidacaoTest {

    // ========== TESTES DE VALIDAÇÃO DE CNPJ ==========
    
    @Test
    public void testCNPJValido() {
        // CNPJ válido com dígitos verificadores corretos
        assertDoesNotThrow(() -> {
            new Restaurante("test@email.com", "123", "Restaurante", "11222333000181");
        });
    }
    
    @Test
    public void testCNPJInvalidoDigitosVerificadores() {
        // CNPJ com dígitos verificadores incorretos
        assertThrows(IllegalArgumentException.class, () -> {
            new Restaurante("test@email.com", "123", "Restaurante", "11222333000100");
        });
    }
    
    @Test
    public void testCNPJInvalidoMuitoCurto() {
        // CNPJ com menos de 14 dígitos
        assertThrows(IllegalArgumentException.class, () -> {
            new Restaurante("test@email.com", "123", "Restaurante", "123456");
        });
    }
    
    @Test
    public void testCNPJInvalidoRepetitivo() {
        // CNPJ com todos os dígitos iguais
        assertThrows(IllegalArgumentException.class, () -> {
            new Restaurante("test@email.com", "123", "Restaurante", "11111111111111");
        });
    }
    
    @Test
    public void testCNPJComFormatacao() {
        // CNPJ válido com formatação (pontos e barra)
        assertDoesNotThrow(() -> {
            new Restaurante("test@email.com", "123", "Restaurante", "11.222.333/0001-81");
        });
    }
    
    // ========== TESTES DE VALIDAÇÃO DE TELEFONE ==========
    
    @Test
    public void testTelefoneValido() {
        // Telefone válido com 11 dígitos
        assertDoesNotThrow(() -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "11999999999");
        });
    }
    
    @Test
    public void testTelefoneComFormatacao() {
        // Telefone válido com formatação
        assertDoesNotThrow(() -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "(11) 99999-9999");
        });
    }
    
    @Test
    public void testTelefoneInvalidoMuitoCurto() {
        // Telefone com menos de 10 dígitos
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "999999");
        });
    }
    
    @Test
    public void testTelefoneInvalidoRepetitivo() {
        // Telefone com todos os dígitos iguais
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "11111111111");
        });
    }
    
    @Test
    public void testTelefoneInvalidoMuitoLongo() {
        // Telefone com mais de 15 dígitos
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", "Cliente Teste", "1234567890123456");
        });
    }
    
    // ========== TESTES DE VALIDAÇÃO DE NOME ==========
    
    @Test
    public void testNomeClienteValido() {
        assertDoesNotThrow(() -> {
            new Cliente("test@email.com", "123", "João da Silva", "11999999999");
        });
    }
    
    @Test
    public void testNomeClienteInvalidoMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", "J", "11999999999");
        });
    }
    
    @Test
    public void testNomeClienteInvalidoMuitoLongo() {
        String nomeLongo = "A".repeat(150);
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("test@email.com", "123", nomeLongo, "11999999999");
        });
    }
    
    @Test
    public void testNomeComCaracteresEspeciais() {
        // Nome com caracteres perigosos (devem ser removidos)
        Cliente cliente = new Cliente("test@email.com", "123", "João<script>", "11999999999");
        assertFalse(cliente.getNome().contains("<"));
        assertFalse(cliente.getNome().contains(">"));
    }
    
    @Test
    public void testNomeRestauranteValido() {
        assertDoesNotThrow(() -> {
            new Restaurante("test@email.com", "123", "Restaurante Bom", "11222333000181");
        });
    }
    
    @Test
    public void testNomeRestauranteInvalidoMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Restaurante("test@email.com", "123", "AB", "11222333000181");
        });
    }
    
    // ========== TESTES DE CUPONS (USO ÚNICO) ==========
    
    @Test
    public void testCupomUsoUnico() {
        Cliente cliente = new Cliente("test@email.com", "123", "Cliente Teste", "11999999999");
        
        // Registra uso do cupom
        cliente.registrarUsoCupom("DESC10");
        
        // Verifica que foi registrado
        assertTrue(cliente.jaUsouCupom("DESC10"));
        
        // Verifica case-insensitive
        assertTrue(cliente.jaUsouCupom("desc10"));
    }
    
    @Test
    public void testCupomNaoUsado() {
        Cliente cliente = new Cliente("test@email.com", "123", "Cliente Teste", "11999999999");
        
        // Cupom não usado
        assertFalse(cliente.jaUsouCupom("DESC10"));
    }
    
    @Test
    public void testMultiplosCuponsUsados() {
        Cliente cliente = new Cliente("test@email.com", "123", "Cliente Teste", "11999999999");
        
        cliente.registrarUsoCupom("DESC10");
        cliente.registrarUsoCupom("DESC20");
        cliente.registrarUsoCupom("BEMVINDO");
        
        assertTrue(cliente.jaUsouCupom("DESC10"));
        assertTrue(cliente.jaUsouCupom("DESC20"));
        assertTrue(cliente.jaUsouCupom("BEMVINDO"));
        assertFalse(cliente.jaUsouCupom("NATAL50"));
    }
    
    // ========== TESTES DE PREÇO ==========
    
    @Test
    public void testPrecoNegativoRejeitado() {
        Produto produto = new Comida("Pizza", "Deliciosa", 50.0, false);
        
        // Tenta definir preço negativo (deve ser rejeitado silenciosamente)
        produto.setPreco(-10.0);
        
        // Preço não deve ter mudado
        assertEquals(50.0, produto.getPreco(), 0.01);
    }
    
    @Test
    public void testPrecoZeroRejeitado() {
        Produto produto = new Comida("Pizza", "Deliciosa", 50.0, false);
        
        // Tenta definir preço zero (deve ser rejeitado)
        produto.setPreco(0.0);
        
        // Preço não deve ter mudado
        assertEquals(50.0, produto.getPreco(), 0.01);
    }
    
    @Test
    public void testPrecoPositivoAceito() {
        Produto produto = new Comida("Pizza", "Deliciosa", 50.0, false);
        
        produto.setPreco(75.50);
        
        assertEquals(75.50, produto.getPreco(), 0.01);
    }
    
    // ========== TESTES DE INTEGRAÇÃO ==========
    
    @Test
    public void testCriacaoCompletaCliente() {
        // Cria cliente com todos os dados válidos
        Cliente cliente = new Cliente(
            "cliente@teste.com",
            "senha123",
            "Maria da Silva",
            "(11) 98765-4321"
        );
        
        assertNotNull(cliente);
        assertEquals("Maria da Silva", cliente.getNome());
        assertEquals("cliente@teste.com", cliente.getEmail());
    }
    
    @Test
    public void testCriacaoCompletaRestaurante() {
        // Cria restaurante com todos os dados válidos
        Restaurante restaurante = new Restaurante(
            "rest@teste.com",
            "senha123",
            "Restaurante Gourmet",
            "11.222.333/0001-81"
        );
        
        assertNotNull(restaurante);
        assertEquals("Restaurante Gourmet", restaurante.getNomeRestaurante());
        assertTrue(restaurante.estaAberto()); // Deve abrir automaticamente
    }
    
    // ========== TESTES DE CNPJs CONHECIDOS ==========
    
    @Test
    public void testCNPJsValidos() {
        // Lista de CNPJs válidos conhecidos
        String[] cnpjsValidos = {
            "11222333000181",
            "11444777000161",
            "06990590000123"
        };
        
        for (String cnpj : cnpjsValidos) {
            assertDoesNotThrow(() -> {
                new Restaurante("test@email.com", "123", "Restaurante", cnpj);
            }, "CNPJ " + cnpj + " deveria ser válido");
        }
    }
}