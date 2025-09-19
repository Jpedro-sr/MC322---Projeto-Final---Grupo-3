public class GolpeDeTentaculoAcao implements AcaoDeCombate {
    private Kraken usuario;

    public GolpeDeTentaculoAcao(Kraken usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        System.out.println("\t> O " + usuario.getNome() + " ataca com seus tentáculos chicoteantes!");

        // Cast para Personagem para acessar getForca() e getArma()
        Personagem personagem = (Personagem) usuario;
        int danoTotal = personagem.getForca() + (personagem.getArma() != null ? personagem.getArma().getDano() : 0);

        System.out.println("\t> O golpe causa " + danoTotal + " de dano.");
        alvo.receberDano(danoTotal);

        // Cast para Kraken para acessar setHeroiAgarrado()
        Kraken kraken = (Kraken) usuario;

        // Sorteia se o Kraken conseguiu agarrar o alvo ou não (20% de chance)
        boolean agarrou = Math.random() < 0.2;
        if (agarrou) {
            System.out.println("\t> (Sucesso) Um dos tentáculos se enrola em você e te agarra firmemente!");
            kraken.setHeroiAgarrado(true);
        } else {
            System.out.println("\t> (Falha) Você consegue se esquivar dos tentáculos!");
        }
    }
}