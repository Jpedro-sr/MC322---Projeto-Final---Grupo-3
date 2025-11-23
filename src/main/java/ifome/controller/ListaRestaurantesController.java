package ifome.controller;

import java.io.IOException;
import java.util.List;

import ifome.model.Restaurante;
import ifome.util.RepositorioRestaurantes;
import ifome.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ListaRestaurantesController {

    @FXML
    private VBox containerRestaurantes;

    @FXML
    public void initialize() {
        carregarLista();
    }

    private void carregarLista() {
        // Pega TODOS os restaurantes do sistema
        List<Restaurante> lista = RepositorioRestaurantes.getInstance().getTodosRestaurantes();

        containerRestaurantes.getChildren().clear();

        if (lista.isEmpty()) {
            Label vazio = new Label("Nenhum restaurante encontrado.");
            containerRestaurantes.getChildren().add(vazio);
            return;
        }

        for (Restaurante r : lista) {
            Button card = criarCardRestaurante(r);
            containerRestaurantes.getChildren().add(card);
        }
    }

    private Button criarCardRestaurante(Restaurante r) {
        String status = r.estaAberto() ? "Aberto" : "Fechado";
        String corStatus = r.estaAberto() ? "#4cd137" : "#7f8fa6";
        
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(80);
        
        // Estilo CSS inline
        btn.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 15;");

        // Conteúdo do Botão
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblNome = new Label(r.getNomeRestaurante());
        lblNome.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblNome.setStyle("-fx-text-fill: #333;");
        
        Label lblDetalhes = new Label(String.format("⭐ %.1f • %s", r.calcularMediaAvaliacoes(), status));
        lblDetalhes.setStyle("-fx-text-fill: " + corStatus + "; -fx-font-size: 14px;");

        vBox.getChildren().addAll(lblNome, lblDetalhes);
        btn.setGraphic(vBox);

        btn.setOnAction(e -> abrirRestaurante(e, r));

        return btn;
    }

    private void abrirRestaurante(ActionEvent event, Restaurante r) {
        if (!r.estaAberto()) {
            mostrarAlerta("Restaurante Fechado", "Este restaurante não está aceitando pedidos no momento.");
            return;
        }

        // Salva na sessão para a próxima tela saber qual carregar
        SessaoUsuario.getInstance().setRestauranteAtual(r);

        try {
            // Carrega a tela de CARDÁPIO
            // IMPORTANTE: O FXML aqui deve ser TelaCardapio.fxml
            Stage stage = (Stage) containerRestaurantes.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaCardapio.fxml"));
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar o cardápio.");
        }
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        Stage stage = (Stage) containerRestaurantes.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ifome/MenuCliente.fxml"));
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