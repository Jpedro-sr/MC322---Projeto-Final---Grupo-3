import java.util.ArrayList;
// Nesse código criamos a classe abstrata Heroi que herda de Personagem. :)

public abstract class Heroi extends Personagem {
    
    // Atributos adicionais de Heroi:
    protected int maxPontosDeVida;
    protected int nivel;
    protected int XP;
    protected int expProximoNivel;
    protected double sorte;

    // Construtor da classe Heroi
    public Heroi(String nome, int pontosDeVida, int forca, int nivel, int XP) {
        super(nome, pontosDeVida, forca);
        this.maxPontosDeVida = pontosDeVida;
        this.nivel = nivel;
        this.XP = XP;
        this.expProximoNivel = 80; // BALANCEAMENTO: Reduzido para 80 para acelerar o primeiro level up
        this.sorte = 0.2; // Sorte base de 20%
    }

    // Método privado para o herói subir de nível
    private void subirDeNivel() {
        this.nivel++;
        this.XP -= this.expProximoNivel;
        this.expProximoNivel *= 1.5; // A meta de XP aumenta em 50% a cada nível

        // Fortalece os atributos do herói
        this.maxPontosDeVida += 25;
        this.forca += 5;
        this.sorte += 0.05; // A sorte aumenta em 5% a cada nível!
        this.pontosDeVida = this.maxPontosDeVida; // Vida totalmente restaurada

        System.out.println("\t*** LEVEL UP! " + this.nome + " subiu para o nível " + this.nivel + "! ***");
        System.out.println("\t> Vida máxima, Força e Sorte aumentadas! Vida totalmente recuperada!");
    }

    // Método que adiciona XP ao herói e verifica se ele pode subir de nível
    public void ganharExperiencia(int XPrecebido) {
        this.XP += XPrecebido;
        System.out.println("[+] Seu heroi " + nome + " ganhou " + XPrecebido + " pontos de XP!" );
        
        // Laço while para o caso de o herói ganhar XP suficiente para subir vários níveis de uma vez
        while (this.XP >= this.expProximoNivel) {
            subirDeNivel();
        }
    }
    
    // Método para equipar uma nova arma
    public void equiparArma(Arma novaArma) {
        // Verifica se o herói tem o nível mínimo necessário
        if (this.nivel >= novaArma.getMinNivel()) {
            this.arma = novaArma; // Equipa a nova arma
            System.out.println("\t> " + this.nome + " equipou a arma: " + novaArma.getClass().getSimpleName() + " (Dano: +" + novaArma.getDano() + ")");
        } else {
            System.out.println("\t> [!] Nível insuficiente para equipar esta arma. O herói precisa ser nível " + novaArma.getMinNivel() + ".");
        }
    }

    @Override
    public void exibirStatus() {
        System.out.println("------------------------");
        System.out.println("~ " + nome);
        System.out.println(" HP: " + pontosDeVida + "/" + maxPontosDeVida);
        System.out.println(" Pontos de Força: " + forca);
        System.out.println(" Nível: " + nivel);
        System.out.println(" XP: " + XP + "/" + expProximoNivel);
        System.out.println(" Sorte: " + (int)(sorte * 100) + "%");
        
        // Adiciona a informação da arma equipada
        if (this.arma != null) {
            System.out.println(" Arma: " + this.arma.getClass().getSimpleName() + " (+" + this.arma.getDano() + " Dano)");
        } else {
            System.out.println(" Arma: Desarmado");
        }
        System.out.println("------------------------");
    }

    // Sobrescreve o método setPontosDeVida para limitar a cura à vida máxima
    @Override
    public void setPontosDeVida(int pontosDeVida) {
        if (pontosDeVida > this.maxPontosDeVida) {
            this.pontosDeVida = this.maxPontosDeVida;
        } else {
            this.pontosDeVida = pontosDeVida;
        }
    }

    // Métodos getters e setters para os atributos adicionais
    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getXP() {
        return XP;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }
    
    public double getSorte(){
        return this.sorte;
    }

    // Vamos obrigar a implementação do método abstrato usarHabilidadeEspecial
    // em todas as classes filhas de Heroi.
    public abstract void usarHabilidadeEspecial(Personagem alvo);

    // Lista de ações disponíveis para o herói
    protected ArrayList<String> acoes = new ArrayList<>();
    {
        acoes.add("Ataque Físico");
        acoes.add("Ataque à Distância");
    }
    
    // Implementação do método escolherAcao para o herói
    // Por hora o herói escolhe uma ação aleatoriamente dentro da lista de ações
@Override
public void escolherAcao(Personagem alvo) {
    int indice = (int) (Math.random() * acoes.size());
    String acaoEscolhida = acoes.get(indice);

    if (acaoEscolhida.equals("Ataque Físico")) {
        this.atacarFisico(alvo);
    } else if (acaoEscolhida.equals("Ataque à Distância")) {
        this.atacarDistancia(alvo);
    }
}
}