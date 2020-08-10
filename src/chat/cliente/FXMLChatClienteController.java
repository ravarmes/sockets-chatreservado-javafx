package chat.cliente;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import chat.objeto.Mensagem;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author rafael
 */
public class FXMLChatClienteController implements Initializable {

    @FXML
    private TextField textIP;
    @FXML
    private TextField textNome;
    @FXML
    private TextField textPorta;
    @FXML
    private TextField textMensagem;
    @FXML
    private TextArea textAreaHistorico;
    @FXML
    private Button buttonConectar;
    @FXML
    private Button buttonSair;
    @FXML
    private ListView listViewUsuarios;

    private Stage dialogStage;

    private Socket socket;
    private String remetente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.textIP.setText("127.0.0.1");
        this.textPorta.setText("54321");
    }

    public void handleButtonSair() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto("Saiu do Chat!");
            mensagem.setAction(Mensagem.Action.DISCONNECT);

            //Saída de Dados do Cliente
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(mensagem); //Enviando mensagem para Servidor

        } catch (IOException ex) {
            Logger.getLogger(FXMLChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.getDialogStage().close();
    }

    public void handleButtonConectar() {
        //Setando o título do AnchorPane com o nome do usuário do Chat
        this.dialogStage.setTitle(this.textNome.getText());
        this.remetente = textNome.getText();

        try {
            //Conectando ao Servidor do Chat
            socket = new Socket(textIP.getText(), Integer.valueOf(textPorta.getText()));
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());

            //Enviando a primeira mensagem informando conexão (apenas para passar o nome do cliente)
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(remetente);
            mensagem.setTexto("Entrou no Chat!");
            mensagem.setAction(Mensagem.Action.CONNECT);

            //Instanciando uma ThreadCliente para ficar recebendo mensagens do Servidor
            ThreadCliente thread = new ThreadCliente(remetente, socket, textAreaHistorico, listViewUsuarios);
            thread.setName("Thread Cliente " + remetente);
            thread.start();

            //Saída de Dados do Cliente
            saida.writeObject(mensagem); //Enviando mensagem para Servidor

        } catch (IOException ex) {
            Logger.getLogger(FXMLChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        desabilitarTextFields();
    }

    public void handleButtonEnviar() {
        try {
            Mensagem mensagem = new Mensagem();
            mensagem.setRemetente(this.remetente);
            mensagem.setTexto(this.textMensagem.getText());
            mensagem.setAction(Mensagem.Action.SEND);

            //Caso tenha selecionado algum usuário
            if (listViewUsuarios.getSelectionModel().getSelectedItem() != null) {
                mensagem.setAction(Mensagem.Action.SEND_ONE);
                mensagem.setDestinatario((String)listViewUsuarios.getSelectionModel().getSelectedItem());
            }

            //Saída de Dados do Cliente
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.writeObject(mensagem); //Enviando mensagem para Servidor

            this.textMensagem.setText("");
            this.listViewUsuarios.getSelectionModel().clearSelection();

        } catch (IOException ex) {
            Logger.getLogger(FXMLChatClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desabilitarTextFields() {
        //Desabilitando alguns TextField após conexão
        this.textIP.setEditable(false);
        this.textNome.setEditable(false);
        this.textPorta.setEditable(false);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
