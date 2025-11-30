package ifome.model;

//usuario
public abstract class Usuario {

    protected String email;
    protected String senha;

  
    public boolean login(String email, String senha) {
        //auentica
        return this.email.equals(email) && this.senha.equals(senha);
    }

    ///logout
    public void logout() {
        System.out.println("✅ Logout realizado.");
    }

 
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

 
    public String getSenha() {
        return this.senha;
    }

 
    public void setSenha(String senha) {
        this.senha = senha;
    }
}