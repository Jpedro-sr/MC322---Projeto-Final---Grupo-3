package ifome.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um Cliente do sistema iFome.
 * Herda de Usuario e gerencia seus endereços, pedidos e carrinho.
 */
public class Cliente extends Usuario {

    private String nome;
    private String telefone;
    private List<Endereco> enderecos;
    private List<Pedido> historicoPedidos;
    private Carrinho carrinho;

    // Construtor completo
    public Cliente(String email, String senha, String nome, String telefone) {
        super();
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        this.telefone = validarTelefone(telefone);
        this.enderecos = new ArrayList<>();
        this.historicoPedidos = new ArrayList<>();
        this.carrinho = new Carrinho();
    }

    // Validar formato de telefone
    private String validarTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) {
            return "Não informado";
        }
        String limpo = telefone.replaceAll("[^0-9]", "");
        if (limpo.length() >= 10) {
            return telefone;
        }
        System.out.println("⚠️ Telefone inválido: " + telefone);
        return "Não informado";
    }

    /**
     * Cadastra um novo cliente no sistema.
     * (Em produção, isso seria integrado com banco de dados)
     */
    public void cadastrar(String nome, String email, String senha, String telefone) {
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || 
            senha == null || senha.isEmpty()) {
            System.out.println("❌ Dados incompletos para cadastro.");
            return;
        }
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = validarTelefone(telefone);
        System.out.println("✅ Cliente " + nome + " cadastrado com sucesso!");
    }

    /**
     * Adiciona um novo endereço à lista de endereços do cliente.
     */
    public void adicionarEndereco(Endereco endereco) {
        if (endereco == null) {
            System.out.println("❌ Endereço inválido.");
            return;
        }
        if (enderecos.contains(endereco)) {
            System.out.println("ℹ️  Este endereço já foi cadastrado.");
            return;
        }
        enderecos.add(endereco);
        System.out.println("✅ Endereço adicionado: " + endereco.getEnderecoCompleto());
    }

    /**
     * Remove um endereço da lista.
     */
    public void removerEndereco(Endereco endereco) {
        if (enderecos.remove(endereco)) {
            System.out.println("✅ Endereço removido.");
        } else {
            System.out.println("❌ Endereço não encontrado.");
        }
    }

    /**
     * Faz um pedido a partir do carrinho.
     * Lança exceções apropriadas se houver problemas.
     */
    public Pedido fazerPedido(Carrinho carrinhoParaPedido) 
            throws ifome.exceptions.RestauranteFechadoException,
                   ifome.exceptions.ValorMinimoException,
                   ifome.exceptions.ProdutoIndisponivelException,
                   ifome.exceptions.PagamentoRecusadoException {

        // Se carrinho não foi especificado, usar carrinho do cliente
        Carrinho carrinhoAtivo = (carrinhoParaPedido != null) ? carrinhoParaPedido : this.carrinho;

        if (carrinhoAtivo.getItens().isEmpty()) {
            throw new ifome.exceptions.ValorMinimoException(
                "❌ Carrinho vazio. Adicione itens antes de fazer pedido."
            );
        }

        // Gerar pedido (valida restaurante, valor mínimo, disponibilidade)
        Pedido pedido = carrinhoAtivo.gerarPedido();

        // Associar cliente ao pedido
        pedido.setCliente(this);

        // Adicionar ao histórico
        historicoPedidos.add(pedido);

        // Limpar carrinho
        carrinhoAtivo.limparCarrinho();

        System.out.println("\n✅ Pedido #" + pedido.getNumeroPedido() + " criado com sucesso!");
        return pedido;
    }

    /**
     * Avalia um pedido do cliente.
     */
    public void avaliarPedido(Pedido pedido, int nota, String comentario) {
        if (pedido == null) {
            System.out.println("❌ Pedido inválido.");
            return;
        }

        if (!historicoPedidos.contains(pedido)) {
            System.out.println("❌ Este pedido não pertence a este cliente.");
            return;
        }

        if (!pedido.getStatus().equals("Entregue")) {
            System.out.println("❌ Apenas pedidos entregues podem ser avaliados.");
            return;
        }

        if (pedido.avaliar(nota, comentario)) {
            System.out.println("✅ Obrigado por avaliar! Sua avaliação foi registrada.");
        } else {
            System.out.println("❌ Erro ao registrar avaliação.");
        }
    }

    /**
     * Versão simplificada de avaliarPedido (apenas nota).
     */
    public void avaliarPedido(Pedido pedido, int nota) {
        avaliarPedido(pedido, nota, "");
    }

    /**
     * Retorna o carrinho do cliente para adição de itens.
     */
    public Carrinho getCarrinho() {
        return this.carrinho;
    }

    /**
     * Define o restaurante do carrinho.
     */
    public void selecionarRestaurante(Restaurante restaurante) {
        this.carrinho.setRestaurante(restaurante);
        this.carrinho.setCliente(this);
        System.out.println("✅ Restaurante selecionado: " + restaurante.getNomeRestaurante());
    }

    /**
     * Rastreia um pedido específico.
     */
    public void rastrearPedido(int numeroPedido) {
        for (Pedido pedido : historicoPedidos) {
            if (pedido.getNumeroPedido() == numeroPedido) {
                System.out.println("\n🚗 STATUS DE RASTREAMENTO:");
                System.out.println(pedido.gerarResumo());
                return;
            }
        }
        System.out.println("❌ Pedido #" + numeroPedido + " não encontrado.");
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public List<Endereco> getEnderecos() {
        return new ArrayList<>(enderecos);
    }

    public List<Pedido> getHistoricoPedidos() {
        return new ArrayList<>(historicoPedidos);
    }

    public int getQuantidadeEnderecos() {
        return enderecos.size();
    }

    public int getQuantidadePedidos() {
        return historicoPedidos.size();
    }

    /**
     * Retorna o último endereço cadastrado.
     */
    public Endereco getEnderecoMaisRecente() {
        if (enderecos.isEmpty()) return null;
        return enderecos.get(enderecos.size() - 1);
    }

    /**
     * Retorna o último pedido realizado.
     */
    public Pedido getPedidoMaisRecente() {
        if (historicoPedidos.isEmpty()) return null;
        return historicoPedidos.get(historicoPedidos.size() - 1);
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome != null ? nome : this.nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = validarTelefone(telefone);
    }

    @Override
    public String toString() {
        return String.format("Cliente: %s | Email: %s | Telefone: %s | Endereços: %d | Pedidos: %d",
            nome, email, telefone, enderecos.size(), historicoPedidos.size());
    }
}