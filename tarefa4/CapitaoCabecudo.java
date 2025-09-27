
public class CapitaoCabecudo extends Heroi {
    
    // Representa a coragem líquida do Capitão (quantidade de rum disponível).
    private int coragemLiquida;
    
    // Sistema de raiva: controla quantos ataques faltam para o Capitão ficar enfurecido.
    private int contadorDeRaiva;
    private int ataquesParaEnfurecer;

    // Construtor define os atributos iniciais e as ações disponíveis do Capitão Cabeçudo.
    public CapitaoCabecudo() {
        // Inicializa o herói com valores específicos e balanceados.
        super("Capitão Cabeçudo", 330, 6, 1, 0);
        this.coragemLiquida = 4;
        this.sorte = 0.25; // Sorte inicial de 25%
        
        // Inicializa o sistema de raiva.
        this.contadorDeRaiva = 0;
        this.ataquesParaEnfurecer = 3;

        // Adiciona as ações que o Capitão pode executar durante o combate.
        this.acoes.add(new AtaqueDoCapitao(this));
        this.acoes.add(new TiroCaolhoAcao(this));
    }

    // Escolhe a ação de combate do turno, variando a narração para dar mais personalidade.
    // A escolha depende da sorte: se for bem-sucedida, executa o ataque especial.
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
    
    // Retorna a coragem líquida atual do Capitão.
    public int getCoragemLiquida() {
        return coragemLiquida;
    }
    
    // Métodos para manipular o sistema de raiva.
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