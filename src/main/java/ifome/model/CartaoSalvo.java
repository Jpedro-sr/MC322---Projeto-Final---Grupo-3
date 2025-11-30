package ifome.model;

import java.util.Objects;

//cartao salvo
public class CartaoSalvo {
    
    private String numeroMascarado; 
    private String numeroCompleto; 
    private String nomeTitular;
    private String cvv;
    private String validade;
    private String apelido; 
    
    public CartaoSalvo(String numeroCompleto, String nomeTitular, String cvv, String validade, String apelido) {
        this.numeroCompleto = numeroCompleto;
        this.numeroMascarado = mascararNumero(numeroCompleto);
        this.nomeTitular = nomeTitular;
        this.cvv = cvv;
        this.validade = validade;
        this.apelido = apelido != null && !apelido.isEmpty() ? apelido : "Meu Cartão";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartaoSalvo that = (CartaoSalvo) o;
        return Objects.equals(numeroCompleto, that.numeroCompleto) && 
               Objects.equals(apelido, that.apelido);
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
    
    // getters
    public String getNumeroMascarado() {
        if (numeroCompleto != null && numeroCompleto.length() > 4) {
            return "**** " + numeroCompleto.substring(numeroCompleto.length() - 4);
        }
        return "****";
    }

    @Override
    public String toString() {
        return apelido + " (" + getNumeroMascarado() + ")";
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
}
