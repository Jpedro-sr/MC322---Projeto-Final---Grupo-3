import java.util.ArrayList;
import java.util.Random;

public abstract class Monstro extends Personagem{

    // Atributo adicional de Monstro:
    protected int xpConcedido;
    // Novo atributo da Tarefa 2: Lista de armas que o monstro pode dropar
    protected ArrayList<Arma> listaDeArmasParaLargar;

    // Construtor da classe Monstro
    public Monstro(String nome, int pontosDeVida, int forca, int xpConcedido) {
        super(nome, pontosDeVida, forca);
        this.xpConcedido = xpConcedido;
        // Inicializa a lista de drops como uma lista vazia.
        // As classes concretas de monstros serão responsáveis por adicionar armas a esta lista.
        this.listaDeArmasParaLargar = new ArrayList<>();
    }

    /**
     * Sorteia e retorna uma arma da lista de drops do monstro.
     * @return um objeto Arma aleatório da lista, ou null se a lista estiver vazia.
     */
    public Arma largaArma() {
        // Verifica se o monstro tem armas para dropar
        if (listaDeArmasParaLargar.isEmpty()) {
            return null;
        }

        // Sorteia um índice aleatório da lista
        Random random = new Random();
        int index = random.nextInt(listaDeArmasParaLargar.size());

        // Retorna a arma no índice sorteado
        return listaDeArmasParaLargar.get(index);
    }

    @Override
    public void exibirStatus() {
        System.out.println("------------------------");
        System.out.println("~ " + nome);
        System.out.println(" HP: " + pontosDeVida);
        System.out.println(" Pontos de Força: " + forca);
        System.out.println(" XP Concedido ao ser derrotado: " + xpConcedido);
        System.out.println("------------------------");
    }

    // Métodos getters e setters para o atributo adicional xpConcedido.
    public int getXpConcedido() {
        return xpConcedido;
    }

    public void setXpConcedido(int xpConcedido) {
        this.xpConcedido = xpConcedido;
    }
}