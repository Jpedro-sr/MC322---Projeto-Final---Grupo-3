package ifome.controller;

import ifome.util.SessaoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MenuClienteController {

    @FXML
    private Label labelNome;

    @FXML
    public void initialize() {
        String nome = SessaoUsuario.getInstance().getClienteLogado().getNome();
        labelNome.setText("Olá, " + nome + "!");
    }

    @FXML
    private void irParaRestaurantes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/ListaRestaurantes.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar lista de restaurantes.");
        }
    }

    @FXML
    private void irParaCarrinho(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaCarrinho.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir o carrinho.");
        }
    }

    @FXML
    private void irParaPedidos(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaMeusPedidos.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar pedidos.");
        }
    }

    @FXML
    private void fazerLogout(ActionEvent event) throws IOException {
        SessaoUsuario.getInstance().logout();
        
        Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaLogin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 360, 640));
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}