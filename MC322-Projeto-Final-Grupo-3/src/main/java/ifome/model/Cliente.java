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

    public void adicionarEndereco(Endereco endereco) {
        if (endereco == null) {
            return;
        }
        if (enderecos.contains(endereco)) {
            return;
        }
        enderecos.add(endereco);
    }

    public void removerEndereco(Endereco endereco) {
        enderecos.remove(endereco);
    }

    public Pedido fazerPedido(Carrinho carrinhoParaPedido) 
            throws ifome.exceptions.RestauranteFechadoException,
                   ifome.exceptions.ValorMinimoException,
                   ifome.exceptions.ProdutoIndisponivelException,
                   ifome.exceptions.PagamentoRecusadoException {

        Carrinho carrinhoAtivo = (carrinhoParaPedido != null) ? carrinhoParaPedido : this.carrinho;

        if (carrinhoAtivo.getItens().isEmpty()) {
            throw new ifome.exceptions.ValorMinimoException(
                "Carrinho vazio."
            );
        }

        Pedido pedido = carrinhoAtivo.gerarPedido();
        pedido.setCliente(this);
        historicoPedidos.add(pedido);
        carrinhoAtivo.limparCarrinho();

        return pedido;
    }

    public void avaliarPedido(Pedido pedido, int nota, String comentario) {
        if (pedido == null) {
            return;
        }

        if (!historicoPedidos.contains(pedido)) {
            return;
        }

        if (!pedido.getStatus().equals("Entregue")) {
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

    public Endereco getEnderecoMaisRecente() {
        if (enderecos.isEmpty()) return null;
        return enderecos.get(enderecos.size() - 1);
    }

    public Pedido getPedidoMaisRecente() {
        if (historicoPedidos.isEmpty()) return null;
        return historicoPedidos.get(historicoPedidos.size() - 1);
    }

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