import java.util.Random;
import java.util.ArrayList;

public abstract class Monstro extends Personagem{

    // Atributos adicionais de Monstro:
    protected int xpConcedido;
    protected ArrayList<Arma> listaDeArmasParaLargar; // Lista de armas que o monstro pode largar

    // Construtor da classe Monstro
    public Monstro(String nome, int pontosDeVida, int forca, int xpConcedido) {
        super(nome, pontosDeVida, forca);
        this.xpConcedido = xpConcedido;

        // Vamos inicializar a lista de armas que o monstro pode largar como uma lista vazia
        this.listaDeArmasParaLargar = new ArrayList<>();
    }

    // Método que sorteia uma arma da lista de armas que o monstro vai largar
    public Arma largaArma() {
        if (listaDeArmasParaLargar.isEmpty()){
            return null;
        }
        
        // Sorteia um índice aleatório da lista
        Random random = new Random();
        int index = random.nextInt(listaDeArmasParaLargar.size());

        // Retorna a arma sorteada no index sorteado
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