package ifome.controller;

import ifome.util.SessaoUsuario;
import ifome.model.Restaurante;
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

public class MenuRestauranteController {

    @FXML
    private Label labelNome;
    
    @FXML
    private Label labelStatus;

    @FXML
    public void initialize() {
        Restaurante restaurante = SessaoUsuario.getInstance().getRestauranteLogado();
        labelNome.setText(restaurante.getNomeRestaurante());
        
        String status = restaurante.estaAberto() ? "🟢 ABERTO" : "🔴 FECHADO";
        labelStatus.setText(status);
        labelStatus.setStyle(
            restaurante.estaAberto() 
                ? "-fx-text-fill: #4cd137;" 
                : "-fx-text-fill: #e74c3c;"
        );
    }

    @FXML
    private void irParaPedidos(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/GerenciarPedidos.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar pedidos.");
        }
    }

    @FXML
    private void irParaCardapio(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaGerenciarCardapio.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar cardápio.");
        }
    }

    @FXML
    private void alternarStatus(ActionEvent event) {
        Restaurante restaurante = SessaoUsuario.getInstance().getRestauranteLogado();
        
        if (restaurante.estaAberto()) {
            restaurante.fecharRestaurante();
            mostrarAlerta("Status Atualizado", "Restaurante FECHADO. Não receberá novos pedidos.");
        } else {
            restaurante.abrirRestaurante();
            mostrarAlerta("Status Atualizado", "Restaurante ABERTO. Pronto para receber pedidos!");
        }
        
        // Atualizar label
        initialize();
    }

    @FXML
    private void irParaEstatisticas(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/Estatisticas.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar estatísticas.");
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