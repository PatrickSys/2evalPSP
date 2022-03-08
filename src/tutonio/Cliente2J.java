import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente2J {
    /**
     *
     *  Segunda clase cliente para comprobar que se pueden conectar 2 clientes diferentes al mismo servidor.
     *
     * */

    public static void main(String[] args) {

        try {
            Scanner sn = new Scanner(System.in);
            sn.useDelimiter("\n");

            Socket sc = new Socket("127.0.0.2", 5000);

            DataInputStream in = new DataInputStream(sc.getInputStream());
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());

            // Leer mensaje del servidor
            String mensaje = in.readUTF();
            System.out.println(mensaje);

            // Escribe el nombre y se lo manda al servidor
            String nombre = sn.next();
            out.writeUTF(nombre);

            // ejecutamos el hilo
            ClienteHilo hilo = new ClienteHilo(in, out);
            hilo.start();
            hilo.join();

        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

}