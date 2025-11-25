package ifome.controller;

import java.io.IOException;
import java.util.List;

import ifome.model.Restaurante;
import ifome.util.RepositorioRestaurantes;
import ifome.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * ✅ CORRIGIDO: Exibe avaliações corretamente
 */
public class ListaRestaurantesController {

    @FXML
    private VBox containerRestaurantes;

    @FXML
    public void initialize() {
        carregarLista();
    }

    private void carregarLista() {
        List<Restaurante> lista = RepositorioRestaurantes.getInstance().getTodosRestaurantes();

        containerRestaurantes.getChildren().clear();

        if (lista.isEmpty()) {
            Label vazio = new Label("Nenhum restaurante encontrado.");
            containerRestaurantes.getChildren().add(vazio);
            return;
        }

        for (Restaurante r : lista) {
            VBox card = criarCardRestaurante(r);
            containerRestaurantes.getChildren().add(card);
        }
    }

    /**
     * ✅ CORRIGIDO: Mostra avaliação média corretamente
     */
    private VBox criarCardRestaurante(Restaurante r) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;");

        // Header: Nome e Status
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblNome = new Label(r.getNomeRestaurante());
        lblNome.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblNome.setStyle("-fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String status = r.estaAberto() ? "🟢 Aberto" : "🔴 Fechado";
        String corStatus = r.estaAberto() ? "#4cd137" : "#e74c3c";
        Label lblStatus = new Label(status);
        lblStatus.setStyle("-fx-text-fill: " + corStatus + "; -fx-font-size: 13px; -fx-font-weight: bold;");

        header.getChildren().addAll(lblNome, spacer, lblStatus);

        // ✅ CORRIGIDO: Calcula e exibe avaliação média
        double mediaAvaliacoes = r.calcularMediaAvaliacoes();
        int totalAvaliacoes = r.getQuantidadeAvaliacoes();
        
        String avaliacaoTexto = mediaAvaliacoes > 0 
            ? String.format("⭐ %.1f (%d avaliações)", mediaAvaliacoes, totalAvaliacoes)
            : "⭐ Sem avaliações";
        
        Label lblAvaliacao = new Label(avaliacaoTexto);
        lblAvaliacao.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        // Descrição (quantidade de produtos)
        Label lblProdutos = new Label(r.getQuantidadeProdutos() + " produtos no cardápio");
        lblProdutos.setStyle("-fx-text-fill: #999; -fx-font-size: 12px;");

        card.getChildren().addAll(header, lblAvaliacao, lblProdutos);

        // Evento de clique
        card.setOnMouseClicked(e -> abrirRestaurante(r));

        // Efeito hover
        card.setOnMouseEntered(e -> {
            if (r.estaAberto()) {
                card.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #ea1d2c; -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;");
            }
        });

        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;");
        });

        return card;
    }

    private void abrirRestaurante(Restaurante r) {
        if (!r.estaAberto()) {
            mostrarAlerta("Restaurante Fechado", "Este restaurante não está aceitando pedidos no momento.");
            return;
        }

        SessaoUsuario.getInstance().setRestauranteAtual(r);

        try {
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