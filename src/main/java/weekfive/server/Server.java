package weekfive.server;

import weekfive.client.ClientThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                ClientThread clientThread = new ClientThread(socket);

                Thread thread = new Thread(clientThread);
                thread.start();
            }
        }catch (IOException e){
            closeServerSocket();
        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
      ServerSocket serverSocket = new ServerSocket(2222);
      Server server = new Server(serverSocket);
      server.startServer();
    }
}
