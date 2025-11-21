package ifome;

import ifome.model.*;

/**
 * Classe principal com testes integrados de todo o sistema.
 * Demonstra o fluxo completo: cliente → carrinho → pedido → pagamento → rastreamento.
 */
public class Aplicacao {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║     iFOME - SISTEMA DE DELIVERY        ║");
        System.out.println("║            TESTE INTEGRADO             ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        try {
            testeRestauranteECardapio();
            testeClienteEEnderecos();
            testeCarrinhoEPedido();
            testePagamento();
            testeRastreamento();
            testeAvaliacao();
            testeCupom();

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║      ✅ TODOS OS TESTES PASSARAM!       ║");
            System.out.println("╚════════════════════════════════════════╝\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Restaurante testeRestauranteECardapio() {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🏪 TESTE 1: RESTAURANTE E CARDÁPIO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        Restaurante pizzaria = new Restaurante(
            "pizzaria@ifome.com", "senha123", "Pizzaria Bella Italia", "12345678000195"
        );

        Endereco endRestaurante = new Endereco(
            "13001-970", "Rua Central", "100", "Centro", "São Paulo", "SP"
        );
        pizzaria.setEndereco(endRestaurante);
        pizzaria.setHorarioFuncionamento("11:00 - 23:00");

        Comida pizzaMozzarela = new Comida("Pizza Mozzarela", "Queijo derretido, oregano", 45.00, true);
        Comida pizzaCalabresa = new Comida("Pizza Calabresa", "Calabresa, cebola, azeitona", 48.00, true);
        Bebida refrigerante = new Bebida("Refrigerante 2L", "Refrigerante gelado", 15.00, 2000);
        Bebida cerveja = new Bebida("Cerveja Artesanal", "Cerveja premium", 12.00, 600);
        Sobremesa brownie = new Sobremesa("Brownie", "Chocolate quente e crocante", 18.00);
        Adicional queijo = new Adicional("Queijo Extra", 5.00);

        pizzaria.adicionarProdutoCardapio(pizzaMozzarela);
        pizzaria.adicionarProdutoCardapio(pizzaCalabresa);
        pizzaria.adicionarProdutoCardapio(refrigerante);
        pizzaria.adicionarProdutoCardapio(cerveja);
        pizzaria.adicionarProdutoCardapio(brownie);
        pizzaria.adicionarProdutoCardapio(queijo);

        pizzaria.abrirRestaurante();

        System.out.println(pizzaria.getInfoRestaurante());
        pizzaria.exibirCardapio();

        return pizzaria;
    }

    private static Cliente testeClienteEEnderecos() {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("👤 TESTE 2: CLIENTE E ENDEREÇOS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        Cliente joao = new Cliente("joao.silva@email.com", "minha_senha", "João Silva", "11999887766");

        Endereco endereco1 = new Endereco(
            "13002-100", "Avenida Paulista", "1000", "Apartamento 42", "Bela Vista", "São Paulo", "SP"
        );
        Endereco endereco2 = new Endereco(
            "13003-200", "Rua Oscar Freire", "500", "Pinheiros", "São Paulo", "SP"
        );

        joao.adicionarEndereco(endereco1);
        joao.adicionarEndereco(endereco2);

        System.out.println("✅ Cliente: " + joao.getNome());
        System.out.println("📧 Email: " + joao.getEmail());
        System.out.println("📞 Telefone: " + joao.getTelefone());
        System.out.println("🏠 Endereços cadastrados: " + joao.getQuantidadeEnderecos());
        for (Endereco e : joao.getEnderecos()) {
            System.out.println("   • " + e.getEnderecoCompleto());
        }

        return joao;
    }

    private static Pedido testeCarrinhoEPedido() throws Exception {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🛒 TESTE 3: CARRINHO E GERAÇÃO DE PEDIDO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        Restaurante pizzaria = new Restaurante(
            "pizzaria@ifome.com", "senha123", "Pizzaria Bella Italia", "12345678000195"
        );
        pizzaria.abrirRestaurante();

        Cliente joao = new Cliente("joao.silva@email.com", "minha_senha", "João Silva", "11999887766");

        Comida pizza = new Comida("Pizza Mozzarela", "Queijo derretido", 45.00, true);
        Bebida refrigerante = new Bebida("Refrigerante 2L", "Gelado", 15.00, 2000);
        pizzaria.adicionarProdutoCardapio(pizza);
        pizzaria.adicionarProdutoCardapio(refrigerante);

        joao.selecionarRestaurante(pizzaria);
        Carrinho carrinho = joao.getCarrinho();

        System.out.println("\n📦 Adicionando itens ao carrinho...\n");
        carrinho.adicionarItem(pizza, 2, "sem cebola");
        carrinho.adicionarItem(refrigerante, 1, "muito gelado");

        System.out.println(carrinho.toString());

        System.out.println("✅ Gerando pedido...\n");
        Pedido pedido = joao.fazerPedido(carrinho);

        return pedido;
    }

    private static void testePagamento() throws Exception {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("💳 TESTE 4: PROCESSAMENTO DE PAGAMENTO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        Restaurante pizzaria = new Restaurante("pizzaria@ifome.com", "senha123", "Pizzaria", "12345678000195");
        pizzaria.abrirRestaurante();
        
        Cliente joao = new Cliente("joao@email.com", "senha", "João", "11999887766");
        
        Comida pizza = new Comida("Pizza", "Queijo", 45.00, true);
        pizzaria.adicionarProdutoCardapio(pizza);

        joao.selecionarRestaurante(pizzaria);
        joao.getCarrinho().adicionarItem(pizza, 1, "");
        Pedido pedido = joao.fazerPedido(joao.getCarrinho());

        System.out.println("\n--- Teste com PIX ---\n");
        FormaPagamento pix = new PIX();
        pedido.setFormaPagamento(pix);
        pedido.processarPagamento();

        System.out.println("\n--- Teste com Cartão de Crédito ---\n");
        FormaPagamento cartao = new CartaoCredito("1234567890123456", "JOÃO SILVA", "123");
        Pedido pedido2 = new Pedido();
        pedido2.adicionarItem(new ItemPedido(pizza, 1, ""));
        pedido2.setFormaPagamento(cartao);
        pedido2.processarPagamento();

        System.out.println("\n--- Teste com Dinheiro ---\n");
        FormaPagamento dinheiro = new Dinheiro(100.00);
        Pedido pedido3 = new Pedido();
        pedido3.adicionarItem(new ItemPedido(pizza, 1, ""));
        pedido3.setFormaPagamento(dinheiro);
        pedido3.processarPagamento();
    }

    private static void testeRastreamento() throws Exception {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🚗 TESTE 5: RASTREAMENTO E STATUS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        Restaurante pizzaria = new Restaurante("pizzaria@ifome.com", "senha123", "Pizzaria", "12345678000195");
        pizzaria.abrirRestaurante();

        Cliente joao = new Cliente("joao@email.com", "senha", "João", "11999887766");
        Comida pizza = new Comida("Pizza", "Queijo", 45.00, true);
        pizzaria.adicionarProdutoCardapio(pizza);

        joao.selecionarRestaurante(pizzaria);
        joao.getCarrinho().adicionarItem(pizza, 1, "");
        Pedido pedido = joao.fazerPedido(joao.getCarrinho());

        pizzaria.aceitarPedido(pedido);

        System.out.println("\n--- Simulando preparação do pedido ---\n");
        pizzaria.atualizarStatusPedido(pedido, "Preparando");
        pizzaria.atualizarStatusPedido(pedido, "Pronto");
        pizzaria.atualizarStatusPedido(pedido, "Em Entrega");
        pizzaria.atualizarStatusPedido(pedido, "Entregue");

        System.out.println(pedido.gerarResumo());
    }

    private static void testeAvaliacao() throws Exception {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("⭐ TESTE 6: AVALIAÇÃO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        Restaurante pizzaria = new Restaurante("pizzaria@ifome.com", "senha123", "Pizzaria", "12345678000195");
        pizzaria.abrirRestaurante();

        Cliente joao = new Cliente("joao@email.com", "senha", "João", "11999887766");
        Comida pizza = new Comida("Pizza", "Queijo", 45.00, true);
        pizzaria.adicionarProdutoCardapio(pizza);

        joao.selecionarRestaurante(pizzaria);
        joao.getCarrinho().adicionarItem(pizza, 1, "");
        Pedido pedido = joao.fazerPedido(joao.getCarrinho());

        pizzaria.aceitarPedido(pedido);
        pizzaria.atualizarStatusPedido(pedido, "Preparando");
        pizzaria.atualizarStatusPedido(pedido, "Pronto");
        pizzaria.atualizarStatusPedido(pedido, "Em Entrega");
        pizzaria.atualizarStatusPedido(pedido, "Entregue");

        System.out.println("\n--- Cliente avaliando o pedido ---\n");
        joao.avaliarPedido(pedido, 5, "Excelente pizza! Muito bom mesmo!");

        System.out.println("\n--- Cliente avaliando o restaurante ---\n");
        pizzaria.avaliar(5, "Ótimo atendimento e comida deliciosa!");

        System.out.println("\n✅ Média de avaliações da Pizzaria: " +
                         String.format("%.1f/5.0", pizzaria.calcularMediaAvaliacoes()));
    }

    private static void testeCupom() throws Exception {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("🎫 TESTE 7: CUPOM E DESCONTO");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        Restaurante pizzaria = new Restaurante("pizzaria@ifome.com", "senha123", "Pizzaria", "12345678000195");
        pizzaria.abrirRestaurante();

        Cliente joao = new Cliente("joao@email.com", "senha", "João", "11999887766");
        Comida pizza = new Comida("Pizza", "Queijo", 45.00, true);
        pizzaria.adicionarProdutoCardapio(pizza);

        joao.selecionarRestaurante(pizzaria);
        Carrinho carrinho = joao.getCarrinho();

        carrinho.adicionarItem(pizza, 2, "");
        System.out.println("\n--- Carrinho sem desconto ---");
        System.out.println("Subtotal: R$" + String.format("%.2f", carrinho.calcularPrecoTotal()));

        System.out.println("\n--- Aplicando cupom (10% desconto) ---\n");
        Cupom cupom10 = Cupom.criarCupomPercentual("PRIMEIRACOMPRA", 10);
        carrinho.aplicarCupom(cupom10);

        System.out.println("Total com desconto: R$" + 
                         String.format("%.2f", carrinho.calcularTotalComDesconto()));

        Carrinho carrinho2 = new Carrinho();
        carrinho2.setRestaurante(pizzaria);
        carrinho2.adicionarItem(pizza, 3, "");

        System.out.println("\n--- Aplicando cupom fixo (R$15 desconto) ---\n");
        Cupom cupomFixo = Cupom.criarCupomFixo("DESCONTO15", 15);
        carrinho2.aplicarCupom(cupomFixo);

        System.out.println("Total com desconto: R$" + 
                         String.format("%.2f", carrinho2.calcularTotalComDesconto()));
    }
}