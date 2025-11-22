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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        List<Restaurante> lista = RepositorioRestaurantes.getInstance().getTodosRestaurantes();

        containerRestaurantes.getChildren().clear();

        if (lista.isEmpty()) {
            Label vazio = new Label("Nenhum restaurante encontrado.");
            containerRestaurantes.getChildren().add(vazio);
            return;
        }

        for (Restaurante r : lista) {
            // Cria o cartão visual do restaurante
            Button card = criarCardRestaurante(r);
            containerRestaurantes.getChildren().add(card);
        }
    }

    private Button criarCardRestaurante(Restaurante r) {
        // Layout do botão: Nome na esquerda, Nota na direita
        // Usamos um botão gigante contendo texto e estilo
        
        String status = r.estaAberto() ? "Aberto" : "Fechado";
        String corStatus = r.estaAberto() ? "#4cd137" : "#7f8fa6"; // Verde ou Cinza
        
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE); // Ocupa toda largura
        btn.setPrefHeight(80);
        
        // Estilo CSS inline para o botão parecer um "Card"
        btn.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");

        // Conteúdo do Botão (VBox com nome e detalhes)
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER_LEFT);
        
        Label lblNome = new Label(r.getNomeRestaurante());
        lblNome.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        Label lblDetalhes = new Label("⭐ " + String.format("%.1f", r.calcularMediaAvaliacoes()) + " • " + status);
        lblDetalhes.setTextFill(javafx.scene.paint.Color.web(corStatus));

        vBox.getChildren().addAll(lblNome, lblDetalhes);
        btn.setGraphic(vBox); // Coloca o VBox dentro do botão

        // Ação ao clicar
        btn.setOnAction(e -> abrirRestaurante(e, r));

        return btn;
    }

    private void abrirRestaurante(ActionEvent event, Restaurante r) {
        if (!r.estaAberto()) {
            mostrarAlerta("Restaurante Fechado", "Este restaurante não está aceitando pedidos no momento.");
            return;
        }

        // Define na sessão qual restaurante foi escolhido
        SessaoUsuario.getInstance().setRestauranteAtual(r);

        // TODO: Redirecionar para a tela de Cardápio (Próximo passo)
        mostrarAlerta("Sucesso", "Você selecionou: " + r.getNomeRestaurante() + "\n(Tela de Cardápio será a próxima)");
        
        // try {
        //    mudarTela(event, "/ifome/TelaCardapio.fxml");
        // } catch (IOException e) { ... }
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        mudarTela(event, "/ifome/MenuCliente.fxml");
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        // O event.getSource() pode vir de um Button dentro de um container dinâmico,
        // então pegamos o Scene do Node genérico
        Node source = (Node) event.getSource();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) source.getScene().getWindow();
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