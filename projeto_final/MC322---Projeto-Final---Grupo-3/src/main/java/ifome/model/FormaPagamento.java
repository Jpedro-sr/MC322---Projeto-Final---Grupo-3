package ifome.model;

/**
 * Classe abstrata que define o contrato para formas de pagamento.
 */
public abstract class FormaPagamento {

    protected boolean pagamentoProcessado;

    public FormaPagamento() {
        this.pagamentoProcessado = false;
    }

    public abstract boolean processarPagamento(double valor);

    public boolean isPagamentoProcessado() {
        return pagamentoProcessado;
    }
}