package ifome.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ifome.model.ItemPedido;
import ifome.model.Pedido;
import ifome.model.Restaurante;
import ifome.util.RepositorioRestaurantes;
import ifome.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * controler das estatisticas
 */
public class EstatisticasController {

    @FXML private Label lblTotalPedidos;
    @FXML private Label lblFaturamentoTotal;
    @FXML private Label lblTicketMedio;
    @FXML private Label lblAvaliacaoMedia;
    @FXML private VBox containerProdutosMaisVendidos;

    private Restaurante restaurante;

    @FXML
    public void initialize() {
        restaurante = SessaoUsuario.getInstance().getRestauranteLogado();
        carregarEstatisticas();
    }

    private void carregarEstatisticas() {
      
        List<Pedido> todosPedidos = RepositorioRestaurantes.getInstance().getTodosPedidos();
        
        List<Pedido> pedidosDoRestaurante = todosPedidos.stream()
            .filter(p -> p.getRestaurante() != null && 
                        p.getRestaurante().getEmail().equals(restaurante.getEmail()))
            .collect(Collectors.toList());

        List<Pedido> pedidosFinalizados = pedidosDoRestaurante.stream()
            .filter(p -> p.getStatus().equals("Entregue") || 
                        p.getStatus().equals("Confirmado") ||
                        p.getStatus().equals("Preparando") ||
                        p.getStatus().equals("Pronto") ||
                        p.getStatus().equals("Em Entrega"))
            .collect(Collectors.toList());

        System.out.println(">>> Estatísticas - Total de pedidos do restaurante: " + pedidosDoRestaurante.size());
        System.out.println(">>> Estatísticas - Pedidos finalizados: " + pedidosFinalizados.size());

        int totalPedidos = pedidosFinalizados.size();
        lblTotalPedidos.setText(String.valueOf(totalPedidos));

        // faturamento Total
        double faturamentoTotal = pedidosFinalizados.stream()
            .mapToDouble(p -> {
                double valor = p.getValorTotal();
                if (valor == 0.0 && !p.getItens().isEmpty()) {
                    valor = p.calcularPrecoTotal();
                }
                return valor;
            })
            .sum();
        
        System.out.println(">>> Faturamento total calculado: R$" + String.format("%.2f", faturamentoTotal));
        lblFaturamentoTotal.setText(String.format("R$ %.2f", faturamentoTotal));

        // ticket medio
        double ticketMedio = totalPedidos > 0 ? faturamentoTotal / totalPedidos : 0;
        lblTicketMedio.setText(String.format("R$ %.2f", ticketMedio));

        // avaliacao média
        double avaliacaoMedia = restaurante.calcularMediaAvaliacoes();
        lblAvaliacaoMedia.setText(String.format("%.1f ⭐", avaliacaoMedia));

        // mais vendidos
        carregarProdutosMaisVendidos(pedidosFinalizados);
    }

    private void carregarProdutosMaisVendidos(List<Pedido> pedidos) {
        containerProdutosMaisVendidos.getChildren().clear();

        
        Map<String, Integer> vendasPorProduto = new HashMap<>();
        Map<String, Double> faturamentoPorProduto = new HashMap<>();

        for (Pedido pedido : pedidos) {
            for (ItemPedido item : pedido.getItens()) {
                String nomeProduto = item.getProduto().getNome();
                int quantidade = item.getQuantidade();
                double faturamento = item.calcularPrecoTotal();

                vendasPorProduto.put(nomeProduto, 
                    vendasPorProduto.getOrDefault(nomeProduto, 0) + quantidade);
                
                faturamentoPorProduto.put(nomeProduto, 
                    faturamentoPorProduto.getOrDefault(nomeProduto, 0.0) + faturamento);
            }
        }

        if (vendasPorProduto.isEmpty()) {
            Label lblVazio = new Label("Nenhuma venda registrada ainda");
            lblVazio.setStyle("-fx-text-fill: #999; -fx-font-size: 14px;");
            containerProdutosMaisVendidos.getChildren().add(lblVazio);
            return;
        }

        // ordena por quantidade
        List<Map.Entry<String, Integer>> topProdutos = vendasPorProduto.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(10)
            .collect(Collectors.toList());

        int posicao = 1;
        for (Map.Entry<String, Integer> entrada : topProdutos) {
            String nomeProduto = entrada.getKey();
            int qtdVendida = entrada.getValue();
            double faturamento = faturamentoPorProduto.get(nomeProduto);

            VBox cardProduto = criarCardProdutoMaisVendido(
                posicao, nomeProduto, qtdVendida, faturamento
            );
            containerProdutosMaisVendidos.getChildren().add(cardProduto);
            posicao++;
        }
    }

    private VBox criarCardProdutoMaisVendido(int posicao, String nome, int quantidade, double faturamento) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(12));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e0e0e0; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
        );

        // posicao e nome
        Label lblPosicao = new Label("#" + posicao + " · " + nome);
        lblPosicao.setFont(Font.font("System", FontWeight.BOLD, 15));
        lblPosicao.setStyle("-fx-text-fill: #333;");

        // qtd vendida
        Label lblQuantidade = new Label("Vendidos: " + quantidade + " un.");
        lblQuantidade.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        // faturamento
        Label lblFaturamento = new Label("Receita: R$ " + String.format("%.2f", faturamento));
        lblFaturamento.setStyle("-fx-text-fill: #4cd137; -fx-font-size: 13px; -fx-font-weight: bold;");

        card.getChildren().addAll(lblPosicao, lblQuantidade, lblFaturamento);

        return card;
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
}