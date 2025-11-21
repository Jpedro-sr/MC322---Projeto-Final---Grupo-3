package ifome.model; 

/**
 * Implementação de pagamento em dinheiro.
 * Calcula troco automaticamente.
 */
public class Dinheiro extends FormaPagamento {

    private double valorRecebido;
    private double troco;

    // Construtor
    public Dinheiro(double valorRecebido) {
        super();
        this.valorRecebido = valorRecebido > 0 ? valorRecebido : 0;
        this.troco = 0;
    }

    // Construtor padrão (sem valor)
    public Dinheiro() {
        this(0);
    }

    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("\n💵 Processando pagamento em Dinheiro...");
        System.out.println("   Valor do pedido: R$" + String.format("%.2f", valor));
        System.out.println("   Valor recebido: R$" + String.format("%.2f", valorRecebido));

        if (valorRecebido < valor) {
            System.out.println("❌ Valor insuficiente. Faltam R$" + 
                             String.format("%.2f", valor - valorRecebido));
            return false;
        }

        this.troco = valorRecebido - valor;
        
        if (troco > 0) {
            System.out.println("✅ Pagamento ACEITO!");
            System.out.println("   🔄 Troco: R$" + String.format("%.2f", troco));
        } else {
            System.out.println("✅ Pagamento EXATO!");
        }

        this.pagamentoProcessado = true;
        return true;
    }

    public double getTroco() {
        return troco;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido > 0 ? valorRecebido : this.valorRecebido;
    }

    @Override
    public String toString() {
        if (troco > 0) {
            return "Dinheiro - Troco: R$" + String.format("%.2f", troco);
        }
        return "Dinheiro";
    }
}