package ifome;

import ifome.util.RepositorioRestaurantes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Aplicacao extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Inicializa dados (Persistência)
        RepositorioRestaurantes.getInstance().inicializarRestaurantes();

        // 2. Carrega o FXML de Login
        Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaLogin.fxml"));
        
        // 3. Configura a cena com tamanho de celular
        Scene scene = new Scene(root, 360, 640);
        
        // 4. Configura a Janela
        stage.setTitle("iFome App");
        stage.setScene(scene);
        stage.setResizable(false); 
        stage.show();
    }

    public static void main(String[] args) {
        // Dica: Se quiser rodar o modo console antigo, descomente a linha abaixo
        // e comente o launch(args):
        
        // AplicacaoConsole.main(args); 
        
        launch(args); // Inicia o modo Gráfico
    }
}