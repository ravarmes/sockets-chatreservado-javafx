package chat.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    
    static int qtdClientes = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(54321);
        System.out.println("A porta 54321 foi aberta!");
        System.out.println("Servidor, com Thread, esperando receber mensagens de vários clientes...");
        while (true) {
            Socket socket;
            socket = serverSocket.accept();
            
            qtdClientes++;
            
            //Mostrando endereço IP do cliente conectado
            System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");

            ThreadServidor thread = new ThreadServidor(socket);
            thread.setName("Thread Servidor: " + String.valueOf(qtdClientes));
            thread.start();
        }
    }
}
