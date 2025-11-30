package ifome.model;

import java.util.ArrayList;
import java.util.List;

//representa o cliente
public class Cliente extends Usuario {

    private String nome;
    private String telefone;
    private List<Endereco> enderecos;
    private List<Pedido> historicoPedidos;
    private List<CartaoSalvo> cartoesSalvos;
    private List<String> cuponsUsados;
    private Carrinho carrinho;
    //limites
    private static final int MAX_NOME_LENGTH = 100;
    private static final int MIN_NOME_LENGTH = 2;
    private static final int MIN_TELEFONE_DIGITOS = 10;
    private static final int MAX_TELEFONE_DIGITOS = 15;

    public Cliente(String email, String senha, String nome, String telefone) {
        super();
        this.email = email;
        this.senha = senha;
        this.nome = validarNome(nome);
        this.telefone = validarTelefone(telefone);
        this.enderecos = new ArrayList<>();
        this.historicoPedidos = new ArrayList<>();
        this.cartoesSalvos = new ArrayList<>();
        this.cuponsUsados = new ArrayList<>();
        this.carrinho = new Carrinho();
    }

    /**
     *attualizacao
     * remove caracteres perigosos
     * valida tamanho mínimo e máximo
     */
    private String validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
       
        String nomeLimpo = nome.trim().replaceAll("[<>\"'&;]", "");
        
        if (nomeLimpo.length() > MAX_NOME_LENGTH) {
            throw new IllegalArgumentException(
                "Nome muito longo. Máximo de " + MAX_NOME_LENGTH + " caracteres"
            );
        }
        
        if (nomeLimpo.length() < MIN_NOME_LENGTH) {
            throw new IllegalArgumentException(
                "Nome muito curto. Mínimo de " + MIN_NOME_LENGTH + " caracteres"
            );
        }
        
        return nomeLimpo;
    }

    //mesma validacao de seguranca
    private String validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return "Não informado";
        }

        String apenasDigitos = telefone.replaceAll("[^0-9]", "");
        
        if (apenasDigitos.isEmpty()) {
            return "Não informado";
        }
        

        if (apenasDigitos.length() < MIN_TELEFONE_DIGITOS) {
            throw new IllegalArgumentException(
                "Telefone inválido. Deve ter entre " + MIN_TELEFONE_DIGITOS + 
                " e " + MAX_TELEFONE_DIGITOS + " dígitos"
            );
        }
        
        if (apenasDigitos.length() > MAX_TELEFONE_DIGITOS) {
            throw new IllegalArgumentException(
                "Telefone inválido. Deve ter entre " + MIN_TELEFONE_DIGITOS + 
                " e " + MAX_TELEFONE_DIGITOS + " dígitos"
            );
        }
        
        return telefone; 
    }

    // ----------------------------------------------------------
    // metodos existentes

    public void cadastrar(String nome, String email, String senha, String telefone) {
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || 
            senha == null || senha.isEmpty()) {
            return;
        }
        this.nome = validarNome(nome);
        this.email = email;
        this.senha = senha;
        this.telefone = validarTelefone(telefone);
    }

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

    public boolean jaUsouCupom(String codigoCupom) {
        if (codigoCupom == null) return false;
        return cuponsUsados.contains(codigoCupom.toUpperCase());
    }

    public void registrarUsoCupom(String codigoCupom) {
        if (codigoCupom != null && !jaUsouCupom(codigoCupom)) {
            cuponsUsados.add(codigoCupom.toUpperCase());
        }
    }
    
    public List<String> getCuponsUsados() {
        return new ArrayList<>(cuponsUsados);
    }

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

    // geters e setters
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

    public void setNome(String nome) { 
        this.nome = validarNome(nome);
    }
    
    public void setTelefone(String telefone) { 
        this.telefone = validarTelefone(telefone);
    }

    @Override
    public String toString() {
        return String.format("Cliente: %s | Email: %s", nome, email);
    }
}