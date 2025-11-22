package ifome;

import ifome.model.*;
import ifome.util.*;
import ifome.exceptions.*;

/**
 * Aplicação principal do iFome
 * VERSÃO COMPLETA - Persistência, exceções específicas e funcionalidades implementadas
 */
public class AplicacaoConsole {

    private static SessaoUsuario sessao;
    private static RepositorioRestaurantes repositorio;

    public static void main(String[] args) {
        try {
            sessao = SessaoUsuario.getInstance();
            repositorio = RepositorioRestaurantes.getInstance();
            
            repositorio.inicializarRestaurantes();
            
            System.out.println("\n==================================================");
            System.out.println("       Bem-vindo ao iFome!");
            System.out.println("==================================================");
            InputManager.pausar("");
            
            menuPrincipal();

        } catch (Exception e) {
            System.err.println("Erro crítico na aplicação: " + e.getMessage());
            e.printStackTrace();
        } finally {
            repositorio.salvarDados();
            InputManager.fechar();
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
        System.out.println("==================================================");
        System.out.println("       iFOME - SISTEMA DE DELIVERY");
        System.out.println("==================================================");
        System.out.println("1. [CLIENTE] Login");
        System.out.println("2. [CLIENTE] Criar Conta");
        System.out.println("3. [RESTAURANTE] Login");
        System.out.println("4. [RESTAURANTE] Cadastrar");
        System.out.println("5. Ver Restaurantes");
        System.out.println("0. Sair");
        System.out.println("==================================================");

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
                System.out.println("Opção inválida!");
                InputManager.pausar("");
        }

        return true;
    }

    private static void fazerLoginCliente() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       LOGIN - CLIENTE");
        System.out.println("==================================================");

        String email = InputManager.lerEmail("Email");
        if (email == null) return;
        
        String senha = InputManager.lerTexto("Senha");
        if (senha == null || senha.isEmpty()) return;

        Cliente cliente = repositorio.buscarClientePorLogin(email, senha);
        
        if (cliente != null) {
            sessao.setClienteLogado(cliente);
            System.out.println("\nBem-vindo, " + cliente.getNome() + "!");
            InputManager.pausar("");
        } else {
            System.out.println("\nEmail ou senha incorretos!");
            InputManager.pausar("");
        }
    }

    private static void fazerLoginRestaurante() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       LOGIN - RESTAURANTE");
        System.out.println("==================================================");

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
            System.out.println("\nCredenciais inválidas!");
            InputManager.pausar("");
        }
    }

    private static void criarContaCliente() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       CADASTRO - CLIENTE");
        System.out.println("==================================================");

        String nome = InputManager.lerTextoObrigatorio("Nome completo");
        if (nome == null) return;
        
        String email = InputManager.lerEmail("Email");
        if (email == null) return;
        
        if (repositorio.emailJaExiste(email)) {
            System.out.println("\nEmail já existe!");
            InputManager.pausar("");
            return;
        }

        String senha = InputManager.lerTexto("Senha");
        if (senha == null || senha.isEmpty()) return;
        
        String telefone = InputManager.lerTelefone("Telefone");
        if (telefone == null) return;

        Cliente novoCliente = new Cliente(email, senha, nome, telefone);
        repositorio.adicionarCliente(novoCliente);
        sessao.setClienteLogado(novoCliente);

        System.out.println("\nConta criada com sucesso!");
        InputManager.pausar("");
    }

    private static void cadastrarRestaurante() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       CADASTRO - RESTAURANTE");
        System.out.println("==================================================");

        String nome = InputManager.lerTextoObrigatorio("Nome do Restaurante");
        if (nome == null) return;
        
        String email = InputManager.lerEmail("Email");
        if (email == null) return;
        
        if (repositorio.emailJaExiste(email)) {
            System.out.println("\nEmail já existe!");
            InputManager.pausar("");
            return;
        }

        String senha = InputManager.lerTexto("Senha");
        if (senha == null || senha.isEmpty()) return;
        
        String cnpj = InputManager.lerTexto("CNPJ");
        if (cnpj == null) return;

        Restaurante novoRestaurante = new Restaurante(email, senha, nome, cnpj);
        novoRestaurante.abrirRestaurante();
        repositorio.adicionarRestaurante(novoRestaurante);
        sessao.setRestauranteLogado(novoRestaurante);

        System.out.println("\nRestaurante cadastrado e ABERTO!");
        InputManager.pausar("");
    }

    private static void menuCliente() {
        InputManager.limparTela();
        Cliente cliente = sessao.getClienteLogado();
        
        System.out.println("==================================================");
        System.out.println("       MENU - CLIENTE");
        System.out.println("==================================================");
        System.out.println("Usuário: " + cliente.getNome());
        System.out.println("==================================================");
        System.out.println("1. Escolher Restaurante");
        System.out.println("2. Ver Carrinho");
        System.out.println("3. Meus Pedidos");
        System.out.println("4. Avaliar Pedido");
        System.out.println("5. Meus Endereços");
        System.out.println("6. Ver Cupons Disponíveis");
        System.out.println("7. Logout");
        System.out.println("==================================================");

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
                repositorio.exibirCuponsDisponiveis();
                InputManager.pausar("");
                break;
            case 7:
                sessao.logout();
                System.out.println("\nAté logo!");
                InputManager.pausar("");
                break;
            default:
                System.out.println("Opção inválida!");
                InputManager.pausar("");
        }
    }

    private static void menuRestaurante() {
        InputManager.limparTela();
        Restaurante restaurante = sessao.getRestauranteLogado();
        
        System.out.println("==================================================");
        System.out.println("       MENU - RESTAURANTE");
        System.out.println("==================================================");
        System.out.println("Restaurante: " + restaurante.getNomeRestaurante());
        String status = restaurante.estaAberto() ? "[ABERTO]" : "[FECHADO]";
        System.out.println("Status: " + status);
        System.out.println("==================================================");
        System.out.println("1. Ver Cardápio");
        System.out.println("2. Adicionar Produto");
        System.out.println("3. Remover Produto");
        System.out.println("4. Gerenciar Pedidos");
        System.out.println("5. Atualizar Status Pedido");
        System.out.println("6. Abrir/Fechar");
        System.out.println("7. Ver Avaliações");
        System.out.println("8. Logout");
        System.out.println("==================================================");

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
                gerenciarPedidos(restaurante);
                break;
            case 5:
                atualizarStatusPedido(restaurante);
                break;
            case 6:
                if (restaurante.estaAberto()) {
                    restaurante.fecharRestaurante();
                    System.out.println("\nRestaurante fechado!");
                } else {
                    restaurante.abrirRestaurante();
                    System.out.println("\nRestaurante aberto!");
                }
                InputManager.pausar("");
                break;
            case 7:
                verAvaliacoes(restaurante);
                break;
            case 8:
                sessao.logout();
                System.out.println("\nAté logo!");
                InputManager.pausar("");
                break;
            default:
                System.out.println("Opção inválida!");
                InputManager.pausar("");
        }
    }

    private static void escolherRestaurante() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       ESCOLHER RESTAURANTE");
        System.out.println("==================================================");

        repositorio.exibirLista();
        
        Integer opcao = InputManager.lerInteiro("\nEscolha (0 para voltar)");
        if (opcao == null || opcao == 0) return;

        Restaurante restaurante = repositorio.obterPorIndice(opcao - 1);
        if (restaurante == null) {
            System.out.println("\nRestaurante não encontrado!");
            InputManager.pausar("");
            return;
        }

        if (!restaurante.estaAberto()) {
            System.out.println("\nRestaurante fechado no momento!");
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
            System.out.println("==================================================");
            System.out.println("       CARDÁPIO - " + restaurante.getNomeRestaurante());
            System.out.println("==================================================");

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
            System.out.println("==================================================");

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
            System.out.println("\nProduto indisponível!");
            InputManager.pausar("");
            return;
        }

        Integer qtd = InputManager.lerInteiroPositivo("Quantidade");
        if (qtd == null) return;

        Cliente cliente = sessao.getClienteLogado();
        Carrinho carrinho = cliente.getCarrinho();
        carrinho.setRestaurante(restaurante);
        carrinho.setCliente(cliente);
        
        String obs = InputManager.lerTexto("Observações (Enter para pular)");
        carrinho.adicionarItem(produto, qtd, obs);
        
        System.out.println("\nAdicionado ao carrinho!");
        InputManager.pausar("");
    }

    private static void verCarrinho() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       CARRINHO");
        System.out.println("==================================================");

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
                    System.out.println("\nRemovido!");
                }
            }
        }

        InputManager.pausar("");
    }

    private static void confirmarPedido() {
        Cliente cliente = sessao.getClienteLogado();
        Carrinho carrinho = cliente.getCarrinho();

        try {
            if (carrinho.getRestaurante() == null) {
                throw new RestauranteFechadoException("Selecione um restaurante primeiro!");
            }

            if (carrinho.getItens().isEmpty()) {
                throw new ValorMinimoException("Carrinho vazio!");
            }

            // Verifica endereços
            if (cliente.getEnderecos().isEmpty()) {
                System.out.println("\nCadastre um endereço!");
                adicionarEndereco(cliente);
                if (cliente.getEnderecos().isEmpty()) {
                    return;
                }
            }

            // Seleciona endereço
            System.out.println("\n--- Endereço de entrega ---");
            for (int i = 0; i < cliente.getEnderecos().size(); i++) {
                System.out.println((i + 1) + ". " + cliente.getEnderecos().get(i).getEnderecoCompleto());
            }
            Integer idEnd = InputManager.lerInteiro("Escolha");
            if (idEnd == null || idEnd < 1 || idEnd > cliente.getEnderecos().size()) {
                System.out.println("\nEndereço inválido!");
                InputManager.pausar("");
                return;
            }

            // Forma de pagamento com input dinâmico
            System.out.println("\n--- Forma de Pagamento ---");
            System.out.println("1. PIX");
            System.out.println("2. Cartão de Crédito");
            System.out.println("3. Dinheiro");
            Integer opcaoPag = InputManager.lerInteiro("Escolha");
            if (opcaoPag == null) return;

            FormaPagamento pag;
            switch (opcaoPag) {
                case 1:
                    pag = new PIX();
                    break;
                case 2:
                    // SOLICITA DADOS DO CARTÃO DINAMICAMENTE
                    System.out.println("\n--- Dados do Cartão ---");
                    String numeroCartao = InputManager.lerTexto("Número (16 dígitos)");
                    if (numeroCartao == null || numeroCartao.isEmpty()) return;
                    
                    String titular = InputManager.lerTexto("Nome do Titular");
                    if (titular == null || titular.isEmpty()) return;
                    
                    String cvv = InputManager.lerTexto("CVV (3 dígitos)");
                    if (cvv == null || cvv.isEmpty()) return;
                    
                    String validade = InputManager.lerTexto("Validade (MM/YY)");
                    if (validade == null || validade.isEmpty()) validade = "12/25";
                    
                    pag = new CartaoCredito(numeroCartao, titular, cvv, validade);
                    break;
                case 3:
                    Double val = InputManager.lerDouble("Valor recebido (R$)");
                    if (val == null) return;
                    pag = new Dinheiro(val);
                    break;
                default:
                    System.out.println("\nOpção inválida!");
                    InputManager.pausar("");
                    return;
            }

            // Cupom de desconto
            System.out.println("\n--- Cupom de Desconto ---");
            if (InputManager.lerConfirmacao("Possui cupom de desconto?")) {
                String codigoCupom = InputManager.lerTexto("Digite o código do cupom");
                if (codigoCupom != null && !codigoCupom.isEmpty()) {
                    Cupom cupom = repositorio.buscarCupom(codigoCupom);
                    if (cupom != null && cupom.estaValido()) {
                        carrinho.aplicarCupom(cupom);
                        System.out.println("\nCupom aplicado com sucesso!");
                        System.out.println(cupom.toString());
                    } else {
                        System.out.println("\nCupom inválido ou expirado!");
                    }
                }
            }

            // Mostra resumo
            System.out.println("\n--- Resumo do Pedido ---");
            System.out.printf("Subtotal: R$%.2f\n", carrinho.calcularPrecoTotal());
            if (carrinho.getCupomAplicado() != null) {
                System.out.printf("Desconto: -R$%.2f\n", 
                    carrinho.calcularPrecoTotal() - carrinho.calcularTotalComDesconto());
            }
            System.out.printf("TOTAL: R$%.2f\n", carrinho.calcularTotalComDesconto());
            
            if (!InputManager.lerConfirmacao("\nConfirmar pedido?")) {
                System.out.println("\nPedido cancelado!");
                InputManager.pausar("");
                return;
            }

            // Gera pedido (pode lançar exceções)
            Pedido pedido = carrinho.gerarPedido();
            pedido.setFormaPagamento(pag);
            pedido.atualizarStatus("Pendente");
            
            // Processa pagamento (pode lançar PagamentoRecusadoException)
            pedido.processarPagamento();

            // Aceita pedido no restaurante
            Restaurante rest = carrinho.getRestaurante();
            rest.aceitarPedido(pedido);
            
            // Adiciona ao histórico e repositório
            cliente.adicionarPedido(pedido);
            repositorio.adicionarPedido(pedido);

            System.out.println("\n==================================================");
            System.out.println("       PEDIDO REALIZADO COM SUCESSO!");
            System.out.println("==================================================");
            System.out.println("Pedido #" + pedido.getNumeroPedido());
            System.out.println("Status: " + pedido.getStatus());
            System.out.println("==================================================");

            cliente.getCarrinho().limparCarrinho();

        } catch (RestauranteFechadoException e) {
            System.out.println("\n❌ ERRO: " + e.getMessage());
            System.out.println("O restaurante está fechado no momento.");
        } catch (ValorMinimoException e) {
            System.out.println("\n❌ ERRO: " + e.getMessage());
            System.out.println("O valor mínimo do pedido é R$15,00.");
        } catch (ProdutoIndisponivelException e) {
            System.out.println("\n❌ ERRO: " + e.getMessage());
            System.out.println("Algum produto do carrinho não está disponível.");
        } catch (PagamentoRecusadoException e) {
            System.out.println("\n❌ ERRO: " + e.getMessage());
            System.out.println("Verifique os dados de pagamento e tente novamente.");
        } catch (Exception e) {
            System.out.println("\n❌ ERRO INESPERADO: " + e.getMessage());
        }

        InputManager.pausar("");
    }

    private static void verPedidos() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       MEUS PEDIDOS");
        System.out.println("==================================================");

        java.util.List<Pedido> pedidos = sessao.getClienteLogado().getHistoricoPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido realizado!");
        } else {
            for (int i = 0; i < pedidos.size(); i++) {
                Pedido p = pedidos.get(i);
                System.out.printf("%d. #%d - %s - R$%.2f - %s\n", 
                    i + 1, p.getNumeroPedido(), p.getRestaurante().getNomeRestaurante(),
                    p.getValorTotal(), p.getStatus());
            }
        }

        InputManager.pausar("");
    }

    /**
     * IMPLEMENTAÇÃO COMPLETA: Avaliar pedidos entregues
     */
    private static void avaliarPedido() {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       AVALIAR PEDIDO");
        System.out.println("==================================================");
        
        Cliente cliente = sessao.getClienteLogado();
        java.util.List<Pedido> pedidos = cliente.getHistoricoPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Sem pedidos para avaliar!");
            InputManager.pausar("");
            return;
        }

        // Filtra apenas pedidos entregues
        java.util.List<Pedido> pedidosEntregues = new java.util.ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getStatus().equals("Entregue")) {
                pedidosEntregues.add(p);
            }
        }

        if (pedidosEntregues.isEmpty()) {
            System.out.println("Nenhum pedido entregue para avaliar!");
            InputManager.pausar("");
            return;
        }

        System.out.println("Pedidos entregues:\n");
        for (int i = 0; i < pedidosEntregues.size(); i++) {
            Pedido p = pedidosEntregues.get(i);
            boolean jaAvaliado = !p.getAvaliacoes().isEmpty();
            String statusAvaliacao = jaAvaliado ? "[JÁ AVALIADO]" : "[PENDENTE]";
            System.out.printf("%d. #%d - %s - R$%.2f %s\n", 
                i + 1, 
                p.getNumeroPedido(), 
                p.getRestaurante().getNomeRestaurante(),
                p.getValorTotal(),
                statusAvaliacao);
        }

        Integer idx = InputManager.lerInteiro("\nEscolha o pedido (0 para cancelar)");
        if (idx == null || idx < 1 || idx > pedidosEntregues.size()) return;

        Pedido ped = pedidosEntregues.get(idx - 1);

        System.out.println("\n--- Avaliação do Pedido #" + ped.getNumeroPedido() + " ---");
        System.out.println("Restaurante: " + ped.getRestaurante().getNomeRestaurante());
        
        Integer nota = InputManager.lerInteiro("Nota (1-5)");
        if (nota == null || nota < 1 || nota > 5) {
            System.out.println("\nNota inválida! Deve ser entre 1 e 5.");
            InputManager.pausar("");
            return;
        }

        String coment = InputManager.lerTexto("Comentário (opcional)");
        
        // Avalia o pedido
        cliente.avaliarPedido(ped, nota, coment != null ? coment : "");
        
        // Também avalia o restaurante
        Restaurante rest = ped.getRestaurante();
        rest.avaliar(nota, coment != null ? coment : "");

        System.out.println("\n==================================================");
        System.out.println("       Avaliação enviada com sucesso!");
        System.out.println("==================================================");
        System.out.printf("Nota: %d/5 - %s\n", nota, getDescricaoNota(nota));
        if (coment != null && !coment.isEmpty()) {
            System.out.println("Comentário: " + coment);
        }
        
        repositorio.salvarDados(); // Salva a avaliação
        InputManager.pausar("");
    }
    
    private static String getDescricaoNota(int nota) {
        switch (nota) {
            case 1: return "Péssimo";
            case 2: return "Ruim";
            case 3: return "Normal";
            case 4: return "Bom";
            case 5: return "Excelente";
            default: return "Indefinido";
        }
    }

    /**
     * IMPLEMENTAÇÃO COMPLETA: Restaurante aceita ou recusa pedidos pendentes
     */
    private static void gerenciarPedidos(Restaurante restaurante) {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       GERENCIAR PEDIDOS");
        System.out.println("==================================================");
        
        java.util.List<Pedido> pedidosPendentes = new java.util.ArrayList<>();
        for (Pedido p : restaurante.getFilaPedidos()) {
            if (p.getStatus().equals("Pendente")) {
                pedidosPendentes.add(p);
            }
        }
        
        if (pedidosPendentes.isEmpty()) {
            System.out.println("Nenhum pedido pendente!");
            InputManager.pausar("");
            return;
        }

        System.out.println("Pedidos pendentes de confirmação:\n");
        for (int i = 0; i < pedidosPendentes.size(); i++) {
            Pedido p = pedidosPendentes.get(i);
            System.out.println((i + 1) + ". PEDIDO #" + p.getNumeroPedido());
            System.out.println("   Cliente: " + p.getCliente().getNome());
            System.out.println("   Total: R$" + String.format("%.2f", p.getValorTotal()));
            System.out.println("   Itens:");
            for (ItemPedido item : p.getItens()) {
                System.out.printf("   - %dx %s\n", 
                    item.getQuantidade(),
                    item.getProduto().getNome()
                );
            }
            System.out.println();
        }

        Integer idx = InputManager.lerInteiro("Escolha o pedido (0 para voltar)");
        if (idx == null || idx < 1 || idx > pedidosPendentes.size()) return;

        Pedido pedidoSelecionado = pedidosPendentes.get(idx - 1);

        System.out.println("\n--- Ações para Pedido #" + pedidoSelecionado.getNumeroPedido() + " ---");
        System.out.println("1. ACEITAR pedido");
        System.out.println("2. RECUSAR pedido");
        System.out.println("0. Voltar");

        Integer acao = InputManager.lerInteiro("Escolha");
        if (acao == null || acao == 0) return;

        try {
            if (acao == 1) {
                // Aceitar pedido
                pedidoSelecionado.atualizarStatus("Confirmado");
                repositorio.salvarDados();
                
                System.out.println("\n✅ Pedido #" + pedidoSelecionado.getNumeroPedido() + " ACEITO!");
                System.out.println("O cliente foi notificado.");
                
            } else if (acao == 2) {
                // Recusar pedido
                String motivo = InputManager.lerTexto("Motivo da recusa (opcional)");
                restaurante.recusarPedido(pedidoSelecionado);
                repositorio.salvarDados();
                
                System.out.println("\n❌ Pedido #" + pedidoSelecionado.getNumeroPedido() + " RECUSADO!");
                if (motivo != null && !motivo.isEmpty()) {
                    System.out.println("Motivo: " + motivo);
                }
                
            } else {
                System.out.println("\nOpção inválida!");
            }
            
        } catch (RestauranteFechadoException e) {
            System.out.println("\n❌ ERRO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n❌ ERRO: " + e.getMessage());
        }

        InputManager.pausar("");
    }

    private static void gerenciarEnderecos() {
        InputManager.limparTela();
        Cliente cliente = sessao.getClienteLogado();
        
        System.out.println("==================================================");
        System.out.println("       MEUS ENDEREÇOS");
        System.out.println("==================================================");

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
        if (cep == null || cep.isEmpty()) return;
        
        String rua = InputManager.lerTexto("Rua");
        if (rua == null || rua.isEmpty()) return;
        
        String num = InputManager.lerTexto("Número");
        if (num == null || num.isEmpty()) return;
        
        String bairro = InputManager.lerTexto("Bairro");
        if (bairro == null || bairro.isEmpty()) return;
        
        String cidade = InputManager.lerTexto("Cidade");
        if (cidade == null || cidade.isEmpty()) return;
        
        String uf = InputManager.lerTexto("Estado");
        if (uf == null || uf.isEmpty()) return;

        Endereco novo = new Endereco(cep, rua, num, bairro, cidade, uf);
        cliente.adicionarEndereco(novo);
        System.out.println("\nAdicionado!");
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
                System.out.println("\nTipo inválido!");
                InputManager.pausar("");
                return;
        }

        restaurante.adicionarProdutoCardapio(prod);
        System.out.println("\nProduto adicionado!");
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
        System.out.println("\nRemovido!");
        InputManager.pausar("");
    }

    private static void atualizarStatusPedido(Restaurante restaurante) {
        InputManager.limparTela();
        System.out.println("==================================================");
        System.out.println("       ATUALIZAR STATUS DE PEDIDO");
        System.out.println("==================================================");
        
        if (restaurante.getFilaPedidos().isEmpty()) {
            System.out.println("Sem pedidos!");
            InputManager.pausar("");
            return;
        }

        java.util.List<Pedido> peds = restaurante.getFilaPedidos();
        System.out.println("Pedidos:\n");
        for (int i = 0; i < peds.size(); i++) {
            Pedido p = peds.get(i);
            System.out.printf("%d. #%d - Cliente: %s - Status: %s\n", 
                i + 1, 
                p.getNumeroPedido(),
                p.getCliente().getNome(),
                p.getStatus());
        }

        Integer idx = InputManager.lerInteiro("\nEscolha (0 para cancelar)");
        if (idx == null || idx < 1 || idx > peds.size()) return;

        Pedido ped = peds.get(idx - 1);

        System.out.println("\n--- Pedido #" + ped.getNumeroPedido() + " ---");
        System.out.println("Status atual: " + ped.getStatus());
        System.out.println("\nOpções de status:");
        System.out.println("1 - Confirmado");
        System.out.println("2 - Preparando");
        System.out.println("3 - Pronto");
        System.out.println("4 - Em Entrega");
        System.out.println("5 - Entregue");
        System.out.println("6 - Cancelado");

        Integer op = InputManager.lerInteiro("\nNovo status");
        if (op == null) return;

        String stat;
        switch (op) {
            case 1: stat = "Confirmado"; break;
            case 2: stat = "Preparando"; break;
            case 3: stat = "Pronto"; break;
            case 4: stat = "Em Entrega"; break;
            case 5: stat = "Entregue"; break;
            case 6: stat = "Cancelado"; break;
            default: 
                System.out.println("\nOpção inválida!");
                InputManager.pausar("");
                return;
        }

        restaurante.atualizarStatusPedido(ped, stat);
        repositorio.salvarDados();
        
        System.out.println("\n==================================================");
        System.out.println("       Status atualizado com sucesso!");
        System.out.println("==================================================");
        System.out.println("Pedido #" + ped.getNumeroPedido() + " → " + stat);
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