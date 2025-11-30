package ifome.model;

import java.util.Date;

/**
 * avaliacao de 1-5 com comentario
 */
public class Avaliacao {
    
    private int nota;
    private String comentario;
    private Date data;


    public Avaliacao(int nota, String comentario) {
        this.nota = validarNota(nota);
        this.comentario = comentario != null ? comentario.trim() : "";
        this.data = new Date();
    }

    // 
    public Avaliacao(int nota) {
        this(nota, "");
    }

    // valida se esta correto
    private int validarNota(int nota) {
        if (nota < 1 || nota > 5) {
            System.out.println("⚠️ Nota inválida. Usando nota 3 como padrão.");
            return 3;
        }
        return nota;
    }

    // getters
    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public Date getData() {
        return data;
    }

    // setters com validação
    public void setNota(int nota) {
        this.nota = validarNota(nota);
    }

    public void setComentario(String comentario) {
        this.comentario = comentario != null ? comentario.trim() : "";
    }

    // Retorna descrição textual da nota
    public String getDescricaoNota() {
        switch (nota) {
            case 1: return "Péssimo";
            case 2: return "Ruim";
            case 3: return "Normal";
            case 4: return "Bom";
            case 5: return "Excelente";
            default: return "Indefinido";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("⭐ ").append(nota).append("/5 - ").append(getDescricaoNota());
        if (!comentario.isEmpty()) {
            sb.append("\n📝 ").append(comentario);
        }
        sb.append("\n📅 ").append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(data));
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Avaliacao avaliacao = (Avaliacao) obj;
        return nota == avaliacao.nota && comentario.equals(avaliacao.comentario);
    }

    @Override
    public int hashCode() {
        return (nota + comentario).hashCode();
    }
}