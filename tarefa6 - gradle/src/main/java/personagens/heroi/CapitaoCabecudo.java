package personagens.heroi;

import combate.AcaoDeCombate;
import combate.Combatente;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Classe concreta do herói Capitão Cabeçudo.
 * Pirata versátil com ataques físicos e habilidade de tiro caolho.
 * Anotada com JAXB para permitir serialização.
 */
@XmlRootElement
public class CapitaoCabecudo extends Heroi {
    
    @XmlElement
    private int coragemLiquida;
    
    @XmlElement
    private int contadorDeRaiva;
    
    @XmlElement
    private int ataquesParaEnfurecer;

    /**
     * Construtor padrão que cria um Capitão Cabeçudo completo.
     * Usado tanto para criar novos heróis quanto para deserialização JAXB.
     */
    public CapitaoCabecudo() {
        // Chama construtor da superclasse com valores completos
        super("Capitão Cabeçudo", 330, 6, 1, 0); // NÍVEL 1, não 0!
        
        // Inicializa atributos específicos
        this.coragemLiquida = 4;
        this.sorte = 0.25;
        this.contadorDeRaiva = 0;
        this.ataquesParaEnfurecer = 3;
        
        // Inicializa ações de combate
        inicializarAcoes();
    }
    
    /**
     * Inicializa ou recria as ações de combate.
     * Chamado no construtor e após deserialização JAXB.
     */
    private void inicializarAcoes() {
        if (this.acoes == null) {
            this.acoes = new java.util.ArrayList<>();
        }
        
        // Só adiciona se a lista estiver vazia (evita duplicação)
        if (this.acoes.isEmpty()) {
            this.acoes.add(new AtaqueDoCapitao(this));
            this.acoes.add(new TiroCaolhoAcao(this));
        }
    }

    /**
     * Escolhe a ação de combate baseada na sorte do capitão.
     * Exibe frases temáticas de pirata antes de atacar.
     */
    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        // Garante que as ações existem (importante após deserialização)
        inicializarAcoes();
        
        // Frases temáticas variadas
        String[] frasesDeAtaque = {
            "O temido " + nome + " saca seu mosquete enferrujado!",
            "Com um grito de 'Pela minha honra!', " + nome + " avança contra o inimigo!",
            nome + " aproveita uma brecha na defesa adversária e parte para o ataque!",
            "'Você vai virar comida de peixe!', brada " + nome + " ao atacar!",
            "Arrr! " + nome + " mostra por que é o terror dos sete mares!"
        };
        String fraseDoTurno = frasesDeAtaque[(int)(Math.random() * frasesDeAtaque.length)];
        System.out.println(fraseDoTurno);

        // Decisão baseada na sorte
        if (Math.random() < this.sorte) {
            System.out.println("\t*** Sorte de pirata! Um golpe de mestre! ***");
            return acoes.get(1); // Tiro Caolho
        } else {
            return acoes.get(0); // Ataque do Capitão
        }
    }
    
    // Getters e métodos específicos do sistema de raiva
    public int getCoragemLiquida() { 
        return coragemLiquida; 
    }
    
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