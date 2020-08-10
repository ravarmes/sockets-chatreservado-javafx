package chat.cliente;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author rafael
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        /*
        Parent root = FXMLLoader.load(getClass().getResource("FXMLChatCliente.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        */
        
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLChatClienteController.class.getResource("FXMLChatCliente.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        FXMLChatClienteController controller = loader.getController();
        controller.setDialogStage(stage);
        
        Scene scene = new Scene(page);
        
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
