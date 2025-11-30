package ifome.util;

import ifome.model.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

//repositorio restaurante, para os restaurantes presettados e mais ocnfiguaçõess
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
    private static final String ARQUIVO_ITENS_PEDIDO = "data/itens_pedido.txt";
    private static final String ARQUIVO_CARDAPIOS = "data/cardapios.txt";
    private static final String ARQUIVO_CUPONS = "data/cupons.txt";
    private static final String ARQUIVO_AVALIACOES = "data/avaliacoes.txt";
    private static final String ARQUIVO_AVALIACOES_PEDIDOS = "data/avaliacoes_pedidos.txt";
    private static final String ARQUIVO_CARTOES = "data/cartoes.txt";
    private static final String ARQUIVO_CUPONS_USADOS = "data/cupons_usados.txt"; 
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        if (!restaurantes.isEmpty()) {
            for (Restaurante r : restaurantes) {
                if (!r.estaAberto()) {
                    r.abrirRestaurante();
                }
            }
            return;
        }
        
        System.out.println(">>> Populando restaurantes iniciais...");

        Restaurante pizzaria = new Restaurante(
            "pizzaria@ifome.com", "123", "Pizzaria Italiana", "30648775000163" // CNPJ Válido
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

        Restaurante burger = new Restaurante(
            "burger@ifome.com", "123", "Burger House", "80932127000176" // CNPJ Válido
        );
        burger.abrirRestaurante();
        burger.adicionarProdutoCardapio(new Comida("X-Burger", 
            "Pao, carne, queijo, alface, tomate", 22.90, false));
        burger.adicionarProdutoCardapio(new Comida("X-Bacon", 
            "Pao, carne, bacon, queijo, alface", 26.90, false));
        burger.adicionarProdutoCardapio(new Bebida("Suco Natural", 
            "Laranja 500ml", 8.00, 500));
        adicionarRestaurante(burger);

        Restaurante sushi = new Restaurante(
            "sushi@ifome.com", "123", "Sushi Master", "91688421000166" // CNPJ Válido
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

    //busca

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

    public Cliente buscarClientePorEmail(String email) {
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        return null;
    }

    public Restaurante buscarRestaurantePorEmail(String email) {
        for (Restaurante r : restaurantes) {
            if (r.getEmail().equals(email)) {
                return r;
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

    public List<Pedido> getTodosPedidos() {
        return new ArrayList<>(pedidos);
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

    //presistencia e save
    //e dessa vez os cupons estão certos

    private void carregarDados() {
        System.out.println(">>> Carregando dados do sistema...");
        carregarRestaurantes();
        carregarClientes();
        carregarCardapios();
        carregarEnderecos();
        carregarPedidos();
        carregarItensPedido();
        carregarCupons();
        carregarCartoes();
        carregarAvaliacoes();
        carregarAvaliacoesPedidos();
        carregarCuponsUsados();
        System.out.println(">>> Dados carregados com sucesso!");
    }

    private void carregarRestaurantes() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_RESTAURANTES), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    Restaurante r = new Restaurante(dados[0], dados[1], dados[2], dados[3]);
                    r.abrirRestaurante();
                    restaurantes.add(r);
                }
            }
            System.out.println(">>> " + restaurantes.size() + " restaurantes carregados");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de restaurantes não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar restaurantes: " + e.getMessage());
        }
    }

    private void carregarClientes() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ARQUIVO_CLIENTES), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    clientes.add(new Cliente(dados[0], dados[1], dados[2], dados[3]));
                }
            }
        } catch (IOException e) { System.out.println("Clientes: arquivo novo ou erro."); }
    }
    
    private void carregarCardapios() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_CARDAPIOS), StandardCharsets.UTF_8))) {
            String linha;
            int totalProdutos = 0;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 6) {
                    String emailRestaurante = dados[0];
                    String tipoProduto = dados[1];
                    String nome = dados[2];
                    String descricao = dados[3];
                    double preco = Double.parseDouble(dados[4]);
                    boolean disponivel = Boolean.parseBoolean(dados[5]);
                    
                    Restaurante restaurante = buscarRestaurantePorEmail(emailRestaurante);
                    if (restaurante != null) {
                        Produto produto = null;
                        
                        switch (tipoProduto) {
                            case "Comida":
                                boolean vegetariano = dados.length > 6 ? Boolean.parseBoolean(dados[6]) : false;
                                produto = new Comida(nome, descricao, preco, vegetariano);
                                break;
                            case "Bebida":
                                int volumeML = dados.length > 6 ? Integer.parseInt(dados[6]) : 350;
                                produto = new Bebida(nome, descricao, preco, volumeML);
                                break;
                            case "Sobremesa":
                                produto = new Sobremesa(nome, descricao, preco);
                                break;
                            case "Adicional":
                                produto = new Adicional(nome, preco);
                                break;
                        }
                        
                        if (produto != null) {
                            produto.setDisponibilidade(disponivel);
                            restaurante.adicionarProdutoCardapio(produto);
                            totalProdutos++;
                        }
                    }
                }
            }
            System.out.println(">>> " + totalProdutos + " produtos carregados nos cardápios");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de cardápios não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar cardápios: " + e.getMessage());
        }
    }

    private void carregarEnderecos() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_ENDERECOS), StandardCharsets.UTF_8))) {
            String linha;
            int totalEnderecos = 0;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 7) {
                    String emailCliente = dados[0];
                    Cliente cliente = buscarClientePorEmail(emailCliente);
                    if (cliente != null) {
                        Endereco end = new Endereco(dados[1], dados[2], dados[3], dados[4], dados[5], dados[6]);
                        cliente.adicionarEndereco(end);
                        totalEnderecos++;
                    }
                }
            }
            System.out.println(">>> " + totalEnderecos + " endereços carregados");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de endereços não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar endereços: " + e.getMessage());
        }
    }

    private void carregarPedidos() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_PEDIDOS), StandardCharsets.UTF_8))) {
            String linha;
            int maiorId = 0;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 6) {
                    int numeroPedido = Integer.parseInt(dados[0]);
                    if (numeroPedido > maiorId) {
                        maiorId = numeroPedido;
                    }
                    
                    Date dataHora = dateFormat.parse(dados[1]);
                    String emailCliente = dados[2];
                    String emailRestaurante = dados[3];
                    String status = dados[4];
                    double valorTotal = Double.parseDouble(dados[5]);
                    
                    Cliente cliente = buscarClientePorEmail(emailCliente);
                    Restaurante restaurante = buscarRestaurantePorEmail(emailRestaurante);
                    
                    if (cliente != null && restaurante != null) {
                        Pedido p = new Pedido(numeroPedido, dataHora, status, valorTotal);
                        p.setCliente(cliente);
                        p.setRestaurante(restaurante);
                        
                        cliente.adicionarPedido(p);
                        
                        if (!status.equals("Entregue") && !status.equals("Cancelado")) {
                            try {
                                restaurante.aceitarPedido(p);
                            } catch (Exception e) {
                                //restaurante pode estar fechado
                            }
                        }
                        
                        pedidos.add(p);
                    }
                }
            }
            
            if (maiorId > 0) {
                Pedido.inicializarContador(maiorId);
            }
            
            System.out.println(">>> " + pedidos.size() + " pedidos carregados");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de pedidos não encontrado. Será criado ao salvar.");
        } catch (IOException | ParseException e) {
            System.err.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void carregarItensPedido() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_ITENS_PEDIDO), StandardCharsets.UTF_8))) {
            String linha;
            int totalItens = 0;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 6) {
                    int numeroPedido = Integer.parseInt(dados[0]);
                    String emailRestaurante = dados[1];
                    String nomeProduto = dados[2];
                    int quantidade = Integer.parseInt(dados[3]);
                    double precoUnitario = Double.parseDouble(dados[4]);
                    String observacoes = dados[5];
                    
                    Pedido pedido = buscarPedidoPorNumero(numeroPedido);
                    Restaurante restaurante = buscarRestaurantePorEmail(emailRestaurante);
                    
                    if (pedido != null && restaurante != null) {
                        Produto produto = restaurante.buscarProduto(nomeProduto);
                        if (produto != null) {
                            ItemPedido item = new ItemPedido(produto, quantidade, observacoes);
                            pedido.adicionarItem(item);
                            totalItens++;
                        }
                    }
                }
            }
            System.out.println(">>> " + totalItens + " itens de pedido carregados");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de itens de pedido não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar itens de pedido: " + e.getMessage());
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
            System.out.println(">>> " + cupons.size() + " cupons carregados");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de cupons não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar cupons: " + e.getMessage());
        }
    }

    private Pedido buscarPedidoPorNumero(int numeroPedido) {
        for (Pedido p : pedidos) {
            if (p.getNumeroPedido() == numeroPedido) {
                return p;
            }
        }
        return null;
    }

    //save

    public void salvarDados() {
        System.out.println(">>> Salvando dados do sistema...");
        salvarRestaurantes();
        salvarClientes();
        salvarCardapios();
        salvarEnderecos();
        salvarPedidos();
        salvarItensPedido();
        salvarCupons();
        salvarCartoes();
        salvarAvaliacoes();
        salvarAvaliacoesPedidos();
        salvarCuponsUsados(); // ✅ NOVO
        System.out.println(">>> Dados salvos com sucesso!");
    }

    private void salvarRestaurantes() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_RESTAURANTES), StandardCharsets.UTF_8))) {
            for (Restaurante r : restaurantes) {
                bw.write(r.getEmail() + ";" + r.getSenha() + ";" + 
                        r.getNomeRestaurante() + ";" + r.getCNPJ());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar restaurantes: " + e.getMessage());
        }
    }

    private void salvarClientes() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ARQUIVO_CLIENTES), StandardCharsets.UTF_8))) {
            for (Cliente c : clientes) {
                bw.write(c.getEmail() + ";" + c.getSenha() + ";" + c.getNome() + ";" + c.getTelefone());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar restaurantes: " + e.getMessage());
        }
    }

    private void salvarCardapios() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_CARDAPIOS), StandardCharsets.UTF_8))) {
            for (Restaurante r : restaurantes) {
                for (Produto p : r.getCardapio()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(r.getEmail()).append(";");
                    sb.append(p.getCategoria()).append(";");
                    sb.append(p.getNome()).append(";");
                    sb.append(p.getDescricao()).append(";");
                    sb.append(p.getPreco()).append(";");
                    sb.append(p.isDisponivel());
                    
                    if (p instanceof Comida) {
                        sb.append(";false");
                    } else if (p instanceof Bebida) {
                        sb.append(";350");
                    }
                    
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar cardápios: " + e.getMessage());
        }
    }

    private void salvarEnderecos() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_ENDERECOS), StandardCharsets.UTF_8))) {
            for (Cliente c : clientes) {
                for (Endereco end : c.getEnderecos()) {
                    bw.write(c.getEmail() + ";" + end.getCep() + ";" + end.getRua() + ";" +
                            end.getNumero() + ";" + end.getBairro() + ";" + 
                            end.getCidade() + ";" + end.getEstado());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar endereços: " + e.getMessage());
        }
    }

    private void salvarPedidos() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_PEDIDOS), StandardCharsets.UTF_8))) {
            for (Pedido p : pedidos) {
                if (p.getCliente() != null && p.getRestaurante() != null) {
                    bw.write(p.getNumeroPedido() + ";" +
                            dateFormat.format(p.getDataHora()) + ";" +
                            p.getCliente().getEmail() + ";" +
                            p.getRestaurante().getEmail() + ";" +
                            p.getStatus() + ";" +
                            p.getValorTotal());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }

    private void salvarItensPedido() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_ITENS_PEDIDO), StandardCharsets.UTF_8))) {
            for (Pedido p : pedidos) {
                if (p.getRestaurante() != null) {
                    for (ItemPedido item : p.getItens()) {
                        bw.write(p.getNumeroPedido() + ";" +
                                p.getRestaurante().getEmail() + ";" +
                                item.getProduto().getNome() + ";" +
                                item.getQuantidade() + ";" +
                                item.getPrecoUnitario() + ";" +
                                item.getObservacoes());
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar itens de pedido: " + e.getMessage());
        }
    }

    private void salvarCupons() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_CUPONS), StandardCharsets.UTF_8))) {
            for (Cupom c : cupons) {
                bw.write(c.getCodigo() + ";" + c.getValorDesconto() + ";" + c.isPercentual());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar cupons: " + e.getMessage());
        }
    }

    private void carregarCuponsUsados() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_CUPONS_USADOS), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 2) {
                    String emailCliente = dados[0];
                    String codigoCupom = dados[1];
                    
                    Cliente cliente = buscarClientePorEmail(emailCliente);
                    if (cliente != null) {
                        cliente.registrarUsoCupom(codigoCupom);
                    }
                }
            }
            System.out.println(">>> Histórico de uso de cupons carregado.");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de cupons usados não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar cupons usados: " + e.getMessage());
        }
    }

    private void salvarCuponsUsados() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_CUPONS_USADOS), StandardCharsets.UTF_8))) {
            for (Cliente c : clientes) {
                for (String codigo : c.getCuponsUsados()) {
                    bw.write(c.getEmail() + ";" + codigo);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar cupons usados: " + e.getMessage());
        }
    }
    

    
    // avaliações
    
    
    private void carregarAvaliacoes() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_AVALIACOES), StandardCharsets.UTF_8))) {
            String linha;
            int totalAvaliacoes = 0;
            while ((linha = br.readLine()) != null) {
               
                String[] dados = linha.split(";", -1);
                if (dados.length >= 3) {
                    String emailRestaurante = dados[0];
                    int nota = Integer.parseInt(dados[1]);
                    String comentario = dados[2];
                    
                    Restaurante restaurante = buscarRestaurantePorEmail(emailRestaurante);
                    if (restaurante != null) {
                        restaurante.avaliar(nota, comentario);
                        totalAvaliacoes++;
                    }
                }
            }
            System.out.println(">>> " + totalAvaliacoes + " avaliações de restaurantes carregadas");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de avaliações não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar avaliações: " + e.getMessage());
        }
    }

    private void salvarAvaliacoes() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_AVALIACOES), StandardCharsets.UTF_8))) {
            for (Restaurante r : restaurantes) {
                for (Avaliacao avaliacao : r.getAvaliacoes()) {
                    bw.write(r.getEmail() + ";" +
                            avaliacao.getNota() + ";" +
                            avaliacao.getComentario());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar avaliações: " + e.getMessage());
        }
    }


//avaliação
    private void carregarAvaliacoesPedidos() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_AVALIACOES_PEDIDOS), StandardCharsets.UTF_8))) {
            String linha;
            int totalAvaliacoesPedidos = 0;
            while ((linha = br.readLine()) != null) {
                
                String[] dados = linha.split(";", -1);
                if (dados.length >= 3) {
                    int numeroPedido = Integer.parseInt(dados[0]);
                    int nota = Integer.parseInt(dados[1]);
                    String comentario = dados[2];
                    
                    Pedido pedido = buscarPedidoPorNumero(numeroPedido);
                    if (pedido != null) {
                        pedido.avaliar(nota, comentario);
                        totalAvaliacoesPedidos++;
                    }
                }
            }
            System.out.println(">>> " + totalAvaliacoesPedidos + " avaliações de pedidos carregadas");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de avaliações de pedidos não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar avaliações de pedidos: " + e.getMessage());
        }
    }

   //salva as avaliaçoes
    private void salvarAvaliacoesPedidos() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_AVALIACOES_PEDIDOS), StandardCharsets.UTF_8))) {
            for (Pedido p : pedidos) {
                if (!p.getAvaliacoes().isEmpty()) {
                    Avaliacao av = p.getAvaliacoes().get(0);
                    bw.write(p.getNumeroPedido() + ";" +
                            av.getNota() + ";" +
                            av.getComentario());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar avaliações de pedidos: " + e.getMessage());
        }
    }
//cupom
    private void inicializarCupons() {
        if (cupons.isEmpty()) {
            cupons.add(Cupom.criarCupomPercentual("BEMVINDO10", 10));
            cupons.add(Cupom.criarCupomPercentual("DESCONTO15", 15));
            cupons.add(Cupom.criarCupomPercentual("PRIMEIRACOMPRA", 20));
            cupons.add(Cupom.criarCupomFixo("ECONOMIZE5", 5.00));
            cupons.add(Cupom.criarCupomFixo("FRETE10", 10.00));
            salvarCupons();
        }
    }

    public Cupom buscarCupom(String codigo) {
        for (Cupom c : cupons) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        return null;
    }

    public void adicionarPedido(Pedido pedido) {
        if (pedido != null && !pedidos.contains(pedido)) {
            pedidos.add(pedido);
        }
    }

    public List<Cupom> getCuponsDisponiveis() {
        List<Cupom> disponiveis = new ArrayList<>();
        for (Cupom c : cupons) {
            if (c.estaValido()) {
                disponiveis.add(c);
            }
        }
        return disponiveis;
    }

    public void exibirCuponsDisponiveis() {
        List<Cupom> disponiveis = getCuponsDisponiveis();
        if (disponiveis.isEmpty()) {
            System.out.println("Nenhum cupom disponível no momento.");
            return;
        }
        
        System.out.println("\n=== CUPONS DISPONÍVEIS ===");
        for (Cupom c : disponiveis) {
            System.out.println("Código: " + c.getCodigo() + " - " + c.getDescricaoDesconto());
        }
    }

    private void carregarCartoes() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(ARQUIVO_CARTOES), StandardCharsets.UTF_8))) {
            String linha;
            int totalCartoes = 0;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 6) {
                    String emailCliente = dados[0];
                    String numeroCompleto = dados[1];
                    String nomeTitular = dados[2];
                    String cvv = dados[3];
                    String validade = dados[4];
                    String apelido = dados[5];
                    
                    Cliente cliente = buscarClientePorEmail(emailCliente);
                    if (cliente != null) {
                        CartaoSalvo cartao = new CartaoSalvo(numeroCompleto, nomeTitular, cvv, validade, apelido);
                        cliente.adicionarCartao(cartao);
                        totalCartoes++;
                    }
                }
            }
            System.out.println(">>> " + totalCartoes + " cartões carregados");
        } catch (FileNotFoundException e) {
            System.out.println(">>> Arquivo de cartões não encontrado. Será criado ao salvar.");
        } catch (IOException e) {
            System.err.println("Erro ao carregar cartões: " + e.getMessage());
        }
    }

    private void salvarCartoes() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ARQUIVO_CARTOES), StandardCharsets.UTF_8))) {
            for (Cliente c : clientes) {
                for (CartaoSalvo cartao : c.getCartoesSalvos()) {
                    bw.write(c.getEmail() + ";" + 
                            cartao.getNumeroCompleto() + ";" + 
                            cartao.getNomeTitular() + ";" + 
                            cartao.getCvv() + ";" + 
                            cartao.getValidade() + ";" + 
                            cartao.getApelido());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar cartões: " + e.getMessage());
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
}