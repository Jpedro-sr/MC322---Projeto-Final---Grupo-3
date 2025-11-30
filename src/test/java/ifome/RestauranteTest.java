package ifome;

import ifome.model.*;
import ifome.exceptions.RestauranteFechadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RestauranteTest {

    private Restaurante restaurante;
    private Produto produto;

    @BeforeEach
    public void setUp() {
    
        restaurante = new Restaurante("rest@email.com", "123", "Restaurante Teste", "11222333000181");
        produto = new Comida("Pizza", "Pizza grande", 45.0, false);
    }

    @Test
    public void testAdicionarProdutoCardapio() {
        restaurante.adicionarProdutoCardapio(produto);
        
        assertEquals(1, restaurante.getCardapio().size());
        assertTrue(restaurante.getCardapio().contains(produto));
    }

    @Test
    public void testRemoverProdutoCardapio() {
        restaurante.adicionarProdutoCardapio(produto);
        restaurante.removerProdutoCardapio(produto);
        
        assertEquals(0, restaurante.getCardapio().size());
    }

    @Test
    public void testRestauranteAbrirFechar() {
        restaurante.fecharRestaurante();
        assertFalse(restaurante.estaAberto());
        
        restaurante.abrirRestaurante();
        assertTrue(restaurante.estaAberto());
    }

    @Test
    public void testAceitarPedidoComRestauranteFechado() {
        restaurante.fecharRestaurante();
        
        Pedido pedido = new Pedido();
        
        assertThrows(RestauranteFechadoException.class, () -> {
            restaurante.aceitarPedido(pedido);
        });
    }

    @Test
    public void testAvaliarRestaurante() {
        restaurante.avaliar(5, "Excelente!");
        restaurante.avaliar(4, "Muito bom");
        
        assertEquals(2, restaurante.getQuantidadeAvaliacoes());
        assertEquals(4.5, restaurante.calcularMediaAvaliacoes(), 0.1);
    }
}