// Representa a ação de afogamento do Kraken.
public class AfogamentoAcao implements AcaoDeCombate {
    private Kraken usuario;

    public AfogamentoAcao(Kraken usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        System.out.println("\t*** O " + usuario.getNome() + " te puxa para as profundezas da água, te afogando! ***");
        
        Kraken kraken = (Kraken) usuario;
        Personagem personagemUsuario = (Personagem) usuario;
        
        int danoTotal = kraken.getDanoDoAfogamento() + 
                       (personagemUsuario.getArma() != null ? personagemUsuario.getArma().getDano() : 0);
        
        System.out.println("\t> O afogamento causa " + danoTotal + " de dano massivo em " + alvo.getNome() + "!");
        alvo.receberDano(danoTotal);
        
        System.out.println("\t> Após receber o dano, o " + alvo.getNome() + " consegue se libertar do Kraken!");
        kraken.setHeroiAgarrado(false); // Reseta o estado
    }
}