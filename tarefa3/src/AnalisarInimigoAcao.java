public class AnalisarInimigoAcao implements AcaoDeCombate {
    private CorsarioSedentario usuario;

    public AnalisarInimigoAcao(CorsarioSedentario usuario) {
        this.usuario = usuario;
    }

    @Override
public void executar(Combatente usuario, Combatente alvo) {
    Personagem personagem = (Personagem) usuario;
    Heroi heroi = (Heroi) usuario;
    
    int danoMinimo = personagem.getForca() + (personagem.getArma() != null ? personagem.getArma().getDano() : 0);
    
    System.out.println("\t> " + usuario.getNome() + " analisa cuidadosamente o inimigo " + alvo.getNome() + "...");
    System.out.println("\t> Dano estimado: " + danoMinimo + " pontos.");
    
    if (Math.random() < heroi.getSorte()) {
        System.out.println("\t> (Sucesso) Você identifica pontos fracos! +2 Pontos de Estudo.");
        heroi.adicionarPontosDeEstudo(2);
    } else {
        System.out.println("\t> (Falha parcial) Análise básica concluída. +1 Ponto de Estudo.");
        heroi.adicionarPontosDeEstudo(1);
    }
    System.out.println("\t> (Pontos de Estudo acumulados: " + heroi.getPontosDeEstudo() + ")");
    }
}