package ifome.model;

import java.util.ArrayList;
import java.util.List;
import ifome.exceptions.RestauranteFechadoException;
import ifome.exceptions.ValorMinimoException;
import ifome.exceptions.ProdutoIndisponivelException;

/**
 * Representa o carrinho de compras do cliente.
 * Implementa Calculavel para calcular totais.
 * Pode gerar um Pedido quando finalizado.
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

    /**
     * Adiciona um item ao carrinho.
     * Se o produto já existe, incrementa a quantidade.
     */
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

    /**
     * Remove um item específico do carrinho.
     */
    public void removerItem(ItemPedido item) {
        if (itens.remove(item)) {
            // Item removido
        }
    }

    /**
     * Remove o primeiro item de um produto específico.
     */
    public void removerPorProduto(Produto p) {
        for (ItemPedido item : itens) {
            if (item.getProduto().equals(p)) {
                removerItem(item);
                return;
            }
        }
    }

    /**
     * Limpa todos os itens do carrinho.
     */
    public void limparCarrinho() {
        itens.clear();
        cupomAplicado = null;
    }

    /**
     * Calcula o subtotal do carrinho sem descontos.
     */
    @Override
    public double calcularPrecoTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularPrecoTotal();
        }
        return total;
    }

    /**
     * Calcula o total com cupom aplicado.
     */
    public double calcularTotalComDesconto() {
        double subtotal = calcularPrecoTotal();
        if (cupomAplicado != null && cupomAplicado.estaValido()) {
            return cupomAplicado.aplicarDesconto(subtotal);
        }
        return subtotal;
    }

    /**
     * Gera um Pedido a partir do carrinho.
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

        double subtotal = calcularPrecoTotal();
        double valorMinimo = 15.0;

        if (subtotal < valorMinimo) {
            throw new ValorMinimoException(
                "Valor mínimo não atingido. Mínimo: R$" + valorMinimo
            );
        }

        for (ItemPedido item : itens) {
            if (!item.getProduto().isDisponivel()) {
                throw new ProdutoIndisponivelException(
                    "Produto " + item.getProduto().getNome() + " não está disponível."
                );
            }
        }

        Pedido pedido = new Pedido();
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }
        
        if (cupomAplicado != null) {
            pedido.setCupomAplicado(cupomAplicado);
        }

        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        //pedido.atualizarStatus("Pendente");

        return pedido;
    }

    /**
     * Aplica um cupom ao carrinho.
     */
    public void aplicarCupom(Cupom cupom) {
        if (cupom == null || !cupom.estaValido()) {
            return;
        }
        this.cupomAplicado = cupom;
    }

    /**
     * Remove o cupom do carrinho.
     */
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