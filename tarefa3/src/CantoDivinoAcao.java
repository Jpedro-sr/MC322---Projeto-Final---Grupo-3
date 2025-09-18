// Representa a ação de ataque mágico da Sereia.
public class CantoDivinoAcao implements AcaoDeCombate {
    private SereiaEncantadora usuario;

    public CantoDivinoAcao(SereiaEncantadora usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        SereiaEncantadora sereia = (SereiaEncantadora) usuario;
        
        System.out.println("\t*** " + usuario.getNome() + " entoa um Canto Divino que ecoa pelas águas! ***");
        
        int danoTotal = sereia.getForca() + sereia.getCantoDivino() + (sereia.getArma() != null ? sereia.getArma().getDano() : 0);
        
        System.out.println("\t> O canto mágico causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
        alvo.receberDano(danoTotal);
    }
}