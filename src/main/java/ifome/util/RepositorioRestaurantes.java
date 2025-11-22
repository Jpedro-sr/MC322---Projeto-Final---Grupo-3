package ifome.util;

import ifome.model.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Singleton para gerenciar restaurantes cadastrados.
 * VERSÃO CORRIGIDA - Persiste endereços e pedidos
 */
public class RepositorioRestaurantes {
    
    private static RepositorioRestaurantes instancia;
    private List<Restaurante> restaurantes;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;
    private List<Cupom> cupons;
    private static final String ARQUIVO_RESTAURANTES = "data/restaurantes.txt";
    private static final String ARQUIVO_CLIENTES = "data/clientes.txt";
    private static final String ARQUIVO_ENDERECOS = "data/enderecos.txt";
    private static final String ARQUIVO_PEDIDOS = "data/pedidos.txt";
    private static final String ARQUIVO_CUPONS = "data/cupons.txt";

    private RepositorioRestaurantes() {
        this.restaurantes = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.cupons = new ArrayList<>();
        new File("data").mkdirs();
        carregarDados();
        inicializarCupons();
    }

    public static RepositorioRestaurantes getInstance() {
        if (instancia == null) {
            instancia = new RepositorioRestaurantes();
        }
        return instancia;
    }

    public void inicializarRestaurantes() {
        // Sempre popula os produtos mesmo se os restaurantes já existem
        if (!restaurantes.isEmpty()) {
            // Adiciona produtos aos restaurantes existentes se estiverem vazios
            for (Restaurante r : restaurantes) {
                if (!r.estaAberto()) {
                    r.abrirRestaurante();
                }
                
                // Se o cardápio está vazio, adiciona produtos padrão
                if (r.getCardapio().isEmpty()) {
                    if (r.getNomeRestaurante().contains("Pizzaria")) {
                        r.adicionarProdutoCardapio(new Comida("Pizza Margherita", 
                            "Molho de tomate, mussarela, manjericao", 45.90, true));
                        r.adicionarProdutoCardapio(new Comida("Pizza Calabresa", 
                            "Molho, mussarela, calabresa, cebola", 48.90, false));
                        r.adicionarProdutoCardapio(new Bebida("Coca-Cola", 
                            "Refrigerante 350ml", 6.00, 350));
                        r.adicionarProdutoCardapio(new Sobremesa("Petit Gateau", 
                            "Com sorvete de baunilha", 18.90));
                    } else if (r.getNomeRestaurante().contains("Burger")) {
                        r.adicionarProdutoCardapio(new Comida("X-Burger", 
                            "Pao, carne, queijo, alface, tomate", 22.90, false));
                        r.adicionarProdutoCardapio(new Comida("X-Bacon", 
                            "Pao, carne, bacon, queijo, alface", 26.90, false));
                        r.adicionarProdutoCardapio(new Bebida("Suco Natural", 
                            "Laranja 500ml", 8.00, 500));
                    } else if (r.getNomeRestaurante().contains("Sushi")) {
                        r.adicionarProdutoCardapio(new Comida("Combo Sashimi", 
                            "12 pecas variadas", 65.90, false));
                        r.adicionarProdutoCardapio(new Comida("Temaki Salmao", 
                            "Temaki de salmao com cream cheese", 28.90, false));
                    }
                }
            }
            return;
        }
        
        System.out.println(">>> Populando restaurantes iniciais...");

        // Pizzaria - ABERTO por padrão
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

        // Burger - ABERTO por padrão
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

        // Sushi - ABERTO por padrão
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

    public void adicionarRestaurante(Restaurante r) {
        if (r == null) return;
        if (!restaurantes.contains(r)) {
            restaurantes.add(r);
        }
    }

    public void adicionarCliente(Cliente c) {
        if (c == null) return;
        if (!clientes.contains(c)) {
            clientes.add(c);
        }
    }

    public Restaurante buscarRestaurantePorLogin(String email, String senha) {
        for (Restaurante r : restaurantes) {
            if (r.getEmail().equals(email) && r.login(email, senha)) {
                return r;
            }
        }
        return null;
    }

    public Cliente buscarClientePorLogin(String email, String senha) {
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email) && c.login(email, senha)) {
                return c;
            }
        }
        return null;
    }

    public boolean emailJaExiste(String email) {
        for (Restaurante r : restaurantes) {
            if (r.getEmail().equals(email)) return true;
        }
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)) return true;
        }
        return false;
    }

    public Restaurante obterPorIndice(int indice) {
        if (indice >= 0 && indice < restaurantes.size()) {
            return restaurantes.get(indice);
        }
        return null;
    }

    public List<Restaurante> getTodosRestaurantes() {
        return new ArrayList<>(restaurantes);
    }

    public List<Cliente> getTodosClientes() {
        return new ArrayList<>(clientes);
    }

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

    // ================ PERSISTÊNCIA CORRIGIDA ================

    private void carregarDados() {
        carregarRestaurantes();
        carregarClientes();
        carregarEnderecos();
        carregarPedidos();
        carregarCupons();
    }

    private void carregarRestaurantes() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_RESTAURANTES), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    Restaurante r = new Restaurante(dados[0], dados[1], dados[2], dados[3]);
                    r.abrirRestaurante(); // Todos abertos por padrão
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
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_CLIENTES), StandardCharsets.UTF_8))) {
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

    private void carregarEnderecos() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_ENDERECOS), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 7) {
                    String emailCliente = dados[0];
                    Cliente cliente = buscarClientePorEmail(emailCliente);
                    if (cliente != null) {
                        Endereco end = new Endereco(dados[1], dados[2], dados[3], dados[4], dados[5], dados[6]);
                        cliente.adicionarEndereco(end);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe ainda
        } catch (IOException e) {
            System.err.println("Erro ao carregar enderecos: " + e.getMessage());
        }
    }

    private void carregarPedidos() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_PEDIDOS), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 5) {
                    int numeroPedido = Integer.parseInt(dados[0]);
                    String emailCliente = dados[1];
                    String emailRestaurante = dados[2];
                    String status = dados[3];
                    double valorTotal = Double.parseDouble(dados[4]);
                    
                    Cliente cliente = buscarClientePorEmail(emailCliente);
                    Restaurante restaurante = buscarRestaurantePorEmail(emailRestaurante);
                    
                    if (cliente != null && restaurante != null) {
                        Pedido p = new Pedido();
                        p.setCliente(cliente);
                        p.setRestaurante(restaurante);
                        p.atualizarStatus(status);
                        
                        cliente.adicionarPedido(p);
                        if (!status.equals("Entregue") && !status.equals("Cancelado")) {
                            try {
                                restaurante.aceitarPedido(p);
                            } catch (Exception e) {
                                // Restaurante pode estar fechado
                            }
                        }
                        pedidos.add(p);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe ainda
        } catch (IOException e) {
            System.err.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void carregarCupons() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_CUPONS), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 3) {
                    String codigo = dados[0];
                    double desconto = Double.parseDouble(dados[1]);
                    boolean ehPercentual = Boolean.parseBoolean(dados[2]);
                    
                    Cupom cupom = new Cupom(codigo, desconto, ehPercentual);
                    cupons.add(cupom);
                }
            }
        } catch (FileNotFoundException e) {
            // Arquivo não existe ainda
        } catch (IOException e) {
            System.err.println("Erro ao carregar cupons: " + e.getMessage());
        }
    }

    private Cliente buscarClientePorEmail(String email) {
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        return null;
    }

    private Restaurante buscarRestaurantePorEmail(String email) {
        for (Restaurante r : restaurantes) {
            if (r.getEmail().equals(email)) {
                return r;
            }
        }
        return null;
    }

    public void salvarDados() {
        salvarRestaurantes();
        salvarClientes();
        salvarEnderecos();
        salvarPedidos();
        salvarCupons();
    }

    private void salvarRestaurantes() {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_RESTAURANTES), StandardCharsets.UTF_8))) {
            for (Restaurante r : restaurantes) {
                pw.println(r.getEmail() + ";" + r.getSenha() + ";" + 
                          r.getNomeRestaurante() + ";" + r.getCNPJ());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar restaurantes: " + e.getMessage());
        }
    }

    private void salvarClientes() {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_CLIENTES), StandardCharsets.UTF_8))) {
            for (Cliente c : clientes) {
                pw.println(c.getEmail() + ";" + c.getSenha() + ";" + 
                          c.getNome() + ";" + c.getTelefone());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    private void salvarEnderecos() {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_ENDERECOS), StandardCharsets.UTF_8))) {
            for (Cliente c : clientes) {
                for (Endereco end : c.getEnderecos()) {
                    pw.println(c.getEmail() + ";" + end.getCep() + ";" + end.getRua() + ";" +
                              end.getNumero() + ";" + end.getBairro() + ";" + 
                              end.getCidade() + ";" + end.getEstado());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar enderecos: " + e.getMessage());
        }
    }

    private void salvarPedidos() {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_PEDIDOS), StandardCharsets.UTF_8))) {
            for (Pedido p : pedidos) {
                if (p.getCliente() != null && p.getRestaurante() != null) {
                    pw.println(p.getNumeroPedido() + ";" + 
                              p.getCliente().getEmail() + ";" +
                              p.getRestaurante().getEmail() + ";" +
                              p.getStatus() + ";" +
                              p.getValorTotal());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }

    private void salvarCupons() {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_CUPONS), StandardCharsets.UTF_8))) {
            for (Cupom c : cupons) {
                pw.println(c.getCodigo() + ";" + c.getValorDesconto() + ";" + c.isPercentual());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar cupons: " + e.getMessage());
        }
    }

    public void limparTodos() {
        restaurantes.clear();
        clientes.clear();
        pedidos.clear();
        cupons.clear();
    }

    public int getQuantidadeRestaurantes() {
        return restaurantes.size();
    }

    public int getQuantidadeClientes() {
        return clientes.size();
    }
    
    // ================ NOVOS MÉTODOS ================
    
    /**
     * Inicializa cupons de desconto padrão do sistema
     */
    private void inicializarCupons() {
        if (cupons.isEmpty()) {
            // Cupons percentuais
            cupons.add(Cupom.criarCupomPercentual("BEMVINDO10", 10));
            cupons.add(Cupom.criarCupomPercentual("DESCONTO15", 15));
            cupons.add(Cupom.criarCupomPercentual("PRIMEIRACOMPRA", 20));
            
            // Cupons de valor fixo
            cupons.add(Cupom.criarCupomFixo("ECONOMIZE5", 5.00));
            cupons.add(Cupom.criarCupomFixo("FRETE10", 10.00));
            
            salvarCupons();
            System.out.println(">>> " + cupons.size() + " cupons inicializados!");
        }
    }
    
    /**
     * Busca um cupom pelo código
     */
    public Cupom buscarCupom(String codigo) {
        for (Cupom c : cupons) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }
    
    /**
     * Adiciona um pedido à lista geral
     */
    public void adicionarPedido(Pedido pedido) {
        if (pedido != null && !pedidos.contains(pedido)) {
            pedidos.add(pedido);
        }
    }
    
    /**
     * Lista todos os cupons disponíveis
     */
    public List<Cupom> getCuponsDisponiveis() {
        List<Cupom> disponiveis = new ArrayList<>();
        for (Cupom c : cupons) {
            if (c.estaValido()) {
                disponiveis.add(c);
            }
        }
        return disponiveis;
    }
    
    /**
     * Exibe todos os cupons válidos
     */
    public void exibirCuponsDisponiveis() {
        List<Cupom> disponiveis = getCuponsDisponiveis();
        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum cupom disponivel no momento.");
            return;
        }
        
        System.out.println("\n=== CUPONS DISPONIVEIS ===");
        for (Cupom c : disponiveis) {
            System.out.println("Codigo: " + c.getCodigo() + " - " + c.getDescricaoDesconto());
        }
    }
}