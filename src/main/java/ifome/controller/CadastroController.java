package ifome.controller;

import java.io.IOException;

import ifome.model.Cliente;
import ifome.util.RepositorioRestaurantes;
import ifome.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CadastroController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoTelefone;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private PasswordField campoConfirmarSenha;

    @FXML
    private void handleCadastro(ActionEvent event) {
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String senha = campoSenha.getText();
        String confirmarSenha = campoConfirmarSenha.getText();

        // Validações básicas
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Campos Obrigatórios", "Por favor, preencha todos os campos.");
            return;
        }

        // Validar formato de email
        if (!email.contains("@") || !email.contains(".")) {
            mostrarAlerta("Email Inválido", "Por favor, digite um email válido.");
            campoEmail.requestFocus();
            return;
        }

        // Validar telefone (mínimo 10 dígitos)
        String telefoneDigitos = telefone.replaceAll("[^0-9]", "");
        if (telefoneDigitos.length() < 10) {
            mostrarAlerta("Telefone Inválido", "O telefone deve ter pelo menos 10 dígitos.");
            campoTelefone.requestFocus();
            return;
        }

        // Validar senha (removido limite mínimo)
        if (senha.isEmpty()) {
            mostrarAlerta("Senha Obrigatória", "Por favor, digite uma senha.");
            campoSenha.requestFocus();
            return;
        }

        // Confirmar senha
        if (!senha.equals(confirmarSenha)) {
            mostrarAlerta("Senhas Diferentes", "As senhas não coincidem. Por favor, tente novamente.");
            campoConfirmarSenha.clear();
            campoConfirmarSenha.requestFocus();
            return;
        }

        RepositorioRestaurantes repo = RepositorioRestaurantes.getInstance();

        // Verificar se email já existe
        if (repo.emailJaExiste(email)) {
            mostrarAlerta("Email já Cadastrado", "Este email já está em uso. Por favor, faça login ou use outro email.");
            campoEmail.requestFocus();
            return;
        }

        // Criar novo cliente
        try {
            Cliente novoCliente = new Cliente(email, senha, nome, telefone);
            repo.adicionarCliente(novoCliente);
            repo.salvarDados();

            // Fazer login automaticamente
            SessaoUsuario.getInstance().setClienteLogado(novoCliente);

            // Mostrar mensagem de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Bem-vindo ao iFome!");
            alert.setContentText("Sua conta foi criada com sucesso, " + nome + "!");
            alert.showAndWait();

            // Redirecionar para o menu do cliente
            mudarTela(event, "/ifome/MenuCliente.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu um erro ao criar sua conta. Por favor, tente novamente.");
        }
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        mudarTela(event, "/ifome/TelaLogin.fxml");
    }

    @FXML
    private void irParaLogin(MouseEvent event) throws IOException {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaLogin.fxml"));
        stage.setScene(new Scene(root, 360, 640));
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 360, 640));
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}