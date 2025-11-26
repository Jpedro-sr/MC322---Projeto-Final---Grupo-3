package ifome.model;

public interface Avaliavel {
    boolean avaliar(int nota);
    
    // Método adicionado para suportar avaliações com texto
    boolean avaliar(int nota, String comentario);
}