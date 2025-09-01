public class CapitaoCabecudo extends Heroi {
    
    private int coragemLiquida; // Quantidade de Rum que o Capitão bebeu

    // Construtor da classe CapitaoCabecudo
    public CapitaoCabecudo() {

        // Chamamos o construtor da superclasse Heroi com os valores específicos do Capitão Cabeçudo
        super("Capitão Cabeçudo", 100, 1, 1, 0);

        // Atributo específico do Capitão Cabeçudo: Coragem Líquida (Rum):
        this.coragemLiquida = 2; // O capitão começa com 2 doses de Rum (garantindo pelo menos 2 de dano no ataque normal, quando acerta)

    }

    // Lógica do ataque do CapitaoCabecudo:
    // 1) Jogamos um d6 e, caso caia em 6, usaremos a habilidade especial (ataque critico)
    // 2) Caso contrário, o ataque normal será a força do personagem * multiplicador
    // O multiplicador é um valor de 0 a 3 sorteado aleatoriamente por outro dado lançado.
    // Só faremos o ataque criítico se o d6 cair em 6.
    @Override
    public void atacar(Personagem alvo){
        System.out.println("O temido" + nome + " saca seu mosquete enferrujado");

        // Rolamos um dado que vai de 1 a 6
        int d6 = (int) (Math.random() * 6) + 1;
        System.out.println("Ele lança um d6 e tira: " + d6 + "!");

        if (d6 == 6){
            System.out.println("Sorte de pirata! Ele usará sua habilidade especial: Tiro Caolho!");
            // Aqui devemos fazer a lógica do ataque critico
            usarHabilidadeEspecial(alvo);

        } else {
            System.out.println("Mais azarado que pirata! Ele usará seu ataque normal!");

            // Rolamos um dado que vai de 0 a 3
            int multiplicador = (int) (Math.random() * 4);
            System.out.println("Ele lança um d4 e...");
            System.out.println("Pelas barbas de Netuno! O multiplicador sorteado foi: " + multiplicador);

            // Calculamos o dano base do ataque normal
            int danoBase  = this.forca * multiplicador;
            
            // Verificamos se o dano base é 0 ou não.
            if (danoBase == 0) {
                System.out.println("O capitão tropeça numa garrafa de Rum...");
                System.out.println("E erra o ataque!!!");
            } else {
                System.out.println("Ele atira seu mosquete contra " + alvo.getNome() + "!");
                int danoTotal = danoBase + this.coragemLiquida; 
                System.out.println("Com a ajuda de sua coragem líquida (Rum), seu ataque ganha " + this.coragemLiquida + " pontos de dano.");
                System.out.println("Causando " + danoTotal + " pontos de dano no total.");
                alvo.receberDano(danoTotal);
            }
        }

    }

    @Override
    public void usarHabilidadeEspecial(Personagem alvo) {
        // Mensagem lúdica de uso da habilidade especial
        System.out.println("O capitão mira com seu único olho e dispara um tiro que acerta em cheio!");

        // Cálculo do dano da habilidade especial
        int danoTotal = this.forca * 6;

        // Mensagem de ataque crítico
        System.out.println("Ele causa um dano crítico de " + danoTotal + " pontos de dano!");

        // De fato, aplicamos o dano ao alvo
        alvo.receberDano(danoTotal);
        }
}
