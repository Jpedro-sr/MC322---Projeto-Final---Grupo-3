public class AtaqueKraken implements AcaoDeCombate {
    private Kraken usuario;

    public AtaqueKraken(Kraken usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        Kraken kraken = (Kraken) usuario;
        
        if (kraken.isHeroiAgarrado()) {
            System.out.println("\t*** O " + usuario.getNome() + " te puxa para as profundezas, te afogando! ***");
            alvo.receberDano(kraken.getDanoDoAfogamento());
            kraken.setHeroiAgarrado(false);
        } else {
            System.out.println("\t> O " + usuario.getNome() + " ataca com seus tentáculos chicoteantes!");
            
            Personagem personagem = (Personagem) usuario;
            alvo.receberDano(personagem.getForca());
            
            if (Math.random() < 0.2) {
                System.out.println("\t> (Sucesso) Um dos tentáculos se enrola em você e te agarra firmemente!");
                kraken.setHeroiAgarrado(true);
            } else {
                System.out.println("\t> (Falha) Você consegue se esquivar dos tentáculos!");
            }
        }
    }
}