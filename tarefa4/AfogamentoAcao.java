// Ação especial do Kraken que simula o afogamento do herói.
// Esta classe calcula o dano total causado pelo afogamento, considerando o dano base do Kraken e o dano da arma equipada.
// Após aplicar o dano, o estado do Kraken é resetado para indicar que o herói se libertou.

public class AfogamentoAcao implements AcaoDeCombate {
    private Kraken usuario;

    // Recebe o Kraken que irá executar a ação de afogamento.
    public AfogamentoAcao(Kraken usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        System.out.println("\t*** O " + usuario.getNome() + " te puxa para as profundezas da água, te afogando! ***");
        
        Kraken kraken = (Kraken) usuario;
        Personagem personagemUsuario = (Personagem) usuario;
        
        // Calcula o dano total do afogamento, somando o dano do Kraken e o dano da arma (se houver).
        int danoTotal = kraken.getDanoDoAfogamento() + 
                       (personagemUsuario.getArma() != null ? personagemUsuario.getArma().getDano() : 0);
        
        System.out.println("\t> O afogamento causa " + danoTotal + " de dano massivo em " + alvo.getNome() + "!");
        alvo.receberDano(danoTotal);
        
        // Após o ataque, o herói se liberta do Kraken.
        System.out.println("\t> Após receber o dano, o " + alvo.getNome() + " consegue se libertar do Kraken!");
        kraken.setHeroiAgarrado(false); // Reseta o estado
    }
}