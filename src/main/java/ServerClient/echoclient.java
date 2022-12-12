package ServerClient;
import java.io.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class echoclient {
    private Socket sock; //connection client/serv
    private BufferedReader bufferReader; //lire des données
    private BufferedWriter bufferWriter; //envoyer des msg
    private String nomClient;

    public echoclient(Socket sock,String nomClient) {
        try{
            this.sock=sock;
            this.bufferWriter=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.bufferReader=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.nomClient= nomClient;

        }catch(IOException e){
            closeThis(sock,bufferReader,bufferWriter);
        }
    }
    public void msgSent(){
        try{

            bufferWriter.write(nomClient);
            bufferWriter.newLine();
            bufferWriter.flush();

            Scanner scanner=new Scanner(System.in);
            while(sock.isConnected()){
                String messageAenvoyer=scanner.nextLine();
                bufferWriter.write(nomClient +": "+ messageAenvoyer);
                bufferWriter.newLine();
                bufferWriter.flush();
            }
        }catch(IOException e){
            closeThis(sock,bufferReader,bufferWriter);
        }
    }

    public void messageListener(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String GroupMsg;
                while(sock.isConnected()){
                     try{
                         GroupMsg=bufferReader.readLine();
                         System.out.println(GroupMsg);
                    }catch(IOException e){
                        closeThis(sock,bufferReader,bufferWriter);
                    }
                }
            }
        });
    }
    public void closeThis(Socket sock ,BufferedReader bufferReader,BufferedWriter bufferWriter){

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

    public static void main(String args[]) throws IOException {
        int port = 8080;
        Scanner scanner=new Scanner(System.in);
        System.out.println("Entrer ton nom d'utilisateur : ");
        String nomClient=scanner.nextLine();
        Socket sock=new Socket("localhost",port);
        echoclient client=new echoclient(sock,nomClient);
        client.messageListener();
        client.msgSent();

    } // end main
} // end class

