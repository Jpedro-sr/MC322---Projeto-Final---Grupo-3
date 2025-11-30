package ifome.util;

import ifome.model.Cliente;
import ifome.model.Restaurante;
import ifome.model.Usuario;

//gerencia a sessao do usuario
public class SessaoUsuario {
    
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;
    private Cliente clienteLogado;
    private Restaurante restauranteLogado;
    private Restaurante restauranteAtual; 
    private String tipoProdutoSelecionado;
    
    private SessaoUsuario() {
        this.usuarioLogado = null;
        this.clienteLogado = null;
        this.restauranteLogado = null;
        this.restauranteAtual = null;
        this.tipoProdutoSelecionado = null; 
    }

    
    public static SessaoUsuario getInstance() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }

    //define o clinete logado
    public void setClienteLogado(Cliente cliente) {
        this.clienteLogado = cliente;
        this.usuarioLogado = cliente;
        this.restauranteLogado = null;
        System.out.println(">>> Cliente logado: " + cliente.getNome());
    }

    // define o resaturante
    public void setRestauranteLogado(Restaurante restaurante) {
        this.restauranteLogado = restaurante;
        this.usuarioLogado = restaurante;
        this.clienteLogado = null;
        System.out.println(">>> Restaurante logado: " + restaurante.getNomeRestaurante());
    }

   
    public void setRestauranteAtual(Restaurante restaurante) {
        this.restauranteAtual = restaurante;
    }

 
    public void setTipoProdutoSelecionado(String tipo) {
        this.tipoProdutoSelecionado = tipo;
        System.out.println(">>> Tipo de produto selecionado: " + tipo);
    }


    public String getTipoProdutoSelecionado() {
        return this.tipoProdutoSelecionado;
    }

   
    public Cliente getClienteLogado() {
        return this.clienteLogado;
    }
    public Restaurante getRestauranteLogado() {
        return this.restauranteLogado;
    }

    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

   
    public Restaurante getRestauranteAtual() {
        return this.restauranteAtual;
    }

  
    public boolean estaLogado() {
        return this.usuarioLogado != null;
    }

   
    public boolean ehCliente() {
        return this.clienteLogado != null;
    }

  
    public boolean ehRestaurante() {
        return this.restauranteLogado != null;
    }


    public void logout() {
        if (usuarioLogado != null) {
            System.out.println(">>> Logout de: " + 
                (clienteLogado != null ? clienteLogado.getNome() : restauranteLogado.getNomeRestaurante()));
        }
        this.usuarioLogado = null;
        this.clienteLogado = null;
        this.restauranteLogado = null;
        this.restauranteAtual = null;
        this.tipoProdutoSelecionado = null;
    }

 
    public void limparSessao() {
        logout();
        instancia = null;
    }

    @Override
    public String toString() {
        if (!estaLogado()) {
            return "Nenhum usuario logado";
        }
        if (ehCliente()) {
            return "Cliente: " + clienteLogado.getNome();
        }
        return "Restaurante: " + restauranteLogado.getNomeRestaurante();
    }
}