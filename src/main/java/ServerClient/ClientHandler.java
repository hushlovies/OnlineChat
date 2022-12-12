package ServerClient;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    //s'occupe de communication entre clients
    public static ArrayList<ClientHandler> clients = new ArrayList<>(); //allow the communication between clients by looping the array
    private Socket sock; //connection client/serv
    private BufferedReader bufferReader; //lire des données
    private BufferedWriter bufferWriter; //envoyer des msg
    private String nomClient;

    public ClientHandler(Socket sock) {
        try{
            this.sock=sock;
            this.bufferWriter=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.bufferReader=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.nomClient= bufferReader.readLine();
            afficheMsg("SERVEUR: "+nomClient+ "est dans le Chat. Dis Bonjour!");
            clients.add(this);
        }catch(IOException e){
            closeThis(sock,bufferReader,bufferWriter);
        }
    }

    //everything we write in here is run on a separate thread
    //handling messages
    @Override
    public void run() {
        String msgClient; //msg venant du client

       do{
            try {
                msgClient=bufferReader.readLine();
                afficheMsg(msgClient);
            }
            catch (IOException e){
                closeThis(sock,bufferReader,bufferWriter);
                break;
            }
        }while(sock.isConnected());
    }
    public void afficheMsg(String msgAenvoyer){
        for(ClientHandler clientHandler: clients){
            try{
                if(clientHandler.nomClient.equals(nomClient)){
                    clientHandler.bufferWriter.write(msgAenvoyer);
                    clientHandler.bufferWriter.newLine();
                    clientHandler.bufferWriter.flush();
                }
            }catch(IOException e){
                closeThis(sock,bufferReader,bufferWriter);
            }
        }

    }
    //enlever le client dans l'arraylist lorsque le user quitte le chat. On ne pet plus lui envoyer de msg, ni recevoir des msgs
    public void rmClientHandler(){
        afficheMsg("SERVEUR: "+nomClient+ "a quitté le chat :'(");
    }
    public void closeThis(Socket sock ,BufferedReader bufferReader,BufferedWriter bufferWriter){
        rmClientHandler();
        try{
            if(bufferReader!=null){
                bufferReader.close();
            }if(bufferWriter!=null){
                bufferWriter.close();
            }if(sock!=null){
                sock.close();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


}
