package personagens.monstros;
// Esta classe representa o ataque especial do Kraken.
// O Kraken pode agarrar o herói com seus tentáculos. Se o herói estiver agarrado, recebe dano de afogamento.
// Caso contrário, o Kraken tenta atacar com seus tentáculos e pode agarrar o herói para o próximo turno.

import combate.AcaoDeCombate;
import combate.Combatente;
import personagens.Personagem;

public class AtaqueKraken implements AcaoDeCombate {
    private Kraken usuario;

    public AtaqueKraken(Kraken usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        Kraken kraken = (Kraken) usuario;
        
        // Se o herói estiver agarrado, aplica o dano de afogamento e libera o herói.
        if (kraken.isHeroiAgarrado()) {
            System.out.println("\t*** O " + usuario.getNome() + " te puxa para as profundezas, te afogando! ***");
            alvo.receberDano(kraken.getDanoDoAfogamento());
            kraken.setHeroiAgarrado(false);
        } else {
            // Ataque padrão com tentáculos e chance de agarrar o herói.
            System.out.println("\t> O " + usuario.getNome() + " ataca com seus tentáculos chicoteantes!");
            
            Personagem personagem = (Personagem) usuario;
            alvo.receberDano(personagem.getForca());
            
            // 20% de chance de agarrar o herói para o próximo turno.
            if (Math.random() < 0.2) {
                System.out.println("\t> (Sucesso) Um dos tentáculos se enrola em você e te agarra firmemente!");
                kraken.setHeroiAgarrado(true);
            } else {
                System.out.println("\t> (Falha) Você consegue se esquivar dos tentáculos!");
            }
        }
    }
}