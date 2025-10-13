package combate;

import personagens.Personagem;
import personagens.monstros.SereiaEncantadora;


public class GolpeDeCaudaAcao implements AcaoDeCombate {
    private SereiaEncantadora usuario;

    public GolpeDeCaudaAcao(SereiaEncantadora usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        System.out.println("\t> " + usuario.getNome() + " ataca rapidamente com um Golpe de Cauda!");

        // Cast para Personagem para acessar getForca() e getArma()
        Personagem personagem = (Personagem) usuario;
        int danoTotal = personagem.getForca() + (personagem.getArma() != null ? personagem.getArma().getDano() : 0);

        System.out.println("\t> O golpe causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
        alvo.receberDano(danoTotal);
    }
}