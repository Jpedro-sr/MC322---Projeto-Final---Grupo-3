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

    // Adicionado @FXML para que a tela consiga chamar este método privado
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = campoEmail.getText();
        String senha = campoSenha.getText();
        
        RepositorioRestaurantes repo = RepositorioRestaurantes.getInstance();
        
        // Tenta logar como Cliente
        Cliente cliente = repo.buscarClientePorLogin(email, senha);
        if (cliente != null) {
            SessaoUsuario.getInstance().setClienteLogado(cliente);
            try {
                // Redireciona para o Menu do Cliente
                mudarTela(event, "/ifome/MenuCliente.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Não foi possível carregar o menu.");
            }
            return;
        }

        // Tenta logar como Restaurante
        Restaurante restaurante = repo.buscarRestaurantePorLogin(email, senha);
        if (restaurante != null) {
            SessaoUsuario.getInstance().setRestauranteLogado(restaurante);
            mostrarAlerta("Sucesso", "Bem-vindo, " + restaurante.getNomeRestaurante() + "!");
            // Futuro: mudarTela(event, "/ifome/MenuRestaurante.fxml");
            return;
        }

        mostrarAlerta("Erro", "Email ou senha inválidos!");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    // Método auxiliar para trocar de tela
    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        // Carrega o arquivo FXML da nova tela
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        
        // Pega a janela (Stage) atual através do botão que foi clicado (event.getSource)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Define a nova cena na janela existente
        stage.setScene(new Scene(root, 360, 640));
    }
}