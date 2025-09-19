/* Esta é uma implementação concreta de uma AcaoDeCombate.
 * Ela representa um ataque físico padrão, onde o dano depende da força
 * do personagem e do dano da arma equipada. */
public class AtaqueFisico implements AcaoDeCombate {

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        // Fazemos o cast para acessar atributos específicos do personagem.
        Personagem pUsuario = (Personagem) usuario;

        // O dano base é a força do personagem.
        int danoBase = pUsuario.getForca();

        // Se houver arma equipada, soma o dano da arma ao dano base.
        Arma arma = pUsuario.getArma();
        int danoArma = (arma != null) ? arma.getDano() : 0;

        int danoTotal = danoBase + danoArma;

        System.out.println("\t> " + usuario.getNome() + " ataca " + alvo.getNome() + " com um golpe físico!");
        System.out.println("\t> O golpe causa " + danoTotal + " de dano.");

        // Aplica o dano ao alvo.
        alvo.receberDano(danoTotal);
    }
}