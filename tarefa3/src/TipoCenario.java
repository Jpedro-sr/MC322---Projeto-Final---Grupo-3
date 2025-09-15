public enum TipoCenario {
    PRAIA_ASSOMBRADA("Praia Assombrada"),
    GRUTA_SUBMERSA("Gruta Submersa"),
    RECIFE_DE_CORAIS_AFIADOS("Recife de Corais Afiados"),
    CEMITERIO_DE_NAVIOS("Cemitério de Navios"),
    COVIL_DO_KRAKEN("Covil do Kraken");

    private final String descricao;

    TipoCenario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}