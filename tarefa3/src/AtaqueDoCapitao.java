// Esta classe representa o ataque especial do Capitão Cabeçudo.
// O dano é calculado com base na força do capitão, um fator de sorte e sua coragem líquida.
// Se o capitão estiver equipado com uma arma, o dano da arma é somado ao ataque.
public class AtaqueDoCapitao implements AcaoDeCombate {
    private CapitaoCabecudo usuarioCapitao;

    // Guarda o capitão que irá executar o ataque.
    public AtaqueDoCapitao(CapitaoCabecudo usuario) {
        this.usuarioCapitao = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        // Calcula o dano base usando a força do capitão e um fator aleatório.
        int danoBase = usuarioCapitao.getForca() * ((int)(Math.random() * 4));
        if (danoBase == 0) {
            System.out.println("\t(Fracasso) O capitão erra o ataque!");
        } else {
            // Soma coragem líquida e dano da arma, se houver.
            int danoTotal = danoBase + usuarioCapitao.getCoragemLiquida();
            if (usuarioCapitao.getArma() != null) {
                danoTotal += usuarioCapitao.getArma().getDano();
            }
            System.out.println("\t> " + usuarioCapitao.getNome() + " ataca causando " + danoTotal + " de dano!");
            alvo.receberDano(danoTotal);
        }
    }
}