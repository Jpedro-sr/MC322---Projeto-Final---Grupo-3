package ifome;

import ifome.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidoTest {

    private Pedido pedido;
    private Cliente cliente;
    private Restaurante restaurante;

    @BeforeEach
    public void setUp() {
       
        cliente = new Cliente("cliente@email.com", "123", "Cliente Teste", "11999999999");
        
        restaurante = new Restaurante("rest@email.com", "123", "Restaurante Teste", "11222333000181");
        
        pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
    }

    @Test
    public void testStatusInicialPendente() {
        assertEquals("Pendente", pedido.getStatus());
    }

    @Test
    public void testAtualizarStatusValido() {
        pedido.atualizarStatus("Confirmado");
        assertEquals("Confirmado", pedido.getStatus());
        
        pedido.atualizarStatus("Preparando");
        assertEquals("Preparando", pedido.getStatus());
        
        pedido.atualizarStatus("Pronto");
        assertEquals("Pronto", pedido.getStatus());
        
        pedido.atualizarStatus("Em Entrega");
        assertEquals("Em Entrega", pedido.getStatus());
        
        pedido.atualizarStatus("Entregue");
        assertEquals("Entregue", pedido.getStatus());
    }

    @Test
    public void testTransicaoInvalidaDeStatus() {
        pedido.atualizarStatus("Confirmado");
        pedido.atualizarStatus("Preparando");
        
   
        pedido.atualizarStatus("Confirmado");
        assertEquals("Preparando", pedido.getStatus()); 
    }

    @Test
    public void testCalcularTotalComItens() {
        Produto pizza = new Comida("Pizza", "Pizza grande", 45.0, false);
        Produto bebida = new Bebida("Coca-Cola", "Refrigerante", 6.0, 350);
        
        pedido.adicionarItem(new ItemPedido(pizza, 2, ""));  
        pedido.adicionarItem(new ItemPedido(bebida, 1, "")); 
        
        double total = pedido.calcularPrecoTotal();
        assertEquals(96.0, total, 0.01);
    }

    @Test
    public void testAvaliarPedido() {
        pedido.atualizarStatus("Entregue");
        
        boolean resultado = pedido.avaliar(5, "Excelente!");
        
        assertTrue(resultado);
        assertEquals(1, pedido.getAvaliacoes().size());
        assertEquals(5, pedido.getAvaliacoes().get(0).getNota());
    }

    @Test
    public void testCalcularMediaAvaliacoes() {
        pedido.avaliar(5);
        pedido.avaliar(4);
        pedido.avaliar(3);
        
        double media = pedido.calcularMediaAvaliacoes();
        assertEquals(4.0, media, 0.1);
    }
}
