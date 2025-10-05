package personagens.monstros;
// Representa o ataque da Sereia Encantadora durante o combate.
// Ela pode realizar um ataque especial com seu canto divino, que tem chance de causar dano extra.
// Caso o ataque especial não ocorra, a sereia realiza um ataque físico padrão.

import combate.AcaoDeCombate;
import combate.Combatente;

public class AtaqueSereia implements AcaoDeCombate {
    private SereiaEncantadora usuario;

    public AtaqueSereia(SereiaEncantadora usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        SereiaEncantadora sereia = (SereiaEncantadora) usuario;
        
        // 30% de chance de ativar o canto divino, causando dano adicional.
        if (Math.random() < 0.3) {
            System.out.println("\t*** " + usuario.getNome() + " entoa um canto divino! ***");
            int dano = sereia.getForca() + sereia.getCantoDivino();
            alvo.receberDano(dano);
            System.out.println("\t> O canto causa " + dano + " pontos de dano!");
        } else {
            // Caso contrário, realiza um ataque físico simples.
            System.out.println("\t> " + usuario.getNome() + " te ataca normalmente!");
            int dano = sereia.getForca();
            alvo.receberDano(dano);
        }
    }
}