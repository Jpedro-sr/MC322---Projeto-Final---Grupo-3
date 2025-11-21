package ifome;

import ifome.model.*;
import ifome.util.*;

/**
 * Aplicação principal interativa do iFome.
 * Menu em terminal com fluxo completo de delivery.
 */
public class Aplicacao {

    private static SessaoUsuario sessao;
    private static RepositorioRestaurantes repositorio;

    public static void main(String[] args) {
        sessao = SessaoUsuario.getInstance();
        repositorio = RepositorioRestaurantes.getInstance();

        try {
            // Inicializar dados
            repositorio.inicializarRestaurantes();
            InputManager.pausar("\n🍔 Bem-vindo ao iFome! Sistema de Delivery");
            
            // Loop principal
            menuPrincipal();

        } catch (Exception e) {
            System.err.println("\n❌ ERRO FATAL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            InputManager.fechar();
        }
    }

    /**
     * Menu principal - exibido se nenhum usuário está logado.
     */
    private static void menuPrincipal() {
        while (true) {
            InputManager.limparTela();
            InputManager.linha();
            System.out.println("🍕 iFOME - SISTEMA DE DELIVERY");
            InputManager.linha();
            System.out.println("1. 👤 Fazer Login");
            System.out.println("2. ➕ Criar Nova Conta");
            System.out.println("3. 📋 Ver Restaurantes");
            System.out.println("4. ❌ Sair");
            InputManager.linha();

            int opcao = InputManager.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    fazerLogin();
                    if (sessao.estaLogado()) {
                        menuCliente();
                    }
                    break;
                case 2:
                    criarConta();
                    break;
                case 3:
                    verRestaurantes();
                    break;
                case 4:
                    System.out.println("\n👋 Obrigado por usar o iFome!");
                    return;
                default:
                    System.out.println("❌ Opção inválida.");
            }

            if (sessao.estaLogado()) {
                break;
            }
        }
    }

    /**
     * Realiza o login de um cliente.
     */
    private static void fazerLogin() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("🔐 LOGIN");
        InputManager.linha();

        String email = InputManager.lerEmail("📧 Email: ");
        String senha = InputManager.lerTexto("🔑 Senha: ");

        // Criar cliente com essas credenciais (simulação)
        Cliente cliente = new Cliente(email, senha, "Cliente", "11999999999");
        
        if (cliente.login(email, senha)) {
            sessao.setClienteLogado(cliente);
            System.out.println("\n✅ Login realizado com sucesso!");
            InputManager.pausar("");
        } else {
            System.out.println("\n❌ Credenciais inválidas.");
            InputManager.pausar("");
        }
    }

    /**
     * Cria uma nova conta.
     */
    private static void criarConta() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("📝 CRIAR NOVA CONTA");
        InputManager.linha();

        String nome = InputManager.lerTextoObrigatorio("👤 Nome completo: ");
        String email = InputManager.lerEmail("📧 Email: ");
        String senha = InputManager.lerTexto("🔑 Senha: ");
        String telefone = InputManager.lerTelefone("📞 Telefone: ");

        Cliente novoCliente = new Cliente(email, senha, nome, telefone);
        sessao.setClienteLogado(novoCliente);

        System.out.println("\n✅ Conta criada com sucesso!");
        System.out.println("   Bem-vindo, " + nome + "!");
        InputManager.pausar("");
    }

    /**
     * Menu do cliente logado.
     */
    private static void menuCliente() {
        while (sessao.estaLogado()) {
            InputManager.limparTela();
            InputManager.linha();
            System.out.println("🍕 iFOME - MENU CLIENTE");
            InputManager.linha();
            System.out.println("👤 Usuário: " + sessao.getClienteLogado().getNome());
            InputManager.linha();
            System.out.println("1. 🏪 Escolher Restaurante");
            System.out.println("2. 🛒 Ver Carrinho");
            System.out.println("3. 📦 Meus Pedidos");
            System.out.println("4. ⭐ Avaliar Pedido");
            System.out.println("5. 📍 Gerenciar Endereços");
            System.out.println("6. 🚪 Logout");
            InputManager.linha();

            int opcao = InputManager.lerInteiro("Escolha uma opção: ");

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
                    System.out.println("✅ Logout realizado!");
                    InputManager.pausar("");
                    return;
                default:
                    System.out.println("❌ Opção inválida.");
            }
        }
    }

    /**
     * Permite escolher um restaurante.
     */
    private static void escolherRestaurante() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("🏪 ESCOLHER RESTAURANTE");
        InputManager.linha();

        repositorio.exibirLista();
        
        int opcao = InputManager.lerInteiro("Escolha um restaurante (0 para voltar): ");

        if (opcao == 0) return;

        Restaurante restaurante = repositorio.obterPorIndice(opcao - 1);
        if (restaurante == null) {
            System.out.println("❌ Restaurante não encontrado.");
            InputManager.pausar("");
            return;
        }

        if (!restaurante.estaAberto()) {
            System.out.println("❌ Restaurante está fechado.");
            InputManager.pausar("");
            return;
        }

        sessao.setRestauranteAtual(restaurante);
        System.out.println("✅ Restaurante selecionado: " + restaurante.getNomeRestaurante());
        
        menuCardapio(restaurante);
    }

    /**
     * Exibe e gerencia o cardápio.
     */
    private static void menuCardapio(Restaurante restaurante) {
        Carrinho carrinho = sessao.getClienteLogado().getCarrinho();
        carrinho.setRestaurante(restaurante);
        carrinho.setCliente(sessao.getClienteLogado());

        while (true) {
            InputManager.limparTela();
            InputManager.linha();
            System.out.println("📋 CARDÁPIO - " + restaurante.getNomeRestaurante());
            InputManager.linha();

            java.util.List<Produto> produtos = restaurante.getCardapio();
            for (int i = 0; i < produtos.size(); i++) {
                Produto p = produtos.get(i);
                String status = p.isDisponivel() ? "✓" : "✗";
                System.out.printf("%d. [%s] %s - R$%.2f\n", 
                    i + 1, status, p.getNome(), p.getPreco());
                System.out.println("   " + p.getDescricao());
            }

            System.out.println("\n" + (produtos.size() + 1) + ". 🛒 Ver Carrinho");
            System.out.println((produtos.size() + 2) + ". ✅ Confirmar Pedido");
            System.out.println((produtos.size() + 3) + ". ⬅️  Voltar");
            InputManager.linha();

            int opcao = InputManager.lerInteiro("Escolha uma opção: ");

            if (opcao >= 1 && opcao <= produtos.size()) {
                Produto produto = produtos.get(opcao - 1);
                if (!produto.isDisponivel()) {
                    System.out.println("❌ Produto não disponível.");
                    InputManager.pausar("");
                    continue;
                }

                int quantidade = InputManager.lerInteiro("Quantidade: ");
                carrinho.adicionarItem(produto, quantidade, "");
                System.out.println("✅ Item adicionado!");
                InputManager.pausar("");

            } else if (opcao == produtos.size() + 1) {
                verCarrinho();

            } else if (opcao == produtos.size() + 2) {
                confirmarPedido();
                return;

            } else if (opcao == produtos.size() + 3) {
                return;

            } else {
                System.out.println("❌ Opção inválida.");
            }
        }
    }

    /**
     * Exibe o carrinho atual.
     */
    private static void verCarrinho() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("🛒 SEU CARRINHO");
        InputManager.linha();

        Carrinho carrinho = sessao.getClienteLogado().getCarrinho();
        System.out.println(carrinho.toString());

        if (!carrinho.getItens().isEmpty()) {
            System.out.println("Deseja remover algum item? (S/N): ");
            if (InputManager.lerConfirmacao("")) {
                java.util.List<ItemPedido> itens = carrinho.getItens();
                for (int i = 0; i < itens.size(); i++) {
                    System.out.println((i + 1) + ". " + itens.get(i).getProduto().getNome());
                }
                int indice = InputManager.lerInteiro("Qual item? (0 para cancelar): ");
                if (indice > 0 && indice <= itens.size()) {
                    carrinho.removerItem(itens.get(indice - 1));
                }
            }
        }

        InputManager.pausar("");
    }

    /**
     * Confirma o pedido e processa pagamento.
     */
    private static void confirmarPedido() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("✅ CONFIRMAR PEDIDO");
        InputManager.linha();

        Carrinho carrinho = sessao.getClienteLogado().getCarrinho();

        if (carrinho.getItens().isEmpty()) {
            System.out.println("❌ Carrinho vazio!");
            InputManager.pausar("");
            return;
        }

        try {
            Pedido pedido = sessao.getClienteLogado().fazerPedido(carrinho);

            // Escolher forma de pagamento
            System.out.println("\n💳 ESCOLHER FORMA DE PAGAMENTO");
            InputManager.linha();
            System.out.println("1. 📱 PIX");
            System.out.println("2. 💳 Cartão de Crédito");
            System.out.println("3. 💵 Dinheiro");
            InputManager.linha();

            int opcaoPagamento = InputManager.lerInteiro("Escolha: ");
            FormaPagamento pagamento = null;

            switch (opcaoPagamento) {
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
                    System.out.println("❌ Opção inválida.");
                    return;
            }

            pedido.setFormaPagamento(pagamento);
            pedido.processarPagamento();

            // Aceitar pedido no restaurante
            sessao.getClienteLogado().selecionarRestaurante(sessao.getRestauranteAtual());
            sessao.getRestauranteAtual().aceitarPedido(pedido);

            System.out.println("\n✅ PEDIDO CONFIRMADO!");
            System.out.println("📋 Número do pedido: #" + pedido.getNumeroPedido());
            System.out.println(pedido.gerarResumo());

            InputManager.pausar("");

        } catch (Exception e) {
            System.out.println("\n❌ Erro ao processar pedido: " + e.getMessage());
            InputManager.pausar("");
        }
    }

    /**
     * Exibe pedidos do cliente.
     */
    private static void verPedidos() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("📦 MEUS PEDIDOS");
        InputManager.linha();

        java.util.List<Pedido> pedidos = sessao.getClienteLogado().getHistoricoPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Você ainda não fez nenhum pedido.");
        } else {
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido p = pedidos.get(i);
                System.out.printf("%d. Pedido #%d - %s - R$%.2f\n", 
                    i + 1, p.getNumeroPedido(), p.getStatus(), p.getValorTotal());
            }
        }

        InputManager.pausar("");
    }

    /**
     * Avalia um pedido.
     */
    private static void avaliarPedido() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("⭐ AVALIAR PEDIDO");
        InputManager.linha();

        java.util.List<Pedido> pedidos = sessao.getClienteLogado().getHistoricoPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("Você não tem pedidos para avaliar.");
            InputManager.pausar("");
            return;
        }

        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            if (p.getStatus().equals("Entregue")) {
                System.out.printf("%d. Pedido #%d - R$%.2f\n", i + 1, p.getNumeroPedido(), p.getValorTotal());
            }
        }

        int indice = InputManager.lerInteiro("Escolha o pedido (0 para voltar): ");
        if (indice <= 0 || indice > pedidos.size()) return;

        Pedido pedido = pedidos.get(indice - 1);
        if (!pedido.getStatus().equals("Entregue")) {
            System.out.println("❌ Apenas pedidos entregues podem ser avaliados.");
            InputManager.pausar("");
            return;
        }

        int nota = InputManager.lerInteiro("Nota (1-5): ");
        String comentario = InputManager.lerTexto("Comentário: ");

        sessao.getClienteLogado().avaliarPedido(pedido, nota, comentario);
        System.out.println("✅ Avaliação registrada!");
        InputManager.pausar("");
    }

    /**
     * Gerencia endereços do cliente.
     */
    private static void gerenciarEnderecos() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("📍 MEUS ENDEREÇOS");
        InputManager.linha();

        Cliente cliente = sessao.getClienteLogado();
        java.util.List<Endereco> enderecos = cliente.getEnderecos();

        if (enderecos.isEmpty()) {
            System.out.println("Você não tem endereços cadastrados.");
        } else {
            for (int i = 0; i < enderecos.size(); i++) {
                System.out.println((i + 1) + ". " + enderecos.get(i).getEnderecoCompleto());
            }
        }

        System.out.println("\n1. ➕ Adicionar novo endereço");
        System.out.println("2. ⬅️  Voltar");

        int opcao = InputManager.lerInteiro("Escolha: ");

        if (opcao == 1) {
            String cep = InputManager.lerTexto("CEP: ");
            String rua = InputManager.lerTexto("Rua: ");
            String numero = InputManager.lerTexto("Número: ");
            String bairro = InputManager.lerTexto("Bairro: ");
            String cidade = InputManager.lerTexto("Cidade: ");
            String estado = InputManager.lerTexto("Estado: ");

            Endereco novoEndereco = new Endereco(cep, rua, numero, bairro, cidade, estado);
            cliente.adicionarEndereco(novoEndereco);
            System.out.println("✅ Endereço adicionado!");
        }

        InputManager.pausar("");
    }

    /**
     * Visualiza lista de restaurantes sem login.
     */
    private static void verRestaurantes() {
        InputManager.limparTela();
        repositorio.exibirLista();
        InputManager.pausar("");
    }
}