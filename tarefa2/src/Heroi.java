// Nesse código criamos a classe abstrata Heroi que herda de Personagem. :)

public abstract class Heroi extends Personagem {
    
    // Atributos adicionais de Heroi:
    protected int maxPontosDeVida;
    protected int nivel;
    protected int XP;
    protected int xpProximoNivel; // Atributo xpProximoNivel adicionado
    protected double sorte; // Atributo sorte adicionado


    // Construtor da classe Heroi
    public Heroi(String nome, int pontosDeVida, int forca, int nivel, int XP) {
        // Veja que aqui usamos o super para chamar apenas o construtor da classe Personagem
        super(nome, pontosDeVida, forca);
        // Inicializamos os atributos adicionais de Heroi
        this.maxPontosDeVida = pontosDeVida;
        this.nivel = nivel;
        this.XP = XP;
        this.xpProximoNivel = 100; // O primeiro nível requer 100 XP
        this.sorte = 0.5; // Inicialmente, a sorte é 50%
    }

    // Método para subir o herói de nível
    // Veja que mantemos ele private, pois só deve ser chamado dentro da classe Heroi
    private void subirDeNivel() {
        this.nivel++;
        this.XP = this.XP - this.xpProximoNivel; // Reduz o XP o suficiente para o próximo nível
        this.xpProximoNivel += 50; // Incrementa o 50 de XP necessário para o próximo nível

        // Aumenta os atributos do herói ao subir de nível
        this.maxPontosDeVida += 20; // Aumenta a vida
        this.forca += 5; // Aumenta a força
        this.pontosDeVida = this.maxPontosDeVida; // Restaura a vida ao máximo

        // Mensagem de nível alcançado
        System.out.println("\t*** LEVEL UP! " + this.nome + " subiu para o nível " + this.nivel + "! ***");
        System.out.println("\t> Vida máxima e Força aumentadas! Vida totalmente recuperada!");
    }

    // Método para equipar uma arma
    public void equiparArma(Arma novaArma) {
        // Verifica se o herói tem o nível mínimo necessário
        if (this.nivel >= novaArma.getMinNivel()) {
            this.arma = novaArma; // Equipa a nova arma
            System.out.println("[+] " + this.nome + " equipou a arma: " + novaArma.getClass().getSimpleName() + " (Dano: +" + novaArma.getDano() + ")");
        } else {
            System.out.println("[!] Nível insuficiente para equipar esta arma. O herói precisa ser nível " + novaArma.getMinNivel() + ".");
        }
    }

    // Método que adiciona XP ao herói:
    public void ganharExperiencia(int XPrecebido) {
        this.XP += XPrecebido;
        System.out.println("[+] Seu heroi " + nome + " ganhou " + XPrecebido + " pontos de XP!" );

        // Verifica se o herói tem XP suficiente para subir de nível mais de uma vez
        while (this.XP >= this.xpProximoNivel) {
            subirDeNivel();
        }
    }

    @Override
    public void exibirStatus() {
        System.out.println("------------------------");
        System.out.println("~ " + nome);
        System.out.println(" HP: " + pontosDeVida + "/" + maxPontosDeVida);
        System.out.println(" Pontos de Força: " + forca);
        System.out.println(" Nível: " + nivel);
        System.out.println(" XP: " + XP);
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

    // Vamos obrigar a implementação do método abstrato usarHabilidadeEspecial
    // em todas as classes filhas de Heroi.
    public abstract void usarHabilidadeEspecial(Personagem alvo);
}