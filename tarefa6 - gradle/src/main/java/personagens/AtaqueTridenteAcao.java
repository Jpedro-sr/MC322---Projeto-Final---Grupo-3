package personagens;
// Representa o ataque especial com tridente, que pode ser realizado por diferentes personagens.
// O dano é calculado pela força do personagem e pelo dano da arma equipada.
// Dependendo do tipo do personagem, o ataque pode aumentar o nível de raiva.

import combate.AcaoDeCombate;
import combate.Combatente;
import personagens.heroi.CapitaoCabecudo;
import personagens.monstros.HomemPeixe;

public class AtaqueTridenteAcao implements AcaoDeCombate {
    private Personagem usuario;

    // Construtor aceita qualquer personagem que possa usar o tridente.
    public AtaqueTridenteAcao(Personagem usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        Personagem personagem = (Personagem) usuario;

        System.out.println("\t> " + usuario.getNome() + " desfere um poderoso Ataque de Tridente!");

        int danoTotal = personagem.getForca() + (personagem.getArma() != null ? personagem.getArma().getDano() : 0);

        System.out.println("\t> O ataque causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
        alvo.receberDano(danoTotal);

        // Se o personagem for Capitão Cabeçudo ou Homem-Peixe, o ataque aumenta a raiva.
        if (usuario instanceof CapitaoCabecudo) {
            CapitaoCabecudo capitao = (CapitaoCabecudo) usuario;
            System.out.println("\t> A violência do ataque deixa o Capitão mais enfurecido!");
            capitao.incrementarRaiva();
            System.out.println("\t> ... sua raiva aumenta! (" + capitao.getContadorDeRaiva() + "/" + capitao.getAtaquesParaEnfurecer() + ")");
        } else if (usuario instanceof HomemPeixe) {
            HomemPeixe homemPeixe = (HomemPeixe) usuario;
            System.out.println("\t> O ataque aumenta a raiva do " + usuario.getNome() + "!");
            homemPeixe.incrementarRaiva();
            System.out.println("\t> ... raiva: (" + homemPeixe.getContadorDeRaiva() + "/" + homemPeixe.getAtaquesParaEnfurecer() + ")");
        }
    }
}