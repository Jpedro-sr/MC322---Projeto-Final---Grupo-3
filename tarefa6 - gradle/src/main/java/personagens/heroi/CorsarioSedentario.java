package personagens.heroi;

import combate.AcaoDeCombate;
import combate.Combatente;
import combate.GolpeDeMestreAcao;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Classe concreta do herói Corsário Sedentário.
 * Focado em estudar inimigos e acumular conhecimento para golpes devastadores.
 * Anotado com JAXB para permitir serialização.
 */
@XmlRootElement
public class CorsarioSedentario extends Heroi {

    @XmlElement
    private int pontosDeEstudo;

    /**
     * Construtor padrão que cria um Corsário Sedentário completo.
     * Usado tanto para criar novos heróis quanto para deserialização JAXB.
     */
    public CorsarioSedentario() {
        // Chama construtor da superclasse com valores completos
        super("Corsário Sedentário", 120, 2, 1, 0); // NÍVEL 1, não 0!
        
        // Inicializa atributos específicos
        this.pontosDeEstudo = 0;
        this.sorte = 0.3;
        
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
            this.acoes.add(new AnalisarInimigoAcao(this));
            this.acoes.add(new GolpeDeMestreAcao(this));
        }
    }

    /**
     * Escolhe a ação de combate baseada nos pontos de estudo acumulados.
     * Estuda até ter conhecimento suficiente, depois executa golpe devastador.
     */
    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        // Garante que as ações existem (importante após deserialização)
        inicializarAcoes();
        
        // Se já estudou o suficiente (3+ pontos), usa golpe de mestre
        if (this.pontosDeEstudo >= 3) {
            return acoes.get(1); // Golpe de Mestre
        } else {
            // Continua estudando o inimigo
            return acoes.get(0); // Analisar Inimigo
        }
    }

    // Métodos de gerenciamento de pontos de estudo
    public int getPontosDeEstudo() {
        return this.pontosDeEstudo;
    }

    public void adicionarPontosDeEstudo(int pontos) {
        this.pontosDeEstudo += pontos;
    }

    public void zerarPontosDeEstudo() {
        this.pontosDeEstudo = 0;
    }
}