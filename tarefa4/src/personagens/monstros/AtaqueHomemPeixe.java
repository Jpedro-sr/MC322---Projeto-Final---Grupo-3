package personagens.monstros;
// Define o ataque do Homem-Peixe, que pode variar conforme o nível de raiva acumulado.
// Se o contador de raiva atingir 3, ele libera um ataque especial (Jato de Amônia).
// Caso contrário, realiza um ataque físico simples e aumenta o contador de raiva.

import combate.AcaoDeCombate;
import combate.Combatente;

public class AtaqueHomemPeixe implements AcaoDeCombate {
    private HomemPeixe usuario;

    public AtaqueHomemPeixe(HomemPeixe usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        HomemPeixe homemPeixe = (HomemPeixe) usuario;
        
        // Se o Homem-Peixe estiver muito irritado, executa o ataque especial.
        if (homemPeixe.getContadorDeRaiva() >= 3) {
            System.out.println("\t*** " + usuario.getNome() + " está enfurecido e lança um Jato de Amônia! ***");
            alvo.receberDano(homemPeixe.getDanoDoJatoDeAmonia());
            homemPeixe.zerarRaiva();
        } else {
            // Ataque físico padrão e incremento do contador de raiva.
            System.out.println("\t> " + usuario.getNome() + " te ataca com suas garras afiadas!");
            alvo.receberDano(homemPeixe.getForca());
            homemPeixe.incrementarRaiva();
        }
    }
}