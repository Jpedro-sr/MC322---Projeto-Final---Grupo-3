import java.util.ArrayList;
import java.util.List;

public abstract class Monstro extends Personagem implements Lootavel {
    protected int xpConcedido;
    protected ArrayList<Arma> listaDeArmasParaLargar;
    protected List<AcaoDeCombate> acoes;

    public Monstro(String nome, int pontosDeVida, int forca, int xpConcedido) {
        super(nome, pontosDeVida, forca);
        this.xpConcedido = xpConcedido;
        this.listaDeArmasParaLargar = new ArrayList<>();
        this.acoes = new ArrayList<>();
    }
    
    // Implementação CORRIGIDA: dropa arma atual, não aleatória da lista
    @Override
    public Item droparLoot() {
        // Se o monstro tem uma arma equipada, dropa ela
        if (this.arma != null) {
            return this.arma;
        }
        
        // Se não tem arma equipada, dropa aleatória da lista (fallback)
        if (!listaDeArmasParaLargar.isEmpty()) {
            int index = (int) (Math.random() * listaDeArmasParaLargar.size());
            return listaDeArmasParaLargar.get(index);
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