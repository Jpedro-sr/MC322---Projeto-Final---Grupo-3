
public class CorsarioSedentario extends Heroi {

    // Atributo específico do Corsário Sedentário.
    private int pontosDeEstudo;

    // Construtor da classe CorsarioSedentario
    public CorsarioSedentario() {
        super("Corsário Sedentário", 120, 2, 1, 0);
        this.pontosDeEstudo = 0;
        this.sorte = 0.3;

        // No construtor, definimos as ações que ele pode fazer.
        this.acoes.add(new AnalisarInimigoAcao(this));
        this.acoes.add(new GolpeDeMestreAcao(this));
    }

    // A IA do Corsário: ele estuda até ter pontos suficientes, depois ataca.
    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        // Se já estudou o suficiente (3 ou mais pontos), ele decide usar sua habilidade final.
        if (this.pontosDeEstudo >= 3) {
            return acoes.get(1); // Retorna a ação "Golpe de Mestre"
        } else {
            // Caso contrário, ele continua preguiçosamente estudando o inimigo.
            return acoes.get(0); // Retorna a ação "Analisar Inimigo"
        }
    }

    // Métodos públicos para que as classes de Ação possam manipular o estado do Corsário.
    public int getPontosDeEstudo() {
        return this.pontosDeEstudo;
    }

    public void adicionarPontosDeEstudo(int pontos) {
        this.pontosDeEstudo += pontos;
    }

    public void zerarPontosDeEstudo() {
        this.pontosDeEstudo = 0;
    }
}