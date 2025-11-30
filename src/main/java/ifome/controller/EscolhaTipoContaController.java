package ifome.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * controller da tela de escolha cliente e restaurante
 */
public class EscolhaTipoContaController {

    @FXML
    private void irParaCadastroCliente(ActionEvent event) {
        try {
            mudarTela(event, "/ifome/TelaCadastro.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaCadastroRestaurante(ActionEvent event) {
        try {
            mudarTela(event, "/ifome/TelaCadastroRestaurante.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltar(ActionEvent event) {
        try {
            mudarTela(event, "/ifome/TelaLogin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaLogin(MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaLogin.fxml"));
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 360, 640));
    }
}