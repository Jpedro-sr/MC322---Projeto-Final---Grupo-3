package ifome.model;

/**
 * Representa um cartão de crédito salvo pelo cliente.
 * Armazena informações de forma segura (número mascarado).
 */
public class CartaoSalvo {
    
    private String numeroMascarado; // Ex: "**** **** **** 1234"
    private String numeroCompleto; // Armazenado de forma simplificada (em produção, usar criptografia)
    private String nomeTitular;
    private String cvv;
    private String validade;
    private String apelido; // Ex: "Cartão Principal", "Nubank", etc.
    
    public CartaoSalvo(String numeroCompleto, String nomeTitular, String cvv, String validade, String apelido) {
        this.numeroCompleto = numeroCompleto;
        this.numeroMascarado = mascararNumero(numeroCompleto);
        this.nomeTitular = nomeTitular;
        this.cvv = cvv;
        this.validade = validade;
        this.apelido = apelido != null && !apelido.isEmpty() ? apelido : "Meu Cartão";
    }
    
    public CartaoSalvo(String numeroCompleto, String nomeTitular, String cvv, String validade) {
        this(numeroCompleto, nomeTitular, cvv, validade, "");
    }
    
    private String mascararNumero(String numero) {
        if (numero == null || numero.length() < 8) {
            return "****";
        }
        String limpo = numero.replaceAll("[^0-9]", "");
        if (limpo.length() == 16) {
            return "**** **** **** " + limpo.substring(12);
        }
        return "****";
    }
    
    public CartaoCredito toCartaoCredito() {
        return new CartaoCredito(numeroCompleto, nomeTitular, cvv, validade);
    }
    
    // Getters
    public String getNumeroMascarado() {
        return numeroMascarado;
    }
    
    public String getNumeroCompleto() {
        return numeroCompleto;
    }
    
    public String getNomeTitular() {
        return nomeTitular;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public String getValidade() {
        return validade;
    }
    
    public String getApelido() {
        return apelido;
    }
    
    public void setApelido(String apelido) {
        this.apelido = apelido != null && !apelido.isEmpty() ? apelido : this.apelido;
    }
    
    @Override
    public String toString() {
        return apelido + " - " + numeroMascarado;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartaoSalvo cartao = (CartaoSalvo) obj;
        return numeroCompleto.equals(cartao.numeroCompleto);
    }
    
    @Override
    public int hashCode() {
        return numeroCompleto.hashCode();
    }
}