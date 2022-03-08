import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente2 {
    /**
     *
     *  Segunda clase cliente para comprobar que se pueden conectar 2 clientes diferentes al mismo servidor.
     *
     * */

    public static void main(String[] args) {

        try {
            Scanner scan = new Scanner(System.in);

            Cliente cliente = new Cliente("127.0.0.1", 5000);

            // Leer mensaje del servidor
            System.out.println("Servidor dice: " + cliente.in.readUTF());

            // Escribe el nombre y se lo manda al servidor
            String message = scan.next();
            cliente.out.writeUTF(message);

            // Empieza el programa cliente
            cliente.start();
            cliente.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    }

