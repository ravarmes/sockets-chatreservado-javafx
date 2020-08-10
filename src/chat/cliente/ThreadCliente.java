package chat.cliente;

import chat.objeto.Mensagem;
import chat.objeto.Mensagem.Action;
import chat.servidor.ThreadServidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ThreadCliente extends Thread {

    private Socket socket;
    private TextArea textArea;
    private String remetente;
    private ListView list;
    boolean sair = false;

    public ThreadCliente(String r, Socket s, TextArea textArea, ListView list) {
        this.remetente = r;
        this.socket = s;
        this.textArea = textArea;
        this.list = list;
    }

    @Override
    public void run() {
        try {
            while (!sair) {
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Mensagem mensagem = (Mensagem) entrada.readObject();
                Action action = mensagem.getAction();

                switch (action) {
                    case CONNECT:
                        conectar(mensagem);
                        break;
                    case DISCONNECT:
                        desconectar(mensagem);
                        break;
                    case SEND:
                        receberMensagem(mensagem);
                        break;
                    case SEND_ONE:
                        receberMensagem(mensagem);
                        break;
                    case USERS_ONLINE:
                        atualizarUsuarios(mensagem);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ThreadServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void conectar(Mensagem mensagem) {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
    }

    public void desconectar(Mensagem mensagem) throws IOException {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));

        if (mensagem.getRemetente().equals(this.remetente)) {
            this.socket.close();
            this.sair = true;
        }
    }

    public void receberMensagem(Mensagem mensagem) throws IOException {
        Platform.runLater(() -> this.textArea.appendText(mensagem.getRemetente() + " >> " + mensagem.getTexto() + "\n"));
    }

    public void atualizarUsuarios(Mensagem mensagem) {
        ArrayList<String> usuariosOnline = mensagem.getUsuariosOnline();

        Platform.runLater(() -> {
            list.getItems().clear();
            list.getItems().addAll(usuariosOnline);
        });

    }

}
