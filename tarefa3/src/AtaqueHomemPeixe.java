// Ação de ataque do Homem-Peixe, com lógica de contador de raiva.
public class AtaqueHomemPeixe implements AcaoDeCombate {
    private HomemPeixe usuario;

    public AtaqueHomemPeixe(HomemPeixe usuario) {
        this.usuario = usuario;
    }

    @Override
public void executar(Combatente usuario, Combatente alvo) {
    HomemPeixe homemPeixe = (HomemPeixe) usuario;
    
    if (homemPeixe.getContadorDeRaiva() >= 3) {
        System.out.println("\t*** " + usuario.getNome() + " está enfurecido e lança um Jato de Amônia! ***");
        alvo.receberDano(homemPeixe.getDanoDoJatoDeAmonia());
        homemPeixe.zerarRaiva();
    } else {
        System.out.println("\t> " + usuario.getNome() + " te ataca com suas garras afiadas!");
        alvo.receberDano(homemPeixe.getForca());
        homemPeixe.incrementarRaiva();
        }
    }
}