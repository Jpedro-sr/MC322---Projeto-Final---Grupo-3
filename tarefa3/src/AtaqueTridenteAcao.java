public class AtaqueTridenteAcao implements AcaoDeCombate {
    private Personagem usuario;

    // Construtor genérico que aceita qualquer Personagem
    public AtaqueTridenteAcao(Personagem usuario) {
        this.usuario = usuario;
    }

    @Override
    public void executar(Combatente usuario, Combatente alvo) {
        Personagem personagem = (Personagem) usuario;
        
        System.out.println("\t> " + usuario.getNome() + " desfere um poderoso Ataque de Tridente!");
        
        int danoTotal = personagem.getForca() + (personagem.getArma() != null ? personagem.getArma().getDano() : 0);
        
        System.out.println("\t> O ataque causa " + danoTotal + " pontos de dano em " + alvo.getNome() + "!");
        alvo.receberDano(danoTotal);
        
        // Lógica específica baseada no tipo do usuário
        if (usuario instanceof CapitaoCabecudo) {
            CapitaoCabecudo capitao = (CapitaoCabecudo) usuario;
            System.out.println("\t> A violência do ataque deixa o Capitão mais enfurecido!");
            capitao.incrementarRaiva();
            System.out.println("\t> ... sua raiva aumenta! (" + capitao.getContadorDeRaiva() + "/" + capitao.getAtaquesParaEnfurecer() + ")");
        } else if (usuario instanceof HomemPeixe) {
            HomemPeixe homemPeixe = (HomemPeixe) usuario;
            System.out.println("\t> O ataque aumenta a raiva do " + usuario.getNome() + "!");
            homemPeixe.incrementarRaiva();
            System.out.println("\t> ... raiva: (" + homemPeixe.getContadorDeRaiva() + "/" + homemPeixe.getAtaquesParaEnfurecer() + ")");
        }
    }
}