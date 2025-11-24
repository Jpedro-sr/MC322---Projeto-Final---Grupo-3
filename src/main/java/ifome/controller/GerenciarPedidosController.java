package ifome.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import ifome.model.ItemPedido;
import ifome.model.Pedido;
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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * ✅ CORRIGIDO: Exibe pedidos com status e valor corretos
 */
public class GerenciarPedidosController {

    @FXML private VBox containerPedidos;
    private Restaurante restaurante;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        restaurante = SessaoUsuario.getInstance().getRestauranteLogado();
        
        // ✅ DEBUG: Mostra informações do restaurante
        System.out.println(">>> Restaurante logado: " + restaurante.getNomeRestaurante());
        System.out.println(">>> Email: " + restaurante.getEmail());
        
        carregarPedidos();
    }

    /**
     * ✅ CORRIGIDO: Carrega pedidos da fila do restaurante
     */
    private void carregarPedidos() {
        containerPedidos.getChildren().clear();

        // ✅ IMPORTANTE: Busca pedidos diretamente do repositório global
        List<Pedido> todosPedidos = RepositorioRestaurantes.getInstance().getTodosPedidos();
        
        // Filtra apenas os pedidos DESTE restaurante
        List<Pedido> pedidosDoRestaurante = todosPedidos.stream()
            .filter(p -> p.getRestaurante() != null && 
                        p.getRestaurante().getEmail().equals(restaurante.getEmail()))
            .toList();

        System.out.println(">>> Total de pedidos no sistema: " + todosPedidos.size());
        System.out.println(">>> Pedidos deste restaurante: " + pedidosDoRestaurante.size());

        if (pedidosDoRestaurante.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));
            emptyBox.setStyle("-fx-background-color: white; -fx-border-radius: 12; -fx-background-radius: 12;");

            Label lblEmpty = new Label("📦 Nenhum pedido no momento");
            lblEmpty.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #999;");

            Label lblSubEmpty = new Label("Aguarde novos pedidos chegarem");
            lblSubEmpty.setStyle("-fx-font-size: 14px; -fx-text-fill: #bbb;");

            emptyBox.getChildren().addAll(lblEmpty, lblSubEmpty);
            containerPedidos.getChildren().add(emptyBox);
            return;
        }

        // ✅ Exibe os pedidos
        for (Pedido pedido : pedidosDoRestaurante) {
            System.out.println(">>> Pedido #" + pedido.getNumeroPedido() + 
                             " | Status: " + pedido.getStatus() + 
                             " | Valor: R$" + String.format("%.2f", pedido.getValorTotal()));
            
            VBox cardPedido = criarCardPedido(pedido);
            containerPedidos.getChildren().add(cardPedido);
        }
    }

    /**
     * ✅ CORRIGIDO: Cria card visual do pedido com valor correto
     */
    private VBox criarCardPedido(Pedido pedido) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12;"
        );

        // Header: Número do pedido e status
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblNumero = new Label("#" + pedido.getNumeroPedido());
        lblNumero.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblNumero.setStyle("-fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String corStatus = getCorStatus(pedido.getStatus());
        Label lblStatus = new Label("● " + pedido.getStatus());
        lblStatus.setStyle("-fx-text-fill: " + corStatus + "; -fx-font-size: 14px; -fx-font-weight: bold;");

        header.getChildren().addAll(lblNumero, spacer, lblStatus);

        // Cliente e Data
        Label lblCliente = new Label("👤 " + pedido.getCliente().getNome());
        lblCliente.setStyle("-fx-text-fill: #666; -fx-font-size: 14px;");

        Label lblData = new Label("📅 " + dateFormat.format(pedido.getDataHora()));
        lblData.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        // Itens (resumo)
        VBox itensBox = new VBox(5);
        itensBox.setStyle("-fx-padding: 10 0 10 0;");
        
        Label lblItensHeader = new Label("📋 Itens:");
        lblItensHeader.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblItensHeader.setStyle("-fx-text-fill: #333;");
        itensBox.getChildren().add(lblItensHeader);

        for (ItemPedido item : pedido.getItens()) {
            Label lblItem = new Label(String.format("  • %dx %s - R$ %.2f", 
                item.getQuantidade(), 
                item.getProduto().getNome(),
                item.calcularPrecoTotal()
            ));
            lblItem.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");
            itensBox.getChildren().add(lblItem);
        }

        // ✅ CORRIGIDO: Valor Total (agora exibe corretamente)
        HBox footerBox = new HBox();
        footerBox.setAlignment(Pos.CENTER_LEFT);
        footerBox.setStyle("-fx-padding: 10 0 0 0; -fx-border-width: 1 0 0 0; -fx-border-color: #e0e0e0;");

        // ✅ Se valor está zerado, recalcula
        double valorExibir = pedido.getValorTotal();
        if (valorExibir == 0.0 && !pedido.getItens().isEmpty()) {
            valorExibir = pedido.calcularPrecoTotal();
            pedido.setValorTotal(valorExibir); // Atualiza no objeto
        }

        Label lblTotal = new Label("Total: R$ " + String.format("%.2f", valorExibir));
        lblTotal.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblTotal.setStyle("-fx-text-fill: #ea1d2c;");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        footerBox.getChildren().add(lblTotal);

        // Botões de ação
        HBox botoesBox = new HBox(10);
        botoesBox.setAlignment(Pos.CENTER_RIGHT);
        botoesBox.setPadding(new Insets(10, 0, 0, 0));

        String status = pedido.getStatus();

        if (status.equals("Pendente")) {
            Button btnRecusar = new Button("✖ Recusar");
            btnRecusar.setStyle(
                "-fx-background-color: #e74c3c; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15;"
            );
            btnRecusar.setOnAction(e -> recusarPedido(pedido));

            Button btnAceitar = new Button("✓ Aceitar");
            btnAceitar.setStyle(
                "-fx-background-color: #4cd137; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15;"
            );
            btnAceitar.setOnAction(e -> aceitarPedido(pedido));

            botoesBox.getChildren().addAll(btnRecusar, btnAceitar);

        } else if (!status.equals("Entregue") && !status.equals("Cancelado")) {
            Button btnAtualizar = new Button("⚙ Atualizar Status");
            btnAtualizar.setStyle(
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 15;"
            );
            btnAtualizar.setOnAction(e -> atualizarStatusPedido(pedido));

            botoesBox.getChildren().add(btnAtualizar);
        }

        footerBox.getChildren().addAll(spacer2, botoesBox);

        card.getChildren().addAll(header, lblCliente, lblData, itensBox, footerBox);

        return card;
    }

    /**
     * ✅ CORRIGIDO: Aceita pedido e processa pagamento
     */
    private void aceitarPedido(Pedido pedido) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Aceitar Pedido");
        confirmacao.setHeaderText("Deseja aceitar o pedido #" + pedido.getNumeroPedido() + "?");
        
        // ✅ Recalcula valor se necessário
        double valorExibir = pedido.getValorTotal();
        if (valorExibir == 0.0 && !pedido.getItens().isEmpty()) {
            valorExibir = pedido.calcularPrecoTotal();
            pedido.setValorTotal(valorExibir);
        }
        
        confirmacao.setContentText(
            "Cliente: " + pedido.getCliente().getNome() + "\n" +
            "Total: R$ " + String.format("%.2f", valorExibir)
        );

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // ✅ Processa pagamento
                pedido.processarPagamento();
                
                // ✅ Salva no repositório GLOBAL
                RepositorioRestaurantes.getInstance().salvarDados();

                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Pedido Aceito");
                sucesso.setHeaderText("✅ Pedido #" + pedido.getNumeroPedido() + " aceito!");
                sucesso.setContentText(
                    "Pagamento processado com sucesso.\n" +
                    "O cliente foi notificado."
                );
                sucesso.showAndWait();

                carregarPedidos();
                
            } catch (ifome.exceptions.PagamentoRecusadoException e) {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setTitle("Pagamento Recusado");
                erro.setHeaderText("❌ Não foi possível processar o pagamento");
                erro.setContentText("Motivo: " + e.getMessage());
                erro.showAndWait();
                
                // Cancela o pedido
                try {
                    restaurante.recusarPedido(pedido);
                    RepositorioRestaurantes.getInstance().salvarDados();
                    carregarPedidos();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            } catch (Exception e) {
                mostrarAlerta("Erro", "Erro ao processar pedido: " + e.getMessage());
            }
        }
    }

    private void recusarPedido(Pedido pedido) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Recusar Pedido");
        confirmacao.setHeaderText("Deseja recusar o pedido #" + pedido.getNumeroPedido() + "?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                restaurante.recusarPedido(pedido);
                RepositorioRestaurantes.getInstance().salvarDados();

                Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
                sucesso.setTitle("Pedido Recusado");
                sucesso.setHeaderText("❌ Pedido #" + pedido.getNumeroPedido() + " recusado");
                sucesso.setContentText("O cliente será notificado.");
                sucesso.showAndWait();

                carregarPedidos();
            } catch (Exception e) {
                mostrarAlerta("Erro", "Erro ao recusar pedido: " + e.getMessage());
            }
        }
    }

    private void atualizarStatusPedido(Pedido pedido) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Atualizar Status");
        dialog.setHeaderText("Pedido #" + pedido.getNumeroPedido() + " - Status atual: " + pedido.getStatus());

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label lblInstrucao = new Label("Selecione o novo status:");
        lblInstrucao.setStyle("-fx-font-weight: bold;");

        String[] statusDisponiveis = getProximosStatus(pedido.getStatus());
        
        VBox opcoesBox = new VBox(10);
        
        for (String novoStatus : statusDisponiveis) {
            Button btnStatus = new Button(novoStatus);
            btnStatus.setMaxWidth(Double.MAX_VALUE);
            btnStatus.setStyle(
                "-fx-background-color: #f0f0f0; " +
                "-fx-text-fill: #333; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 12; " +
                "-fx-border-color: #ddd; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 5;"
            );
            
            btnStatus.setOnAction(e -> {
                dialog.setResult(novoStatus);
                dialog.close();
            });
            
            opcoesBox.getChildren().add(btnStatus);
        }

        vbox.getChildren().addAll(lblInstrucao, opcoesBox);
        dialog.getDialogPane().setContent(vbox);

        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(btnCancelar);

        Optional<String> resultado = dialog.showAndWait();
        
        if (resultado.isPresent()) {
            String novoStatus = resultado.get();
            pedido.atualizarStatus(novoStatus);
            RepositorioRestaurantes.getInstance().salvarDados();

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Status Atualizado");
            sucesso.setHeaderText("✅ Pedido #" + pedido.getNumeroPedido());
            sucesso.setContentText("Novo status: " + novoStatus);
            sucesso.showAndWait();

            carregarPedidos();
        }
    }

    private String[] getProximosStatus(String statusAtual) {
        switch (statusAtual) {
            case "Confirmado":
                return new String[]{"Preparando", "Cancelado"};
            case "Preparando":
                return new String[]{"Pronto", "Cancelado"};
            case "Pronto":
                return new String[]{"Em Entrega"};
            case "Em Entrega":
                return new String[]{"Entregue", "Cancelado"};
            default:
                return new String[]{};
        }
    }

    private String getCorStatus(String status) {
        switch (status) {
            case "Pendente":
                return "#f39c12";
            case "Confirmado":
                return "#3498db";
            case "Preparando":
                return "#e67e22";
            case "Pronto":
                return "#9b59b6";
            case "Em Entrega":
                return "#1abc9c";
            case "Entregue":
                return "#4cd137";
            case "Cancelado":
                return "#e74c3c";
            default:
                return "#95a5a6";
        }
    }

    @FXML
    private void voltar(ActionEvent event) throws IOException {
        mudarTela(event, "/ifome/MenuRestaurante.fxml");
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