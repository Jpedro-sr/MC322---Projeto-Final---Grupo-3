// Esta ação permite ao CorsarioSedentario analisar o inimigo durante o combate.
// O objetivo é estimar o dano mínimo que pode ser causado e conceder pontos de estudo ao herói,
// que podem ser usados para aprimorar estratégias ou habilidades.

public class AnalisarInimigoAcao implements AcaoDeCombate {
    private CorsarioSedentario usuario;

    // Inicializa a ação com o CorsarioSedentario que irá executar a análise.
    public AnalisarInimigoAcao(CorsarioSedentario usuario) {
        this.usuario = usuario;
    }

    // Executa a análise do inimigo, mostrando o dano estimado e concedendo pontos de estudo ao herói.
    // A quantidade de pontos depende da sorte do herói.
    @Override
public void executar(Combatente usuario, Combatente alvo) {
    Personagem personagem = (Personagem) usuario;
    Heroi heroi = (Heroi) usuario;
    
    int danoMinimo = personagem.getForca() + (personagem.getArma() != null ? personagem.getArma().getDano() : 0);
    
    System.out.println("\t> " + usuario.getNome() + " analisa cuidadosamente o inimigo " + alvo.getNome() + "...");
    System.out.println("\t> Dano estimado: " + danoMinimo + " pontos.");
    
    // Se a sorte do herói for suficiente, ele identifica pontos fracos e ganha mais pontos de estudo.
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