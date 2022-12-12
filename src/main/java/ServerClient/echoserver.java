package ServerClient;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class echoserver
{
    private ServerSocket serverSocket;

    public echoserver(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer()
    {
        //for input and output error handling
        try
        {
            //while the socket is open
            while(!serverSocket.isClosed())
            {
                Socket sock=serverSocket.accept(); //program is blocked till a client connects. when a client connects, asocket is returned
                System.out.println("Une nouvelle personne est connectée");
                ClientHandler client=new ClientHandler(sock);
                Thread thread= new Thread(client);
                thread.start();
            }

        } catch (IOException e) {

        }
    }
    //if error, shut down server socket
    public void closeServerSocket(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public static void main(String args[]) throws IOException {
        int port=1212;
        ServerSocket serverSocket = new ServerSocket(port);
        echoserver server = new echoserver(serverSocket);
        server.startServer();
    }

}


