interface Combatente {
    public String getNome();
    public boolean estaVivo();
    public void receberDano(int dano);
    public void receberCura(int cura);
    public void escolherAcao(Personagem alvo); //Como combatente é uma interface e não 
                                               //uma classe, utilizamos Personagem aqui
}
