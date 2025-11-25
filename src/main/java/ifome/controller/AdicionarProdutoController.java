package ifome.controller;

import java.io.IOException;
import ifome.model.*;
import ifome.util.RepositorioRestaurantes;
import ifome.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controlador para a tela de adicionar produto.
 * Formulário dinâmico que se adapta ao tipo de produto escolhido.
 */
public class AdicionarProdutoController {

    @FXML private Label lblTitulo;
    
    // Campos básicos
    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private TextField txtPreco;
    @FXML private Button btnAdicionar;

    // Containers específicos
    @FXML private VBox boxComida;
    @FXML private VBox boxBebida;
    @FXML private VBox boxSobremesa;
    @FXML private VBox boxAdicional;

    // Campos específicos - Comida
    @FXML private CheckBox chkVegetariano;
    @FXML private CheckBox chkVegano;

    // Campos específicos - Bebida
    @FXML private TextField txtVolumeML;

    // Campos específicos - Sobremesa
    @FXML private RadioButton rbGelada;
    @FXML private RadioButton rbQuente;
    @FXML private RadioButton rbAmbiente;
    @FXML private ToggleGroup grupoTemperatura;

    private Restaurante restaurante;
    private String tipoProduto;

    @FXML
    public void initialize() {
        try {
            restaurante = SessaoUsuario.getInstance().getRestauranteLogado();
            tipoProduto = SessaoUsuario.getInstance().getTipoProdutoSelecionado();

            if (restaurante == null) {
                mostrarAlerta("Erro", "Restaurante não identificado. Faça login novamente.");
                return;
            }

            if (tipoProduto == null || tipoProduto.isEmpty()) {
                mostrarAlerta("Erro", "Tipo de produto não selecionado.");
                return;
            }

            // Atualiza o título
            lblTitulo.setText("Nova " + tipoProduto);

            // Exibe apenas os campos específicos do tipo selecionado
            configurarCamposEspecificos();

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar tela de adicionar produto:");
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao carregar formulário: " + e.getMessage());
        }
    }

    /**
     * Configura quais campos específicos devem ser exibidos
     */
    private void configurarCamposEspecificos() {
        // Esconde todos primeiro
        boxComida.setVisible(false);
        boxComida.setManaged(false);
        boxBebida.setVisible(false);
        boxBebida.setManaged(false);
        boxSobremesa.setVisible(false);
        boxSobremesa.setManaged(false);
        boxAdicional.setVisible(false);
        boxAdicional.setManaged(false);

        // Exibe apenas o container do tipo selecionado
        switch (tipoProduto) {
            case "Comida":
                boxComida.setVisible(true);
                boxComida.setManaged(true);
                break;
            case "Bebida":
                boxBebida.setVisible(true);
                boxBebida.setManaged(true);
                break;
            case "Sobremesa":
                boxSobremesa.setVisible(true);
                boxSobremesa.setManaged(true);
                break;
            case "Adicional":
                boxAdicional.setVisible(true);
                boxAdicional.setManaged(true);
                break;
        }
    }

    @FXML
    private void adicionarProduto(ActionEvent event) {
        try {
            // Validação dos campos básicos
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String precoStr = txtPreco.getText().trim().replace(",", ".");

            if (nome.isEmpty()) {
                mostrarAlerta("Erro", "Nome do produto é obrigatório.");
                txtNome.requestFocus();
                return;
            }

            if (precoStr.isEmpty()) {
                mostrarAlerta("Erro", "Preço é obrigatório.");
                txtPreco.requestFocus();
                return;
            }

            double preco;
            try {
                preco = Double.parseDouble(precoStr);
                if (preco <= 0) {
                    mostrarAlerta("Erro", "Preço deve ser maior que zero.");
                    txtPreco.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro", "Preço inválido. Use números e ponto/vírgula para decimais.");
                txtPreco.requestFocus();
                return;
            }

            // Cria o produto de acordo com o tipo
            Produto novoProduto = criarProduto(nome, descricao, preco);

            if (novoProduto == null) {
                mostrarAlerta("Erro", "Erro ao criar produto. Verifique os campos.");
                return;
            }

            // Adiciona ao cardápio do restaurante
            restaurante.adicionarProdutoCardapio(novoProduto);
            RepositorioRestaurantes.getInstance().salvarDados();

            // Mostra mensagem de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("✅ Produto adicionado!");
            alert.setContentText(nome + " foi adicionado ao cardápio com sucesso.");
            alert.showAndWait();

            // Volta para a tela de gerenciar cardápio
            mudarTela(event, "/ifome/TelaGerenciarCardapio.fxml");

        } catch (Exception e) {
            System.err.println("❌ Erro ao adicionar produto:");
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao adicionar produto: " + e.getMessage());
        }
    }

    /**
     * Cria o produto específico de acordo com o tipo selecionado
     */
    private Produto criarProduto(String nome, String descricao, double preco) {
        switch (tipoProduto) {
            case "Comida":
                return criarComida(nome, descricao, preco);
            case "Bebida":
                return criarBebida(nome, descricao, preco);
            case "Sobremesa":
                return criarSobremesa(nome, descricao, preco);
            case "Adicional":
                return criarAdicional(nome, preco);
            default:
                return null;
        }
    }

    /**
     * Cria uma Comida com suas características específicas
     */
    private Comida criarComida(String nome, String descricao, double preco) {
        boolean vegetariano = chkVegetariano.isSelected();
        boolean vegano = chkVegano.isSelected();
        
        // Se é vegano, automaticamente é vegetariano
        if (vegano) {
            vegetariano = true;
        }

        return new Comida(nome, descricao, preco, vegetariano);
    }

    /**
     * Cria uma Bebida com volume em ML
     */
    private Bebida criarBebida(String nome, String descricao, double preco) {
        String volumeStr = txtVolumeML.getText().trim();
        
        if (volumeStr.isEmpty()) {
            mostrarAlerta("Erro", "Volume (ml) é obrigatório para bebidas.");
            txtVolumeML.requestFocus();
            return null;
        }

        int volumeML;
        try {
            volumeML = Integer.parseInt(volumeStr);
            if (volumeML <= 0) {
                mostrarAlerta("Erro", "Volume deve ser maior que zero.");
                txtVolumeML.requestFocus();
                return null;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Volume inválido. Digite apenas números.");
            txtVolumeML.requestFocus();
            return null;
        }

        return new Bebida(nome, descricao, preco, volumeML);
    }

    /**
     * Cria uma Sobremesa com temperatura
     */
    private Sobremesa criarSobremesa(String nome, String descricao, double preco) {
        // Determina a temperatura selecionada
        String temperatura = "Ambiente"; // default
        
        if (rbGelada.isSelected()) {
            temperatura = "Gelada";
        } else if (rbQuente.isSelected()) {
            temperatura = "Quente";
        } else if (rbAmbiente.isSelected()) {
            temperatura = "Ambiente";
        }

        // Cria a sobremesa básica
        Sobremesa sobremesa = new Sobremesa(nome, descricao, preco);
        
        // Como a classe Sobremesa atual não tem campo temperatura,
        // você precisará adicionar esse campo na classe Sobremesa.java
        // Por enquanto, adiciona a temperatura na descrição
        if (!descricao.isEmpty()) {
            sobremesa.setDescricao(descricao + " (Servida " + temperatura.toLowerCase() + ")");
        } else {
            sobremesa.setDescricao("Servida " + temperatura.toLowerCase());
        }

        return sobremesa;
    }

    /**
     * Cria um Adicional (usa apenas campos básicos)
     */
    private Adicional criarAdicional(String nome, double preco) {
        return new Adicional(nome, preco);
    }

    @FXML
    private void voltar(ActionEvent event) {
        try {
            mudarTela(event, "/ifome/TelaEscolherTipoProduto.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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