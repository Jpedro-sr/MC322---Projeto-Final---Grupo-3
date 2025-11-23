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

            // Define forma de pagamento com base na seleção
            if (rbPix.isSelected()) {
                pagamento = new PIX();
                
            } else if (rbCartao.isSelected()) {
                // Abre dialog para dados do cartão
                pagamento = solicitarDadosCartao();
                if (pagamento == null) {
                    return; // Usuário cancelou
                }
                
            } else if (rbDinheiro.isSelected()) {
                // Abre dialog para valor do troco
                pagamento = solicitarValorDinheiro();
                if (pagamento == null) {
                    return; // Usuário cancelou
                }
            }

            // Gera pedido
            Pedido pedido = carrinho.gerarPedido();
            pedido.setFormaPagamento(pagamento);

            // Processa pagamento
            pedido.processarPagamento();

            // Aceita pedido no restaurante
            carrinho.getRestaurante().aceitarPedido(pedido);

            // Adiciona ao histórico
            cliente.adicionarPedido(pedido);
            RepositorioRestaurantes.getInstance().adicionarPedido(pedido);
            RepositorioRestaurantes.getInstance().salvarDados();

            // Limpa carrinho
            carrinho.limparCarrinho();

            // Mostra confirmação
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

    private CartaoCredito solicitarDadosCartao() {
        Dialog<CartaoCredito> dialog = new Dialog<>();
        dialog.setTitle("Dados do Cartão");
        dialog.setHeaderText("Preencha os dados do seu cartão");

        // Botões
        ButtonType btnConfirmar = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnConfirmar, btnCancelar);

        // Conteúdo do diálogo
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label lblNumero = new Label("Número do Cartão:");
        TextField txtNumero = new TextField();
        txtNumero.setPromptText("0000 0000 0000 0000");

        Label lblTitular = new Label("Nome do Titular:");
        TextField txtTitular = new TextField();
        txtTitular.setPromptText("Como está no cartão");

        Label lblValidade = new Label("Validade (MM/AA):");
        TextField txtValidade = new TextField();
        txtValidade.setPromptText("MM/AA");

        Label lblCVV = new Label("CVV:");
        TextField txtCVV = new TextField();
        txtCVV.setPromptText("000");

        vbox.getChildren().addAll(
            lblNumero, txtNumero,
            lblTitular, txtTitular,
            lblValidade, txtValidade,
            lblCVV, txtCVV
        );

        dialog.getDialogPane().setContent(vbox);

        // Converte o resultado
        dialog.setResultConverter(button -> {
            if (button == btnConfirmar) {

                String numero = txtNumero.getText().trim().replaceAll("[^0-9]", "");
                String titular = txtTitular.getText().trim();
                String validade = txtValidade.getText().trim();
                String cvv = txtCVV.getText().trim();

                // Validações iguais às que você usava
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

                return new CartaoCredito(numero, titular, cvv, validade);
            }

            return null; // Cancelado
        });

        // Exibe e aguarda
        Optional<CartaoCredito> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }

    private Dinheiro solicitarValorDinheiro() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Pagamento em Dinheiro");
        dialog.setHeaderText("Troco para quanto?");
        dialog.setContentText("Valor (R$):");

        // Estilizar
        dialog.getDialogPane().setStyle("-fx-background-color: white;");

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