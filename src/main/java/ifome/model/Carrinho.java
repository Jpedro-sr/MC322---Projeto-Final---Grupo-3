package ifome.model;

import java.util.ArrayList;
import java.util.List;
import ifome.exceptions.RestauranteFechadoException;
import ifome.exceptions.ValorMinimoException;
import ifome.exceptions.ProdutoIndisponivelException;

/**
 * Representa o carrinho de compras do cliente.
 * CORRIGIDO: Calcula valor total corretamente ao gerar pedido
 */
public class Carrinho implements Calculavel {

    private List<ItemPedido> itens;
    private Cliente cliente;
    private Restaurante restaurante;
    private Cupom cupomAplicado;

    public Carrinho(Cliente cliente, Restaurante restaurante) {
        this.itens = new ArrayList<>();
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.cupomAplicado = null;
    }

    public Carrinho() {
        this(null, null);
    }

    public void adicionarItem(Produto p, int quantidade, String obs) {
        if (p == null || quantidade <= 0) {
            return;
        }

        for (ItemPedido item : itens) {
            if (item.getProduto().equals(p)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }

        ItemPedido novoItem = new ItemPedido(p, quantidade, obs);
        this.itens.add(novoItem);
    }

    public void adicionarItem(Produto p, int quantidade) {
        adicionarItem(p, quantidade, "");
    }

    public void removerItem(ItemPedido item) {
        if (itens.remove(item)) {
            // Item removido
        }
    }

    public void removerPorProduto(Produto p) {
        for (ItemPedido item : itens) {
            if (item.getProduto().equals(p)) {
                removerItem(item);
                return;
            }
        }
    }

    public void limparCarrinho() {
        itens.clear();
        cupomAplicado = null;
    }

    @Override
    public double calcularPrecoTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularPrecoTotal();
        }
        return total;
    }

    public double calcularTotalComDesconto() {
        double subtotal = calcularPrecoTotal();
        if (cupomAplicado != null && cupomAplicado.estaValido()) {
            return cupomAplicado.aplicarDesconto(subtotal);
        }
        return subtotal;
    }

    /**
     * ✅ CORRIGIDO: Gera pedido com valor total calculado corretamente
     */
    public Pedido gerarPedido() throws RestauranteFechadoException, ValorMinimoException, ProdutoIndisponivelException {
        
        if (itens.isEmpty()) {
            throw new ValorMinimoException("Carrinho vazio.");
        }

        if (restaurante == null) {
            throw new RestauranteFechadoException("Nenhum restaurante selecionado.");
        }

        if (!restaurante.estaAberto()) {
            throw new RestauranteFechadoException(
                "Restaurante " + restaurante.getNomeRestaurante() + " está fechado."
            );
        }

        // Validação de valor mínimo
        double subtotal = calcularPrecoTotal();
        double valorMinimo = 15.0;

        if (subtotal < valorMinimo) {
            throw new ValorMinimoException(
                "Valor mínimo não atingido. Mínimo: R$" + valorMinimo
            );
        }

        // Validação de disponibilidade dos produtos
        for (ItemPedido item : itens) {
            if (!item.getProduto().isDisponivel()) {
                throw new ProdutoIndisponivelException(
                    "Produto " + item.getProduto().getNome() + " não está disponível."
                );
            }
        }

        // ✅ CORRIGIDO: Cria pedido e configura valor ANTES de adicionar itens
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        
        // Adiciona todos os itens
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }
        
        // Aplica cupom se existir
        if (cupomAplicado != null) {
            pedido.setCupomAplicado(cupomAplicado);
        }

        // ✅ CRÍTICO: Calcula o valor total DEPOIS de adicionar os itens
        double valorFinal = pedido.calcularPrecoTotal();
        pedido.setValorTotal(valorFinal);
        
        // Define status inicial como Pendente (aguardando confirmação do restaurante)
        pedido.atualizarStatus("Pendente");

        System.out.println(">>> Pedido gerado: #" + pedido.getNumeroPedido() + 
                          " | Valor: R$" + String.format("%.2f", valorFinal));

        return pedido;
    }

    public void aplicarCupom(Cupom cupom) {
        if (cupom == null || !cupom.estaValido()) {
            return;
        }
        this.cupomAplicado = cupom;
    }

    public void removerCupom() {
        this.cupomAplicado = null;
    }

    public List<ItemPedido> getItens() {
        return new ArrayList<>(itens);
    }

    public int getQuantidadeItens() {
        return itens.size();
    }

    public Cupom getCupomAplicado() {
        return cupomAplicado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== CARRINHO ===\n");
        if (itens.isEmpty()) {
            sb.append("(vazio)\n");
        } else {
            for (int i = 0; i < itens.size(); i++) {
                ItemPedido item = itens.get(i);
                sb.append(String.format("%d. %s x%d - R$%.2f\n", 
                    i + 1, 
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.calcularPrecoTotal()
                ));
            }
        }
        sb.append("Subtotal: R$").append(String.format("%.2f", calcularPrecoTotal())).append("\n");
        if (cupomAplicado != null && cupomAplicado.estaValido()) {
            sb.append("Desconto: ").append(cupomAplicado.getDescricaoDesconto()).append("\n");
            sb.append("Total: R$").append(String.format("%.2f", calcularTotalComDesconto())).append("\n");
        }
        sb.append("==============\n");
        return sb.toString();
    }
}