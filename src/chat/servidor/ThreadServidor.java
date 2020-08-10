package chat.servidor;

import chat.objeto.Mensagem;
import chat.objeto.Mensagem.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadServidor extends Thread {

    private static Map<String, Socket> clientesMap = new HashMap<>();
    private Socket socket;

    public ThreadServidor(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        boolean sair = false;
        try {
            while (!sair) {
                //Entrada: recebendo mensagem do Cliente
                ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
                Mensagem mensagem = (Mensagem) entrada.readObject();//Recebendo mensagem do Cliente
                Action action = mensagem.getAction();

                switch (action) {
                    case CONNECT:
                        conectar(mensagem);
                        enviarMensagemTodos(mensagem);
                        enviarUsuariosOnline();
                        break;
                    case DISCONNECT:
                        desconectar(mensagem);
                        enviarMensagemTodos(mensagem);
                        enviarUsuariosOnline();
                        sair = true;
                        break;
                    case SEND:
                        enviarMensagemTodos(mensagem);
                        break;
                    case SEND_ONE:
                        enviarMensagemReservada(mensagem);
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
        clientesMap.put(mensagem.getRemetente(), socket);
    }

    public void desconectar(Mensagem mensagem) throws IOException {
        clientesMap.remove(mensagem.getRemetente());
    }

    public void enviarMensagemTodos(Mensagem mensagem) throws IOException {
        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
            saida.writeObject(mensagem);
        }
    }

    public void enviarMensagemReservada(Mensagem mensagem) throws IOException {
        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            if (mensagem.getDestinatario().equals(cliente.getKey())) {
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
                saida.writeObject(mensagem);
            }
        }
    }
    
    public void enviarUsuariosOnline() throws IOException {
        ArrayList<String> usuariosOnline = new ArrayList();
        
        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            usuariosOnline.add(cliente.getKey());
        }
        
        Mensagem mensagem = new Mensagem();
        mensagem.setAction(Action.USERS_ONLINE);
        mensagem.setUsuariosOnline(usuariosOnline);
        
        for (Map.Entry<String, Socket> cliente : clientesMap.entrySet()) {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getValue().getOutputStream());
            saida.writeObject(mensagem);
        }
        
    }

}
