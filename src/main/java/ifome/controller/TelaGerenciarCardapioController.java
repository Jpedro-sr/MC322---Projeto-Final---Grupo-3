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

        // Linha 2: Descrição
        Label lblDesc = new Label(p.getDescricao() + " (" + p.getCategoria() + ")");
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

    @FXML
    private void adicionarProduto() {
        try {
            Dialog<Produto> dialog = new Dialog<>();
            dialog.setTitle("Novo Produto");
            dialog.setHeaderText("Adicionar item ao cardápio");

            VBox vbox = new VBox(10);
            vbox.setPadding(new Insets(20));
            
            TextField txtNome = new TextField(); 
            txtNome.setPromptText("Nome do produto");
            
            TextField txtDesc = new TextField(); 
            txtDesc.setPromptText("Descrição");
            
            TextField txtPreco = new TextField(); 
            txtPreco.setPromptText("Preço (R$)");
            
            ComboBox<String> cmbTipo = new ComboBox<>();
            cmbTipo.getItems().addAll("Comida", "Bebida", "Sobremesa", "Adicional");
            cmbTipo.setPromptText("Categoria");
            cmbTipo.setMaxWidth(Double.MAX_VALUE);

            vbox.getChildren().addAll(
                new Label("Nome:"), txtNome,
                new Label("Descrição:"), txtDesc,
                new Label("Preço:"), txtPreco,
                new Label("Tipo:"), cmbTipo
            );

            dialog.getDialogPane().setContent(vbox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    try {
                        String nome = txtNome.getText().trim();
                        String desc = txtDesc.getText().trim();
                        String precoStr = txtPreco.getText().trim().replace(",", ".");
                        
                        if (nome.isEmpty()) {
                            mostrarAlerta("Erro", "Nome do produto é obrigatório.");
                            return null;
                        }
                        
                        if (precoStr.isEmpty()) {
                            mostrarAlerta("Erro", "Preço é obrigatório.");
                            return null;
                        }
                        
                        double preco = Double.parseDouble(precoStr);
                        String tipo = cmbTipo.getValue();

                        if (tipo == null) {
                            mostrarAlerta("Erro", "Selecione uma categoria.");
                            return null;
                        }

                        // Cria o produto baseado no tipo
                        switch (tipo) {
                            case "Comida":
                                return new Comida(nome, desc, preco, false);
                            case "Bebida":
                                return new Bebida(nome, desc, preco, 350);
                            case "Sobremesa":
                                return new Sobremesa(nome, desc, preco);
                            case "Adicional":
                                return new Adicional(nome, preco);
                            default:
                                return null;
                        }
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Erro", "Preço inválido. Use números e ponto/vírgula para decimais.");
                        return null;
                    }
                }
                return null;
            });

            Optional<Produto> result = dialog.showAndWait();
            
            if (result.isPresent()) {
                Produto novo = result.get();
                restaurante.adicionarProdutoCardapio(novo);
                RepositorioRestaurantes.getInstance().salvarDados();
                carregarCardapio();
                mostrarAlerta("Sucesso", "Produto adicionado com sucesso!");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao adicionar produto:");
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao adicionar produto: " + e.getMessage());
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