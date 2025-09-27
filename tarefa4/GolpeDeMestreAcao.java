
public class GolpeDeMestreAcao implements AcaoDeCombate {
    private CorsarioSedentario usuario;

    public GolpeDeMestreAcao(CorsarioSedentario usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        CorsarioSedentario corsario = (CorsarioSedentario) usuario;

        System.out.println("\t*** " + usuario.getNome() + " concentra todo seu conhecimento em um Golpe de Mestre! ***");

        int bonusDeDano = corsario.getPontosDeEstudo() * 15;
        int danoArma = (corsario.getArma() != null ? corsario.getArma().getDano() : 0);
        int danoTotal = corsario.getForca() + bonusDeDano + danoArma;

        System.out.println("\t> Bônus de conhecimento: +" + bonusDeDano + " de dano!");
        if(corsario.getArma() != null){
            System.out.println("\t> Sua arma, " + corsario.getArma().getNome() + ", adiciona +" + danoArma + " de dano!");
        }

        System.out.println("\t> Dano total: " + danoTotal + " pontos!");
        alvo.receberDano(danoTotal);
        corsario.zerarPontosDeEstudo();
    }
}