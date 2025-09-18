/**
 * Enum que representa os diferentes tipos de ambientes do jogo.
 * Cada cenário possui uma descrição e pode aplicar um efeito narrativo.
 * (Conforme Tarefa 3, Seção 3.2)
 */
public enum TipoCenario {
    PRAIA_ASSOMBRADA("Praia Assombrada") {
        @Override
        public Evento criarEvento() {
            return new EmboscadaCarangueijos();
        }
    },
    GRUTA_SUBMERSA("Gruta Submersa") {
        @Override
        public Evento criarEvento() {
            return new CardumePeixesDescontrolados();
        }
    },
    COVIL_DO_KRAKEN("Covil do Kraken") {
        @Override
        public Evento criarEvento() {
            return new ElevacaoNivelAgua();
        }
    },
    RECIFE_DE_CORAIS_AFIADOS("Recife de Corais Afiados") {
        @Override
        public Evento criarEvento() {
            return null; // Sem evento específico
        }
    },
    CEMITERIO_DE_NAVIOS("Cemitério de Navios") {
        @Override
        public Evento criarEvento() {
            return null; // Sem evento específico
        }
    };

    // Atributo para guardar a descrição de cada cenário.
    private final String descricao;

    /**
     * Construtor do Enum.
     * @param descricao A descrição textual do cenário.
     */
    TipoCenario(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna a descrição formatada do cenário.
     * @return A descrição do cenário.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Método que descreve o ambiente para o jogador quando uma fase inicia.
     * No futuro, poderia aplicar efeitos reais (ex: bônus ou penalidades).
     * @param heroi O herói que entra no cenário.
     */
    public void aplicarEfeitos(Heroi heroi) {
        System.out.println(heroi.getNome() + " adentra o cenário: " + this.descricao + "...");
        
        // Verifica se há evento para este cenário
        Evento evento = criarEvento();
        if (evento != null && evento.verificarGatilho(heroi)) {
            evento.executar(heroi);
        }
    }
    
    /**
     * Método abstrato que cada cenário implementa para criar seu evento específico.
     * @return o evento específico do cenário ou null se não houver.
     */
    public abstract Evento criarEvento();
}