package ifome.util;

import ifome.model.Cliente;
import ifome.model.Restaurante;
import ifome.model.Usuario;

/**
 * Singleton para gerenciar a sessão do usuário logado.
 * Mantém apenas UM usuário logado por vez (Cliente OU Restaurante).
 */
public class SessaoUsuario {
    
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;
    private Cliente clienteLogado;
    private Restaurante restauranteLogado;
    private Restaurante restauranteAtual; // Restaurante sendo navegado pelo cliente

    // Construtor privado (Singleton)
    private SessaoUsuario() {
        this.usuarioLogado = null;
        this.clienteLogado = null;
        this.restauranteLogado = null;
        this.restauranteAtual = null;
    }

    /**
     * Retorna a instância única (Singleton).
     */
    public static SessaoUsuario getInstance() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }

    /**
     * Define o cliente logado.
     */
    public void setClienteLogado(Cliente cliente) {
        this.clienteLogado = cliente;
        this.usuarioLogado = cliente;
        this.restauranteLogado = null;
        System.out.println(">>> Cliente logado: " + cliente.getNome());
    }

    /**
     * Define o restaurante logado.
     */
    public void setRestauranteLogado(Restaurante restaurante) {
        this.restauranteLogado = restaurante;
        this.usuarioLogado = restaurante;
        this.clienteLogado = null;
        System.out.println(">>> Restaurante logado: " + restaurante.getNomeRestaurante());
    }

    /**
     * Define o restaurante sendo navegado pelo cliente.
     */
    public void setRestauranteAtual(Restaurante restaurante) {
        this.restauranteAtual = restaurante;
    }

    /**
     * Retorna o cliente logado (ou null).
     */
    public Cliente getClienteLogado() {
        return this.clienteLogado;
    }

    /**
     * Retorna o restaurante logado (ou null).
     */
    public Restaurante getRestauranteLogado() {
        return this.restauranteLogado;
    }

    /**
     * Retorna o usuário logado genérico.
     */
    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    /**
     * Retorna o restaurante atual sendo navegado.
     */
    public Restaurante getRestauranteAtual() {
        return this.restauranteAtual;
    }

    /**
     * Verifica se há alguém logado.
     */
    public boolean estaLogado() {
        return this.usuarioLogado != null;
    }

    /**
     * Verifica se é um cliente logado.
     */
    public boolean ehCliente() {
        return this.clienteLogado != null;
    }

    /**
     * Verifica se é um restaurante logado.
     */
    public boolean ehRestaurante() {
        return this.restauranteLogado != null;
    }

    /**
     * Faz logout do usuário atual.
     */
    public void logout() {
        if (usuarioLogado != null) {
            System.out.println(">>> Logout de: " + 
                (clienteLogado != null ? clienteLogado.getNome() : restauranteLogado.getNomeRestaurante()));
        }
        this.usuarioLogado = null;
        this.clienteLogado = null;
        this.restauranteLogado = null;
        this.restauranteAtual = null;
    }

    /**
     * Limpa toda a sessão (para testes).
     */
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