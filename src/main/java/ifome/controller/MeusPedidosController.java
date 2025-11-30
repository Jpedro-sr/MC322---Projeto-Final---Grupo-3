package ifome.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import ifome.model.Avaliacao;
import ifome.model.Cliente;
import ifome.model.ItemPedido;
import ifome.model.Pedido;
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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * controller de pedidos
 * atualizacao:
 * exibe avaliações com comentários e informações completas dos produtos 
 * que tava errado antes
 */
public class MeusPedidosController {

    @FXML private VBox containerPedidos;
    private Cliente cliente;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        cliente = SessaoUsuario.getInstance().getClienteLogado();
        carregarPedidos();
    }

    private void carregarPedidos() {
        containerPedidos.getChildren().clear();

        List<Pedido> pedidos = cliente.getHistoricoPedidos();

        if (pedidos.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));
            emptyBox.setStyle("-fx-background-color: white; -fx-border-radius: 12; -fx-background-radius: 12;");

            Label lblEmpty = new Label("📦 Nenhum pedido realizado");
            lblEmpty.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #999;");

            Label lblSubEmpty = new Label("Faça seu primeiro pedido!");
            lblSubEmpty.setStyle("-fx-font-size: 14px; -fx-text-fill: #bbb;");

            emptyBox.getChildren().addAll(lblEmpty, lblSubEmpty);
            containerPedidos.getChildren().add(emptyBox);
            return;
        }

        for (Pedido pedido : pedidos) {
            VBox cardPedido = criarCardPedido(pedido);
            containerPedidos.getChildren().add(cardPedido);
        }
    }

    
    private VBox criarCardPedido(Pedido pedido) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12;"
        );

    
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblRestaurante = new Label(pedido.getRestaurante().getNomeRestaurante());
        lblRestaurante.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblRestaurante.setStyle("-fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblNumero = new Label("#" + pedido.getNumeroPedido());
        lblNumero.setStyle("-fx-text-fill: #666; -fx-font-size: 14px; -fx-font-weight: bold;");

        header.getChildren().addAll(lblRestaurante, spacer, lblNumero);

        // data e Hora
        Label lblData = new Label("📅 " + dateFormat.format(pedido.getDataHora()));
        lblData.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        // status com cor
        String corStatus = getCorStatus(pedido.getStatus());
        Label lblStatus = new Label("● " + pedido.getStatus());
        lblStatus.setStyle("-fx-text-fill: " + corStatus + "; -fx-font-size: 14px; -fx-font-weight: bold;");

        // itens do pedido com info especifica
        VBox boxItens = new VBox(5);
        boxItens.setPadding(new Insets(10, 0, 10, 0));
        boxItens.setStyle("-fx-border-width: 1 0 1 0; -fx-border-color: #f0f0f0;");
        
        Label lblItensHeader = new Label("📋 Itens do Pedido:");
        lblItensHeader.setStyle("-fx-text-fill: #666; -fx-font-size: 13px; -fx-font-weight: bold;");
        boxItens.getChildren().add(lblItensHeader);

        for (ItemPedido item : pedido.getItens()) {
            VBox itemBox = new VBox(3);
            itemBox.setPadding(new Insets(5, 0, 5, 10));
            
            // nome e quantidade
            Label lblItem = new Label(String.format("%dx %s", 
                item.getQuantidade(), 
                item.getProduto().getNome()
            ));
            lblItem.setStyle("-fx-text-fill: #333; -fx-font-size: 13px; -fx-font-weight: bold;");
            
         // mais info
            StringBuilder detalhes = new StringBuilder();
            
            if (item.getProduto() instanceof ifome.model.Bebida) {
                ifome.model.Bebida bebida = (ifome.model.Bebida) item.getProduto();
                detalhes.append("   🥤 ").append(bebida.getVolumeML()).append("ml");
            } else if (item.getProduto() instanceof ifome.model.Sobremesa) {
                ifome.model.Sobremesa sobremesa = (ifome.model.Sobremesa) item.getProduto();
                detalhes.append("   ").append(sobremesa.getIconeTemperatura())
                       .append(" ").append(sobremesa.getTemperatura());
            } else if (item.getProduto() instanceof ifome.model.Comida) {
                detalhes.append("   🍽️ Comida");
            }
            
            if (detalhes.length() > 0) {
                Label lblDetalhes = new Label(detalhes.toString());
                lblDetalhes.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
                itemBox.getChildren().add(lblDetalhes);
            }
            
            // observações
            if (!item.getObservacoes().isEmpty()) {
                Label lblObs = new Label("   💬 " + item.getObservacoes());
                lblObs.setStyle("-fx-text-fill: #999; -fx-font-size: 11px; -fx-font-style: italic;");
                lblObs.setWrapText(true);
                itemBox.getChildren().add(lblObs);
            }
            
            // preço
            Label lblPreco = new Label(String.format("   R$ %.2f", item.calcularPrecoTotal()));
            lblPreco.setStyle("-fx-text-fill: #ea1d2c; -fx-font-size: 12px; -fx-font-weight: bold;");
            
            itemBox.getChildren().addAll(lblItem, lblPreco);
            boxItens.getChildren().add(itemBox);
        }

        // valor total
        HBox footerBox = new HBox();
        footerBox.setAlignment(Pos.CENTER_LEFT);
        footerBox.setStyle("-fx-padding: 10 0 0 0; -fx-border-width: 1 0 0 0; -fx-border-color: #e0e0e0;");

        double valorExibir = pedido.getValorTotal();
        if (valorExibir == 0.0 && !pedido.getItens().isEmpty()) {
            valorExibir = pedido.calcularPrecoTotal();
        }

        Label lblTotal = new Label("Total: R$ " + String.format("%.2f", valorExibir));
        lblTotal.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblTotal.setStyle("-fx-text-fill: #ea1d2c;");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        footerBox.getChildren().add(lblTotal);

        // avaliacao com comentario
        // eu e o chico
        // corrigimos isso, tava bugado
        if (!pedido.getAvaliacoes().isEmpty()) {
            Avaliacao avaliacao = pedido.getAvaliacoes().get(0);
            
            VBox boxAvaliacao = new VBox(5);
            boxAvaliacao.setPadding(new Insets(10, 0, 0, 0));
            boxAvaliacao.setStyle("-fx-border-width: 1 0 0 0; -fx-border-color: #e0e0e0;");
            
            Label lblAvaliacaoTitulo = new Label("✅ Sua Avaliação");
            lblAvaliacaoTitulo.setStyle("-fx-text-fill: #4cd137; -fx-font-size: 13px; -fx-font-weight: bold;");
            
            String estrelas = "⭐".repeat(avaliacao.getNota());
            Label lblNota = new Label(estrelas + " " + avaliacao.getNota() + "/5");
            lblNota.setStyle("-fx-text-fill: #333; -fx-font-size: 14px;");
            
            boxAvaliacao.getChildren().addAll(lblAvaliacaoTitulo, lblNota);
            
            if (!avaliacao.getComentario().isEmpty()) {
                Label lblComentario = new Label("💬 \"" + avaliacao.getComentario() + "\"");
                lblComentario.setStyle("-fx-text-fill: #666; -fx-font-size: 13px; -fx-font-style: italic;");
                lblComentario.setWrapText(true);
                lblComentario.setMaxWidth(300);
                boxAvaliacao.getChildren().add(lblComentario);
            }
            
            card.getChildren().addAll(header, lblData, lblStatus, boxItens, footerBox, boxAvaliacao);
            
        } else if (pedido.getStatus().equals("Entregue")) {
            
            Button btnAvaliar = new Button("⭐ Avaliar");
            btnAvaliar.setStyle(
                "-fx-background-color: #ea1d2c; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15;"
            );

            btnAvaliar.setOnAction(e -> abrirDialogAvaliacao(pedido));

            footerBox.getChildren().addAll(spacer2, btnAvaliar);
            card.getChildren().addAll(header, lblData, lblStatus, boxItens, footerBox);
            
        } else {
            card.getChildren().addAll(header, lblData, lblStatus, boxItens, footerBox);
        }

        return card;
    }

    private void abrirDialogAvaliacao(Pedido pedido) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Avaliar Pedido");
        dialog.setHeaderText("Como foi sua experiência com " + pedido.getRestaurante().getNomeRestaurante() + "?");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setStyle("-fx-background-color: white;");

        Label lblNota = new Label("Nota (1-5 estrelas):");
        lblNota.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        ToggleGroup grupoNotas = new ToggleGroup();
        HBox boxEstrelas = new HBox(10);
        boxEstrelas.setAlignment(Pos.CENTER);

        for (int i = 1; i <= 5; i++) {
            RadioButton rb = new RadioButton();
            rb.setUserData(i);
            rb.setToggleGroup(grupoNotas);
            rb.setStyle("-fx-font-size: 24px;");
            
            Label lblEstrela = new Label("⭐");
            lblEstrela.setStyle("-fx-font-size: 28px;");
            
            VBox estrelaBox = new VBox(2);
            estrelaBox.setAlignment(Pos.CENTER);
            estrelaBox.getChildren().addAll(lblEstrela, rb);
            
            boxEstrelas.getChildren().add(estrelaBox);
        }

        Label lblComentario = new Label("Comentário (opcional):");
        lblComentario.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextArea txtComentario = new TextArea();
        txtComentario.setPromptText("Conte como foi sua experiência...");
        txtComentario.setPrefRowCount(4);
        txtComentario.setWrapText(true);
        txtComentario.setStyle("-fx-font-size: 13px; -fx-border-color: #dcdcdc; -fx-border-radius: 5; -fx-background-radius: 5;");

        vbox.getChildren().addAll(lblNota, boxEstrelas, lblComentario, txtComentario);
        dialog.getDialogPane().setContent(vbox);

        ButtonType btnAvaliar = new ButtonType("Enviar Avaliação", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAvaliar, btnCancelar);

        Button btnAvaliarNode = (Button) dialog.getDialogPane().lookupButton(btnAvaliar);
        btnAvaliarNode.setStyle("-fx-background-color: #ea1d2c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == btnAvaliar) {
            if (grupoNotas.getSelectedToggle() == null) {
                mostrarAlerta("Atenção", "Por favor, selecione uma nota.");
                return;
            }

            int nota = (int) grupoNotas.getSelectedToggle().getUserData();
            String comentario = txtComentario.getText().trim();

            // avaliar pedido
            cliente.avaliarPedido(pedido, nota, comentario);

            // avaliar restaurante
            pedido.getRestaurante().avaliar(nota, comentario);

            // salvar
            RepositorioRestaurantes.getInstance().salvarDados();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Avaliação Enviada");
            alert.setHeaderText("Obrigado pela sua avaliação!");
            alert.setContentText("Sua opinião é muito importante para nós. 🌟");
            alert.showAndWait();

            carregarPedidos();
        }
    }

    private String getCorStatus(String status) {
        switch (status) {
            case "Entregue":
                return "#4cd137";
            case "Cancelado":
                return "#e74c3c";
            case "Em Entrega":
                return "#3498db";
            case "Pronto":
            case "Preparando":
                return "#f39c12";
            case "Confirmado":
            case "Pendente":
                return "#95a5a6";
            default:
                return "#666";
        }
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        mudarTela(event, "/ifome/MenuCliente.fxml");
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        Node source = (Node) event.getSource();
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) source.getScene().getWindow();
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