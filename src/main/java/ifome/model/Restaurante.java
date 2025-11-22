package ifome.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Restaurante no sistema iFome.
 * Herda de Usuario e gerencia cardápio, pedidos e avaliações.
 */
public class Restaurante extends Usuario implements Avaliavel {

    private String nomeRestaurante;
    private String cnpj;
    private Endereco endereco;
    private String horarioFuncionamento; // Ex: "11:00 - 23:00"
    private List<Produto> cardapio;
    private List<Pedido> filaPedidos;
    private List<Avaliacao> avaliacoes;
    private boolean aberto;

    // Construtor completo
    public Restaurante(String email, String senha, String nomeRestaurante, String cnpj) {
        super();
        this.email = email;
        this.senha = senha;
        this.nomeRestaurante = nomeRestaurante;
        this.cnpj = validarCNPJ(cnpj) ? cnpj : "00.000.000/0000-00";
        this.cardapio = new ArrayList<>();
        this.filaPedidos = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.aberto = true; 
        this.horarioFuncionamento = "Não informado";
    }

    // Validar CNPJ (formato simplificado)
    private boolean validarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) return false;
        String limpo = cnpj.replaceAll("[^0-9]", "");
        return limpo.length() == 14;
    }

    /**
     * Abre o restaurante (começa a aceitar pedidos).
     */
    public void abrirRestaurante() {
        this.aberto = true;
        System.out.println("🟢 " + nomeRestaurante + " ABERTO!");
    }

    /**
     * Fecha o restaurante (para de aceitar pedidos).
     */
    public void fecharRestaurante() {
        this.aberto = false;
        System.out.println("🔴 " + nomeRestaurante + " FECHADO!");
    }

    /**
     * Verifica se o restaurante está aberto.
     * IMPORTANTE: Este método é OBRIGATÓRIO pelo UML original.
     */
    public boolean estaAberto() {
        return this.aberto;
    }

    /**
     * Adiciona um novo produto ao cardápio.
     */
    public void adicionarProdutoCardapio(Produto produto) {
        if (produto == null) {
            System.out.println("❌ Produto inválido.");
            return;
        }
        if (cardapio.contains(produto)) {
            System.out.println("ℹ️  Produto " + produto.getNome() + " já existe no cardápio.");
            return;
        }
        cardapio.add(produto);
        System.out.println("✅ Produto adicionado: " + produto.getNome() + 
                         " - R$" + String.format("%.2f", produto.getPreco()));
    }

    /**
     * Remove um produto do cardápio.
     */
    public void removerProdutoCardapio(Produto produto) {
        if (cardapio.remove(produto)) {
            System.out.println("✅ Produto removido: " + produto.getNome());
        } else {
            System.out.println("❌ Produto não encontrado no cardápio.");
        }
    }

    /**
     * Atualiza informações de um produto (preço, descrição, disponibilidade).
     */
    public void atualizarProdutoCardapio(Produto produto) {
        if (produto == null) {
            System.out.println("❌ Produto inválido.");
            return;
        }

        for (Produto p : cardapio) {
            if (p.equals(produto)) {
                p.setPreco(produto.getPreco());
                p.setDescricao(produto.getDescricao());
                p.setDisponibilidade(produto.isDisponivel());
                System.out.println("✅ Produto atualizado: " + produto.getNome());
                return;
            }
        }
        System.out.println("❌ Produto não encontrado no cardápio.");
    }

    /**
     * Busca um produto no cardápio pelo nome.
     */
    public Produto buscarProduto(String nome) {
        for (Produto p : cardapio) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Aceita um pedido (adiciona à fila de preparação).
     */
    public void aceitarPedido(Pedido pedido) 
            throws ifome.exceptions.RestauranteFechadoException {
        if (pedido == null) {
            System.out.println("❌ Pedido inválido.");
            return;
        }

        if (!aberto) {
            throw new ifome.exceptions.RestauranteFechadoException(
                "❌ Restaurante fechado. Não é possível aceitar pedidos."
            );
        }

        if (filaPedidos.contains(pedido)) {
            System.out.println("ℹ️  Pedido #" + pedido.getNumeroPedido() + " já está na fila.");
            return;
        }

        filaPedidos.add(pedido);
        pedido.atualizarStatus("Confirmado");
        System.out.println("✅ Pedido #" + pedido.getNumeroPedido() + " aceito e adicionado à fila!");
    }

    /**
     * Recusa um pedido (remove da fila).
     */
    public void recusarPedido(Pedido pedido) 
            throws ifome.exceptions.RestauranteFechadoException {
        if (pedido == null) {
            System.out.println("❌ Pedido inválido.");
            return;
        }

        if (filaPedidos.remove(pedido)) {
            pedido.atualizarStatus("Cancelado");
            System.out.println("❌ Pedido #" + pedido.getNumeroPedido() + " recusado!");
        } else {
            System.out.println("❌ Pedido não encontrado na fila.");
        }
    }

    /**
     * Atualiza o status de um pedido (ex: Preparando → Pronto).
     */
    public void atualizarStatusPedido(Pedido pedido, String novoStatus) {
        if (pedido == null) {
            System.out.println("❌ Pedido inválido.");
            return;
        }

        if (!filaPedidos.contains(pedido)) {
            System.out.println("❌ Pedido não está na fila deste restaurante.");
            return;
        }

        pedido.atualizarStatus(novoStatus);
    }

    /**
     * Retorna a fila de pedidos do restaurante.
     */
    public List<Pedido> getFilaPedidos() {
        return new ArrayList<>(filaPedidos);
    }

    /**
     * Conta quantos pedidos estão na fila.
     */
    public int getTamanhofila() {
        return filaPedidos.size();
    }

    /**
     * Retorna o próximo pedido da fila (sem remover).
     */
    public Pedido getPrimeirosPedido() {
        if (filaPedidos.isEmpty()) {
            System.out.println("ℹ️  Nenhum pedido na fila.");
            return null;
        }
        return filaPedidos.get(0);
    }

    // ============ MÉTODOS DA INTERFACE AVALIAVEL ============

    /**
     * Avalia o restaurante (nota de 1-5).
     * Implementação obrigatória da interface Avaliavel.
     */
    @Override
    public boolean avaliar(int nota) {
        Avaliacao avaliacao = new Avaliacao(nota);
        return avaliacoes.add(avaliacao);
    }

    /**
     * Avalia o restaurante com nota e comentário.
     */
    public boolean avaliar(int nota, String comentario) {
        Avaliacao avaliacao = new Avaliacao(nota, comentario);
        return avaliacoes.add(avaliacao);
    }

    /**
     * Calcula a média de avaliações do restaurante.
     */
    public double calcularMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) return 0;
        int soma = 0;
        for (Avaliacao avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        return (double) soma / avaliacoes.size();
    }

    /**
     * Retorna lista de avaliações.
     */
    public List<Avaliacao> getAvaliacoes() {
        return new ArrayList<>(avaliacoes);
    }

    // Getters
    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public String getCNPJ() {
        return cnpj;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public List<Produto> getCardapio() {
        return new ArrayList<>(cardapio);
    }

    public int getQuantidadeProdutos() {
        return cardapio.size();
    }

    public int getQuantidadeAvaliacoes() {
        return avaliacoes.size();
    }

    // Setters
    public void setNomeRestaurante(String nome) {
        this.nomeRestaurante = nome != null ? nome : this.nomeRestaurante;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        System.out.println("✅ Endereço do restaurante atualizado.");
    }

    public void setHorarioFuncionamento(String horario) {
        this.horarioFuncionamento = horario != null ? horario : "Não informado";
        System.out.println("✅ Horário de funcionamento atualizado para: " + horario);
    }

    /**
     * Retorna informações formatadas do restaurante.
     */
    public String getInfoRestaurante() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════╗\n");
        sb.append("║    INFORMAÇÕES DO RESTAURANTE          ║\n");
        sb.append("╚════════════════════════════════════════╝\n\n");
        sb.append("🏪 Nome: ").append(nomeRestaurante).append("\n");
        sb.append("📝 CNPJ: ").append(cnpj).append("\n");
        sb.append("🕐 Horário: ").append(horarioFuncionamento).append("\n");
        sb.append("📊 Status: ").append(aberto ? "🟢 ABERTO" : "🔴 FECHADO").append("\n");
        sb.append("📋 Produtos: ").append(cardapio.size()).append("\n");
        sb.append("📦 Pedidos na fila: ").append(filaPedidos.size()).append("\n");
        sb.append("⭐ Avaliação: ").append(String.format("%.1f/5.0", calcularMediaAvaliacoes()))
          .append(" (").append(avaliacoes.size()).append(" avaliações)\n");
        return sb.toString();
    }

    /**
     * Exibe o cardápio formatado.
     */
    public void exibirCardapio() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  CARDÁPIO - " + nomeRestaurante);
        System.out.println("╚════════════════════════════════════════╝\n");

        if (cardapio.isEmpty()) {
            System.out.println("(cardápio vazio)\n");
            return;
        }

        for (int i = 0; i < cardapio.size(); i++) {
            Produto p = cardapio.get(i);
            System.out.printf("%d. %s - R$%.2f %s\n",
                i + 1,
                p.getNome(),
                p.getPreco(),
                (p.isDisponivel() ? "✓" : "✗ (indisponível)")
            );
            System.out.println("   " + p.getDescricao() + "\n");
        }
    }

    @Override
    public String toString() {
        return String.format("Restaurante: %s | CNPJ: %s | Produtos: %d | Status: %s",
            nomeRestaurante, cnpj, cardapio.size(), aberto ? "Aberto" : "Fechado");
    }
}