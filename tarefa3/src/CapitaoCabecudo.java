public class CapitaoCabecudo extends Heroi {
    
    // Atributo específico do Capitão Cabeçudo: Coragem Líquida (Rum)
    private int coragemLiquida;
    
    // ADICIONADO: Sistema de raiva para o AtaqueTridenteAcao
    private int contadorDeRaiva;
    private int ataquesParaEnfurecer;

    // Construtor da classe CapitaoCabecudo
    public CapitaoCabecudo() {
        // Chamamos o construtor da superclasse Heroi com os valores ESPECÍFICOS E BALANCEADOS do Capitão Cabeçudo
        super("Capitão Cabeçudo", 330, 6, 1, 0);
        this.coragemLiquida = 4;
        this.sorte = 0.25; // Sorte inicial do Capitão Cabeçudo é 25%
        
        // ADICIONADO: Inicialização do sistema de raiva
        this.contadorDeRaiva = 0;
        this.ataquesParaEnfurecer = 3;

        // No construtor, definimos as ações que ele pode fazer, passando a própria instância.
        this.acoes.add(new AtaqueDoCapitao(this));
        this.acoes.add(new TiroCaolhoAcao(this));
    }

    // A lógica de escolher a ação agora vive aqui, baseada na sorte.
    // O antigo método atacar() foi removido.
    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        // Para variar a narração, sorteamos uma frase de ataque
        String[] frasesDeAtaque = {
            "O temido " + nome + " saca seu mosquete enferrujado!",
            "Com um grito de 'Pela minha honra!', " + nome + " avança contra o inimigo!",
            nome + " aproveita uma brecha na defesa adversária e parte para o ataque!",
            "'Você vai virar comida de peixe!', brada " + nome + " ao atacar!",
            "Arrr! " + nome + " mostra por que é o terror dos sete mares!"
        };
        String fraseDoTurno = frasesDeAtaque[(int)(Math.random() * frasesDeAtaque.length)];
        System.out.println(fraseDoTurno);

        // A sorte decide se ele usa a ação especial (índice 1) ou o ataque normal (índice 0).
        if (Math.random() < this.sorte) {
            System.out.println("\t*** Sorte de pirata! Um golpe de mestre! ***");
            return acoes.get(1); // Retorna a ação "Tiro Caolho"
        } else {
            return acoes.get(0); // Retorna a ação "Ataque do Capitão"
        }
    }
    
    // Getter para o atributo único, para que as classes de Ação possam acessá-lo.
    public int getCoragemLiquida() {
        return coragemLiquida;
    }
    
    // MÉTODOS ADICIONADOS: Sistema de raiva para AtaqueTridenteAcao
    public void incrementarRaiva() {
        this.contadorDeRaiva++;
    }

    public int getContadorDeRaiva() {
        return this.contadorDeRaiva;
    }

    public int getAtaquesParaEnfurecer() {
        return this.ataquesParaEnfurecer;
    }
    
    public void zerarRaiva() {
        this.contadorDeRaiva = 0;
    }
}