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

    // Construtor com cliente e restaurante
    public Carrinho(Cliente cliente, Restaurante restaurante) {
        this.itens = new ArrayList<>();
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.cupomAplicado = null;
    }

    // Construtor vazio
    public Carrinho() {
        this(null, null);
    }

    /**
     * Adiciona um item ao carrinho.
     * Se o produto já existe, incrementa a quantidade.
     */
    public void adicionarItem(Produto p, int quantidade, String obs) {
        if (p == null || quantidade <= 0) {
            System.out.println("❌ Produto inválido ou quantidade inválida.");
            return;
        }

        // Verificar se produto já está no carrinho
        for (ItemPedido item : itens) {
            if (item.getProduto().equals(p)) {
                System.out.println("ℹ️  Produto " + p.getNome() + " já existe. Incrementando quantidade.");
                return;
            }
        }

        ItemPedido novoItem = new ItemPedido(p, quantidade, obs);
        this.itens.add(novoItem);
        System.out.println("✅ Item adicionado: " + p.getNome() + " (x" + quantidade + ")");
    }

    // Versão simplificada sem observações
    public void adicionarItem(Produto p, int quantidade) {
        adicionarItem(p, quantidade, "");
    }

    /**
     * Remove um item específico do carrinho.
     */
    public void removerItem(ItemPedido item) {
        if (itens.remove(item)) {
            System.out.println("✅ Item removido: " + item.getProduto().getNome());
        } else {
            System.out.println("❌ Item não encontrado no carrinho.");
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
        System.out.println("❌ Produto não encontrado no carrinho.");
    }

    /**
     * Limpa todos os itens do carrinho.
     */
    public void limparCarrinho() {
        if (itens.isEmpty()) {
            System.out.println("ℹ️  Carrinho já estava vazio.");
            return;
        }
        itens.clear();
        cupomAplicado = null;
        System.out.println("✅ Carrinho limpo.");
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
     * Lança exceções se houver problemas.
     */
    public Pedido gerarPedido() throws RestauranteFechadoException, ValorMinimoException, ProdutoIndisponivelException {
        
        // Validações
        if (itens.isEmpty()) {
            throw new ValorMinimoException(
                "❌ Carrinho vazio. Adicione itens antes de fazer pedido."
            );
        }

        if (restaurante == null) {
            throw new RestauranteFechadoException(
                "❌ Nenhum restaurante selecionado."
            );
        }

        if (!restaurante.estaAberto()) {
            throw new RestauranteFechadoException(
                "❌ Restaurante " + restaurante.getNomeRestaurante() + " está fechado."
            );
        }

        double subtotal = calcularPrecoTotal();
        double valorMinimo = 15.0; // Valor mínimo padrão

        if (subtotal < valorMinimo) {
            throw new ValorMinimoException(
                "❌ Valor mínimo não atingido. Mínimo: R$" + valorMinimo + 
                " | Seu carrinho: R$" + String.format("%.2f", subtotal)
            );
        }

        // Verificar disponibilidade dos produtos
        for (ItemPedido item : itens) {
            if (!item.getProduto().isDisponivel()) {
                throw new ProdutoIndisponivelException(
                    "❌ Produto " + item.getProduto().getNome() + " não está disponível."
                );
            }
        }

        // Criar pedido
        Pedido pedido = new Pedido();
        for (ItemPedido item : itens) {
            pedido.adicionarItem(item);
        }
        
        if (cupomAplicado != null) {
            pedido.setCupomAplicado(cupomAplicado);
        }

        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.atualizarStatus("Pendente");

        System.out.println("\n✅ Pedido gerado com sucesso!");
        System.out.println("   Itens: " + itens.size());
        System.out.println("   Subtotal: R$" + String.format("%.2f", subtotal));
        if (cupomAplicado != null) {
            System.out.println("   Desconto: " + cupomAplicado.getDescricaoDesconto());
        }
        System.out.println("   Total: R$" + String.format("%.2f", pedido.calcularPrecoTotal()));

        return pedido;
    }

    /**
     * Aplica um cupom ao carrinho.
     */
    public void aplicarCupom(Cupom cupom) {
        if (cupom == null) {
            System.out.println("❌ Cupom inválido.");
            return;
        }
        if (!cupom.estaValido()) {
            System.out.println("❌ Cupom não está válido.");
            return;
        }
        this.cupomAplicado = cupom;
        System.out.println("✅ Cupom " + cupom.getCodigo() + " aplicado!");
        System.out.println("   Novo total: R$" + String.format("%.2f", calcularTotalComDesconto()));
    }

    /**
     * Remove o cupom do carrinho.
     */
    public void removerCupom() {
        this.cupomAplicado = null;
        System.out.println("✅ Cupom removido.");
    }

    // Getters
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

    // Setters
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n🛒 === CARRINHO ===\n");
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
        sb.append("==================\n");
        return sb.toString();
    }
}