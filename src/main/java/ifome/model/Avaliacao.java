package ifome.model;

import java.util.Date;

/**
 * Representa uma avaliação de um pedido ou restaurante.
 * Armazena nota (1-5), comentário e data.
 */
public class Avaliacao {
    
    private int nota;
    private String comentario;
    private Date data;

    // Construtor completo
    public Avaliacao(int nota, String comentario) {
        this.nota = validarNota(nota);
        this.comentario = comentario != null ? comentario.trim() : "";
        this.data = new Date();
    }

    // Construtor com apenas nota
    public Avaliacao(int nota) {
        this(nota, "");
    }

    // Validar se nota está entre 1 e 5
    private int validarNota(int nota) {
        if (nota < 1 || nota > 5) {
            System.out.println("⚠️ Nota inválida. Usando nota 3 como padrão.");
            return 3;
        }
        return nota;
    }

    // Getters
    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public Date getData() {
        return data;
    }

    // Setters com validação
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