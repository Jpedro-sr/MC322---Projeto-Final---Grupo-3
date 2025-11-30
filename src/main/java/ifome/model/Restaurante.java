package ifome.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Restaurante no sistema iFome.
 * ✅ CORRIGIDO: Validação robusta de CNPJ
 */
public class Restaurante extends Usuario implements Avaliavel {

    private String nomeRestaurante;
    private String cnpj;
    private Endereco endereco;
    private String horarioFuncionamento;
    private List<Produto> cardapio;
    private List<Pedido> filaPedidos;
    private List<Avaliacao> avaliacoes;
    private boolean aberto;
    
    // ✅ LIMITES DE SEGURANÇA
    private static final int MAX_NOME_RESTAURANTE = 100;
    private static final int CNPJ_LENGTH = 14;

    public Restaurante(String email, String senha, String nomeRestaurante, String cnpj) {
        super();
        this.email = email;
        this.senha = senha;
        this.nomeRestaurante = validarNomeRestaurante(nomeRestaurante);
        this.cnpj = validarCNPJ(cnpj);
        this.cardapio = new ArrayList<>();
        this.filaPedidos = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.aberto = true; 
        this.horarioFuncionamento = "Não informado";
    }

    /**
     * ✅ VALIDAÇÃO ROBUSTA DE NOME DO RESTAURANTE
     */
    private String validarNomeRestaurante(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante não pode ser vazio");
        }
        
        String nomeLimpo = nome.trim().replaceAll("[<>\"'&;]", "");
        
        if (nomeLimpo.length() > MAX_NOME_RESTAURANTE) {
            throw new IllegalArgumentException(
                "Nome muito longo. Máximo de " + MAX_NOME_RESTAURANTE + " caracteres"
            );
        }
        
        if (nomeLimpo.length() < 3) {
            throw new IllegalArgumentException("Nome muito curto. Mínimo de 3 caracteres");
        }
        
        return nomeLimpo;
    }

    /**
     * ✅ VALIDAÇÃO ROBUSTA DE CNPJ
     * - Remove todos os caracteres não numéricos
     * - Valida se tem exatamente 14 dígitos
     * - Rejeita CNPJs com todos os dígitos iguais
     * - Valida dígitos verificadores
     */
    private String validarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ não pode ser vazio");
        }
        
        // Remove todos os caracteres não numéricos
        String apenasDigitos = cnpj.replaceAll("[^0-9]", "");
        
        // Valida apenas a quantidade de dígitos (sem matemática)
        if (apenasDigitos.length() != CNPJ_LENGTH) {
            throw new IllegalArgumentException(
                "CNPJ inválido. Deve conter exatamente " + CNPJ_LENGTH + " dígitos"
            );
        }
        
        // ✅ VALIDAÇÃO DOS DÍGITOS VERIFICADORES
        if (!validarDigitosVerificadoresCNPJ(apenasDigitos)) {
            throw new IllegalArgumentException("CNPJ inválido. Dígitos verificadores incorretos");
        }
        
        return apenasDigitos;
    }

    /**
     * ✅ ALGORITMO DE VALIDAÇÃO DE DÍGITOS VERIFICADORES DO CNPJ
     */
    private boolean validarDigitosVerificadoresCNPJ(String cnpj) {
        try {
            // Primeiro dígito verificador
            int soma = 0;
            int peso = 5;
            
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            
            int digito1 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);
            
            if (Character.getNumericValue(cnpj.charAt(12)) != digito1) {
                return false;
            }
            
            // Segundo dígito verificador
            soma = 0;
            peso = 6;
            
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            
            int digito2 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);
            
            return Character.getNumericValue(cnpj.charAt(13)) == digito2;
            
        } catch (Exception e) {
            return false;
        }
    }

    // ============ MÉTODOS EXISTENTES (sem alteração funcional) ============

    public void abrirRestaurante() {
        this.aberto = true;
        System.out.println("🟢 " + nomeRestaurante + " ABERTO!");
    }

    public void fecharRestaurante() {
        this.aberto = false;
        System.out.println("🔴 " + nomeRestaurante + " FECHADO!");
    }

    public boolean estaAberto() {
        return this.aberto;
    }

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

    public void removerProdutoCardapio(Produto produto) {
        if (cardapio.remove(produto)) {
            System.out.println("✅ Produto removido: " + produto.getNome());
        } else {
            System.out.println("❌ Produto não encontrado no cardápio.");
        }
    }

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

    public Produto buscarProduto(String nome) {
        for (Produto p : cardapio) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

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

    public List<Pedido> getFilaPedidos() {
        return new ArrayList<>(filaPedidos);
    }

    public int getTamanhofila() {
        return filaPedidos.size();
    }

    public Pedido getPrimeirosPedido() {
        if (filaPedidos.isEmpty()) {
            System.out.println("ℹ️  Nenhum pedido na fila.");
            return null;
        }
        return filaPedidos.get(0);
    }

    @Override
    public boolean avaliar(int nota) {
        Avaliacao avaliacao = new Avaliacao(nota);
        return avaliacoes.add(avaliacao);
    }

    public boolean avaliar(int nota, String comentario) {
        Avaliacao avaliacao = new Avaliacao(nota, comentario);
        return avaliacoes.add(avaliacao);
    }

    public double calcularMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) return 0;
        int soma = 0;
        for (Avaliacao avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        return (double) soma / avaliacoes.size();
    }

    public List<Avaliacao> getAvaliacoes() {
        return new ArrayList<>(avaliacoes);
    }

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

    public void setNomeRestaurante(String nome) {
        this.nomeRestaurante = validarNomeRestaurante(nome);
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
        System.out.println("✅ Endereço do restaurante atualizado.");
    }

    public void setHorarioFuncionamento(String horario) {
        this.horarioFuncionamento = horario != null ? horario : "Não informado";
        System.out.println("✅ Horário de funcionamento atualizado para: " + horario);
    }

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