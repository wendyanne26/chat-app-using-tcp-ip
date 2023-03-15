package weekfive.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }catch(IOException e){
            closeAll(socket, bufferedWriter, bufferedReader);
        }
    }


    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username +": "+ messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            closeAll(socket, bufferedWriter,bufferedReader);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            String messageFromChat;
            while(socket.isConnected()){
                try{
                    messageFromChat = bufferedReader.readLine();
                    System.out.println(messageFromChat);
                }catch (IOException e){
                    closeAll(socket, bufferedWriter, bufferedReader);
                    }
                }
            }
        }).start();
    }



    public void closeAll(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){
        try{
            if(bufferedReader != null){
                socket.close();
            }
            if(bufferedWriter != null){
                socket.close();
            }
            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 2222);
        Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage();

    }
}
