// =====================================================
// ARQUIVO: src/test/java/ifome/CarrinhoTest.java
// =====================================================

package ifome;

import ifome.model.*;
import ifome.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CarrinhoTest {

    private Carrinho carrinho;
    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente("teste@email.com", "123", "Teste Cliente", "11999999999");
        restaurante = new Restaurante("rest@email.com", "123", "Restaurante Teste", "12345678000199");
        restaurante.abrirRestaurante();
        
        carrinho = new Carrinho(cliente, restaurante);
        
        produto1 = new Comida("Pizza", "Pizza grande", 45.0, false);
        produto2 = new Bebida("Coca-Cola", "Refrigerante", 6.0, 350);
    }

    @Test
    public void testAdicionarItem() {
        carrinho.adicionarItem(produto1, 2, "Sem cebola");
        
        assertEquals(1, carrinho.getItens().size());
        assertEquals(2, carrinho.getItens().get(0).getQuantidade());
    }

    @Test
    public void testCalcularPrecoTotal() {
        carrinho.adicionarItem(produto1, 2, ""); // 2 x 45 = 90
        carrinho.adicionarItem(produto2, 1, ""); // 1 x 6 = 6
        
        assertEquals(96.0, carrinho.calcularPrecoTotal(), 0.01);
    }

    @Test
    public void testAplicarCupom() {
        carrinho.adicionarItem(produto1, 2, ""); // Total: 90
        
        Cupom cupom = Cupom.criarCupomPercentual("DESC10", 10);
        carrinho.aplicarCupom(cupom);
        
        assertEquals(81.0, carrinho.calcularTotalComDesconto(), 0.01); // 90 - 10% = 81
    }

    @Test
    public void testGerarPedidoComRestauranteFechado() {
        restaurante.fecharRestaurante();
        carrinho.adicionarItem(produto1, 1, "");
        
        assertThrows(RestauranteFechadoException.class, () -> {
            carrinho.gerarPedido();
        });
    }

    @Test
    public void testGerarPedidoComValorMinimo() {
        // Adiciona produto com valor muito baixo (abaixo de R$ 15)
        Produto produtoBarato = new Adicional("Guardanapo", 1.0);
        carrinho.adicionarItem(produtoBarato, 1, "");
        
        assertThrows(ValorMinimoException.class, () -> {
            carrinho.gerarPedido();
        });
    }

    @Test
    public void testLimparCarrinho() {
        carrinho.adicionarItem(produto1, 2, "");
        carrinho.adicionarItem(produto2, 1, "");
        
        carrinho.limparCarrinho();
        
        assertEquals(0, carrinho.getItens().size());
        assertEquals(0.0, carrinho.calcularPrecoTotal(), 0.01);
    }
}

