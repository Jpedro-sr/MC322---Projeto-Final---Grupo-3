package ifome;

import ifome.model.*;
import ifome.util.*;
import ifome.exceptions.*;

/**
 * Aplicação principal do iFome - VERSÃO CORRIGIDA
 * Combina a lógica estável com o design excelente
 */
public class Aplicacao {

    private static SessaoUsuario sessao;
    private static RepositorioRestaurantes repositorio;

    public static void main(String[] args) {
        // Configurar encoding para Windows
        configurarEncoding();
        
        sessao = SessaoUsuario.getInstance();
        repositorio = RepositorioRestaurantes.getInstance();

        try {
            repositorio.inicializarRestaurantes();
            InputManager.pausar("\nBem-vindo ao iFome! Sistema de Delivery");
            
            menuPrincipal();

        } catch (Exception e) {
            System.err.println("\nERRO FATAL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            repositorio.salvarDados();
            InputManager.fechar();
        }
    }

    /**
     * Configura encoding UTF-8 para Windows.
     */
    private static void configurarEncoding() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Tenta configurar UTF-8 no Windows
                System.out.println("Sistema Windows detectado");
            }
        } catch (Exception e) {
            // Ignora erro de configuração
        }
    }

    // ==================== MENU PRINCIPAL ====================

    private static void menuPrincipal() {
        boolean executando = true;
        
        while (executando) {
            if (!sessao.estaLogado()) {
                executando = exibirMenuInicial();
            } else {
                // Verifica tipo de usuário
                if (sessao.ehCliente()) {
                    menuCliente();
                } else if (sessao.ehRestaurante()) {
                    menuRestaurante();
                }
            }
        }
        
        System.out.println("\nObrigado por usar o iFome!");
    }

    /**
     * Menu inicial (sem login).
     */
    private static boolean exibirMenuInicial() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("iFOME - SISTEMA DE DELIVERY");
        InputManager.linha();
        System.out.println("1. [CLIENTE] Login");
        System.out.println("2. [CLIENTE] Criar Conta");
        System.out.println("3. [RESTAURANTE] Login");
        System.out.println("4. [RESTAURANTE] Cadastrar");
        System.out.println("5. Ver Restaurantes Disponiveis");
        System.out.println("0. Sair");
        InputManager.linha();

        int opcao = InputManager.lerInteiro("Escolha: ");

        switch (opcao) {
            case 1:
                fazerLoginCliente();
                break;
            case 2:
                criarContaCliente();
                break;
            case 3:
                fazerLoginRestaurante();
                break;
            case 4:
                cadastrarRestaurante();
                break;
            case 5:
                verRestaurantes();
                break;
            case 0:
                return false; // Encerra aplicação
            default:
                System.out.println("Opcao invalida.");
                InputManager.pausar("");
        }

        return true;
    }

    // ==================== LOGIN E CADASTRO ====================

    /**
     * Login de cliente.
     */
    private static void fazerLoginCliente() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("LOGIN CLIENTE");
        InputManager.linha();

        String email = InputManager.lerEmail("Email: ");
        String senha = InputManager.lerTexto("Senha: ");

        Cliente cliente = repositorio.buscarClientePorLogin(email, senha);
        
        if (cliente != null) {
            sessao.setClienteLogado(cliente);
            System.out.println("\nLogin realizado com sucesso!");
            System.out.println("Bem-vindo, " + cliente.getNome() + "!");
            InputManager.pausar("");
        } else {
            System.out.println("\nEmail ou senha incorretos!");
            InputManager.pausar("");
        }
    }

    /**
     * Login de restaurante.
     */
    private static void fazerLoginRestaurante() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("LOGIN RESTAURANTE");
        InputManager.linha();

        String email = InputManager.lerEmail("Email: ");
        String senha = InputManager.lerTexto("Senha: ");

        Restaurante restaurante = repositorio.buscarRestaurantePorLogin(email, senha);
        
        if (restaurante != null) {
            sessao.setRestauranteLogado(restaurante);
            System.out.println("\nLogin realizado com sucesso!");
            System.out.println("Bem-vindo, " + restaurante.getNomeRestaurante() + "!");
            InputManager.pausar("");
        } else {
            System.out.println("\nEmail ou senha incorretos!");
            InputManager.pausar("");
        }
    }

    /**
     * Criar conta de cliente.
     */
    private static void criarContaCliente() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("CRIAR CONTA CLIENTE");
        InputManager.linha();

        String nome = InputManager.lerTextoObrigatorio("Nome completo: ");
        String email = InputManager.lerEmail("Email: ");
        
        if (repositorio.emailJaExiste(email)) {
            System.out.println("\nEmail ja cadastrado!");
            InputManager.pausar("");
            return;
        }

        String senha = InputManager.lerTexto("Senha: ");
        String telefone = InputManager.lerTelefone("Telefone: ");

        Cliente novoCliente = new Cliente(email, senha, nome, telefone);
        repositorio.adicionarCliente(novoCliente);
        sessao.setClienteLogado(novoCliente);

        System.out.println("\nConta criada com sucesso!");
        System.out.println("Bem-vindo, " + nome + "!");
        InputManager.pausar("");
    }

    /**
     * Cadastrar restaurante.
     */
    private static void cadastrarRestaurante() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("CADASTRAR RESTAURANTE");
        InputManager.linha();

        String nome = InputManager.lerTextoObrigatorio("Nome do Restaurante: ");
        String email = InputManager.lerEmail("Email: ");
        
        if (repositorio.emailJaExiste(email)) {
            System.out.println("\nEmail ja cadastrado!");
            InputManager.pausar("");
            return;
        }

        String senha = InputManager.lerTexto("Senha: ");
        String cnpj = InputManager.lerTexto("CNPJ: ");

        Restaurante novoRestaurante = new Restaurante(email, senha, nome, cnpj);
        repositorio.adicionarRestaurante(novoRestaurante);
        sessao.setRestauranteLogado(novoRestaurante);

        System.out.println("\nRestaurante cadastrado com sucesso!");
        System.out.println("Bem-vindo, " + nome + "!");
        InputManager.pausar("");
    }

    // ==================== MENU CLIENTE ====================

    private static void menuCliente() {
        InputManager.limparTela();
        Cliente cliente = sessao.getClienteLogado();
        
        InputManager.linha();
        System.out.println("MENU CLIENTE");
        InputManager.linha();
        System.out.println("Usuario: " + cliente.getNome());
        InputManager.linha();
        System.out.println("1. Escolher Restaurante");
        System.out.println("2. Ver Carrinho");
        System.out.println("3. Meus Pedidos");
        System.out.println("4. Avaliar Pedido");
        System.out.println("5. Gerenciar Enderecos");
        System.out.println("6. Logout");
        InputManager.linha();

        int opcao = InputManager.lerInteiro("Escolha: ");

        switch (opcao) {
            case 1:
                escolherRestaurante();
                break;
            case 2:
                verCarrinho();
                break;
            case 3:
                verPedidos();
                break;
            case 4:
                avaliarPedido();
                break;
            case 5:
                gerenciarEnderecos();
                break;
            case 6:
                sessao.logout();
                System.out.println("Logout realizado!");
                InputManager.pausar("");
                break;
            default:
                System.out.println("Opcao invalida.");
                InputManager.pausar("");
        }
    }

    // ==================== MENU RESTAURANTE ====================

    private static void menuRestaurante() {
        InputManager.limparTela();
        Restaurante restaurante = sessao.getRestauranteLogado();
        
        InputManager.linha();
        System.out.println("MENU RESTAURANTE");
        InputManager.linha();
        System.out.println("Restaurante: " + restaurante.getNomeRestaurante());
        String status = restaurante.estaAberto() ? "[ABERTO]" : "[FECHADO]";
        System.out.println("Status: " + status);
        InputManager.linha();
        System.out.println("1. Ver Cardapio");
        System.out.println("2. Adicionar Produto ao Cardapio");
        System.out.println("3. Remover Produto do Cardapio");
        System.out.println("4. Ver Pedidos Pendentes");
        System.out.println("5. Aceitar/Atualizar Pedido");
        System.out.println("6. Abrir/Fechar Restaurante");
        System.out.println("7. Ver Avaliacoes");
        System.out.println("8. Logout");
        InputManager.linha();

        int opcao = InputManager.lerInteiro("Escolha: ");

        switch (opcao) {
            case 1:
                restaurante.exibirCardapio();
                InputManager.pausar("");
                break;
            case 2:
                adicionarProdutoCardapio(restaurante);
                break;
            case 3:
                removerProdutoCardapio(restaurante);
                break;
            case 4:
                verPedidosPendentes(restaurante);
                break;
            case 5:
                gerenciarPedido(restaurante);
                break;
            case 6:
                toggleRestaurante(restaurante);
                break;
            case 7:
                verAvaliacoesRestaurante(restaurante);
                break;
            case 8:
                sessao.logout();
                System.out.println("Logout realizado!");
                InputManager.pausar("");
                break;
            default:
                System.out.println("Opcao invalida.");
                InputManager.pausar("");
        }
    }

    // ==================== FUNCIONALIDADES CLIENTE ====================

    private static void escolherRestaurante() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("ESCOLHER RESTAURANTE");
        InputManager.linha();

        repositorio.exibirLista();
        
        int opcao = InputManager.lerInteiro("\nEscolha um restaurante (0 para voltar): ");

        if (opcao == 0) return;

        Restaurante restaurante = repositorio.obterPorIndice(opcao - 1);
        if (restaurante == null) {
            System.out.println("Restaurante nao encontrado.");
            InputManager.pausar("");
            return;
        }

        if (!restaurante.estaAberto()) {
            System.out.println("Restaurante esta fechado.");
            InputManager.pausar("");
            return;
        }

        sessao.setRestauranteAtual(restaurante);
        System.out.println("Restaurante selecionado: " + restaurante.getNomeRestaurante());
        
        menuCardapio(restaurante);
    }

    private static void menuCardapio(Restaurante restaurante) {
        boolean continuarNoCardapio = true;
        
        while (continuarNoCardapio && sessao.estaLogado()) {
            InputManager.limparTela();
            InputManager.linha();
            System.out.println("CARDAPIO - " + restaurante.getNomeRestaurante());
            InputManager.linha();

            java.util.List<Produto> produtos = restaurante.getCardapio();
            for (int i = 0; i < produtos.size(); i++) {
                Produto p = produtos.get(i);
                String status = p.isDisponivel() ? "[OK]" : "[INDISPONIVEL]";
                System.out.printf("%d. %s %s - R$%.2f\n", 
                    i + 1, status, p.getNome(), p.getPreco());
                System.out.println("   " + p.getDescricao());
            }

            System.out.println("\n" + (produtos.size() + 1) + ". Ver Carrinho");
            System.out.println((produtos.size() + 2) + ". Confirmar Pedido");
            System.out.println((produtos.size() + 3) + ". Voltar");
            InputManager.linha();

            int opcao = InputManager.lerInteiro("Escolha: ");

            if (opcao >= 1 && opcao <= produtos.size()) {
                adicionarAoCarrinho(produtos.get(opcao - 1), restaurante);
            } else if (opcao == produtos.size() + 1) {
                verCarrinho();
            } else if (opcao == produtos.size() + 2) {
                confirmarPedido();
                continuarNoCardapio = false;
            } else if (opcao == produtos.size() + 3) {
                continuarNoCardapio = false;
            } else {
                System.out.println("Opcao invalida.");
                InputManager.pausar("");
            }
        }
    }

    private static void adicionarAoCarrinho(Produto produto, Restaurante restaurante) {
        if (!produto.isDisponivel()) {
            System.out.println("Produto nao disponivel.");
            InputManager.pausar("");
            return;
        }

        int quantidade = InputManager.lerInteiro("Quantidade: ");
        if (quantidade <= 0) {
            System.out.println("Quantidade invalida.");
            InputManager.pausar("");
            return;
        }

        Cliente cliente = sessao.getClienteLogado();
        Carrinho carrinho = cliente.getCarrinho();
        carrinho.setRestaurante(restaurante);
        carrinho.setCliente(cliente);
        
        String obs = InputManager.lerTexto("Observacoes (Enter para pular): ");
        carrinho.adicionarItem(produto, quantidade, obs);
        
        System.out.println("Item adicionado ao carrinho!");
        InputManager.pausar("");
    }

    private static void verCarrinho() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("SEU CARRINHO");
        InputManager.linha();

        Carrinho carrinho = sessao.getClienteLogado().getCarrinho();
        System.out.println(carrinho.toString());

        if (!carrinho.getItens().isEmpty()) {
            if (InputManager.lerConfirmacao("\nDeseja remover algum item?")) {
                java.util.List<ItemPedido> itens = carrinho.getItens();
                for (int i = 0; i < itens.size(); i++) {
                    System.out.println((i + 1) + ". " + itens.get(i).getProduto().getNome());
                }
                int indice = InputManager.lerInteiro("Qual item? (0 para cancelar): ");
                if (indice > 0 && indice <= itens.size()) {
                    carrinho.removerItem(itens.get(indice - 1));
                    System.out.println("Item removido!");
                }
            }
        }

        InputManager.pausar("");
    }

    private static void confirmarPedido() {
        Cliente cliente = sessao.getClienteLogado();
        Carrinho carrinho = cliente.getCarrinho();

        // ✅ VALIDAÇÃO CRÍTICA ADICIONADA
        if (carrinho.getRestaurante() == null) {
            System.out.println("\n[X] Voce precisa escolher um restaurante primeiro!");
            System.out.println("    Volte ao menu e selecione 'Escolher Restaurante'.");
            InputManager.pausar("");
            return;
        }

        if (carrinho.getItens().isEmpty()) {
            System.out.println("Carrinho vazio!");
            InputManager.pausar("");
            return;
        }

        // Verificar/adicionar endereço
        if (cliente.getEnderecos().isEmpty()) {
            System.out.println("\nVoce precisa cadastrar um endereco primeiro!");
            adicionarEndereco(cliente);
            if (cliente.getEnderecos().isEmpty()) {
                return;
            }
        }

        // Selecionar endereço
        System.out.println("\n--- Escolha o endereco de entrega ---");
        for (int i = 0; i < cliente.getEnderecos().size(); i++) {
            System.out.println((i + 1) + ". " + cliente.getEnderecos().get(i).getEnderecoCompleto());
        }
        int indiceEnd = InputManager.lerInteiro("Escolha: ") - 1;

        if (indiceEnd < 0 || indiceEnd >= cliente.getEnderecos().size()) {
            System.out.println("Endereco invalido!");
            InputManager.pausar("");
            return;
        }

        // Escolher forma de pagamento
        System.out.println("\n--- Forma de Pagamento ---");
        System.out.println("1. PIX");
        System.out.println("2. Cartao de Credito");
        System.out.println("3. Dinheiro");
        int opcaoPag = InputManager.lerInteiro("Escolha: ");

        FormaPagamento pagamento;
        switch (opcaoPag) {
            case 1:
                pagamento = new PIX();
                break;
            case 2:
                pagamento = new CartaoCredito("1234567890123456", "CLIENTE", "123");
                break;
            case 3:
                double valor = InputManager.lerInteiro("Valor recebido (R$): ");
                pagamento = new Dinheiro(valor);
                break;
            default:
                System.out.println("Opcao invalida!");
                InputManager.pausar("");
                return;
        }

        try {
            Pedido pedido = carrinho.gerarPedido();
            pedido.setFormaPagamento(pagamento);
            pedido.processarPagamento();

            // Adicionar pedido ao restaurante
            Restaurante restaurante = carrinho.getRestaurante();
            restaurante.aceitarPedido(pedido);

            System.out.println("\nPEDIDO REALIZADO COM SUCESSO!");
            System.out.println(pedido.gerarResumo());

            // Limpar carrinho
            cliente.getCarrinho().limparCarrinho();

        } catch (Exception e) {
            System.out.println("\nErro ao processar pedido: " + e.getMessage());
        }

        InputManager.pausar("");
    }

    private static void verPedidos() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("MEUS PEDIDOS");
        InputManager.linha();

        java.util.List<Pedido> pedidos = sessao.getClienteLogado().getHistoricoPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Voce ainda nao fez nenhum pedido.");
        } else {
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido p = pedidos.get(i);
                System.out.printf("%d. Pedido #%d - %s - R$%.2f\n", 
                    i + 1, p.getNumeroPedido(), p.getStatus(), p.getValorTotal());
            }
        }

        InputManager.pausar("");
    }

    private static void avaliarPedido() {
        // Implementação similar ao código original
        System.out.println("Funcionalidade de avaliar pedido");
        InputManager.pausar("");
    }

    private static void gerenciarEnderecos() {
        InputManager.limparTela();
        Cliente cliente = sessao.getClienteLogado();
        
        InputManager.linha();
        System.out.println("MEUS ENDERECOS");
        InputManager.linha();

        java.util.List<Endereco> enderecos = cliente.getEnderecos();

        if (enderecos.isEmpty()) {
            System.out.println("Voce nao tem enderecos cadastrados.");
        } else {
            for (int i = 0; i < enderecos.size(); i++) {
                System.out.println((i + 1) + ". " + enderecos.get(i).getEnderecoCompleto());
            }
        }

        System.out.println("\n1. Adicionar novo endereco");
        System.out.println("2. Voltar");

        int opcao = InputManager.lerInteiro("Escolha: ");

        if (opcao == 1) {
            adicionarEndereco(cliente);
        }
    }

    private static void adicionarEndereco(Cliente cliente) {
        System.out.println("\n--- Adicionar Endereco ---");
        String cep = InputManager.lerTexto("CEP: ");
        String rua = InputManager.lerTexto("Rua: ");
        String numero = InputManager.lerTexto("Numero: ");
        String bairro = InputManager.lerTexto("Bairro: ");
        String cidade = InputManager.lerTexto("Cidade: ");
        String estado = InputManager.lerTexto("Estado: ");

        Endereco novoEndereco = new Endereco(cep, rua, numero, bairro, cidade, estado);
        cliente.adicionarEndereco(novoEndereco);
        System.out.println("Endereco adicionado!");
        InputManager.pausar("");
    }

    // ==================== FUNCIONALIDADES RESTAURANTE ====================

    private static void adicionarProdutoCardapio(Restaurante restaurante) {
        System.out.println("\n--- Adicionar Produto ---");
        System.out.println("Tipo: 1-Comida 2-Bebida 3-Sobremesa 4-Adicional");
        int tipo = InputManager.lerInteiro("Escolha: ");

        String nome = InputManager.lerTextoObrigatorio("Nome: ");
        String desc = InputManager.lerTexto("Descricao: ");
        
        String precoStr = InputManager.lerTexto("Preco: R$ ");
        double preco = Double.parseDouble(precoStr.replace(",", "."));

        Produto produto;
        switch (tipo) {
            case 1:
                produto = new Comida(nome, desc, preco, false);
                break;
            case 2:
                produto = new Bebida(nome, desc, preco, 350);
                break;
            case 3:
                produto = new Sobremesa(nome, desc, preco);
                break;
            case 4:
                produto = new Adicional(nome, preco);
                break;
            default:
                System.out.println("Tipo invalido!");
                InputManager.pausar("");
                return;
        }

        restaurante.adicionarProdutoCardapio(produto);
        System.out.println("Produto adicionado!");
        InputManager.pausar("");
    }

    private static void removerProdutoCardapio(Restaurante restaurante) {
        restaurante.exibirCardapio();
        
        if (restaurante.getCardapio().isEmpty()) {
            InputManager.pausar("");
            return;
        }

        int indice = InputManager.lerInteiro("\nEscolha o produto para remover (0 para cancelar): ") - 1;

        if (indice >= 0 && indice < restaurante.getCardapio().size()) {
            Produto p = restaurante.getCardapio().get(indice);
            restaurante.removerProdutoCardapio(p);
            System.out.println("Produto removido!");
        }
        
        InputManager.pausar("");
    }

    private static void verPedidosPendentes(Restaurante restaurante) {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("PEDIDOS PENDENTES");
        InputManager.linha();
        
        if (restaurante.getFilaPedidos().isEmpty()) {
            System.out.println("Nenhum pedido pendente.");
            } else {
                for (int i = 0; i < restaurante.getFilaPedidos().size(); i++) {
                Pedido p = restaurante.getFilaPedidos().get(i);
                
                System.out.println("\n" + (i + 1) + ". PEDIDO #" + p.getNumeroPedido());
                System.out.println("   Status: " + p.getStatus());
                System.out.println("   Cliente: " + p.getCliente().getNome());
                System.out.println("   Total: R$" + String.format("%.2f", p.getValorTotal()));
                
                // ✅ NOVO: Mostra itens do pedido
                System.out.println("   Itens:");
                for (ItemPedido item : p.getItens()) {
                    System.out.printf("   - %dx %s (R$%.2f)\n", 
                        item.getQuantidade(),
                        item.getProduto().getNome(),
                        item.calcularPrecoTotal()
                    );
                    
                    // ✅ NOVO: Mostra observações do cliente
                    if (!item.getObservacoes().isEmpty()) {
                        System.out.println("     Obs: " + item.getObservacoes());
                    }
                }
            }
        }
    }

    private static void gerenciarPedido(Restaurante restaurante) {
        // Implementação simplificada
        System.out.println("Funcionalidade de gerenciar pedidos");
        InputManager.pausar("");
    }

    private static void toggleRestaurante(Restaurante restaurante) {
        if (restaurante.estaAberto()) {
            restaurante.fecharRestaurante();
        } else {
            restaurante.abrirRestaurante();
        }
        InputManager.pausar("");
    }

    private static void verAvaliacoesRestaurante(Restaurante restaurante) {
        System.out.println("\n=== AVALIACOES ===");
        System.out.printf("Nota Media: %.1f\n", restaurante.calcularMediaAvaliacoes());
        System.out.printf("Total de Avaliacoes: %d\n", restaurante.getQuantidadeAvaliacoes());
        InputManager.pausar("");
    }

    private static void verRestaurantes() {
        InputManager.limparTela();
        repositorio.exibirLista();
        InputManager.pausar("");
    }
}