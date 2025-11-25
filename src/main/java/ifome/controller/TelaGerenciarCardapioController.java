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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * ✅ ATUALIZADO: Agora redireciona para TelaEscolherTipoProduto ao invés de abrir dialog
 */
public class TelaGerenciarCardapioController {

    @FXML private VBox containerProdutos;
    private Restaurante restaurante;

    @FXML
    public void initialize() {
        try {
            restaurante = SessaoUsuario.getInstance().getRestauranteLogado();
            
            if (restaurante == null) {
                mostrarAlerta("Erro", "Restaurante não identificado. Faça login novamente.");
                return;
            }
            
            carregarCardapio();
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar gerenciamento de cardápio:");
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar cardápio: " + e.getMessage());
        }
    }

    private void carregarCardapio() {
        try {
            containerProdutos.getChildren().clear();

            if (restaurante.getCardapio().isEmpty()) {
                Label vazio = new Label("Seu cardápio está vazio.");
                vazio.setStyle("-fx-text-fill: #999; -fx-font-size: 14px;");
                containerProdutos.getChildren().add(vazio);
                return;
            }

            for (Produto p : restaurante.getCardapio()) {
                VBox cardProduto = criarCardProduto(p);
                containerProdutos.getChildren().add(cardProduto);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar produtos:");
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao exibir produtos.");
        }
    }

    private VBox criarCardProduto(Produto p) {
        VBox card = new VBox(8);
        String opacidade = p.isDisponivel() ? "1.0" : "0.6";
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-padding: 15; -fx-opacity: " + opacidade + ";");

        // Linha 1: Nome e Preço
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label lblNome = new Label(p.getNome());
        lblNome.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblPreco = new Label(String.format("R$ %.2f", p.getPreco()));
        lblPreco.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblPreco.setStyle("-fx-text-fill: #ea1d2c;");

        header.getChildren().addAll(lblNome, spacer, lblPreco);

        // Linha 2: Descrição + Categoria + Detalhes específicos
        StringBuilder detalhes = new StringBuilder();
        detalhes.append(p.getDescricao());
        
        if (!p.getDescricao().isEmpty()) {
            detalhes.append(" • ");
        }
        
        detalhes.append(p.getCategoria());
        
        // Adiciona informações específicas de cada tipo
        if (p instanceof Sobremesa) {
            Sobremesa sobremesa = (Sobremesa) p;
            detalhes.append(" • ").append(sobremesa.getIconeTemperatura()).append(" ").append(sobremesa.getTemperatura());
        } else if (p instanceof Bebida) {
            // Se adicionar volumeML público na classe Bebida, pode exibir aqui
            detalhes.append(" • Bebida");
        } else if (p instanceof Comida) {
            detalhes.append(" • Comida");
        }
        
        Label lblDesc = new Label(detalhes.toString());
        lblDesc.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        lblDesc.setWrapText(true);

        // Linha 3: Botões de Ação
        HBox boxBotoes = new HBox(10);
        boxBotoes.setAlignment(Pos.CENTER_RIGHT);
        boxBotoes.setPadding(new Insets(10, 0, 0, 0));

        // Botão Disponibilidade
        Button btnStatus = new Button(p.isDisponivel() ? "Pausar Venda" : "Ativar Venda");
        btnStatus.setStyle("-fx-background-color: " + (p.isDisponivel() ? "#f39c12" : "#27ae60") + 
                          "; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");
        btnStatus.setOnAction(e -> {
            try {
                p.setDisponibilidade(!p.isDisponivel());
                RepositorioRestaurantes.getInstance().salvarDados();
                carregarCardapio();
            } catch (Exception ex) {
                System.err.println("❌ Erro ao alterar disponibilidade:");
                ex.printStackTrace();
                mostrarAlerta("Erro", "Erro ao alterar disponibilidade.");
            }
        });

        // Botão Remover
        Button btnRemover = new Button("Remover");
        btnRemover.setStyle("-fx-background-color: transparent; -fx-text-fill: #c0392b; " +
                           "-fx-border-color: #c0392b; -fx-border-radius: 5; -fx-cursor: hand;");
        btnRemover.setOnAction(e -> {
            try {
                Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacao.setTitle("Remover Produto");
                confirmacao.setHeaderText("Deseja remover " + p.getNome() + "?");
                confirmacao.setContentText("Esta ação não pode ser desfeita.");
                
                Optional<ButtonType> result = confirmacao.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    restaurante.removerProdutoCardapio(p);
                    RepositorioRestaurantes.getInstance().salvarDados();
                    carregarCardapio();
                }
            } catch (Exception ex) {
                System.err.println("❌ Erro ao remover produto:");
                ex.printStackTrace();
                mostrarAlerta("Erro", "Erro ao remover produto.");
            }
        });

        boxBotoes.getChildren().addAll(btnStatus, btnRemover);

        card.getChildren().addAll(header, lblDesc, boxBotoes);
        return card;
    }

    /**
     * ✅ ATUALIZADO: Agora vai para TelaEscolherTipoProduto ao invés de abrir dialog
     */
    @FXML
    private void adicionarProduto() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaEscolherTipoProduto.fxml"));
            Stage stage = (Stage) containerProdutos.getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            System.err.println("❌ Erro ao abrir tela de escolha de tipo:");
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao abrir tela: " + e.getMessage());
        }
    }

    @FXML
    private void voltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ifome/MenuRestaurante.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 360, 640));
        } catch (IOException e) {
            System.err.println("❌ Erro ao voltar:");
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao voltar para o menu.");
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}