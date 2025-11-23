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
                // FLUXO CORRIGIDO: Verifica salvos antes de pedir dados
                pagamento = processarPagamentoCartao();
                if (pagamento == null) return; // Cancelado
                
            } else if (rbDinheiro.isSelected()) {
                pagamento = solicitarValorDinheiro();
                if (pagamento == null) return;
            }

            // ✅ CORREÇÃO: Gera pedido
            Pedido pedido = carrinho.gerarPedido();
            pedido.setFormaPagamento(pagamento);

            // ✅ CORREÇÃO: Define como Pendente (não processa pagamento ainda)
            pedido.atualizarStatus("Pendente");

            // ✅ CORREÇÃO: Adiciona à fila do restaurante SEM aceitar automaticamente
            Restaurante restaurante = carrinho.getRestaurante();
            
            // NÃO usar restaurante.aceitarPedido() porque ele força "Confirmado"
            // Adicionar diretamente à fila como PENDENTE
            restaurante.getFilaPedidos().add(pedido);

            // Adiciona ao histórico
            cliente.adicionarPedido(pedido);
            RepositorioRestaurantes.getInstance().adicionarPedido(pedido);
            RepositorioRestaurantes.getInstance().salvarDados();

            // Limpa carrinho
            carrinho.limparCarrinho();

            // Mostra confirmação
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pedido Enviado");
            alert.setHeaderText("✅ Pedido enviado com sucesso!");
            alert.setContentText(
                "Pedido #" + pedido.getNumeroPedido() + "\n" +
                "Status: Aguardando confirmação do restaurante\n" +
                "Total: R$ " + String.format("%.2f", pedido.getValorTotal()) + "\n\n" +
                "Você receberá uma notificação quando o restaurante aceitar."
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

        // Se tiver cartões salvos, oferece a escolha
        if (!cartoesSalvos.isEmpty()) {
            // Cria uma lista mista (Cartões + Opção de Novo)
            List<Object> opcoes = new ArrayList<>(cartoesSalvos);
            opcoes.add("➕ Usar outro cartão");

            ChoiceDialog<Object> dialog = new ChoiceDialog<>(opcoes.get(0), opcoes);
            dialog.setTitle("Seus Cartões");
            dialog.setHeaderText("Escolha como deseja pagar:");
            dialog.setContentText("Cartão:");

            Optional<Object> resultado = dialog.showAndWait();
            
            if (!resultado.isPresent()) return null; // Cancelou

            Object selecao = resultado.get();
            
            if (selecao instanceof CartaoSalvo) {
                // Usou cartão salvo
                CartaoSalvo s = (CartaoSalvo) selecao;
                return new CartaoCredito(s.getNumeroCompleto(), s.getNomeTitular(), s.getCvv(), s.getValidade());
            }
        }

        // Se não tem salvos ou escolheu "Outro", abre formulário
        return solicitarDadosNovoCartao();
    }

    private CartaoCredito solicitarDadosNovoCartao() {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Novo Cartão");
        dialog.setHeaderText("Digite os dados do cartão");

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmar, ButtonType.CANCEL);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: white;");

        TextField txtNumero = new TextField(); txtNumero.setPromptText("Número (16 dígitos)");
        TextField txtTitular = new TextField(); txtTitular.setPromptText("Nome do Titular");
        TextField txtValidade = new TextField(); txtValidade.setPromptText("Validade (MM/AA)");
        TextField txtCVV = new TextField(); txtCVV.setPromptText("CVV");
        
        // CHECKBOX PARA SALVAR
        CheckBox chkSalvar = new CheckBox("Salvar cartão para próximas compras");
        TextField txtApelido = new TextField(); 
        txtApelido.setPromptText("Apelido (ex: Nubank)");
        txtApelido.disableProperty().bind(chkSalvar.selectedProperty().not()); // Só habilita se marcar salvar

        vbox.getChildren().addAll(
            new Label("Número:"), txtNumero,
            new Label("Titular:"), txtTitular,
            new Label("Validade:"), txtValidade,
            new Label("CVV:"), txtCVV,
            new Separator(),
            chkSalvar, txtApelido
        );

        dialog.getDialogPane().setContent(vbox);

        dialog.setResultConverter(btn -> {
            if (btn == btnConfirmar) {
                String num = txtNumero.getText().replaceAll("[^0-9]", "");
                String tit = txtTitular.getText();
                String val = txtValidade.getText();
                String cvv = txtCVV.getText();

                if (num.length() != 16 || tit.isEmpty() || cvv.length() < 3) return null;
                if (val.isEmpty()) val = "12/30";

                // LÓGICA DE SALVAR
                if (chkSalvar.isSelected()) {
                    String apelido = txtApelido.getText().isEmpty() ? 
                                     "Cartão final " + num.substring(12) : txtApelido.getText();
                    
                    CartaoSalvo novoSalvo = new CartaoSalvo(num, tit, cvv, val, apelido);
                    
                    // Salva na memória e no arquivo IMEDIATAMENTE
                    cliente.adicionarCartao(novoSalvo);
                    RepositorioRestaurantes.getInstance().salvarDados();
                }

                return new CartaoCredito(num, tit, cvv, val);
            }
            return null;
        });

        Optional<CartaoCredito> result = dialog.showAndWait();
        return result.orElse(null);
    }
    
    /**
     * Mostra QR Code simulado do PIX e aguarda confirmação.
     */
    private boolean mostrarQRCodePIX() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Pagamento PIX");
        dialog.setHeaderText("Escaneie o QR Code para pagar");

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: white;");

        // Simulação de QR Code (ASCII Art simplificado)
        Label lblQRCode = new Label(
            "█████████████████████\n" +
            "██ ▄▄▄▄▄ █▀ █▀██ ▄▄▄▄▄ ██\n" +
            "██ █   █ █▀▄ ▀██ █   █ ██\n" +
            "██ █▄▄▄█ █ ▀▄ ██ █▄▄▄█ ██\n" +
            "██▄▄▄▄▄▄▄█▄█ █▄█▄▄▄▄▄▄▄██\n" +
            "██ ▄ ▄  ▄ ▄█▄ ▄▀▀ ▀▄▀ ▄██\n" +
            "██▄ ▀██▄▄█  ▄ █▄  ▄▄█ ▄██\n" +
            "██ ▄▄▄▄▄ █▄ ▄  █ ▄ █▄▀ ██\n" +
            "██ █   █ █ ▄▀▄██▀ █▄▀▄ ██\n" +
            "██ █▄▄▄█ █ ▀▀▄ ▄ ▄  ▀▀ ██\n" +
            "██▄▄▄▄▄▄▄█▄█▄██▄█▄██▄█▄██\n" +
            "█████████████████████"
        );
        lblQRCode.setFont(Font.font("Monospaced", 10));
        lblQRCode.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-width: 2;");

        Label lblInstrucoes = new Label("Abra o app do seu banco e escaneie o QR Code");
        lblInstrucoes.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label lblValor = new Label(String.format("Valor: R$ %.2f", carrinho.calcularTotalComDesconto()));
        lblValor.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblValor.setStyle("-fx-text-fill: #ea1d2c;");

        vbox.getChildren().addAll(lblQRCode, lblInstrucoes, lblValor);
        dialog.getDialogPane().setContent(vbox);

        ButtonType btnConfirmarPag = new ButtonType("Já Paguei", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmarPag, btnCancelar);

        Button btnConfirmar = (Button) dialog.getDialogPane().lookupButton(btnConfirmarPag);
        btnConfirmar.setStyle("-fx-background-color: #4cd137; -fx-text-fill: white; -fx-font-weight: bold;");

        Optional<ButtonType> result = dialog.showAndWait();
        return result.isPresent() && result.get() == btnConfirmarPag;
    }

    /**
     * Permite ao usuário escolher um cartão salvo ou adicionar um novo.
     */
    private FormaPagamento selecionarOuAdicionarCartao() {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Selecionar Cartão");
        dialog.setHeaderText("Escolha um cartão ou adicione um novo");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        ToggleGroup grupoCartoes = new ToggleGroup();
        
        List<CartaoSalvo> cartoes = cliente.getCartoesSalvos();
        
        for (CartaoSalvo cartao : cartoes) {
            RadioButton rb = new RadioButton();
            rb.setUserData(cartao);
            rb.setToggleGroup(grupoCartoes);
            rb.setStyle("-fx-font-size: 14px;");
            
            HBox hbox = new HBox(10);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 5;");
            
            VBox infoBox = new VBox(3);
            Label lblNome = new Label(cartao.getApelido());
            lblNome.setFont(Font.font("System", FontWeight.BOLD, 14));
            
            Label lblNumero = new Label(cartao.getNumeroMascarado());
            lblNumero.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
            
            infoBox.getChildren().addAll(lblNome, lblNumero);
            hbox.getChildren().addAll(rb, infoBox);
            
            vbox.getChildren().add(hbox);
        }

        // Botão "Adicionar novo cartão"
        Button btnNovoCartao = new Button("+ Adicionar Novo Cartão");
        btnNovoCartao.setMaxWidth(Double.MAX_VALUE);
        btnNovoCartao.setStyle(
            "-fx-background-color: #f0f0f0; -fx-text-fill: #333; " +
            "-fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 15; " +
            "-fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5;"
        );
        
        final CartaoCredito[] novoCartao = {null};
        
        btnNovoCartao.setOnAction(e -> {
            novoCartao[0] = solicitarDadosCartaoNovo(true);
            if (novoCartao[0] != null) {
                dialog.setResult(novoCartao[0]);
                dialog.close();
            }
        });
        
        vbox.getChildren().add(btnNovoCartao);
        dialog.getDialogPane().setContent(vbox);

        ButtonType btnContinuar = new ButtonType("Continuar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnContinuar, btnCancelar);

        dialog.setResultConverter(button -> {
            if (button == btnContinuar) {
                if (grupoCartoes.getSelectedToggle() != null) {
                    CartaoSalvo selecionado = (CartaoSalvo) grupoCartoes.getSelectedToggle().getUserData();
                    return selecionado.toCartaoCredito();
                }
            }
            return novoCartao[0];
        });

        Optional<CartaoCredito> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }

    /**
     * Solicita dados de um novo cartão com opção de salvar.
     */
    private CartaoCredito solicitarDadosCartaoNovo(boolean permitirSalvar) {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Novo Cartão");
        dialog.setHeaderText("Preencha os dados do cartão");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        TextField txtApelido = new TextField();
        txtApelido.setPromptText("Apelido do cartão (ex: Cartão Principal)");
        txtApelido.setStyle("-fx-font-size: 13px; -fx-padding: 10;");

        TextField txtNumero = new TextField();
        txtNumero.setPromptText("0000 0000 0000 0000");
        txtNumero.setStyle("-fx-font-size: 13px; -fx-padding: 10;");

        TextField txtTitular = new TextField();
        txtTitular.setPromptText("Nome do Titular");
        txtTitular.setStyle("-fx-font-size: 13px; -fx-padding: 10;");

        HBox hboxInferior = new HBox(10);
        TextField txtValidade = new TextField();
        txtValidade.setPromptText("MM/AA");
        txtValidade.setPrefWidth(100);
        txtValidade.setStyle("-fx-font-size: 13px; -fx-padding: 10;");

        TextField txtCVV = new TextField();
        txtCVV.setPromptText("CVV");
        txtCVV.setPrefWidth(80);
        txtCVV.setStyle("-fx-font-size: 13px; -fx-padding: 10;");
        
        hboxInferior.getChildren().addAll(txtValidade, txtCVV);

        CheckBox chkSalvar = new CheckBox("Salvar este cartão para próximas compras");
        chkSalvar.setStyle("-fx-font-size: 12px;");

        if (permitirSalvar) {
            vbox.getChildren().addAll(
                new Label("Apelido (opcional):"), txtApelido,
                new Label("Número do Cartão:"), txtNumero,
                new Label("Nome do Titular:"), txtTitular,
                new Label("Validade e CVV:"), hboxInferior,
                chkSalvar
            );
        } else {
            vbox.getChildren().addAll(
                new Label("Número do Cartão:"), txtNumero,
                new Label("Nome do Titular:"), txtTitular,
                new Label("Validade e CVV:"), hboxInferior
            );
        }

        dialog.getDialogPane().setContent(vbox);

        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmar, btnCancelar);

        dialog.setResultConverter(button -> {
            if (button == btnConfirmar) {
                String numero = txtNumero.getText().trim().replaceAll("[^0-9]", "");
                String titular = txtTitular.getText().trim();
                String validade = txtValidade.getText().trim();
                String cvv = txtCVV.getText().trim();

                if (numero.length() != 16) {
                    mostrarAlerta("Erro", "O número do cartão deve ter 16 dígitos.");
                    return null;
                }

                if (titular.isEmpty()) {
                    mostrarAlerta("Erro", "Digite o nome do titular.");
                    return null;
                }

                if (!cvv.matches("\\d{3,4}")) {
                    mostrarAlerta("Erro", "CVV inválido.");
                    return null;
                }

                if (validade.isEmpty()) {
                    validade = "12/30";
                }

                // Salvar cartão se solicitado
                if (permitirSalvar && chkSalvar.isSelected()) {
                    String apelido = txtApelido.getText().trim();
                    if (apelido.isEmpty()) {
                        apelido = "Cartão " + numero.substring(12);
                    }
                    
                    CartaoSalvo novoCartao = new CartaoSalvo(numero, titular, cvv, validade, apelido);
                    cliente.adicionarCartao(novoCartao);
                    RepositorioRestaurantes.getInstance().salvarDados();
                }

                return new CartaoCredito(numero, titular, cvv, validade);
            }
            return null;
        });

        Optional<CartaoCredito> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }

    private Dinheiro solicitarValorDinheiro() {
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
                    return null;
                }

                return new Dinheiro(valor);

            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Digite um valor válido.");
                return null;
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
}