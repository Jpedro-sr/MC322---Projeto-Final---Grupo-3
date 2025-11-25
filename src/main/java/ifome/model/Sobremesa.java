package ifome.model;

/**
 * ✅ ATUALIZADO: Agora inclui o campo temperatura (Gelada, Quente, Ambiente)
 */
public class Sobremesa extends Produto {
    
    private String temperatura; // "Gelada", "Quente", "Ambiente"
    
    // Construtor completo (novo)
    public Sobremesa(String nome, String desc, double preco, String temperatura) {
        this.nome = nome;
        this.descricao = desc;
        this.preco = preco;
        this.categoria = "Sobremesa";
        this.temperatura = validarTemperatura(temperatura);
    }
    
    // Construtor original (mantido para compatibilidade)
    public Sobremesa(String nome, String desc, double preco) {
        this(nome, desc, preco, "Ambiente"); // Default: Ambiente
    }
    
    /**
     * Valida se a temperatura é válida
     */
    private String validarTemperatura(String temp) {
        if (temp == null || temp.isEmpty()) {
            return "Ambiente";
        }
        
        String tempUpper = temp.trim();
        if (tempUpper.equals("Gelada") || tempUpper.equals("Quente") || tempUpper.equals("Ambiente")) {
            return tempUpper;
        }
        
        System.out.println("⚠️ Temperatura inválida: " + temp + ". Usando 'Ambiente' como padrão.");
        return "Ambiente";
    }
    
    // Getters e Setters
    public String getTemperatura() {
        return temperatura;
    }
    
    public void setTemperatura(String temperatura) {
        this.temperatura = validarTemperatura(temperatura);
    }
    
    /**
     * Retorna o ícone/emoji da temperatura
     */
    public String getIconeTemperatura() {
        switch (temperatura) {
            case "Gelada":
                return "❄️";
            case "Quente":
                return "🔥";
            case "Ambiente":
                return "🌡️";
            default:
                return "";
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + " (" + getIconeTemperatura() + " " + temperatura + ")";
    }
}