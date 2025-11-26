package ifome.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Cliente do sistema iFome.
 * ATUALIZADO: Suporte a cartões de crédito salvos e cupons usados.
 */
public class Cliente extends Usuario {

    private String nome;
    private String telefone;
    private List<Endereco> enderecos;
    private List<Pedido> historicoPedidos;
    private List<CartaoSalvo> cartoesSalvos;
    private List<String> cuponsUsados; // ✅ NOVO: Lista de códigos de cupons já usados
    private Carrinho carrinho;

    public Cliente(String email, String senha, String nome, String telefone) {
        super();
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.telefone = validarTelefone(telefone);
        this.enderecos = new ArrayList<>();
        this.historicoPedidos = new ArrayList<>();
        this.cartoesSalvos = new ArrayList<>();
        this.cuponsUsados = new ArrayList<>(); // ✅ Inicializa lista
        this.carrinho = new Carrinho();
    }

    // ... (Mantenha os métodos existentes de validação e cadastro) ...

    private String validarTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return "Não informado";
        }
        String limpo = telefone.replaceAll("[^0-9]", "");
        if (limpo.length() >= 10) {
            return telefone;
        }
        return "Não informado";
    }

    public void cadastrar(String nome, String email, String senha, String telefone) {
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || 
            senha == null || senha.isEmpty()) {
            return;
        }
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = validarTelefone(telefone);
    }
    
    // ... (Métodos de Endereço e Cartão mantidos iguais) ...

    public void adicionarEndereco(Endereco endereco) {
        if (endereco == null) return;
        if (enderecos.contains(endereco)) return;
        enderecos.add(endereco);
    }

    public void removerEndereco(Endereco endereco) {
        enderecos.remove(endereco);
    }

    public void adicionarCartao(CartaoSalvo cartao) {
        if (cartao == null) return;
        if (cartoesSalvos.contains(cartao)) return;
        cartoesSalvos.add(cartao);
    }
    
    public void removerCartao(CartaoSalvo cartao) {
        cartoesSalvos.remove(cartao);
    }
    
    public CartaoSalvo buscarCartaoPorNumero(String numeroMascarado) {
        for (CartaoSalvo c : cartoesSalvos) {
            if (c.getNumeroMascarado().equals(numeroMascarado)) return c;
        }
        return null;
    }
    
    public List<CartaoSalvo> getCartoesSalvos() {
        return new ArrayList<>(cartoesSalvos);
    }
    
    public boolean temCartoesSalvos() {
        return !cartoesSalvos.isEmpty();
    }

    // ============ ✅ NOVOS MÉTODOS DE CUPONS USADOS ============

    /**
     * Verifica se o cliente já usou um cupom específico.
     */
    public boolean jaUsouCupom(String codigoCupom) {
        if (codigoCupom == null) return false;
        return cuponsUsados.contains(codigoCupom.toUpperCase());
    }

    /**
     * Registra que o cliente usou um cupom.
     */
    public void registrarUsoCupom(String codigoCupom) {
        if (codigoCupom != null && !jaUsouCupom(codigoCupom)) {
            cuponsUsados.add(codigoCupom.toUpperCase());
        }
    }
    
    /**
     * Retorna a lista de cupons usados (para persistência).
     */
    public List<String> getCuponsUsados() {
        return new ArrayList<>(cuponsUsados);
    }

    // ============ MÉTODOS EXISTENTES (Pedido, Avaliação, etc) ============

    public void adicionarPedido(Pedido pedido) {
        if (pedido != null) {
            this.historicoPedidos.add(pedido);
        }
    }

    public Pedido fazerPedido(Carrinho carrinhoParaPedido) 
            throws ifome.exceptions.RestauranteFechadoException,
                   ifome.exceptions.ValorMinimoException,
                   ifome.exceptions.ProdutoIndisponivelException,
                   ifome.exceptions.PagamentoRecusadoException {

        Carrinho carrinhoAtivo = (carrinhoParaPedido != null) ? carrinhoParaPedido : this.carrinho;

        if (carrinhoAtivo.getItens().isEmpty()) {
            throw new ifome.exceptions.ValorMinimoException("Carrinho vazio.");
        }

        Pedido pedido = carrinhoAtivo.gerarPedido();
        pedido.setCliente(this);
        historicoPedidos.add(pedido);
        carrinhoAtivo.limparCarrinho();

        return pedido;
    }

    public void avaliarPedido(Pedido pedido, int nota, String comentario) {
        if (pedido == null || !historicoPedidos.contains(pedido) || !pedido.getStatus().equals("Entregue")) {
            return;
        }
        pedido.avaliar(nota, comentario);
    }

    public void avaliarPedido(Pedido pedido, int nota) {
        avaliarPedido(pedido, nota, "");
    }

    public Carrinho getCarrinho() {
        return this.carrinho;
    }

    public void selecionarRestaurante(Restaurante restaurante) {
        this.carrinho.setRestaurante(restaurante);
        this.carrinho.setCliente(this);
    }

    public void rastrearPedido(int numeroPedido) {
        for (Pedido pedido : historicoPedidos) {
            if (pedido.getNumeroPedido() == numeroPedido) {
                System.out.println(pedido.gerarResumo());
                return;
            }
        }
    }

    // Getters e Setters básicos
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public List<Endereco> getEnderecos() { return new ArrayList<>(enderecos); }
    public List<Pedido> getHistoricoPedidos() { return new ArrayList<>(historicoPedidos); }
    public int getQuantidadeEnderecos() { return enderecos.size(); }
    public int getQuantidadePedidos() { return historicoPedidos.size(); }
    
    public Endereco getEnderecoMaisRecente() {
        return enderecos.isEmpty() ? null : enderecos.get(enderecos.size() - 1);
    }

    public Pedido getPedidoMaisRecente() {
        return historicoPedidos.isEmpty() ? null : historicoPedidos.get(historicoPedidos.size() - 1);
    }

    public void setNome(String nome) { this.nome = nome != null ? nome : this.nome; }
    public void setTelefone(String telefone) { this.telefone = validarTelefone(telefone); }

    @Override
    public String toString() {
        return String.format("Cliente: %s | Email: %s", nome, email);
    }
}