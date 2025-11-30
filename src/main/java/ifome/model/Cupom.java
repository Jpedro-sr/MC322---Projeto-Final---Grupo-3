package ifome.model;

import java.util.Date;

//cupom
public class Cupom {

    private String codigo;
    private double valorDesconto;
    private boolean ehPercentual; 
    private Date dataValidade;
    private boolean ativo;

    public Cupom(String codigo, double valorDesconto, boolean ehPercentual, Date dataValidade) {
        this.codigo = codigo != null && !codigo.isEmpty() ? codigo.toUpperCase() : "INVALIDO";
        this.valorDesconto = validarDesconto(valorDesconto, ehPercentual);
        this.ehPercentual = ehPercentual;
        this.dataValidade = dataValidade;
        this.ativo = true;
    }

    
    public Cupom(String codigo, double valorDesconto, boolean ehPercentual) {
        this(codigo, valorDesconto, ehPercentual, null);
    }

    
    public static Cupom criarCupomPercentual(String codigo, double percentual) {
        return new Cupom(codigo, percentual, true);
    }


    public static Cupom criarCupomFixo(String codigo, double valorFixo) {
        return new Cupom(codigo, valorFixo, false);
    }


    private double validarDesconto(double valor, boolean ehPercentual) {
        if (valor < 0) {
            System.out.println("⚠️ Desconto não pode ser negativo.");
            return 0;
        }
        if (ehPercentual && valor > 100) {
            System.out.println("⚠️ Desconto percentual não pode ser maior que 100%.");
            return 100;
        }
        return valor;
    }

    // verifica validade
    public boolean estaValido() {
        if (!ativo) return false;
        if (dataValidade != null && new Date().after(dataValidade)) {
            System.out.println("⚠️ Cupom expirado em " + dataValidade);
            return false;
        }
        return true;
    }

    // desconto
    public double aplicarDesconto(double valorTotal) {
        if (!estaValido()) {
            System.out.println("⚠️ Cupom " + codigo + " não pode ser aplicado.");
            return valorTotal;
        }

        double desconto;
        if (ehPercentual) {
            desconto = valorTotal * (valorDesconto / 100.0);
        } else {
            desconto = Math.min(valorDesconto, valorTotal); 
        }

        double valorFinal = valorTotal - desconto;
        System.out.println("✅ Cupom aplicado: " + codigo + " | Desconto: R$" + 
                         String.format("%.2f", desconto) + " | Total: R$" + 
                         String.format("%.2f", valorFinal));
        return valorFinal;
    }

    // getters e setter
    public String getCodigo() {
        return codigo;
    }

    public double getValorDesconto() {
        return valorDesconto;
    }

    public boolean isPercentual() {
        return ehPercentual;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo != null && !codigo.isEmpty() ? codigo.toUpperCase() : this.codigo;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = validarDesconto(valorDesconto, ehPercentual);
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getDescricaoDesconto() {
        if (ehPercentual) {
            return valorDesconto + "% de desconto";
        } else {
            return "R$" + String.format("%.2f", valorDesconto) + " de desconto";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🎫 Cupom: ").append(codigo).append("\n");
        sb.append("📊 Desconto: ").append(getDescricaoDesconto()).append("\n");
        sb.append("✓ Status: ").append(ativo ? "Ativo" : "Inativo");
        if (dataValidade != null) {
            sb.append(" | Validade: ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(dataValidade));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cupom cupom = (Cupom) obj;
        return codigo.equals(cupom.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}