package ifome.util;

import ifome.model.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * Singleton para gerenciar restaurantes cadastrados.
 * Carrega/salva restaurantes em arquivo.
 */
public class RepositorioRestaurantes {
    
    private static RepositorioRestaurantes instancia;
    private List<Restaurante> restaurantes;
    private List<Cliente> clientes;
    private static final String ARQUIVO_RESTAURANTES = "data/restaurantes.txt";
    private static final String ARQUIVO_CLIENTES = "data/clientes.txt";

    // Construtor privado (Singleton)
    private RepositorioRestaurantes() {
        this.restaurantes = new ArrayList<>();
        this.clientes = new ArrayList<>();
        new File("data").mkdirs();
        carregarDados();
    }

    /**
     * Retorna a instância única (Singleton).
     */
    public static RepositorioRestaurantes getInstance() {
        if (instancia == null) {
            instancia = new RepositorioRestaurantes();
        }
        return instancia;
    }

    /**
     * Inicializa restaurantes com dados de exemplo.
     */
    public void inicializarRestaurantes() {
        if (restaurantes.isEmpty()) {
            System.out.println(">>> Populando restaurantes iniciais...");

            // Pizzaria
            Restaurante pizzaria = new Restaurante(
                "pizzaria@ifome.com", "123", "Pizzaria Italiana", "12345678000199"
            );
            pizzaria.abrirRestaurante();
            pizzaria.adicionarProdutoCardapio(new Comida("Pizza Margherita", 
                "Molho de tomate, mussarela, manjericao", 45.90, true));
            pizzaria.adicionarProdutoCardapio(new Comida("Pizza Calabresa", 
                "Molho, mussarela, calabresa, cebola", 48.90, false));
            pizzaria.adicionarProdutoCardapio(new Bebida("Coca-Cola", 
                "Refrigerante 350ml", 6.00, 350));
            pizzaria.adicionarProdutoCardapio(new Sobremesa("Petit Gateau", 
                "Com sorvete de baunilha", 18.90));
            adicionarRestaurante(pizzaria);

            // Burger
            Restaurante burger = new Restaurante(
                "burger@ifome.com", "123", "Burger House", "98765432000188"
            );
            burger.abrirRestaurante();
            burger.adicionarProdutoCardapio(new Comida("X-Burger", 
                "Pao, carne, queijo, alface, tomate", 22.90, false));
            burger.adicionarProdutoCardapio(new Comida("X-Bacon", 
                "Pao, carne, bacon, queijo, alface", 26.90, false));
            burger.adicionarProdutoCardapio(new Bebida("Suco Natural", 
                "Laranja 500ml", 8.00, 500));
            adicionarRestaurante(burger);

            // Sushi
            Restaurante sushi = new Restaurante(
                "sushi@ifome.com", "123", "Sushi Master", "11122233000144"
            );
            sushi.abrirRestaurante();
            sushi.adicionarProdutoCardapio(new Comida("Combo Sashimi", 
                "12 pecas variadas", 65.90, false));
            sushi.adicionarProdutoCardapio(new Comida("Temaki Salmao", 
                "Temaki de salmao com cream cheese", 28.90, false));
            adicionarRestaurante(sushi);

            salvarDados();
            System.out.println(">>> " + restaurantes.size() + " restaurantes carregados!");
        }
    }

    /**
     * Adiciona um restaurante ao repositório.
     */
    public void adicionarRestaurante(Restaurante r) {
        if (r == null) return;
        if (!restaurantes.contains(r)) {
            restaurantes.add(r);
        }
    }

    /**
     * Adiciona um cliente ao repositório.
     */
    public void adicionarCliente(Cliente c) {
        if (c == null) return;
        if (!clientes.contains(c)) {
            clientes.add(c);
        }
    }

    /**
     * Busca restaurante por email e senha (login).
     */
    public Restaurante buscarRestaurantePorLogin(String email, String senha) {
        for (Restaurante r : restaurantes) {
            if (r.getEmail().equals(email) && r.login(email, senha)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Busca cliente por email e senha (login).
     */
    public Cliente buscarClientePorLogin(String email, String senha) {
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email) && c.login(email, senha)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Verifica se email já existe (cliente ou restaurante).
     */
    public boolean emailJaExiste(String email) {
        for (Restaurante r : restaurantes) {
            if (r.getEmail().equals(email)) return true;
        }
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)) return true;
        }
        return false;
    }

    /**
     * Retorna restaurante por índice.
     */
    public Restaurante obterPorIndice(int indice) {
        if (indice >= 0 && indice < restaurantes.size()) {
            return restaurantes.get(indice);
        }
        return null;
    }

    /**
     * Retorna todos os restaurantes.
     */
    public List<Restaurante> getTodosRestaurantes() {
        return new ArrayList<>(restaurantes);
    }

    /**
     * Retorna todos os clientes.
     */
    public List<Cliente> getTodosClientes() {
        return new ArrayList<>(clientes);
    }

    /**
     * Exibe lista de restaurantes formatada.
     */
    public void exibirLista() {
        if (restaurantes.isEmpty()) {
            System.out.println("Nenhum restaurante cadastrado.");
            return;
        }

        for (int i = 0; i < restaurantes.size(); i++) {
            Restaurante r = restaurantes.get(i);
            String status = r.estaAberto() ? "[ABERTO]" : "[FECHADO]";
            System.out.printf("%d. %s %s - Nota: %.1f (%d avaliacoes)\n", 
                i + 1, 
                status,
                r.getNomeRestaurante(), 
                r.calcularMediaAvaliacoes(),
                r.getQuantidadeAvaliacoes()
            );
        }
    }

    /**
     * Busca restaurantes por nome.
     */
    public List<Restaurante> buscarPorNome(String termo) {
        List<Restaurante> resultado = new ArrayList<>();
        String termoLower = termo.toLowerCase();
        
        for (Restaurante r : restaurantes) {
            if (r.getNomeRestaurante().toLowerCase().contains(termoLower)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    // ================ PERSISTÊNCIA ================

    /**
     * Carrega dados dos arquivos.
     */
    private void carregarDados() {
        carregarRestaurantes();
        carregarClientes();
    }

    private void carregarRestaurantes() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_RESTAURANTES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    Restaurante r = new Restaurante(dados[0], dados[1], dados[2], dados[3]);
                    restaurantes.add(r);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de restaurantes nao encontrado. Criando novo...");
        } catch (IOException e) {
            System.err.println("Erro ao carregar restaurantes: " + e.getMessage());
        }
    }

    private void carregarClientes() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_CLIENTES))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    Cliente c = new Cliente(dados[0], dados[1], dados[2], dados[3]);
                    clientes.add(c);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de clientes nao encontrado. Criando novo...");
        } catch (IOException e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    /**
     * Salva dados nos arquivos.
     */
    public void salvarDados() {
        salvarRestaurantes();
        salvarClientes();
    }

    private void salvarRestaurantes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_RESTAURANTES))) {
            for (Restaurante r : restaurantes) {
                pw.println(r.getEmail() + ";" + r.getSenha() + ";" + 
                          r.getNomeRestaurante() + ";" + r.getCNPJ());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar restaurantes: " + e.getMessage());
        }
    }

    private void salvarClientes() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO_CLIENTES))) {
            for (Cliente c : clientes) {
                pw.println(c.getEmail() + ";" + c.getSenha() + ";" + 
                          c.getNome() + ";" + c.getTelefone());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    /**
     * Limpa todos os dados (para testes).
     */
    public void limparTodos() {
        restaurantes.clear();
        clientes.clear();
    }

    public int getQuantidadeRestaurantes() {
        return restaurantes.size();
    }

    public int getQuantidadeClientes() {
        return clientes.size();
    }
}