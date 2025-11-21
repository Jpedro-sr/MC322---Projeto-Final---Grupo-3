package ifome;

import ifome.model.*;
import ifome.util.*;
import ifome.exceptions.*;

/**
 * Aplicação principal do iFome
 * Gerencia os menus e fluxo da aplicação
 * @author Rafael e Matheus
 */
public class Aplicacao {

    private static SessaoUsuario sessao;
    private static RepositorioRestaurantes repositorio;

    public static void main(String[] args) {
        try {
            configurarEncoding();
            
            sessao = SessaoUsuario.getInstance();
            repositorio = RepositorioRestaurantes.getInstance();
            
            repositorio.inicializarRestaurantes();
            InputManager.pausar("\nBem-vindo ao iFome!");
            
            menuPrincipal();

        } catch (Exception e) {
            System.err.println("Erro na aplicação: " + e.getMessage());
            e.printStackTrace();
        } finally {
            repositorio.salvarDados();
            InputManager.fechar();
        }
    }

    private static void configurarEncoding() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                System.out.println("Sistema Windows detectado");
            }
        } catch (Exception e) {
            // ignorar
        }
    }

    private static void menuPrincipal() {
        boolean rodando = true;
        
        while (rodando) {
            if (!sessao.estaLogado()) {
                rodando = exibirMenuInicial();
            } else {
                if (sessao.ehCliente()) {
                    menuCliente();
                } else if (sessao.ehRestaurante()) {
                    menuRestaurante();
                }
            }
        }
        
        System.out.println("\nObrigado por usar o iFome!");
    }

    private static boolean exibirMenuInicial() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("iFOME - SISTEMA DE DELIVERY");
        InputManager.linha();
        System.out.println("1. [CLIENTE] Login");
        System.out.println("2. [CLIENTE] Criar Conta");
        System.out.println("3. [RESTAURANTE] Login");
        System.out.println("4. [RESTAURANTE] Cadastrar");
        System.out.println("5. Ver Restaurantes");
        System.out.println("0. Sair");
        InputManager.linha();

        Integer opcao = InputManager.lerInteiro("Escolha");
        if (opcao == null) return true;

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
                return false;
            default:
                System.out.println("Opcao invalida!");
                InputManager.pausar("");
        }

        return true;
    }

    private static void fazerLoginCliente() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("LOGIN - CLIENTE");
        InputManager.linha();

        String email = InputManager.lerEmail("Email");
        if (email == null) return;
        
        String senha = InputManager.lerTexto("Senha");
        if (senha == null || senha.isEmpty()) return;

        Cliente cliente = repositorio.buscarClientePorLogin(email, senha);
        
        if (cliente != null) {
            sessao.setClienteLogado(cliente);
            System.out.println("\nBem-vindo, " + cliente.getNome());
            InputManager.pausar("");
        } else {
            System.out.println("Email ou senha incorretos!");
            InputManager.pausar("");
        }
    }

    private static void fazerLoginRestaurante() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("LOGIN - RESTAURANTE");
        InputManager.linha();

        String email = InputManager.lerEmail("Email");
        if (email == null) return;
        
        String senha = InputManager.lerTexto("Senha");
        if (senha == null || senha.isEmpty()) return;

        Restaurante restaurante = repositorio.buscarRestaurantePorLogin(email, senha);
        
        if (restaurante != null) {
            sessao.setRestauranteLogado(restaurante);
            System.out.println("\nAcesso concedido!");
            InputManager.pausar("");
        } else {
            System.out.println("Credenciais inválidas!");
            InputManager.pausar("");
        }
    }

    private static void criarContaCliente() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("CADASTRO - CLIENTE");
        InputManager.linha();

        String nome = InputManager.lerTextoObrigatorio("Nome completo");
        if (nome == null) return;
        
        String email = InputManager.lerEmail("Email");
        if (email == null) return;
        
        if (repositorio.emailJaExiste(email)) {
            System.out.println("Email já existe!");
            InputManager.pausar("");
            return;
        }

        String senha = InputManager.lerTexto("Senha");
        String telefone = InputManager.lerTelefone("Telefone");

        Cliente novoCliente = new Cliente(email, senha, nome, telefone);
        repositorio.adicionarCliente(novoCliente);
        sessao.setClienteLogado(novoCliente);

        System.out.println("\nConta criada com sucesso!");
        InputManager.pausar("");
    }

    private static void cadastrarRestaurante() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("CADASTRO - RESTAURANTE");
        InputManager.linha();

        String nome = InputManager.lerTextoObrigatorio("Nome do Restaurante");
        if (nome == null) return;
        
        String email = InputManager.lerEmail("Email");
        if (email == null) return;
        
        if (repositorio.emailJaExiste(email)) {
            System.out.println("Email já existe!");
            InputManager.pausar("");
            return;
        }

        String senha = InputManager.lerTexto("Senha");
        String cnpj = InputManager.lerTexto("CNPJ");

        Restaurante novoRestaurante = new Restaurante(email, senha, nome, cnpj);
        repositorio.adicionarRestaurante(novoRestaurante);
        sessao.setRestauranteLogado(novoRestaurante);

        System.out.println("\nRestaurante cadastrado!");
        InputManager.pausar("");
    }

    private static void menuCliente() {
        InputManager.limparTela();
        Cliente cliente = sessao.getClienteLogado();
        
        InputManager.linha();
        System.out.println("MENU - CLIENTE");
        InputManager.linha();
        System.out.println("Usuário: " + cliente.getNome());
        InputManager.linha();
        System.out.println("1. Escolher Restaurante");
        System.out.println("2. Ver Carrinho");
        System.out.println("3. Meus Pedidos");
        System.out.println("4. Avaliar Pedido");
        System.out.println("5. Meus Endereços");
        System.out.println("6. Logout");
        InputManager.linha();

        Integer opcao = InputManager.lerInteiro("Escolha");
        if (opcao == null) return;

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
                System.out.println("Até logo!");
                InputManager.pausar("");
                break;
            default:
                System.out.println("Opcao inválida!");
                InputManager.pausar("");
        }
    }

    private static void menuRestaurante() {
        InputManager.limparTela();
        Restaurante restaurante = sessao.getRestauranteLogado();
        
        InputManager.linha();
        System.out.println("MENU - RESTAURANTE");
        InputManager.linha();
        System.out.println("Restaurante: " + restaurante.getNomeRestaurante());
        String status = restaurante.estaAberto() ? "[ABERTO]" : "[FECHADO]";
        System.out.println("Status: " + status);
        InputManager.linha();
        System.out.println("1. Ver Cardápio");
        System.out.println("2. Adicionar Produto");
        System.out.println("3. Remover Produto");
        System.out.println("4. Ver Pedidos");
        System.out.println("5. Atualizar Pedido");
        System.out.println("6. Abrir/Fechar");
        System.out.println("7. Ver Avaliações");
        System.out.println("8. Logout");
        InputManager.linha();

        Integer opcao = InputManager.lerInteiro("Escolha");
        if (opcao == null) return;

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
                atualizarStatusPedido(restaurante);
                break;
            case 6:
                if (restaurante.estaAberto()) {
                    restaurante.fecharRestaurante();
                    System.out.println("Restaurante fechado!");
                } else {
                    restaurante.abrirRestaurante();
                    System.out.println("Restaurante aberto!");
                }
                InputManager.pausar("");
                break;
            case 7:
                verAvaliacoes(restaurante);
                break;
            case 8:
                sessao.logout();
                System.out.println("Até logo!");
                InputManager.pausar("");
                break;
            default:
                System.out.println("Opção inválida!");
                InputManager.pausar("");
        }
    }

    private static void escolherRestaurante() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("ESCOLHER RESTAURANTE");
        InputManager.linha();

        repositorio.exibirLista();
        
        Integer opcao = InputManager.lerInteiro("\nEscolha (0 para voltar)");
        if (opcao == null || opcao == 0) return;

        Restaurante restaurante = repositorio.obterPorIndice(opcao - 1);
        if (restaurante == null) {
            System.out.println("Restaurante não encontrado!");
            InputManager.pausar("");
            return;
        }

        if (!restaurante.estaAberto()) {
            System.out.println("Restaurante fechado no momento!");
            InputManager.pausar("");
            return;
        }

        sessao.setRestauranteAtual(restaurante);
        menuCardapio(restaurante);
    }

    private static void menuCardapio(Restaurante restaurante) {
        boolean continua = true;
        
        while (continua && sessao.estaLogado()) {
            InputManager.limparTela();
            InputManager.linha();
            System.out.println("CARDÁPIO - " + restaurante.getNomeRestaurante());
            InputManager.linha();

            java.util.List<Produto> produtos = restaurante.getCardapio();
            for (int i = 0; i < produtos.size(); i++) {
                Produto p = produtos.get(i);
                String ok = p.isDisponivel() ? "[OK]" : "[X]";
                System.out.printf("%d. %s %s - R$%.2f\n", 
                    i + 1, ok, p.getNome(), p.getPreco());
                System.out.println("   " + p.getDescricao());
            }

            System.out.println("\n" + (produtos.size() + 1) + ". Carrinho");
            System.out.println((produtos.size() + 2) + ". Finalizar");
            System.out.println((produtos.size() + 3) + ". Voltar");
            InputManager.linha();

            Integer opcao = InputManager.lerInteiro("Escolha");
            if (opcao == null) continue;

            if (opcao >= 1 && opcao <= produtos.size()) {
                adicionarAoCarrinho(produtos.get(opcao - 1), restaurante);
            } else if (opcao == produtos.size() + 1) {
                verCarrinho();
            } else if (opcao == produtos.size() + 2) {
                confirmarPedido();
                continua = false;
            } else if (opcao == produtos.size() + 3) {
                continua = false;
            }
        }
    }

    private static void adicionarAoCarrinho(Produto produto, Restaurante restaurante) {
        if (!produto.isDisponivel()) {
            System.out.println("Produto indisponível!");
            InputManager.pausar("");
            return;
        }

        Integer qtd = InputManager.lerInteiro("Quantidade");
        if (qtd == null || qtd <= 0) {
            System.out.println("Quantidade inválida!");
            InputManager.pausar("");
            return;
        }

        Cliente cliente = sessao.getClienteLogado();
        Carrinho carrinho = cliente.getCarrinho();
        carrinho.setRestaurante(restaurante);
        carrinho.setCliente(cliente);
        
        String obs = InputManager.lerTexto("Observações (Enter para pular)");
        carrinho.adicionarItem(produto, qtd, obs);
        
        System.out.println("Adicionado ao carrinho!");
        InputManager.pausar("");
    }

    private static void verCarrinho() {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("CARRINHO");
        InputManager.linha();

        Carrinho carrinho = sessao.getClienteLogado().getCarrinho();
        System.out.println(carrinho.toString());

        if (!carrinho.getItens().isEmpty()) {
            if (InputManager.lerConfirmacao("\nRemover item?")) {
                java.util.List<ItemPedido> itens = carrinho.getItens();
                for (int i = 0; i < itens.size(); i++) {
                    System.out.println((i + 1) + ". " + itens.get(i).getProduto().getNome());
                }
                Integer idx = InputManager.lerInteiro("Qual? (0 para cancelar)");
                if (idx != null && idx > 0 && idx <= itens.size()) {
                    carrinho.removerItem(itens.get(idx - 1));
                    System.out.println("Removido!");
                }
            }
        }

        InputManager.pausar("");
    }

    private static void confirmarPedido() {
        Cliente cliente = sessao.getClienteLogado();
        Carrinho carrinho = cliente.getCarrinho();

        if (carrinho.getRestaurante() == null) {
            System.out.println("Selecione um restaurante primeiro!");
            InputManager.pausar("");
            return;
        }

        if (carrinho.getItens().isEmpty()) {
            System.out.println("Carrinho vazio!");
            InputManager.pausar("");
            return;
        }

        if (cliente.getEnderecos().isEmpty()) {
            System.out.println("Cadastre um endereço!");
            adicionarEndereco(cliente);
            if (cliente.getEnderecos().isEmpty()) {
                return;
            }
        }

        System.out.println("\n--- Endereço de entrega ---");
        for (int i = 0; i < cliente.getEnderecos().size(); i++) {
            System.out.println((i + 1) + ". " + cliente.getEnderecos().get(i).getEnderecoCompleto());
        }
        Integer idEnd = InputManager.lerInteiro("Escolha");
        if (idEnd == null || idEnd < 1 || idEnd > cliente.getEnderecos().size()) {
            System.out.println("Endereço inválido!");
            InputManager.pausar("");
            return;
        }

        System.out.println("\n--- Forma de Pagamento ---");
        System.out.println("1. PIX");
        System.out.println("2. Cartão");
        System.out.println("3. Dinheiro");
        Integer opcaoPag = InputManager.lerInteiro("Escolha");
        if (opcaoPag == null) return;

        FormaPagamento pag;
        switch (opcaoPag) {
            case 1:
                pag = new PIX();
                break;
            case 2:
                pag = new CartaoCredito("1234567890123456", "CLIENTE", "123");
                break;
            case 3:
                Double val = InputManager.lerDouble("Valor recebido (R$)");
                if (val == null) {
                    System.out.println("Valor inválido!");
                    InputManager.pausar("");
                    return;
                }
                pag = new Dinheiro(val);
                break;
            default:
                System.out.println("Opção inválida!");
                InputManager.pausar("");
                return;
        }

        try {
            Pedido pedido = carrinho.gerarPedido();
            pedido.setFormaPagamento(pag);
            pedido.processarPagamento();

            Restaurante rest = carrinho.getRestaurante();
            rest.aceitarPedido(pedido);

            System.out.println("\nPEDIDO CONFIRMADO!");
            System.out.println(pedido.gerarResumo());

            cliente.getCarrinho().limparCarrinho();

        } catch (Exception e) {
            System.out.println("\nErro: " + e.getMessage());
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
            System.out.println("Nenhum pedido realizado!");
        } else {
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido p = pedidos.get(i);
                System.out.printf("%d. #%d - %s - R$%.2f\n", 
                    i + 1, p.getNumeroPedido(), p.getStatus(), p.getValorTotal());
            }
        }

        InputManager.pausar("");
    }

    private static void avaliarPedido() {
        InputManager.limparTela();
        java.util.List<Pedido> pedidos = sessao.getClienteLogado().getHistoricoPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Sem pedidos para avaliar!");
            InputManager.pausar("");
            return;
        }

        System.out.println("Seus pedidos:");
        for (int i = 0; i < pedidos.size(); i++) {
            System.out.printf("%d. #%d - %s\n", i + 1, pedidos.get(i).getNumeroPedido(), pedidos.get(i).getStatus());
        }

        Integer idx = InputManager.lerInteiro("Escolha (0 para cancelar)");
        if (idx == null || idx < 1 || idx > pedidos.size()) return;

        Pedido ped = pedidos.get(idx - 1);

        if (!ped.getStatus().equals("Entregue")) {
            System.out.println("Só pedidos entregues podem ser avaliados!");
            InputManager.pausar("");
            return;
        }

        Integer nota = InputManager.lerInteiro("Nota (1-5)");
        if (nota == null || nota < 1 || nota > 5) {
            System.out.println("Nota inválida!");
            InputManager.pausar("");
            return;
        }

        String coment = InputManager.lerTexto("Comentário");
        sessao.getClienteLogado().avaliarPedido(ped, nota, coment);

        System.out.println("Avaliação enviada!");
        InputManager.pausar("");
    }

    private static void gerenciarEnderecos() {
        InputManager.limparTela();
        Cliente cliente = sessao.getClienteLogado();
        
        InputManager.linha();
        System.out.println("MEUS ENDEREÇOS");
        InputManager.linha();

        java.util.List<Endereco> ends = cliente.getEnderecos();

        if (ends.isEmpty()) {
            System.out.println("Nenhum endereço!");
        } else {
            for (int i = 0; i < ends.size(); i++) {
                System.out.println((i + 1) + ". " + ends.get(i).getEnderecoCompleto());
            }
        }

        System.out.println("\n1. Adicionar");
        System.out.println("2. Voltar");

        Integer op = InputManager.lerInteiro("Escolha");
        if (op == null) return;

        if (op == 1) {
            adicionarEndereco(cliente);
        }
    }

    private static void adicionarEndereco(Cliente cliente) {
        System.out.println("\n--- Novo Endereço ---");
        String cep = InputManager.lerTexto("CEP");
        String rua = InputManager.lerTexto("Rua");
        String num = InputManager.lerTexto("Número");
        String bairro = InputManager.lerTexto("Bairro");
        String cidade = InputManager.lerTexto("Cidade");
        String uf = InputManager.lerTexto("Estado");

        Endereco novo = new Endereco(cep, rua, num, bairro, cidade, uf);
        cliente.adicionarEndereco(novo);
        System.out.println("Adicionado!");
        InputManager.pausar("");
    }

    private static void adicionarProdutoCardapio(Restaurante restaurante) {
        System.out.println("\n--- Novo Produto ---");
        System.out.println("1-Comida 2-Bebida 3-Sobremesa 4-Adicional");
        Integer tipo = InputManager.lerInteiro("Tipo");
        if (tipo == null) return;

        String nome = InputManager.lerTextoObrigatorio("Nome");
        if (nome == null) return;
        String desc = InputManager.lerTexto("Descrição");
        Double preco = InputManager.lerDouble("Preço (R$)");
        if (preco == null) return;

        Produto prod;
        switch (tipo) {
            case 1:
                prod = new Comida(nome, desc, preco, false);
                break;
            case 2:
                prod = new Bebida(nome, desc, preco, 350);
                break;
            case 3:
                prod = new Sobremesa(nome, desc, preco);
                break;
            case 4:
                prod = new Adicional(nome, preco);
                break;
            default:
                System.out.println("Tipo inválido!");
                InputManager.pausar("");
                return;
        }

        restaurante.adicionarProdutoCardapio(prod);
        System.out.println("Produto adicionado!");
        InputManager.pausar("");
    }

    private static void removerProdutoCardapio(Restaurante restaurante) {
        restaurante.exibirCardapio();
        
        if (restaurante.getCardapio().isEmpty()) {
            InputManager.pausar("");
            return;
        }

        Integer idx = InputManager.lerInteiro("\nQual? (0 para cancelar)");
        if (idx == null || idx < 1 || idx > restaurante.getCardapio().size()) {
            InputManager.pausar("");
            return;
        }

        Produto p = restaurante.getCardapio().get(idx - 1);
        restaurante.removerProdutoCardapio(p);
        System.out.println("Removido!");
        InputManager.pausar("");
    }

    private static void verPedidosPendentes(Restaurante restaurante) {
        InputManager.limparTela();
        InputManager.linha();
        System.out.println("PEDIDOS");
        InputManager.linha();
        
        if (restaurante.getFilaPedidos().isEmpty()) {
            System.out.println("Nenhum pedido!");
        } else {
            for (int i = 0; i < restaurante.getFilaPedidos().size(); i++) {
                Pedido p = restaurante.getFilaPedidos().get(i);
                
                System.out.println((i + 1) + ". PEDIDO #" + p.getNumeroPedido());
                System.out.println("   Status: " + p.getStatus());
                System.out.println("   Cliente: " + p.getCliente().getNome());
                System.out.println("   Total: R$" + String.format("%.2f", p.getValorTotal()));
                
                System.out.println("   Itens:");
                for (ItemPedido item : p.getItens()) {
                    System.out.printf("   - %dx %s (R$%.2f)\n", 
                        item.getQuantidade(),
                        item.getProduto().getNome(),
                        item.calcularPrecoTotal()
                    );
                    
                    if (!item.getObservacoes().isEmpty()) {
                        System.out.println("     Obs: " + item.getObservacoes());
                    }
                }
            }
        }
        InputManager.pausar("");
    }

    private static void atualizarStatusPedido(Restaurante restaurante) {
        InputManager.limparTela();
        
        if (restaurante.getFilaPedidos().isEmpty()) {
            System.out.println("Sem pedidos!");
            InputManager.pausar("");
            return;
        }

        java.util.List<Pedido> peds = restaurante.getFilaPedidos();
        System.out.println("Pedidos:");
        for (int i = 0; i < peds.size(); i++) {
            System.out.println((i + 1) + ". #" + peds.get(i).getNumeroPedido() + 
                             " - " + peds.get(i).getStatus());
        }

        Integer idx = InputManager.lerInteiro("Escolha (0 para cancelar)");
        if (idx == null || idx < 1 || idx > peds.size()) return;

        Pedido ped = peds.get(idx - 1);

        System.out.println("Status: " + ped.getStatus());
        System.out.println("1-Preparando 2-Pronto 3-Em Entrega 4-Entregue 5-Cancelado");

        Integer op = InputManager.lerInteiro("Novo status");
        if (op == null) return;

        String stat;
        switch (op) {
            case 1: stat = "Preparando"; break;
            case 2: stat = "Pronto"; break;
            case 3: stat = "Em Entrega"; break;
            case 4: stat = "Entregue"; break;
            case 5: stat = "Cancelado"; break;
            default: return;
        }

        restaurante.atualizarStatusPedido(ped, stat);
        System.out.println("Atualizado!");
        InputManager.pausar("");
    }

    private static void verAvaliacoes(Restaurante restaurante) {
        InputManager.limparTela();
        System.out.println("\n=== AVALIAÇÕES ===");
        System.out.printf("Média: %.1f/5\n", restaurante.calcularMediaAvaliacoes());
        System.out.printf("Total: %d\n", restaurante.getQuantidadeAvaliacoes());
        
        if (!restaurante.getAvaliacoes().isEmpty()) {
            System.out.println("\nComentários:");
            for (Avaliacao av : restaurante.getAvaliacoes()) {
                System.out.println("- " + av.getNota() + "/5: " + av.getComentario());
            }
        }
        InputManager.pausar("");
    }

    private static void verRestaurantes() {
        InputManager.limparTela();
        repositorio.exibirLista();
        InputManager.pausar("");
    }
}