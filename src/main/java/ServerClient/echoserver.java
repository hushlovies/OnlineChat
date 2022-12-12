package ServerClient;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class echoserver
{
    private ServerSocket serverSocket;

    public echoserver(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startingServer()
    {
        //for input and output error handling
        try
        {
            //while the socket is open
            while(!serverSocket.isClosed())
            {
                Socket serv=serverSocket.accept(); //program is blocked till a client connects. when a client connects, asocket is returned
                System.out.println("Une nouvelle personne est connectée");
                ClientHandler client=new ClientHandler(serv);

                Thread thread= new Thread(client);
                thread.start();
            }

        } catch (IOException e) {

        }
    }
    //if error, shut down server socket
    public void classServerSocket(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
            }
        }catch(IOException e){
            throw new RuntimeException(e);
        }

    }
    public static void main(String args[]) throws IOException {

        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        echoserver server = new echoserver(serverSocket);
        server.startingServer();
    }

}


