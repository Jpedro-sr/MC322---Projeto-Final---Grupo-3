package personagens.monstros;

import java.util.ArrayList;
import java.util.List;

import combate.AcaoDeCombate;
import combate.Combatente;
import itens.armas.Arma;
import personagens.Lootavel;
import personagens.Personagem;

/**
 * Classe base para todos os monstros.
 * REFATORADO: Agora usa AGREGAÇÃO para o sistema de loot.
 * A lista de armas guarda CLASSES, não instâncias.
 * (Conforme Tarefa 6, Seção 3.6)
 */
public abstract class Monstro extends Personagem implements Lootavel {
    
    protected int xpConcedido;
    
    // REFATORADO: Lista de CLASSES de armas (Agregação)
    protected List<Class<? extends Arma>> classesDeArmasParaLargar;
    
    protected ArrayList<AcaoDeCombate> acoes;

    public Monstro(String nome, int pontosDeVida, int forca, int xpConcedido) {
        super(nome, pontosDeVida, forca);
        this.xpConcedido = xpConcedido;
        this.classesDeArmasParaLargar = new ArrayList<>();
        this.acoes = new ArrayList<>();
    }

    /**
     * Implementação do loot usando AGREGAÇÃO.
     * Retorna a arma equipada (se houver) ou instancia uma nova classe aleatória.
     */
    @Override
    public Arma droparLoot() {
        // Se o monstro tem uma arma equipada, dropa ela
        if (this.arma != null) {
            return this.arma;
        }

        // Caso contrário, instancia uma classe aleatória da lista
        if (!classesDeArmasParaLargar.isEmpty()) {
            try {
                int index = (int) (Math.random() * classesDeArmasParaLargar.size());
                Class<? extends Arma> classeArma = classesDeArmasParaLargar.get(index);
                
                // Instancia a arma usando reflexão
                return classeArma.getDeclaredConstructor().newInstance();
                
            } catch (Exception e) {
                System.err.println("[ERRO] Falha ao instanciar arma: " + e.getMessage());
                return null;
            }
        }

        return null; // Sem loot
    }

    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        if (!acoes.isEmpty()) {
            return acoes.get(0);
        }
        return null;
    }

    public int getXpConcedido() {
        return xpConcedido;
    }

    public void setXpConcedido(int xpConcedido) {
        this.xpConcedido = xpConcedido;
    }
}