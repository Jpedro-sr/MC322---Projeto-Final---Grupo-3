package ifome;

import ifome.util.RepositorioRestaurantes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// versao nova da aplicação. O outro arquivo ficou obsoleto e fragmentado nos controllers e tudo mais
//

public class Aplicacao extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // inicializa persistencia
        RepositorioRestaurantes.getInstance().inicializarRestaurantes();

        // carrega o FXML de login
        Parent root = FXMLLoader.load(getClass().getResource("/ifome/TelaLogin.fxml"));
        
        // configura a cena com tamanho de celular
        Scene scene = new Scene(root, 360, 640);
        
        // configura a janela
        stage.setTitle("iFome App");
        stage.setScene(scene);
        stage.setResizable(false); 
        stage.show();
    }

    //metodo antigo pode funcionar mas é bom remover
    public static void main(String[] args) {
   
        
        launch(args); 
    }
}