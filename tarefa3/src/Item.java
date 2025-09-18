/**
 * Interface base para qualquer item no jogo (armas, poções, etc.).
 * Define o contrato de que todo item deve ter um nome.
 * (Conforme Tarefa 3, Seção 3.1.3)
 */
public interface Item {

    /**
     * Retorna o nome do item.
     * @return uma String contendo o nome do item.
     */
    String getNome();
}