package ifome.model;

//sobremesa
public class Sobremesa extends Produto {
    
    private String temperatura; 
    // o metodo parece correto agora
    
    public Sobremesa(String nome, String desc, double preco, String temperatura) {
        this.nome = nome;
        this.descricao = desc;
        this.preco = preco;
        this.categoria = "Sobremesa";
        this.temperatura = validarTemperatura(temperatura);
    }
    
    public Sobremesa(String nome, String desc, double preco) {
        this(nome, desc, preco, "Ambiente"); 
    }
    
    
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
    

    public String getTemperatura() {
        return temperatura;
    }
    
    public void setTemperatura(String temperatura) {
        this.temperatura = validarTemperatura(temperatura);
    }
    
    // vei, tem que ver se o emoji não vai quebrar o display. Vamos manter por enquanto.
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