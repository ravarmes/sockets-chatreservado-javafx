package chat.objeto;

import java.io.Serializable;
import java.util.ArrayList;

public class Mensagem implements Serializable {

    private String remetente;
    private String destinatario;
    private String texto;
    private Action action;
    private ArrayList<String> usuariosOnline;

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public ArrayList<String> getUsuariosOnline() {
        return usuariosOnline;
    }

    public void setUsuariosOnline(ArrayList<String> usuariosOnline) {
        this.usuariosOnline = usuariosOnline;
    }

    public enum Action {
        CONNECT, DISCONNECT, SEND, SEND_ONE, USERS_ONLINE
    }
}
