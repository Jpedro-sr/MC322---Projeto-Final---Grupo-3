// Ação de ataque da Sereia, com lógica probabilística.
public class AtaqueSereia implements AcaoDeCombate {
    private SereiaEncantadora usuario;

    public AtaqueSereia(SereiaEncantadora usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        SereiaEncantadora sereia = (SereiaEncantadora) usuario;
        
        if (Math.random() < 0.3) {
            System.out.println("\t*** " + usuario.getNome() + " entoa um canto divino! ***");
            int dano = sereia.getForca() + sereia.getCantoDivino();
            alvo.receberDano(dano);
            System.out.println("\t> O canto causa " + dano + " pontos de dano!");
        } else {
            System.out.println("\t> " + usuario.getNome() + " te ataca normalmente!");
            int dano = sereia.getForca();
            alvo.receberDano(dano);
        }
    }
}