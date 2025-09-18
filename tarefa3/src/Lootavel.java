/**
 * Interface que representa qualquer entidade que pode deixar uma recompensa (loot)
 * ao ser derrotada, como um monstro.
 * (Conforme Tarefa 3, Seção 3.1.3)
 */
public interface Lootavel {

    /**
     * Sorteia e retorna um item da lista de possíveis drops da entidade.
     * @return um objeto que implementa a interface Item.
     */
    Item droparLoot();
}