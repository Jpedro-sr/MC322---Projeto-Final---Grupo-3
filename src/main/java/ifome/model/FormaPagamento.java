package ifome.model;

//formasde pagamento
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