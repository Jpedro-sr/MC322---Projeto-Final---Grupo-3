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
     * (No seu UML, este método está como `login(email, senha, String: boolean)`, 
     * ajustei para o padrão Java)
     * @param email O email fornecido.
     * @param senha A senha fornecida.
     * @return true se a autenticação for bem-sucedida, false caso contrário.
     */
    public boolean login(String email, String senha) {
        // Lógica de autenticação
        return this.email.equals(email) && this.senha.equals(senha);
    }
}