public class TiroCaolhoAcao implements AcaoDeCombate {
    private CapitaoCabecudo usuarioCapitao;

    public TiroCaolhoAcao(CapitaoCabecudo usuario) {
        this.usuarioCapitao = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        int danoHabilidade = usuarioCapitao.getForca() * 6;
        int danoTotal = danoHabilidade;
        if (usuarioCapitao.getArma() != null) {
            danoTotal += usuarioCapitao.getArma().getDano();
        }
        System.out.println("\t> Dano CRÍTICO de " + danoTotal + " pontos!");
        alvo.receberDano(danoTotal);
    }
}