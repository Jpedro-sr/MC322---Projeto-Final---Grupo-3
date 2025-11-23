package ifome.controller;

import java.io.IOException;

import ifome.model.Cliente;
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
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

   @FXML
    private void handleLogin(ActionEvent event) {
        String email = campoEmail.getText();
        String senha = campoSenha.getText();
        
        RepositorioRestaurantes repo = RepositorioRestaurantes.getInstance();
        
        // 1. Tenta logar como Cliente
        Cliente cliente = repo.buscarClientePorLogin(email, senha);
        if (cliente != null) {
            SessaoUsuario.getInstance().setClienteLogado(cliente);
            try {
                mudarTela(event, "/ifome/MenuCliente.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Não foi possível carregar o menu do cliente.");
            }
            return;
        }

        // 2. Tenta logar como Restaurante
        Restaurante restaurante = repo.buscarRestaurantePorLogin(email, senha);
        if (restaurante != null) {
            SessaoUsuario.getInstance().setRestauranteLogado(restaurante);
            try {
                mudarTela(event, "/ifome/MenuRestaurante.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Erro ao carregar menu do restaurante.\nVerifique se MenuRestaurante.fxml existe.");
            }
            return;
        }

        mostrarAlerta("Erro", "Email ou senha inválidos!");
    }

    @FXML
    private void irParaCadastro(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaCadastro.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar a tela de cadastro.");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 360, 640));
    }
}