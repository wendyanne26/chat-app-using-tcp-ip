package weekfive.client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread implements Runnable {

    private Socket socket;
    private static ArrayList<ClientThread> clientThreadList = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;


    public ClientThread(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientThreadList.add(this);
            broadcastMessage( "SERVER: " +clientUsername+" has joined the chat");
        }catch(IOException e){
            closeAll(socket, bufferedWriter,bufferedReader);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

            while (socket.isConnected()) {
                try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
                }
                catch(IOException e){
                    closeAll(socket, bufferedWriter, bufferedReader);
                    break;
                }
        }
    }
    public void broadcastMessage(String messageToBroadcast) {
        for (ClientThread currentClientThread : clientThreadList) {
            try {
                if(!currentClientThread.clientUsername.equals(clientUsername)){
                    currentClientThread.bufferedWriter.write(messageToBroadcast);
                    currentClientThread.bufferedWriter.newLine();
                    currentClientThread.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeAll(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    public void removeClient(){
        clientThreadList.remove(this);
        broadcastMessage("SERVER: "+ clientUsername +" has left the chat");
    }
    public void closeAll(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        removeClient();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}