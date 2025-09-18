public class HomemPeixe extends Monstro {

    private int danoDoJatoDeAmonia;
    private int contadorDeRaiva;
    private static final int ATAQUES_PARA_ENFURECER = 3;

    public HomemPeixe() {
        super("Homem-Peixe", 50, 5, 40);
        this.danoDoJatoDeAmonia = 25;
        this.contadorDeRaiva = 0;
        
        // CORRIGIDO: Equipa a arma diretamente
        this.arma = new MosqueteEnferrujado("Mosquete Enferrujado", 14, 2);
        
        // OPCIONAL: Mantém na lista para casos especiais
        this.listaDeArmasParaLargar.add(new MosqueteEnferrujado("Mosquete Enferrujado", 14, 2));

        // Define as ações
        this.acoes.add(new AtaqueTridenteAcao(this)); 
        this.acoes.add(new JatoDeAmoniaAcao(this));   
    }

    @Override
    public AcaoDeCombate escolherAcao(Combatente alvo) {
        if (this.contadorDeRaiva >= ATAQUES_PARA_ENFURECER) {
            return acoes.get(1); // JatoDeAmoniaAcao
        } else {
            return acoes.get(0); // AtaqueTridenteAcao
        }
    }
    
    // Métodos específicos do HomemPeixe
    public int getDanoDoJatoDeAmonia() { return this.danoDoJatoDeAmonia; }
    public int getContadorDeRaiva() { return this.contadorDeRaiva; }
    public int getAtaquesParaEnfurecer() { return ATAQUES_PARA_ENFURECER; }
    public void incrementarRaiva() { this.contadorDeRaiva++; }
    public void zerarRaiva() { this.contadorDeRaiva = 0; }
}