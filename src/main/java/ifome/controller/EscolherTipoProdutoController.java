package ifome.controller;

import java.io.IOException;
import ifome.util.SessaoUsuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * controller para os produtos
 */
public class EscolherTipoProdutoController {

    @FXML
    private void irParaFormComida(ActionEvent event) {
        try {
            // tipo
            SessaoUsuario.getInstance().setTipoProdutoSelecionado("Comida");
            mudarTela(event, "/ifome/TelaAdicionarProduto.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaFormBebida(ActionEvent event) {
        try {
            SessaoUsuario.getInstance().setTipoProdutoSelecionado("Bebida");
            mudarTela(event, "/ifome/TelaAdicionarProduto.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaFormSobremesa(ActionEvent event) {
        try {
            SessaoUsuario.getInstance().setTipoProdutoSelecionado("Sobremesa");
            mudarTela(event, "/ifome/TelaAdicionarProduto.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaFormAdicional(ActionEvent event) {
        try {
            SessaoUsuario.getInstance().setTipoProdutoSelecionado("Adicional");
            mudarTela(event, "/ifome/TelaAdicionarProduto.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltar(ActionEvent event) {
        try {
            mudarTela(event, "/ifome/TelaGerenciarCardapio.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mudarTela(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 360, 640));
    }
}