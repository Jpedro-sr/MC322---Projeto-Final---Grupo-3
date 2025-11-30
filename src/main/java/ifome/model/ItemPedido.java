package ifome.model;

// idem do pedido ou do carrinho
public class ItemPedido implements Calculavel {

    private int quantidade;
    private String observacoes;
    private double precoUnitario;
    private Produto produto;

    public ItemPedido(Produto produto, int quantidade, String observacoes) {
        this.produto = produto;
        this.quantidade = quantidade > 0 ? quantidade : 1;
        this.observacoes = observacoes != null ? observacoes : "";
        this.precoUnitario = produto.getPreco();
    }

   
    public ItemPedido(Produto produto, int quantidade) {
        this(produto, quantidade, "");
    }

    //calculo do subtotal
    @Override
    public double calcularPrecoTotal() {
        return this.precoUnitario * this.quantidade;
    }


    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade > 0) {
            this.quantidade = quantidade;
        } else {
            System.out.println("❌ Quantidade deve ser maior que 0.");
        }
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes != null ? observacoes : "";
    }

 //retorna o fromato
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(produto.getNome()).append(" x").append(quantidade);
        sb.append(" = R$").append(String.format("%.2f", calcularPrecoTotal()));
        if (!observacoes.isEmpty()) {
            sb.append(" (").append(observacoes).append(")");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemPedido item = (ItemPedido) obj;
        return produto.equals(item.produto) && quantidade == item.quantidade;
    }

    @Override
    public int hashCode() {
        return (produto.getNome() + quantidade).hashCode();
    }
}