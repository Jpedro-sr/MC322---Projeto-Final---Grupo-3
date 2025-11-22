package ifome.model;

/**
 * Classe abstrata que representa um usuário genérico do sistema.
 * Contém a lógica de autenticação base.
 */
public abstract class Usuario {

    protected String email;
    protected String senha;

    /**
     * Tenta autenticar o usuário.
     * @param email O email fornecido.
     * @param senha A senha fornecida.
     * @return true se a autenticação for bem-sucedida, false caso contrário.
     */
    public boolean login(String email, String senha) {
        // Lógica de autenticação
        return this.email.equals(email) && this.senha.equals(senha);
    }

    /**
     * Faz logout do usuário.
     */
    public void logout() {
        System.out.println("✅ Logout realizado.");
    }

    /**
     * Getter para email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Setter para email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter para senha
     */
    public String getSenha() {
        return this.senha;
    }

    /**
     * Setter para senha
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}