package servidor;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor{
    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Servidor(){}
    void run()
    {
        try{
            //1. creating a server socket
            providerSocket = new ServerSocket(2004, 10);
            //2. Wait for connection
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());
            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful");
            //4. The two parts communicate via the input and output streams
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("client>" + message);
                    respuestas((message.charAt(0)-48) == 50 ? 6 : message.charAt(0)-48);
                }
                catch(ClassNotFoundException classnot){
                    System.err.println("Data received in unknown format");
                }
            }while(!message.equals("bye"));
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                providerSocket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    void respuestas(int n){
        if(n == 1)
            randomFortuna();
        
        else{
            ArrayList<String> resp = new ArrayList();
            resp.add("Mi nombre es Servidor-24");
            resp.add("Mis hobbies son programar, ver anime y jugar videojuegos");
            resp.add("Mis animes preferidos son Yu Yu Hakusho, Naruto, Dragon Ball, Tokio Ghoul y Los Caballeros del Zodiaco");
            resp.add("Mis videojuegos favoritos son Kingdom Hearts, The Legend of Zelda y Super Smash Brothers");
            resp.add("bye");
            
            sendMessage(resp.get(n-2));
        }
    }
    
    void randomFortuna(){
        ArrayList<String> fort = new ArrayList<>();
        fort.add("Te va a caer un piano encima");
        fort.add("Hoy vas a ganar la lotería");
        fort.add("Mañana ganarás un boleto a crucero todo incluido gratis");
        fort.add("Te vas a ganar un playstation 4");
        fort.add("Según tu horóscopo, sos la persona más desafortunada del mundo");
        int n = (int)(Math.random()*100%fort.size());
        sendMessage(fort.get(n));
    }
    public static void main(String args[])
    {
        Servidor server = new Servidor();
        while(true){
            server.run();
        }
    }
}