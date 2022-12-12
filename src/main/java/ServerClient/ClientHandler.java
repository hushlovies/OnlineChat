package ServerClient;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    //s'occupe de communication entre clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //allow the communication between clients by looping the array
    private Socket socket; //connection client/serv
    private BufferedReader bufferedReader; //lire des données
    private BufferedWriter bufferedWriter; //envoyer des msg
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try{
            this.socket=socket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername= bufferedReader.readLine();
            clientHandlers.add(this);
            afficheMsg("SERVEUR: "+clientUsername+ " est dans le Chat. Dis Bonjour!");
        }catch(IOException e){
            closeThis(socket,bufferedReader,bufferedWriter);
        }
    }

    //everything we write in here is run on a separate thread
    //handling messages
    @Override
    public void run() {
        String msgFromClient; //msg venant du client

        while(socket.isConnected()){
            try {
                msgFromClient=bufferedReader.readLine();
                afficheMsg(msgFromClient);
            }
            catch (IOException e){
                closeThis(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }
    //foeach client, permet d''éviter l'affichage de ton propre msg sur to terminal
    public void afficheMsg(String msgToSend){
        for(ClientHandler clientHandler: clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername))
                {
                    clientHandler.bufferedWriter.write(msgToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush(); //manual flush for the buffer to stop
                }
            }catch(IOException e){
                closeThis(socket,bufferedReader,bufferedWriter);
            }
        }

    }
    //enlever le client dans l'arraylist lorsque le user quitte le chat. On ne pet plus lui envoyer de msg, ni recevoir des msgs
    public void rmClientHandler(){
        clientHandlers.remove(this);
        afficheMsg("SERVEUR: "+clientUsername+ "a quitté le chat ");
    }
    public void closeThis(Socket socket ,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        rmClientHandler();
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket!=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
