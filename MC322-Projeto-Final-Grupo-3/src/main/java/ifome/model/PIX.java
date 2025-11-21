package ifome.model;

/**
 * Implementação de pagamento por PIX.
 * Simula a integração com sistema PIX.
 */
public class PIX extends FormaPagamento {

    private String chavePIX;
    private String tipoClave; // CPF, EMAIL, TELEFONE, ALEATORIA

    // Construtor com chave PIX
    public PIX(String chavePIX, String tipoClave) {
        super();
        this.chavePIX = chavePIX != null ? chavePIX : "chave.aleatoria";
        this.tipoClave = tipoClave != null ? tipoClave : "ALEATORIA";
    }

    // Construtor padrão (PIX aleatório)
    public PIX() {
        this("chave-" + System.currentTimeMillis(), "ALEATORIA");
    }

    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("\n📱 Processando pagamento por PIX...");
        System.out.println("   Valor: R$" + String.format("%.2f", valor));
        System.out.println("   Chave: " + maskChavePIX());
        System.out.println("   Tipo: " + tipoClave);

        try {
            // Simular processamento
            Thread.sleep(1000);
            System.out.println("✅ Pagamento PIX CONFIRMADO!");
            this.pagamentoProcessado = true;
            return true;
        } catch (InterruptedException e) {
            System.out.println("⚠️ Erro ao processar PIX.");
            return false;
        }
    }

    // Mascarar chave PIX para exibição
    private String maskChavePIX() {
        if (chavePIX.length() > 10) {
            return chavePIX.substring(0, 5) + "****" + chavePIX.substring(chavePIX.length() - 5);
        }
        return "****";
    }

    public String getChavePIX() {
        return chavePIX;
    }

    @Override
    public String toString() {
        return "PIX - Chave: " + maskChavePIX() + " (" + tipoClave + ")";
    }
}