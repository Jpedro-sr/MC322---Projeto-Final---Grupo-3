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
       //telefone valido
        cliente = new Cliente("teste@email.com", "123", "Teste Cliente", "11999999999");
        
        // verificadores
        restaurante = new Restaurante("rest@email.com", "123", "Restaurante Teste", "11222333000181");
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
        carrinho.adicionarItem(produto1, 2, ""); 
        carrinho.adicionarItem(produto2, 1, ""); 
        
        assertEquals(96.0, carrinho.calcularPrecoTotal(), 0.01);
    }

    @Test
    public void testAplicarCupom() {
        carrinho.adicionarItem(produto1, 2, ""); 
        
        Cupom cupom = Cupom.criarCupomPercentual("DESC10", 10);
        carrinho.aplicarCupom(cupom);
        
        assertEquals(81.0, carrinho.calcularTotalComDesconto(), 0.01); 
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