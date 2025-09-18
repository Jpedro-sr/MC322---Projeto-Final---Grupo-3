public class AtaqueDoCapitao implements AcaoDeCombate {
    private CapitaoCabecudo usuarioCapitao;

    public AtaqueDoCapitao(CapitaoCabecudo usuario) {
        this.usuarioCapitao = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        // Usamos 'usuarioCapitao' em vez de 'usuario'
        int danoBase = usuarioCapitao.getForca() * ((int)(Math.random() * 4));
        if (danoBase == 0) {
            System.out.println("\t(Fracasso) O capitão erra o ataque!");
        } else {
            int danoTotal = danoBase + usuarioCapitao.getCoragemLiquida();
            if (usuarioCapitao.getArma() != null) {
                danoTotal += usuarioCapitao.getArma().getDano();
            }
            System.out.println("\t> " + usuarioCapitao.getNome() + " ataca causando " + danoTotal + " de dano!");
            alvo.receberDano(danoTotal);
        }
    }
}