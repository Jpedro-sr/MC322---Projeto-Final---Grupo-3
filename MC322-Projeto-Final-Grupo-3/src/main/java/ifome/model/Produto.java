package ifome.model;

/**
 * Classe abstrata para um produto do cardápio.
 * Implementa Promocional para aplicar descontos.
 */
public abstract class Produto implements Promocional {

    protected String nome;
    protected String descricao;
    protected double preco;
    protected String categoria;
    protected boolean disponivel;

    // Construtor padrão
    public Produto() {
        this.disponivel = true;
    }

    // Getters
    public double getPreco() {
        return this.preco;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public boolean isDisponivel() {
        return this.disponivel;
    }

    // Setters
    public void setPreco(double preco) {
        if (preco > 0) {
            this.preco = preco;
        } else {
            System.out.println("❌ Preço deve ser positivo.");
        }
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao : "";
    }

    public void setDisponibilidade(boolean disponivel) {
        this.disponivel = disponivel;
        System.out.println("✅ Produto " + nome + " agora está " + 
                         (disponivel ? "DISPONÍVEL" : "INDISPONÍVEL"));
    }

    /**
     * Método da interface Promocional.
     * Aplica desconto percentual ao produto.
     */
    @Override
    public double aplicarDesconto(double percentual) {
        if (percentual < 0 || percentual > 100) {
            System.out.println("❌ Desconto percentual deve estar entre 0 e 100.");
            return this.preco;
        }
        double desconto = this.preco * (percentual / 100.0);
        this.preco -= desconto;
        System.out.println("✅ Desconto de " + percentual + "% aplicado a " + nome + 
                         ". Novo preço: R$" + String.format("%.2f", this.preco));
        return this.preco;
    }

    /**
     * Retorna o produto formatado para exibição.
     */
    @Override
    public String toString() {
        return String.format("%s - R$%.2f %s", 
            nome, 
            preco, 
            (disponivel ? "✓" : "✗ (indisponível)")
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return nome.equals(produto.nome) && categoria.equals(produto.categoria);
    }

    @Override
    public int hashCode() {
        return (nome + categoria).hashCode();
    }
}