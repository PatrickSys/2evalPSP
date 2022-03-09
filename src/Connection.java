import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/************************************************************************
 Made by        PatrickSys
 Date           27/02/2022
 Package        PACKAGE_NAME
 Description:
 ************************************************************************/


public class Connection {

    public static final int PORT = 5000; //Puerto para la conexión
    public static final String HOST = "127.0.0.1"; //Host para la conexión
    public static final String SERVERCONN = "server";
    public static final String CLIENTCONN = "client";
    protected String mensajeServidor; //Mensajes entrantes (recibidos) en el servidor
    protected ServerSocket ss; //Socket del servidor
    protected Socket cs; //Socket del cliente
    protected OutputStream salidaServidor, salidaCliente; //Flujo de datos de salida

    public Connection(String typeOfConnection) throws IOException //Constructor
    {
        if(typeOfConnection.equalsIgnoreCase(SERVERCONN))
        {
            ss = new ServerSocket(PORT);//Se crea el socket para el servidor en puerto 1234
            cs = new Socket(); //Socket para el cliente
        }
        else
        {
            cs = new Socket(HOST, PORT); //Socket para el cliente en localhost en puerto 1234
        }
    }
}
