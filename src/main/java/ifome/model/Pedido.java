package ifome.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

/**
 pedido finalizado
 */
public class Pedido implements Rastreavel, Calculavel, Avaliavel {

    private static int contador = 1000;
    private int numeroPedido;
    private Date dataHora;
    private String status;
    private double valorTotal;
    private double desconto;

    private List<ItemPedido> itens;
    private List<Avaliacao> avaliacoes;
    private FormaPagamento formaPagamento;
    private Cupom cupomAplicado;
    private Cliente cliente;
    private Restaurante restaurante;

    public Pedido() {
        this.numeroPedido = contador++;
        this.itens = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.dataHora = new Date();
        this.status = "Pendente";
        this.valorTotal = 0;
        this.desconto = 0;
        this.formaPagamento = null;
        this.cupomAplicado = null;
        this.cliente = null;
        this.restaurante = null;
    }

    /**
    p dido
     */
    public static void inicializarContador(int maiorIdExistente) {
        if (maiorIdExistente >= contador) {
            contador = maiorIdExistente + 1;
            System.out.println(">>> Contador de pedidos inicializado em: " + contador);
        }
    }

  
    public Pedido(int numeroPedido, Date dataHora, String status, double valorTotal) {
        this.numeroPedido = numeroPedido;
        this.dataHora = dataHora;
        this.status = status;
        this.valorTotal = valorTotal;
        this.itens = new ArrayList<>();
        this.avaliacoes = new ArrayList<>();
        this.desconto = 0;
        this.formaPagamento = null;
        this.cupomAplicado = null;
       
        if (numeroPedido >= contador) {
            contador = numeroPedido + 1;
        }
    }


    @Override
    public String getStatus() {
        return this.status;
    }

    public void atualizarStatus(String novoStatus) {
        String[] statusValidos = {
            "Pendente", "Confirmado", "Preparando", "Pronto", "Em Entrega", "Entregue", "Cancelado"
        };

        boolean statusValido = false;
        for (String s : statusValidos) {
            if (s.equals(novoStatus)) {
                statusValido = true;
                break;
            }
        }

        if (!statusValido) {
            System.out.println("❌ Status inválido: " + novoStatus);
            return;
        }

        if (!ehTransicaoValida(this.status, novoStatus)) {
            System.out.println("❌ Transição inválida de " + this.status + " para " + novoStatus);
            return;
        }

        String statusAnterior = this.status;
        this.status = novoStatus;
        System.out.println("✅ Pedido #" + numeroPedido + ": " + statusAnterior + " → " + novoStatus);
    }

    private boolean ehTransicaoValida(String statusAtual, String novoStatus) {
        if (statusAtual.equals("Pendente")) {
            return novoStatus.equals("Confirmado") || novoStatus.equals("Cancelado");
        } else if (statusAtual.equals("Confirmado")) {
            return novoStatus.equals("Preparando") || novoStatus.equals("Cancelado");
        } else if (statusAtual.equals("Preparando")) {
            return novoStatus.equals("Pronto") || novoStatus.equals("Cancelado");
        } else if (statusAtual.equals("Pronto")) {
            return novoStatus.equals("Em Entrega");
        } else if (statusAtual.equals("Em Entrega")) {
            return novoStatus.equals("Entregue") || novoStatus.equals("Cancelado");
        } else if (statusAtual.equals("Entregue")) {
            return false;
        }
        return false;
    }

 

    @Override
    public double calcularPrecoTotal() {
        this.valorTotal = 0;
        for (ItemPedido item : itens) {
            this.valorTotal += item.calcularPrecoTotal();
        }

        this.desconto = 0;
        if (cupomAplicado != null && cupomAplicado.estaValido()) {
            double valorComDesconto = cupomAplicado.aplicarDesconto(this.valorTotal);
            this.desconto = this.valorTotal - valorComDesconto;
            this.valorTotal = valorComDesconto;
        }

        return this.valorTotal;
    }


    @Override
    public boolean avaliar(int nota) {
        Avaliacao avaliacao = new Avaliacao(nota);
        return avaliacoes.add(avaliacao);
    }

    public boolean avaliar(int nota, String comentario) {
        Avaliacao avaliacao = new Avaliacao(nota, comentario);
        boolean adicionado = avaliacoes.add(avaliacao);
        if (adicionado) {
            System.out.println("✅ Avaliação registrada para pedido #" + numeroPedido);
        }
        return adicionado;
    }

  

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            System.out.println("❌ Item inválido.");
            return;
        }
        itens.add(item);
    }

    public void removerItem(ItemPedido item) {
        if (itens.remove(item)) {
            System.out.println("✅ Item removido do pedido.");
        } else {
            System.out.println("❌ Item não encontrado.");
        }
    }


    public String gerarResumo() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();

        sb.append("\n╔════════════════════════════════════════╗\n");
        sb.append("║          RESUMO DO PEDIDO              ║\n");
        sb.append("╚════════════════════════════════════════╝\n\n");

        sb.append("📋 NÚMERO: #").append(numeroPedido).append("\n");
        sb.append("📅 DATA: ").append(sdf.format(dataHora)).append("\n");
        sb.append("📍 STATUS: ").append(status).append("\n\n");

        if (cliente != null) {
            sb.append("👤 CLIENTE: ").append(cliente.getNome()).append("\n");
            sb.append("   📞 ").append(cliente.getTelefone()).append("\n");
        }

        if (restaurante != null) {
            sb.append("🏪 RESTAURANTE: ").append(restaurante.getNomeRestaurante()).append("\n");
        }

        sb.append("\n┌─────────────────────────────────────────┐\n");
        sb.append("│            ITENS DO PEDIDO              │\n");
        sb.append("└─────────────────────────────────────────┘\n");

        if (itens.isEmpty()) {
            sb.append("(nenhum item)\n");
        } else {
            for (int i = 0; i < itens.size(); i++) {
                ItemPedido item = itens.get(i);
                sb.append(String.format("%d. %s\n", i + 1, item.getProduto().getNome()));
                sb.append(String.format("   Qtd: %dx | Preço Unitário: R$%.2f\n", 
                    item.getQuantidade(), item.getPrecoUnitario()));
                sb.append(String.format("   Subtotal: R$%.2f\n", item.calcularPrecoTotal()));
                if (!item.getObservacoes().isEmpty()) {
                    sb.append(String.format("   Obs: %s\n", item.getObservacoes()));
                }
            }
        }

        sb.append("\n┌─────────────────────────────────────────┐\n");
        sb.append("│            VALORES                      │\n");
        sb.append("└─────────────────────────────────────────┘\n");

        double subtotal = 0;
        for (ItemPedido item : itens) {
            subtotal += item.calcularPrecoTotal();
        }

        sb.append(String.format("Subtotal: R$%.2f\n", subtotal));

        if (cupomAplicado != null && cupomAplicado.estaValido()) {
            sb.append("Cupom: ").append(cupomAplicado.getCodigo()).append("\n");
            sb.append(String.format("Desconto: -R$%.2f\n", desconto));
        }

        sb.append("\n");
        sb.append(String.format("💰 TOTAL: R$%.2f\n\n", valorTotal > 0 ? valorTotal : subtotal));

        if (formaPagamento != null) {
            sb.append("💳 PAGAMENTO: ").append(formaPagamento.toString()).append("\n");
        }

        if (!avaliacoes.isEmpty()) {
            sb.append("\n⭐ AVALIAÇÃO: ");
            double media = calcularMediaAvaliacoes();
            sb.append(String.format("%.1f/5.0", media)).append("\n");
        }

        sb.append("\n╚════════════════════════════════════════╝\n");
        return sb.toString();
    }

    

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public FormaPagamento getFormaPagamento() {
        return this.formaPagamento;
    }

    public boolean processarPagamento() 
            throws ifome.exceptions.PagamentoRecusadoException {
        if (formaPagamento == null) {
            throw new ifome.exceptions.PagamentoRecusadoException(
                "❌ Nenhuma forma de pagamento definida."
            );
        }

        double total = calcularPrecoTotal();
        if (total <= 0) {
            total = 0;
            for (ItemPedido item : itens) {
                total += item.calcularPrecoTotal();
            }
        }

        if (!formaPagamento.processarPagamento(total)) {
            throw new ifome.exceptions.PagamentoRecusadoException(
                "❌ Pagamento recusado."
            );
        }

        this.atualizarStatus("Confirmado");
        return true;
    }


    public void setCupomAplicado(Cupom cupom) {
        this.cupomAplicado = cupom;
    }

    public Cupom getCupomAplicado() {
        return cupomAplicado;
    }


    public int getNumeroPedido() {
        return numeroPedido;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public double getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getDesconto() {
        return desconto;
    }

    public List<ItemPedido> getItens() {
        return new ArrayList<>(itens);
    }

    public List<Avaliacao> getAvaliacoes() {
        return new ArrayList<>(avaliacoes);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens != null ? itens : new ArrayList<>();
    }
    
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public double calcularMediaAvaliacoes() {
        if (avaliacoes.isEmpty()) return 0;
        int soma = 0;
        for (Avaliacao avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        return (double) soma / avaliacoes.size();
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d - Status: %s - Total: R$%.2f", 
            numeroPedido, status, valorTotal);
    }
}