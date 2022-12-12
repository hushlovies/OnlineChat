package ServerClient;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class echoclient {
    private Socket socket; //connection client/serv
    private BufferedReader bufferedReader; //lire des données
    private BufferedWriter bufferedWriter; //envoyer des msg
    private String pseudo;

    public echoclient(Socket socket,String pseudo) {
        try{
            this.socket=socket;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pseudo= pseudo;

        }catch(IOException e){
            closeThis(socket,bufferedReader,bufferedWriter);
        }
    }
    public void msgSent(){
        try{

            bufferedWriter.write(pseudo);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner=new Scanner(System.in);
            while(socket.isConnected()){
                String msgAenvoyer=scanner.nextLine();
                bufferedWriter.write(pseudo +": "+ msgAenvoyer);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            closeThis(socket,bufferedReader,bufferedWriter);
        }
    }

    public void messageListener(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String GroupMsg;
                while(socket.isConnected()){
                     try{
                         GroupMsg=bufferedReader.readLine();
                         System.out.println(GroupMsg);
                    }catch(IOException e){
                        closeThis(socket,bufferedReader,bufferedWriter);
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
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Entrer ton nom d'utilisateur : ");
        String pseudo=scanner.nextLine();
        Socket socket=new Socket("localhost",1212);
        echoclient client=new echoclient(socket,pseudo);
        client.messageListener();
        client.msgSent();

    } // end main
} // end class

