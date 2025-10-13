package personagens;

import itens.armas.Arma;

// Interface que representa qualquer entidade que pode deixar uma recompensa (loot) ao ser derrotada, como um monstro.

public interface Lootavel {

    // Sorteia e retorna um item da lista de possíveis drops da entidade.

    Arma droparLoot();
}