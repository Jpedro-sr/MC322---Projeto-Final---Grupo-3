package ifome.controller;

import java.io.IOException;
import java.util.Optional;

import ifome.model.*;
import ifome.util.RepositorioRestaurantes;
import ifome.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PagamentoController {

    @FXML private Label lblRestaurante;
    @FXML private Label lblItens;
    @FXML private Label lblTotal;
    @FXML private Button btnPix;
    @FXML private Button btnCartao;
    @FXML private Button btnDinheiro;
    @FXML private Label iconPix;
    @FXML private Label iconCartao;
    @FXML private Label iconDinheiro;
    @FXML private Button btnConfirmar;
    @FXML private RadioButton rbPix;
    @FXML private RadioButton rbCartao;
    @FXML private RadioButton rbDinheiro;
    @FXML private ToggleGroup grupoPagamento; // Agrupa os botões
    

    private Cliente cliente;
    private Carrinho carrinho;
    private String formaPagamentoSelecionada = null;
    private FormaPagamento pagamento = null;

    @FXML
    public void initialize() {
        cliente = SessaoUsuario.getInstance().getClienteLogado();
        carrinho = cliente.getCarrinho();

        if (carrinho.getRestaurante() != null) {
            lblRestaurante.setText("📍 " + carrinho.getRestaurante().getNomeRestaurante());
        }

        lblItens.setText(carrinho.getItens().size() + " itens");
        lblTotal.setText(String.format("R$ %.2f", carrinho.calcularTotalComDesconto()));
    }

    @FXML
    private void selecionarPix(ActionEvent event) {
        resetarSelecao();
        formaPagamentoSelecionada = "PIX";
        iconPix.setText("🔵");
        btnPix.setStyle("-fx-background-color: white; -fx-border-color: #4cd137; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 15;");
        
        btnConfirmar.setDisable(false);
        btnConfirmar.setStyle("-fx-background-color: #4cd137; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-size: 16px;");
        
        // PIX não precisa de dados adicionais
        pagamento = new PIX();
    }

    @FXML
    private void selecionarCartao(ActionEvent event) {
        resetarSelecao();
        formaPagamentoSelecionada = "CARTAO";
        iconCartao.setText("🔵");
        btnCartao.setStyle("-fx-background-color: white; -fx-border-color: #4cd137; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 15;");
        
        // Abrir dialog para dados do cartão
        mostrarDialogCartao();
    }

    @FXML
    private void selecionarDinheiro(ActionEvent event) {
        resetarSelecao();
        formaPagamentoSelecionada = "DINHEIRO";
        iconDinheiro.setText("🔵");
        btnDinheiro.setStyle("-fx-background-color: white; -fx-border-color: #4cd137; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 15;");
        
        // Abrir dialog para valor do troco
        mostrarDialogDinheiro();
    }

    private void resetarSelecao() {
        iconPix.setText("⚪");
        iconCartao.setText("⚪");
        iconDinheiro.setText("⚪");
        
        btnPix.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 15;");
        btnCartao.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 15;");
        btnDinheiro.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 15;");
        
        btnConfirmar.setDisable(true);
        btnConfirmar.setStyle("-fx-background-color: #cccccc; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-size: 16px;");
        
        pagamento = null;
    }

    private void mostrarDialogCartao() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Dados do Cartão");
        dialog.setHeaderText("Preencha os dados do seu cartão");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        TextField txtNumero = new TextField();
        txtNumero.setPromptText("Número do Cartão (16 dígitos)");
        txtNumero.setStyle("-fx-pref-height: 40; -fx-font-size: 14px;");

        TextField txtTitular = new TextField();
        txtTitular.setPromptText("Nome do Titular");
        txtTitular.setStyle("-fx-pref-height: 40; -fx-font-size: 14px;");

        TextField txtValidade = new TextField();
        txtValidade.setPromptText("Validade (MM/AA)");
        txtValidade.setStyle("-fx-pref-height: 40; -fx-font-size: 14px;");

        TextField txtCVV = new TextField();
        txtCVV.setPromptText("CVV (3 dígitos)");
        txtCVV.setStyle("-fx-pref-height: 40; -fx-font-size: 14px;");

        vbox.getChildren().addAll(
            new Label("Número do Cartão:"), txtNumero,
            new Label("Nome do Titular:"), txtTitular,
            new Label("Validade:"), txtValidade,
            new Label("CVV:"), txtCVV
        );

        dialog.getDialogPane().setContent(vbox);

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == btnConfirmar) {
            String numero = txtNumero.getText().trim();
            String titular = txtTitular.getText().trim();
            String validade = txtValidade.getText().trim();
            String cvv = txtCVV.getText().trim();

            // Validações básicas
            String numeroLimpo = numero.replaceAll("[^0-9]", "");
            if (numeroLimpo.length() != 16) {
                mostrarAlerta("Erro", "O número do cartão deve ter 16 dígitos.");
                resetarSelecao();
                return;
            }

            if (titular.isEmpty()) {
                mostrarAlerta("Erro", "Digite o nome do titular.");
                resetarSelecao();
                return;
            }

            if (!cvv.matches("\\d{3,4}")) {
                mostrarAlerta("Erro", "CVV inválido. Digite 3 ou 4 dígitos.");
                resetarSelecao();
                return;
            }

            // Criar pagamento
            pagamento = new CartaoCredito(numeroLimpo, titular, cvv, validade);
            
            this.btnConfirmar.setDisable(false);
            this.btnConfirmar.setStyle("-fx-background-color: #4cd137; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-size: 16px;");
        } else {
            resetarSelecao();
        }
    }

    private void mostrarDialogDinheiro() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Pagamento em Dinheiro");
        dialog.setHeaderText("Troco para quanto?");
        dialog.setContentText("Valor (R$):");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                String valorStr = result.get().replace(",", ".");
                double valor = Double.parseDouble(valorStr);

                double totalPedido = carrinho.calcularTotalComDesconto();

                if (valor < totalPedido) {
                    mostrarAlerta("Valor Insuficiente", 
                        String.format("O valor deve ser no mínimo R$ %.2f", totalPedido));
                    resetarSelecao();
                    return;
                }

                pagamento = new Dinheiro(valor);
                
                btnConfirmar.setDisable(false);
                btnConfirmar.setStyle("-fx-background-color: #4cd137; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-size: 16px;");

            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Digite um valor válido.");
                resetarSelecao();
            }
        } else {
            resetarSelecao();
        }
    }

    @FXML
    private void confirmarPagamento(ActionEvent event) {
        if (grupoPagamento.getSelectedToggle() == null) {
            mostrarAlerta("Atenção", "Selecione uma forma de pagamento!");
            return;
        }

        try {
            // Gera o pedido a partir do carrinho
            Pedido pedido = carrinho.gerarPedido();

            // Define a forma de pagamento com base na seleção
            if (rbPix.isSelected()) {
                pedido.setFormaPagamento(new PIX());
            } else if (rbCartao.isSelected()) {
                // Em um app real, abriria uma tela para digitar os dados do cartão
                pedido.setFormaPagamento(new CartaoCredito("1234", "CLIENTE", "123")); 
            } else if (rbDinheiro.isSelected()) {
                // Assume valor exato para simplificar
                pedido.setFormaPagamento(new Dinheiro(pedido.getValorTotal()));
            }

            // Processa e finaliza
            pedido.processarPagamento();
            carrinho.getRestaurante().aceitarPedido(pedido);
            carrinho.limparCarrinho();

            mostrarAlerta("Sucesso", "Pagamento confirmado! Seu pedido foi enviado.");
            
            // Volta para o Menu Principal
            voltarParaMenu(event);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Falha ao processar pagamento: " + e.getMessage());
        }
    }

    private void voltarParaMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ifome/MenuCliente.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 360, 640));
    }



    private void mostrarCodigoPix(Pedido pedido, ActionEvent event) {
        // Gerar código PIX simulado
        String codigoPix = gerarCodigoPix();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Pagamento PIX");
        dialog.setHeaderText("Escaneie o QR Code ou copie o código");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-alignment: center;");

        // QR Code simulado (emoji)
        Label lblQR = new Label("⬛⬜⬛⬜⬛\n⬜⬛⬜⬛⬜\n⬛⬜⬛⬜⬛\n⬜⬛⬜⬛⬜\n⬛⬜⬛⬜⬛");
        lblQR.setStyle("-fx-font-size: 40px; -fx-font-family: monospace;");

        Label lblCodigo = new Label(codigoPix);
        lblCodigo.setStyle("-fx-font-size: 10px; -fx-text-fill: #666; -fx-font-family: monospace;");
        lblCodigo.setWrapText(true);

        Button btnCopiar = new Button("📋 Copiar Código");
        btnCopiar.setStyle("-fx-background-color: #4cd137; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 20;");
        btnCopiar.setOnAction(e -> {
            // Simular cópia (em produção, usar Clipboard)
            mostrarAlerta("Código Copiado", "Código PIX copiado para a área de transferência!");
        });

        Label lblInstrucao = new Label("Após realizar o pagamento, clique em 'Confirmar'");
        lblInstrucao.setStyle("-fx-font-size: 12px; -fx-text-fill: #666; -fx-text-alignment: center;");
        lblInstrucao.setWrapText(true);

        vbox.getChildren().addAll(lblQR, lblCodigo, btnCopiar, lblInstrucao);
        dialog.getDialogPane().setContent(vbox);

        ButtonType btnConfirmarPix = new ButtonType("Já Paguei", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmarPix, btnCancelar);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == btnConfirmarPix) {
            try {
                // Processar pagamento
                pedido.processarPagamento();

                // Aceitar pedido no restaurante
                Restaurante rest = carrinho.getRestaurante();
                rest.aceitarPedido(pedido);

                // Adicionar ao histórico
                cliente.adicionarPedido(pedido);
                RepositorioRestaurantes.getInstance().adicionarPedido(pedido);
                RepositorioRestaurantes.getInstance().salvarDados();

                // Limpar carrinho
                carrinho.limparCarrinho();

                // Mostrar confirmação
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pedido Confirmado");
                alert.setHeaderText("✅ Pedido realizado com sucesso!");
                alert.setContentText(
                    "Pedido #" + pedido.getNumeroPedido() + "\n" +
                    "Status: " + pedido.getStatus() + "\n" +
                    "Total: R$ " + String.format("%.2f", pedido.getValorTotal())
                );
                alert.showAndWait();

                // Voltar para o menu
                mudarTela(event, "/ifome/MenuCliente.fxml");

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro", "Erro ao processar pagamento: " + e.getMessage());
            }
        }
    }

    private String gerarCodigoPix() {
        // Gerar código PIX simulado (em produção, usar API real)
        String caracteres = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            int index = (int) (Math.random() * caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        mudarTela(event, "/ifome/TelaCarrinho.fxml");
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