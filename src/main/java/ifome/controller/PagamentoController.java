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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * ✅ CORRIGIDO: Fluxo de pagamento com valor correto e status Pendente
 */
public class PagamentoController {

    @FXML private Label lblRestaurante;
    @FXML private Label lblItens;
    @FXML private Label lblTotal;
    @FXML private RadioButton rbPix;
    @FXML private RadioButton rbCartao;
    @FXML private RadioButton rbDinheiro;
    @FXML private ToggleGroup grupoPagamento;
    @FXML private Button btnConfirmar;

    private Cliente cliente;
    private Carrinho carrinho;

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

    /**
     * ✅ CORRIGIDO: Gera pedido com valor correto e adiciona à fila do restaurante
     */
    @FXML
    private void confirmarPagamento(ActionEvent event) {
        if (grupoPagamento.getSelectedToggle() == null) {
            mostrarAlerta("Atenção", "Selecione uma forma de pagamento!");
            return;
        }

        try {
            FormaPagamento pagamento = null;

            if (rbPix.isSelected()) {
                if (mostrarQRCodePIX()) {
                    pagamento = new PIX();
                } else {
                    return;
                }
                
            } else if (rbCartao.isSelected()) {
                pagamento = processarPagamentoCartao();
                if (pagamento == null) return;
                
            } else if (rbDinheiro.isSelected()) {
                pagamento = solicitarValorDinheiro();
                if (pagamento == null) return;
            }

            // ✅ CORRIGIDO: Gera pedido (já com valor calculado)
            Pedido pedido = carrinho.gerarPedido();
            pedido.setFormaPagamento(pagamento);
            
            // ✅ IMPORTANTE: Pedido inicia como "Pendente" (aguardando confirmação)
            pedido.atualizarStatus("Pendente");

            // ✅ CRÍTICO: Adiciona pedido à fila do RESTAURANTE
            Restaurante restaurante = carrinho.getRestaurante();
            restaurante.getFilaPedidos().add(pedido);

            // Adiciona ao histórico do cliente
            cliente.adicionarPedido(pedido);
            
            // Salva no repositório global
            RepositorioRestaurantes.getInstance().adicionarPedido(pedido);
            RepositorioRestaurantes.getInstance().salvarDados();

            // Limpa o carrinho APÓS salvar tudo
            carrinho.limparCarrinho();

            // ✅ Mostra confirmação com valor correto
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pedido Enviado");
            alert.setHeaderText("✅ Pedido enviado com sucesso!");
            alert.setContentText(
                "Pedido #" + pedido.getNumeroPedido() + "\n" +
                "Status: Aguardando confirmação do restaurante\n" +
                "Total: R$ " + String.format("%.2f", pedido.getValorTotal())
            );
            alert.showAndWait();

            mudarTela(event, "/ifome/MenuCliente.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao processar pedido: " + e.getMessage());
        }
    }

    private FormaPagamento processarPagamentoCartao() {
        if (!cliente.getCartoesSalvos().isEmpty()) {
            return mostrarDialogEscolherCartao();
        } else {
            return solicitarDadosNovoCartao();
        }
    }

    private CartaoCredito mostrarDialogEscolherCartao() {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Seus Cartões");
        dialog.setHeaderText("  Escolha um cartão");
        estilizarDialog(dialog);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        ToggleGroup grupoCartoes = new ToggleGroup();
        for (CartaoSalvo cartao : cliente.getCartoesSalvos()) {
            vbox.getChildren().add(criarCardCartao(cartao, grupoCartoes));
        }

        Button btnNovo = new Button("➕ Adicionar Novo Cartão");
        btnNovo.setMaxWidth(Double.MAX_VALUE);
        btnNovo.setStyle("-fx-background-color: white; -fx-border-color: #ea1d2c; -fx-text-fill: #ea1d2c; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 8; -fx-border-radius: 8;");
        
        btnNovo.setOnAction(e -> {
            CartaoCredito novo = solicitarDadosNovoCartao();
            if (novo != null) dialog.setResult(novo);
        });
        
        vbox.getChildren().add(new Separator());
        vbox.getChildren().add(btnNovo);
        dialog.getDialogPane().setContent(vbox);
        
        ButtonType btnContinuar = new ButtonType("Continuar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnContinuar, ButtonType.CANCEL);
        estilizarBotoesDialog(dialog);

        dialog.setResultConverter(bt -> {
            if (bt == btnContinuar && grupoCartoes.getSelectedToggle() != null) {
                CartaoSalvo selecionado = (CartaoSalvo) grupoCartoes.getSelectedToggle().getUserData();
                return selecionado.toCartaoCredito();
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private HBox criarCardCartao(CartaoSalvo cartao, ToggleGroup grupo) {
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-padding: 10; -fx-border-color: #ddd; -fx-border-radius: 5;");

        RadioButton rb = new RadioButton(cartao.getApelido() + "\n" + cartao.getNumeroMascarado());
        rb.setUserData(cartao);
        rb.setToggleGroup(grupo);
        rb.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rb, javafx.scene.layout.Priority.ALWAYS);

        Button btnDel = new Button("🗑️");
        btnDel.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-cursor: hand;");
        btnDel.setOnAction(e -> {
            cliente.removerCartao(cartao);
            RepositorioRestaurantes.getInstance().salvarDados();
            ((VBox) hbox.getParent()).getChildren().remove(hbox);
        });

        hbox.getChildren().addAll(rb, btnDel);
        return hbox;
    }

    private CartaoCredito solicitarDadosNovoCartao() {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Novo Cartão");
        dialog.setHeaderText("Dados do Cartão");
        estilizarDialog(dialog);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        TextField txtApelido = criarCampoEstilizado("Apelido (Ex: Nubank)");
        TextField txtNumero = criarCampoEstilizado("Número (16 dígitos)");
        TextField txtTitular = criarCampoEstilizado("Nome do Titular");
        
        HBox boxVal = new HBox(10);
        TextField txtVal = criarCampoEstilizado("MM/AA"); txtVal.setPrefWidth(100);
        TextField txtCvv = criarCampoEstilizado("CVV"); txtCvv.setPrefWidth(80);
        boxVal.getChildren().addAll(txtVal, txtCvv);

        CheckBox chkSalvar = new CheckBox("Salvar Cartão");
        chkSalvar.setSelected(true);
        txtApelido.disableProperty().bind(chkSalvar.selectedProperty().not());

        vbox.getChildren().addAll(new Label("Apelido:"), txtApelido, 
                                  new Label("Número:"), txtNumero, 
                                  new Label("Titular:"), txtTitular, 
                                  new Label("Dados:"), boxVal, chkSalvar);
        
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        estilizarBotoesDialog(dialog);
        
        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                if (txtNumero.getText().length() < 13 || txtTitular.getText().isEmpty()) return null;
                
                String num = txtNumero.getText();
                String tit = txtTitular.getText();
                String val = txtVal.getText().isEmpty() ? "12/30" : txtVal.getText();
                String cvv = txtCvv.getText();
                
                if (chkSalvar.isSelected()) {
                    String apelido = txtApelido.getText().isEmpty() ? "Meu Cartão" : txtApelido.getText();
                    CartaoSalvo salvo = new CartaoSalvo(num, tit, cvv, val, apelido);
                    cliente.adicionarCartao(salvo);
                    RepositorioRestaurantes.getInstance().salvarDados();
                }
                return new CartaoCredito(num, tit, cvv, val);
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private boolean mostrarQRCodePIX() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pagamento PIX");
        alert.setHeaderText("Pagamento PIX");
        alert.setGraphic(null);
        estilizarDialog(alert);
        
        String codigoPix = "00020126360014BR.GOV.BCB.PIX0114+551199999" + 
                           System.currentTimeMillis() + 
                           "5204000053039865802BR5913iFomeDelivery6008BRASILIA62070503***6304";
        
        TextArea area = new TextArea(codigoPix);
        area.setEditable(false);
        area.setWrapText(true);
        area.setMaxHeight(80);
        area.setStyle("-fx-font-family: 'Monospaced'; -fx-control-inner-background: #f4f4f4; -fx-highlight-fill: #ea1d2c; -fx-highlight-text-fill: white;");
        
        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        
        Label lblInstrucao = new Label("Copie o código abaixo e cole no app do seu banco:");
        lblInstrucao.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        
        Label lblValor = new Label(String.format("Valor: R$ %.2f", carrinho.calcularTotalComDesconto()));
        lblValor.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblValor.setStyle("-fx-text-fill: #ea1d2c;");

        content.getChildren().addAll(lblValor, lblInstrucao, area);
        alert.getDialogPane().setContent(content);
        estilizarBotoesDialog(alert);

        return alert.showAndWait().map(bt -> bt == ButtonType.OK).orElse(false);
    }

    private Dinheiro solicitarValorDinheiro() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dinheiro");
        dialog.setHeaderText("Troco para quanto?");
        dialog.setContentText("Valor (R$):");
        dialog.setGraphic(null);
        estilizarDialog(dialog);
        
        TextField input = dialog.getEditor();
        input.setStyle("-fx-padding: 10; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #ddd;");
        estilizarBotoesDialog(dialog);

        Optional<String> res = dialog.showAndWait();
        if (res.isPresent()) {
            try {
                double valor = Double.parseDouble(res.get().replace(",", "."));
                if (valor < carrinho.calcularTotalComDesconto()) {
                    mostrarAlerta("Erro", "Valor insuficiente.");
                    return null;
                }
                return new Dinheiro(valor);
            } catch (Exception e) {
                mostrarAlerta("Erro", "Valor inválido");
            }
        }
        return null;
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

    private void estilizarDialog(Dialog<?> dialog) {
        DialogPane pane = dialog.getDialogPane();
        pane.setStyle("-fx-background-color: white; -fx-font-family: 'System';");
        
        Node header = pane.lookup(".header-panel");
        if (header != null) {
            header.setStyle("-fx-background-color: #ea1d2c; -fx-padding: 15;");
            header.lookupAll(".label").forEach(node -> {
                node.setStyle(
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 20px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-alignment: center;"
                );
            });
        }
    }

    private void estilizarBotoesDialog(Dialog<?> dialog) {
        DialogPane pane = dialog.getDialogPane();
        Node btnOk = pane.lookupButton(ButtonType.OK);
        if (btnOk != null) btnOk.setStyle("-fx-background-color: #ea1d2c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");
        
        Node btnSim = pane.lookupButton(ButtonType.YES);
        if (btnSim != null) btnSim.setStyle("-fx-background-color: #ea1d2c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");

        Node btnCancel = pane.lookupButton(ButtonType.CANCEL);
        if (btnCancel != null) btnCancel.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-text-fill: #666; -fx-cursor: hand; -fx-background-radius: 5; -fx-border-radius: 5;");
    }

    private TextField criarCampoEstilizado(String prompt) {
        TextField txt = new TextField();
        txt.setPromptText(prompt);
        txt.setStyle("-fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #e0e0e0; -fx-background-color: #f9f9f9;");
        return txt;
    }
}