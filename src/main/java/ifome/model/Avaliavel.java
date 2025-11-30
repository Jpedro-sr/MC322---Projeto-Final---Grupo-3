package ifome.model;

public interface Avaliavel {
    boolean avaliar(int nota);
    
    // avaliacao com texto
    boolean avaliar(int nota, String comentario);
}