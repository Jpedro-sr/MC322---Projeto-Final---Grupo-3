/**
 * Esta é uma implementação concreta de uma AcaoDeCombate.
 * Ela representa um ataque físico padrão, cujo dano é baseado na força
 * do usuário e na arma que ele está equipando.
 * (Conforme Tarefa 3, Seção 3.3.2)
 */
public class AtaqueFisico implements AcaoDeCombate {

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        // Para acessar atributos como 'forca' e 'arma', precisamos fazer um "cast"
        // do tipo Combatente para o tipo Personagem.
        Personagem pUsuario = (Personagem) usuario;
        
        // O dano total é a soma da força base do personagem com o dano da arma.
        int danoBase = pUsuario.getForca();
        
        // Verificamos se o personagem tem uma arma equipada.
        Arma arma = pUsuario.getArma();
        int danoArma = (arma != null) ? arma.getDano() : 0;
        
        int danoTotal = danoBase + danoArma;

        System.out.println("\t> " + usuario.getNome() + " ataca " + alvo.getNome() + " com um golpe físico!");
        System.out.println("\t> O golpe causa " + danoTotal + " de dano.");
        
        // Aplicamos o dano no alvo.
        alvo.receberDano(danoTotal);
    }
}