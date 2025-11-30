package ifome.model;

//endereco br
public class Endereco {
    
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;

    
    public Endereco(String cep, String rua, String numero, String bairro, 
                    String complemento, String cidade, String estado) {
        this.cep = validarCEP(cep) ? cep : "00000-000";
        this.rua = rua != null ? rua : "";
        this.numero = numero != null ? numero : "";
        this.bairro = bairro != null ? bairro : "";
        this.complemento = complemento != null ? complemento : "";
        this.cidade = cidade != null ? cidade : "";
        this.estado = estado != null ? estado : "";
    }

    public Endereco(String cep, String rua, String numero, String bairro, String cidade, String estado) {
        this(cep, rua, numero, bairro, "", cidade, estado);
    }

    public Endereco(String cep, String rua, String numero) {
        this(cep, rua, numero, "", "", "", "");
    }

    private boolean validarCEP(String cep) {
        if (cep == null || cep.isEmpty()) return false;
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        return cepLimpo.length() == 8 && cepLimpo.matches("\\d{5}\\d{3}");
    }

    private String formatarCEP(String cep) {
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        if (cepLimpo.length() == 8) {
            return cepLimpo.substring(0, 5) + "-" + cepLimpo.substring(5);
        }
        return cep;
    }


    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(rua);
        if (!numero.isEmpty()) {
            sb.append(", ").append(numero);
        }
        if (!complemento.isEmpty()) {
            sb.append(" - ").append(complemento);
        }
        sb.append(" | ").append(bairro);
        if (!cidade.isEmpty()) {
            sb.append(", ").append(cidade);
        }
        if (!estado.isEmpty()) {
            sb.append(" - ").append(estado);
        }
        sb.append(" | CEP: ").append(formatarCEP(cep));
        return sb.toString();
    }

    public String getCep() {
        return cep;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setCep(String cep) {
        this.cep = validarCEP(cep) ? cep : this.cep;
    }

    public void setRua(String rua) {
        this.rua = rua != null ? rua : this.rua;
    }

    public void setNumero(String numero) {
        this.numero = numero != null ? numero : this.numero;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro != null ? bairro : this.bairro;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento != null ? complemento : "";
    }

    public void setCidade(String cidade) {
        this.cidade = cidade != null ? cidade : this.cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado != null ? estado : this.estado;
    }

    @Override
    public String toString() {
        return getEnderecoCompleto();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Endereco endereco = (Endereco) obj;
        return cep.equals(endereco.cep) && rua.equals(endereco.rua) && numero.equals(endereco.numero);
    }

    @Override
    public int hashCode() {
        return (cep + rua + numero).hashCode();
    }
}