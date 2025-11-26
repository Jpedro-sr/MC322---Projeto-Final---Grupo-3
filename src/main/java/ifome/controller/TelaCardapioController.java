package ifome.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ifome.model.Cliente;
import ifome.model.Produto;
import ifome.model.Restaurante;
import ifome.model.Carrinho;
import ifome.model.Bebida;
import ifome.model.Comida;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * ✅ CORRIGIDO: Exibe informações específicas dos produtos (volume, etc.)
 */
public class TelaCardapioController {

    @FXML
    private VBox containerProdutos;

    @FXML
    private Label lblNomeRestaurante;

    @FXML
    private Label lblAvaliacaoRestaurante;

    @FXML
    private Label lblStatusRestaurante;

    @FXML
    private Button btnVerCarrinho;

    private Restaurante restaurante;
    private Cliente cliente;

    @FXML
    public void initialize() {
        restaurante = SessaoUsuario.getInstance().getRestauranteAtual();
        cliente = SessaoUsuario.getInstance().getClienteLogado();

        if (restaurante == null) {
            mostrarAlerta("Erro", "Nenhum restaurante selecionado!");
            return;
        }

        lblNomeRestaurante.setText(restaurante.getNomeRestaurante());
        lblAvaliacaoRestaurante.setText(String.format("⭐ %.1f", restaurante.calcularMediaAvaliacoes()));
        lblStatusRestaurante.setText(restaurante.estaAberto() ? "• Aberto" : "• Fechado");
        lblStatusRestaurante.setStyle("-fx-text-fill: " + (restaurante.estaAberto() ? "#4cd137" : "#e74c3c"));

        carregarProdutos();
        atualizarContadorCarrinho();
    }

    private void carregarProdutos() {
        List<Produto> produtos = restaurante.getCardapio();

        containerProdutos.getChildren().clear();

        if (produtos.isEmpty()) {
            Label vazio = new Label("Nenhum produto disponível.");
            vazio.setStyle("-fx-text-fill: #999; -fx-font-size: 14px;");
            containerProdutos.getChildren().add(vazio);
            return;
        }

        for (Produto produto : produtos) {
            Button cardProduto = criarCardProduto(produto);
            containerProdutos.getChildren().add(cardProduto);
        }
    }

    /**
     * ✅ CORRIGIDO: Exibe informações específicas (volume para bebidas, etc.)
     */
    private Button criarCardProduto(Produto produto) {
        Button card = new Button();
        card.setMaxWidth(Double.MAX_VALUE);
        card.setPrefHeight(100);
        card.setAlignment(Pos.CENTER_LEFT);
        
        String corDisponibilidade = produto.isDisponivel() ? "white" : "#f9f9f9";
        String opacidade = produto.isDisponivel() ? "1.0" : "0.6";
        
        card.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand; " +
            "-fx-opacity: %s; " +
            "-fx-padding: 15;",
            corDisponibilidade, opacidade
        ));

        card.setOnMouseEntered(e -> {
            if (produto.isDisponivel()) {
                card.setStyle(
                    "-fx-background-color: #f8f8f8; " +
                    "-fx-border-color: #ea1d2c; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-cursor: hand; " +
                    "-fx-padding: 15; " +
                    "-fx-scale-x: 0.98; " +
                    "-fx-scale-y: 0.98;"
                );
            }
        });

        card.setOnMouseExited(e -> {
            card.setStyle(String.format(
                "-fx-background-color: %s; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 12; " +
                "-fx-background-radius: 12; " +
                "-fx-cursor: hand; " +
                "-fx-opacity: %s; " +
                "-fx-padding: 15;",
                corDisponibilidade, opacidade
            ));
        });

        card.setOnMousePressed(e -> {
            if (produto.isDisponivel()) {
                card.setStyle(
                    "-fx-background-color: #f0f0f0; " +
                    "-fx-border-color: #ea1d2c; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-cursor: hand; " +
                    "-fx-padding: 15; " +
                    "-fx-translate-y: 2;"
                );
            }
        });

        card.setOnMouseReleased(e -> {
            if (produto.isDisponivel()) {
                card.setStyle(
                    "-fx-background-color: #f8f8f8; " +
                    "-fx-border-color: #ea1d2c; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-cursor: hand; " +
                    "-fx-padding: 15;"
                );
            }
        });

        // Layout interno
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER_LEFT);

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // ✅ Símbolo baseado no tipo
        String simbolo = getSimbolo(produto);
        Label lblSimbolo = new Label(simbolo);
        lblSimbolo.setStyle("-fx-font-size: 20px;");

        Label lblNome = new Label(produto.getNome());
        lblNome.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblNome.setStyle("-fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblPreco = new Label(String.format("R$ %.2f", produto.getPreco()));
        lblPreco.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblPreco.setStyle("-fx-text-fill: #ea1d2c;");

        headerBox.getChildren().addAll(lblSimbolo, lblNome, spacer, lblPreco);

        // ✅ CORRIGIDO: Exibe descrição + informações específicas
        StringBuilder descricaoCompleta = new StringBuilder();

        if (!produto.getDescricao().isEmpty()) {
            descricaoCompleta.append(produto.getDescricao());
        }

        // Adiciona volume para bebidas
        if (produto instanceof Bebida) {
            Bebida bebida = (Bebida) produto;
            if (descricaoCompleta.length() > 0) {
                descricaoCompleta.append(" • ");
            }
            descricaoCompleta.append(bebida.getVolumeML()).append("ml");
        }

        // Adiciona tags de vegetariano/vegano para comidas
        if (produto instanceof Comida) {
            Comida comida = (Comida) produto;
            if (comida.ehVegano()) {
                if (descricaoCompleta.length() > 0) {
                    descricaoCompleta.append(" • ");
                }
                descricaoCompleta.append("Vegano");
            } else if (comida.ehVegetariano()) {
                if (descricaoCompleta.length() > 0) {
                    descricaoCompleta.append(" • ");
                }
                descricaoCompleta.append("Vegetariano");
            }
        }

        Label lblDescricao = new Label(descricaoCompleta.toString());
        lblDescricao.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
        lblDescricao.setWrapText(true);

        Label lblDisponibilidade = new Label(produto.isDisponivel() ? "" : "❌ Indisponível");
        lblDisponibilidade.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px; -fx-font-weight: bold;");

        vBox.getChildren().addAll(headerBox, lblDescricao, lblDisponibilidade);
        card.setGraphic(vBox);

        card.setOnAction(e -> {
            if (produto.isDisponivel()) {
                abrirDialogAdicionarProduto(produto);
            } else {
                mostrarAlerta("Produto Indisponível", "Este produto não está disponível no momento.");
            }
        });

        return card;
    }

    /**
     * ✅ Retorna símbolo correto para cada tipo
     */
    private String getSimbolo(Produto p) {
        String categoria = p.getCategoria();
        
        if (categoria == null || categoria.isEmpty()) {
            return "🍔";
        }
        
        switch (categoria) {
            case "Comida":
                return "🍔";
            case "Bebida":
                return "🥛";
            case "Sobremesa":
                return "🍰";
            case "Adicional":
                return "➕";
            default:
                return "🍔";
        }
    }

    private void abrirDialogAdicionarProduto(Produto produto) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Adicionar ao Carrinho");
        dialog.setHeaderText(produto.getNome() + " - R$ " + String.format("%.2f", produto.getPreco()));

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER_LEFT);

        Label lblQuantidade = new Label("Quantidade:");
        lblQuantidade.setStyle("-fx-font-weight: bold;");
        
        Spinner<Integer> spinnerQtd = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1);
        spinnerQtd.setValueFactory(valueFactory);
        spinnerQtd.setEditable(true);
        spinnerQtd.setPrefWidth(100);

        Label lblObs = new Label("Observações (opcional):");
        lblObs.setStyle("-fx-font-weight: bold;");
        
        TextArea txtObs = new TextArea();
        txtObs.setPromptText("Ex: Sem cebola, bem passado...");
        txtObs.setPrefRowCount(3);
        txtObs.setWrapText(true);

        vbox.getChildren().addAll(lblQuantidade, spinnerQtd, lblObs, txtObs);
        dialog.getDialogPane().setContent(vbox);

        ButtonType btnAdicionar = new ButtonType("Adicionar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAdicionar, btnCancelar);

        Button btnAdd = (Button) dialog.getDialogPane().lookupButton(btnAdicionar);
        btnAdd.setStyle("-fx-background-color: #ea1d2c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == btnAdicionar) {
            int quantidade = spinnerQtd.getValue();
            String observacoes = txtObs.getText().trim();

            Carrinho carrinho = cliente.getCarrinho();
            carrinho.setRestaurante(restaurante);
            carrinho.setCliente(cliente);
            carrinho.adicionarItem(produto, quantidade, observacoes);

            mostrarAlerta("Sucesso", produto.getNome() + " adicionado ao carrinho!");
            atualizarContadorCarrinho();
        }
    }

    private void atualizarContadorCarrinho() {
        Carrinho carrinho = cliente.getCarrinho();
        int qtdItens = carrinho.getItens().size();
        btnVerCarrinho.setText(String.format("🛒 Ver Carrinho (%d)", qtdItens));
    }

    @FXML
    private void irParaCarrinho(ActionEvent event) {
        try {
            mudarTela(event, "/ifome/TelaCarrinho.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar tela de carrinho.");
        }
    }

    @FXML
    private void voltar(ActionEvent event) {
        try {
            mudarTela(event, "/ifome/ListaRestaurantes.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar.");
        }
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
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