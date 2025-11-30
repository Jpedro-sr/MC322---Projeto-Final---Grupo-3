package ifome.model;

//pagamento por cartao
public class CartaoCredito extends FormaPagamento {

    private String numeroCartao;
    private String nomeTitular;
    private String cvv;
    private String dataValidade; // formato MM/YY

   
    public CartaoCredito(String numeroCartao, String nomeTitular, String cvv, String dataValidade) {
        super();
        this.numeroCartao = validarNumeroCartao(numeroCartao) ? numeroCartao : "";
        this.nomeTitular = nomeTitular != null ? nomeTitular.toUpperCase() : "";
        this.cvv = validarCVV(cvv) ? cvv : "";
        this.dataValidade = validarDataValidade(dataValidade) ? dataValidade : "";
    }

    // construtor com dados principais
    public CartaoCredito(String numeroCartao, String nomeTitular, String cvv) {
        this(numeroCartao, nomeTitular, cvv, "12/25");
    }

    // valida sem formatacao
    private boolean validarNumeroCartao(String numero) {
        if (numero == null) return false;
        String limpo = numero.replaceAll("[^0-9]", "");
        return limpo.length() == 16;
    }

    // cvv
    private boolean validarCVV(String cvv) {
        if (cvv == null) return false;
        return cvv.matches("\\d{3,4}");
    }

   
    private boolean validarDataValidade(String data) {
        if (data == null) return false;
        return data.matches("\\d{2}/\\d{2}");
    }

    public String getNumeroCartaoMascarado() {
        if (numeroCartao.length() >= 8) {
            return numeroCartao.substring(0, 4) + " **** **** " + numeroCartao.substring(12);
        }
        return "****";
    }

    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("\n💳 Processando pagamento por Cartão de Crédito...");
        System.out.println("   Valor: R$" + String.format("%.2f", valor));
        System.out.println("   Cartão: " + getNumeroCartaoMascarado());
        System.out.println("   Titular: " + nomeTitular);

        // simula validacao
        if (numeroCartao.isEmpty() || cvv.isEmpty() || dataValidade.isEmpty()) {
            System.out.println("❌ Dados do cartão inválidos. Pagamento recusado.");
            return false;
        }

        //simula aprovacao
        boolean aprovado = !cvv.equals("000");
        if (aprovado) {
            System.out.println("✅ Pagamento APROVADO!");
            this.pagamentoProcessado = true;
            return true;
        } else {
            System.out.println("❌ Pagamento RECUSADO. Cartão bloqueado.");
            return false;
        }
    }

    // Getters
    public String getNomeCartao() {
        return nomeTitular;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    @Override
    public String toString() {
        return "Cartão: " + getNumeroCartaoMascarado() + " | Titular: " + nomeTitular;
    }
}