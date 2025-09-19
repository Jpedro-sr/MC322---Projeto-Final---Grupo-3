public class SereiaEncantadora extends Monstro {

    // Atributo específico da Sereia Encantadora: Canto Divino
    private int cantoDivino;

    // Construtor da classe SereiaEncantadora
    public SereiaEncantadora() {
        super("Sereia Encantadora", 75, 7, 25);
        this.cantoDivino = 10;

        this.arma = new Cutelo("Cutelo de Coral", 10, 1);

        // OPCIONAL: Mantém na lista para casos especiais
        this.listaDeArmasParaLargar.add(new Cutelo("Cutelo de Coral", 10, 1));

        // Define as ações que a Sereia pode fazer.
        this.acoes.add(new GolpeDeCaudaAcao(this)); // Ação no índice 0
        this.acoes.add(new CantoDivinoAcao(this));   // Ação no índice 1
    }

    // A IA da Sereia: uma chance de 30% de usar o Canto Divino.
    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        System.out.println("A " + this.nome + " inicia sua melodia mortal...");

        // Sorteia um valor. Se for menor que 0.3 (30% de chance), usa a habilidade especial.
        if (Math.random() < 0.3) {
            return acoes.get(1); // Retorna CantoDivinoAcao
        } else {
            return acoes.get(0); // Retorna GolpeDeCaudaAcao
        }
    }

    // Getter para o atributo único, para que a Ação possa acessá-lo.
    public int getCantoDivino() {
        return this.cantoDivino;
    }
}