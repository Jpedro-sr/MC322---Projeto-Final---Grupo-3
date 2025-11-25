package ifome.controller;

import java.io.IOException;
import ifome.model.Restaurante;
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

public class CadastroRestauranteController {

    @FXML private TextField campoNome;
    @FXML private TextField campoCNPJ;
    @FXML private TextField campoEmail;
    @FXML private PasswordField campoSenha;
    @FXML private PasswordField campoConfirmarSenha;

    @FXML
    private void handleCadastro(ActionEvent event) {
        String nome = campoNome.getText().trim();
        String cnpj = campoCNPJ.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText();
        String confirmarSenha = campoConfirmarSenha.getText();

        // Validações básicas
        if (nome.isEmpty() || cnpj.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Campos Obrigatórios", "Por favor, preencha todos os campos.");
            return;
        }

        // Validar formato de email
        if (!email.contains("@") || !email.contains(".")) {
            mostrarAlerta("Email Inválido", "Por favor, digite um email válido.");
            campoEmail.requestFocus();
            return;
        }

        // Validar CNPJ (mínimo 14 dígitos)
        String cnpjDigitos = cnpj.replaceAll("[^0-9]", "");
        if (cnpjDigitos.length() != 14) {
            mostrarAlerta("CNPJ Inválido", "O CNPJ deve ter 14 dígitos.");
            campoCNPJ.requestFocus();
            return;
        }

        // Validar senha não vazia
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

        // Criar novo restaurante
        try {
            Restaurante novoRestaurante = new Restaurante(email, senha, nome, cnpj);
            novoRestaurante.abrirRestaurante(); // Abre automaticamente
            repo.adicionarRestaurante(novoRestaurante);
            repo.salvarDados();

            // Fazer login automaticamente
            SessaoUsuario.getInstance().setRestauranteLogado(novoRestaurante);

            // Mostrar mensagem de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cadastro Realizado");
            alert.setHeaderText("Bem-vindo ao iFome!");
            alert.setContentText("Seu restaurante foi cadastrado com sucesso!\n\nAgora você pode adicionar produtos ao seu cardápio.");
            alert.showAndWait();

            // Redirecionar para o menu do restaurante
            mudarTela(event, "/ifome/MenuRestaurante.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu um erro ao criar sua conta. Por favor, tente novamente.");
        }
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        mudarTela(event, "/ifome/TelaEscolhaTipoConta.fxml");
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