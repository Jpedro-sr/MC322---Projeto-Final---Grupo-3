// Esta classe representa o ataque mágico especial da Sereia Encantadora.
// O dano do Canto Divino é calculado somando a força da sereia, o poder do canto e o dano da arma equipada, se houver.

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