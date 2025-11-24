package ifome.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

            Pedido pedido = carrinho.gerarPedido();
            pedido.setFormaPagamento(pagamento);
            pedido.atualizarStatus("Pendente");

            Restaurante restaurante = carrinho.getRestaurante();
            restaurante.getFilaPedidos().add(pedido);

            cliente.adicionarPedido(pedido);
            RepositorioRestaurantes.getInstance().adicionarPedido(pedido);
            RepositorioRestaurantes.getInstance().salvarDados();

            carrinho.limparCarrinho();

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
        List<CartaoSalvo> cartoesSalvos = cliente.getCartoesSalvos();
        if (!cartoesSalvos.isEmpty()) {
            return mostrarDialogEscolherCartao();
        } else {
            return solicitarDadosNovoCartao();
        }
    }

    private CartaoCredito mostrarDialogEscolherCartao() {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Seus Cartões");
        dialog.setHeaderText("  Escolha um cartão");
        
        // APLICA O ESTILO VISUAL NOVO
        estilizarDialog(dialog);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        ToggleGroup grupoCartoes = new ToggleGroup();
        for (CartaoSalvo cartao : cliente.getCartoesSalvos()) {
            vbox.getChildren().add(criarCardCartao(cartao, grupoCartoes));
        }

        Button btnNovo = new Button("➕ Adicionar Novo Cartão");
        btnNovo.setMaxWidth(Double.MAX_VALUE);
        // Estilo do botão dentro do dialog
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

        // Estiliza os botões do rodapé do dialog
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

    /**
     * ✅ CORRIGIDO: Cria card visual para cada cartão salvo
     */
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
            // CORREÇÃO: Remove da memória e da tela visualmente
            cliente.removerCartao(cartao);
            RepositorioRestaurantes.getInstance().salvarDados();
            ((VBox) hbox.getParent()).getChildren().remove(hbox);
        });

        hbox.getChildren().addAll(rb, btnDel);
        return hbox;
    }

    /**
     * ✅ CORRIGIDO: Solicita dados de novo cartão e SALVA imediatamente
     */
    private CartaoCredito solicitarDadosNovoCartao() {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Novo Cartão");
        dialog.setHeaderText("Dados do Cartão");
        estilizarDialog(dialog); // Aplica estilo

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        // Campos com estilo melhorado
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
                // Validações básicas
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

        Optional<CartaoCredito> res = dialog.showAndWait();
        return res.orElse(null);
    }

    private boolean mostrarQRCodePIX() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pagamento PIX");
        alert.setHeaderText("Pagamento PIX"); // O estilo novo vai deixar isso branco e grande
        alert.setGraphic(null); // Remove ícone padrão
        estilizarDialog(alert);
        
        // Gera um código PIX aleatório simulado
        String codigoPix = "00020126360014BR.GOV.BCB.PIX0114+551199999" + 
                           System.currentTimeMillis() + // Garante que muda sempre
                           "5204000053039865802BR5913iFomeDelivery6008BRASILIA62070503***6304";
        
        TextArea area = new TextArea(codigoPix);
        area.setEditable(false); // Não deixa editar, mas deixa copiar
        area.setWrapText(true);  // Quebra linha se for grande
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
        
        // Botões personalizados
        estilizarBotoesDialog(alert);

        return alert.showAndWait().map(bt -> bt == ButtonType.OK).orElse(false);
    }

    private Dinheiro solicitarValorDinheiro() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dinheiro");
        dialog.setHeaderText("Troco para quanto?");
        dialog.setContentText("Valor (R$):");
        
        // Remove o ícone padrão feio e aplica estilo
        dialog.setGraphic(null); 
        estilizarDialog(dialog);
        
        // Estiliza o campo de texto interno do TextInputDialog
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

    // --- MÉTODOS VISUAIS AUXILIARES (A Mágica do Design) ---

    private void estilizarDialog(Dialog<?> dialog) {
        DialogPane pane = dialog.getDialogPane();
        
        // Fundo Branco e Borda
        pane.setStyle("-fx-background-color: white; -fx-font-family: 'System';");
        
        // Estiliza o cabeçalho (header) para vermelho
        Node header = pane.lookup(".header-panel");
        if (header != null) {
            header.setStyle("-fx-background-color: #ea1d2c; -fx-padding: 15;");
            
            // Busca o Label DENTRO do header para mudar a cor e fonte
            header.lookupAll(".label").forEach(node -> {
                node.setStyle(
                    "-fx-text-fill: white; " +       // Cor Branca
                    "-fx-font-size: 20px; " +        // Tamanho Maior
                    "-fx-font-weight: bold; " +      // Negrito
                    "-fx-alignment: center;"         // Centralizado
                );
            });
        }
    }

    private void estilizarBotoesDialog(Dialog<?> dialog) {
        DialogPane pane = dialog.getDialogPane();
        // Botão OK/Confirmar fica Vermelho
        Node btnOk = pane.lookupButton(ButtonType.OK);
        if (btnOk != null) btnOk.setStyle("-fx-background-color: #ea1d2c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");
        
        Node btnSim = pane.lookupButton(ButtonType.YES); // Caso use YES/NO
        if (btnSim != null) btnSim.setStyle("-fx-background-color: #ea1d2c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");

        // Botão Cancelar fica transparente/cinza
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