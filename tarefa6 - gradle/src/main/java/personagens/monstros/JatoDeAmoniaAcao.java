package personagens.monstros;
// Representa a ação de ataque especial do Homem-Peixe, que libera a fúria.

import combate.AcaoDeCombate;
import combate.Combatente;

public class JatoDeAmoniaAcao implements AcaoDeCombate {
    private HomemPeixe usuario;

    public JatoDeAmoniaAcao(HomemPeixe usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        HomemPeixe homemPeixe = (HomemPeixe) usuario;

        System.out.println("\t*** " + usuario.getNome() + " está enfurecido e lança um devastador Jato de Amônia! ***");

        int danoTotal = homemPeixe.getDanoDoJatoDeAmonia() + (homemPeixe.getArma() != null ? homemPeixe.getArma().getDano() : 0);

        System.out.println("\t> O jato tóxico causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
        alvo.receberDano(danoTotal);

        System.out.println("\t> Após usar toda sua raiva, o " + usuario.getNome() + " se acalma.");
        homemPeixe.zerarRaiva();
    }
}