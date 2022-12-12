package ServerClient;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class echoclient {
    private Socket sock; //connection client/serv
    private BufferedReader bufferedReader; //lire des données
    private BufferedWriter bufferedWriter; //envoyer des msg
    private String username;

    public echoclient(Socket sock,String username) {
        try{
            this.sock=sock;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            this.bufferedReader=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.username= username;

        }catch(IOException e){
            closeThis(sock,bufferedReader,bufferedWriter);
        }
    }
    public void sendMessage(){
        try{

            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner=new Scanner(System.in);
            while(sock.isConnected()){
                String msgToSend=scanner.nextLine();
                bufferedWriter.write(username +": "+ msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            closeThis(sock,bufferedReader,bufferedWriter);
        }
    }

    public void messageListener(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String GroupMsg;
                while(sock.isConnected()){
                     try{
                         GroupMsg=bufferedReader.readLine();
                         System.out.println(GroupMsg);
                    }catch(IOException e){
                        closeThis(sock,bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();
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
        String username=scanner.nextLine();
        Socket socket=new Socket("localhost",1212);
        echoclient client=new echoclient(socket,username);
        client.messageListener();
        client.sendMessage();

    } // end main
} // end class

