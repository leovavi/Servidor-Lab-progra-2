package cliente;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Cliente(){}
    void run()
    {
        try{
            //1. creating a socket to connect to the server
            requestSocket = new Socket("localhost", 2004);
            System.out.println("Connected to localhost in port 2004");
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            //3: Communicating with the server
            do{
                try{
                    message = (String)in.readObject();
                    System.out.println("server>" + message);
                    Scanner sc = new Scanner(System.in);
                    System.out.println("\nMENU\n-----\n1.Preguntar fortuna\n2.Preguntar nombre\n3.Preguntar hobbies");
                    System.out.println("4.Preguntar animes preferidos\n5.Preguntar videojuegos favoritos\n6.Despedirse");
                    System.out.print("\nIngrese una opción: ");
                    int opc = sc.nextInt();
                    System.out.println();
                    opcion(opc);
                }
                catch(ClassNotFoundException classNot){
                    System.err.println("data received in unknown format");
                }
            }while(!message.equals("bye"));
        }
        catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            //4: Closing connection
            try{
                in.close();
                out.close();
                requestSocket.close();
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
            System.out.println("client>" + msg);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    
    void opcion(int n){
        ArrayList<String> opc = new ArrayList<>();
        opc.add("1- Cual es mi fortuna?");
        opc.add("2- Cual es tu nombre?");
        opc.add("3- Cuales son tus hobbies?");
        opc.add("4- Cuales son tus animes preferidos?");
        opc.add("5- Que videojuegos son tus favoritos?");
        opc.add("6- bye");
        
        sendMessage(opc.get(n-1).charAt(0) == '6' ? "bye" : opc.get(n-1));
    }
    
    public static void main(String args[])
    {
        Cliente client = new Cliente();
        client.run();
    }
}