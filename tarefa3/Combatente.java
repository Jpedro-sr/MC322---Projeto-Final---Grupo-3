interface Combatente {
    public void getNome();
    public void estaVivo();
    public void receberDano(int dano);
    public void receberCura(int cura);
    public void escolherAcao(Combatente alvo);
}
