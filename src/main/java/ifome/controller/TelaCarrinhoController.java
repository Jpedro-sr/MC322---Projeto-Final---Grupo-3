package ifome.controller;

import java.io.IOException;
import java.util.List;

import ifome.model.Carrinho;
import ifome.model.Cliente;
import ifome.model.Cupom;
import ifome.model.ItemPedido;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TelaCarrinhoController {

    @FXML private VBox containerItens;
    @FXML private Label lblRestaurante;
    @FXML private Label lblSubtotal;
    @FXML private Label lblDesconto;
    @FXML private Label lblTotal;
    @FXML private TextField txtCupom;
    @FXML private Button btnAplicarCupom;
    @FXML private Button btnFinalizarPedido;
    @FXML private Label lblCupomStatus;
    @FXML private HBox boxDesconto;

    private Cliente cliente;
    private Carrinho carrinho;

    // controller tela carrinho
    @FXML
    public void initialize() {
        cliente = SessaoUsuario.getInstance().getClienteLogado();
        carrinho = cliente.getCarrinho();

        if (carrinho.getRestaurante() != null) {
            lblRestaurante.setText("📍 " + carrinho.getRestaurante().getNomeRestaurante());
        } else {
            lblRestaurante.setText("📍 Nenhum restaurante selecionado");
        }

        carregarItens();
        atualizarValores();
        
        aplicarEfeitoHoverBotao(btnAplicarCupom, "#4cd137", "#3db829");
        aplicarEfeitoHoverBotao(btnFinalizarPedido, "#4cd137", "#3db829");
    }

    private void carregarItens() {
        containerItens.getChildren().clear();

        List<ItemPedido> itens = carrinho.getItens();

        if (itens.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));
            emptyBox.setStyle("-fx-background-color: white; -fx-border-radius: 12; -fx-background-radius: 12;");

            Label lblEmpty = new Label("🛒 Seu carrinho está vazio");
            lblEmpty.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #999;");

            Label lblSubEmpty = new Label("Adicione itens do cardápio");
            lblSubEmpty.setStyle("-fx-font-size: 14px; -fx-text-fill: #bbb;");

            emptyBox.getChildren().addAll(lblEmpty, lblSubEmpty);
            containerItens.getChildren().add(emptyBox);
            
            btnFinalizarPedido.setDisable(true);
            return;
        }

        btnFinalizarPedido.setDisable(false);

        for (ItemPedido item : itens) {
            VBox cardItem = criarCardItem(item);
            containerItens.getChildren().add(cardItem);
        }
    }

    private VBox criarCardItem(ItemPedido item) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12;"
        );

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblNome = new Label(item.getProduto().getNome());
        lblNome.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblNome.setStyle("-fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnRemover = new Button("✕");
        btnRemover.setStyle(
            "-fx-background-color: #ffe0e0; " +
            "-fx-text-fill: #e74c3c; " +
            "-fx-cursor: hand; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 5 10;"
        );
        
        aplicarEfeitoHoverBotao(btnRemover, "#ffe0e0", "#ffcccc");

        btnRemover.setOnAction(e -> {
            carrinho.removerItem(item);
            carregarItens();
            atualizarValores();
            mostrarAlerta("Removido", item.getProduto().getNome() + " removido do carrinho.");
        });

        header.getChildren().addAll(lblNome, spacer, btnRemover);

        HBox detalhes = new HBox();
        detalhes.setAlignment(Pos.CENTER_LEFT);
        detalhes.setSpacing(10);

        Label lblQtd = new Label("Qtd: " + item.getQuantidade());
        lblQtd.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Label lblPreco = new Label(String.format("R$ %.2f", item.calcularPrecoTotal()));
        lblPreco.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblPreco.setStyle("-fx-text-fill: #ea1d2c;");

        detalhes.getChildren().addAll(lblQtd, spacer2, lblPreco);

        if (!item.getObservacoes().isEmpty()) {
            Label lblObs = new Label("📝 " + item.getObservacoes());
            lblObs.setStyle("-fx-text-fill: #999; -fx-font-size: 12px; -fx-font-style: italic;");
            lblObs.setWrapText(true);
            card.getChildren().addAll(header, detalhes, lblObs);
        } else {
            card.getChildren().addAll(header, detalhes);
        }

        return card;
    }

    private void atualizarValores() {
        double subtotal = carrinho.calcularPrecoTotal();
        double total = carrinho.calcularTotalComDesconto();
        double desconto = subtotal - total;

        lblSubtotal.setText(String.format("R$ %.2f", subtotal));
        lblTotal.setText(String.format("R$ %.2f", total));

        if (desconto > 0) {
            lblDesconto.setText(String.format("- R$ %.2f", desconto));
            boxDesconto.setVisible(true);
            boxDesconto.setManaged(true);
        } else {
            boxDesconto.setVisible(false);
            boxDesconto.setManaged(false);
        }
    }

    @FXML
    private void aplicarCupom(ActionEvent event) {
        String codigoCupom = txtCupom.getText().trim();

        if (codigoCupom.isEmpty()) {
            mostrarAlerta("Atenção", "Digite um código de cupom válido.");
            return;
        }
        
        // verifica o uso
        if (cliente.jaUsouCupom(codigoCupom)) {
            mostrarAlerta("Cupom já utilizado", "Você já usou este cupom anteriormente.");
            lblCupomStatus.setText("❌ Cupom já utilizado");
            lblCupomStatus.setStyle("-fx-text-fill: #e74c3c;");
            lblCupomStatus.setVisible(true);
            return;
        }

        Cupom cupom = RepositorioRestaurantes.getInstance().buscarCupom(codigoCupom);

        if (cupom == null || !cupom.estaValido()) {
            mostrarAlerta("Cupom Inválido", "O cupom digitado não existe ou está expirado.");
            lblCupomStatus.setText("❌ Cupom inválido");
            lblCupomStatus.setStyle("-fx-text-fill: #e74c3c;");
            lblCupomStatus.setVisible(true);
            return;
        }

        carrinho.aplicarCupom(cupom);
        atualizarValores();

        lblCupomStatus.setText("✅ Cupom aplicado: " + cupom.getDescricaoDesconto());
        lblCupomStatus.setStyle("-fx-text-fill: #4cd137;");
        lblCupomStatus.setVisible(true);

        mostrarAlerta("Cupom Aplicado", "Desconto de " + cupom.getDescricaoDesconto() + " aplicado!");
    }

    @FXML
    private void finalizarPedido(ActionEvent event) {
        if (carrinho.getItens().isEmpty()) {
            mostrarAlerta("Carrinho Vazio", "Adicione itens antes de finalizar o pedido.");
            return;
        }

        if (carrinho.getRestaurante() == null) {
            mostrarAlerta("Erro", "Nenhum restaurante selecionado.");
            return;
        }

        double valorTotal = carrinho.calcularTotalComDesconto();
        double valorMinimo = 15.0;

        if (valorTotal < valorMinimo) {
            mostrarAlerta("Valor Mínimo", 
                String.format("O valor mínimo do pedido é R$ %.2f.\nValor atual: R$ %.2f", 
                valorMinimo, valorTotal));
            return;
        }

        // ir para tela de pagamento
        try {
            mudarTela(event, "/ifome/TelaPagamento.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar tela de pagamento.");
        }
    }

    @FXML
    private void voltar(ActionEvent event) {
        try {
            if (carrinho.getRestaurante() != null) {
                mudarTela(event, "/ifome/TelaCardapio.fxml");
            } else {
                mudarTela(event, "/ifome/MenuCliente.fxml");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar.");
        }
    }

    private void aplicarEfeitoHoverBotao(Button btn, String corNormal, String corHover) {
        String estiloOriginal = btn.getStyle();
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle(estiloOriginal.replace(corNormal, corHover) + " -fx-scale-x: 1.02; -fx-scale-y: 1.02;");
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(estiloOriginal);
        });
        
        btn.setOnMousePressed(e -> {
            btn.setStyle(estiloOriginal.replace(corNormal, corHover) + " -fx-translate-y: 2;");
        });
        
        btn.setOnMouseReleased(e -> {
            btn.setStyle(estiloOriginal.replace(corNormal, corHover));
        });
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