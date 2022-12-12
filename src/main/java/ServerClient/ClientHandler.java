package ServerClient;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    //s'occupe de communication entre clients
    public static ArrayList<ClientHandler> clients = new ArrayList<>(); //allow the communication between clients by looping the array
    private Socket sock; //connection client/serv
    private BufferedReader bufferedReader; //lire des données
    private BufferedWriter bufferedWriter; //envoyer des msg
    private String nomClient;

    public ClientHandler(Socket sock) {
        try{
            this.sock=sock;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.nomClient= bufferedReader.readLine();
            clients.add(this);
            afficheMsg("SERVEUR: "+nomClient+ " est dans le Chat. Dis Bonjour!");
        }catch(IOException e){
            closeThis(sock,bufferedReader,bufferedWriter);
        }
    }

    //everything we write in here is run on a separate thread
    //handling messages
    @Override
    public void run() {
        String msgDeClient; //msg venant du client

        while(sock.isConnected()){
            try {
                msgDeClient=bufferedReader.readLine();
                afficheMsg(msgDeClient);
            }
            catch (IOException e){
                closeThis(sock,bufferedReader,bufferedWriter);
                break;
            }
        }
    }
    //foeach client, permet d''éviter l'affichage de ton propre msg sur to terminal
    public void afficheMsg(String msgToSend){
        for(ClientHandler client: clients){
            try{
                if(!client.nomClient.equals(nomClient))
                {
                    client.bufferedWriter.write(msgToSend);
                    client.bufferedWriter.newLine();
                    client.bufferedWriter.flush(); //manual flush for the buffer to stop
                }
            }catch(IOException e){
                closeThis(sock,bufferedReader,bufferedWriter);
            }
        }

    }
    //enlever le client dans l'arraylist lorsque le user quitte le chat. On ne pet plus lui envoyer de msg, ni recevoir des msgs
    public void rmClientHandler(){
        clients.remove(this);
        afficheMsg("SERVEUR: "+nomClient+ " a quitté le chat ");
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
