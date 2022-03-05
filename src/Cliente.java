import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Thread{
    private DataInputStream in;
    private DataOutputStream out;
    private Socket sc;
    private Scanner scanner;

    public Cliente(String hostName, int port)  throws IOException {
        this.sc = new Socket(hostName, port);
        this.in = new DataInputStream(this.sc.getInputStream());
        this.out = new DataOutputStream(this.sc.getOutputStream());
        this.scanner = new Scanner(System.in);

    }

//    private void read() throws IOException {
//        System.out.println(this.in.readUTF());
//    }
//
//
//    private void write() throws IOException {
//        String message = scanner.next();
//        this.out.writeUTF(message);
//    }
    @Override
    public void run() {


        String mensaje;

        while (true) {

            try {
                System.out.println("mensaje a enviar al servidor: ");
                mensaje = scanner.next();

                out.writeUTF(mensaje);
                System.out.println("Servidor dice: " + in.readUTF());
            } catch (IOException e) {
               e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        try {
            Scanner scan = new Scanner(System.in);

            Cliente cliente = new Cliente("127.0.0.1", 5000);

            // Leer mensaje del servidor
            System.out.println("Servidor dice: " + cliente.in.readUTF());

            // Escribe el nombre y se lo manda al servidor
            String message = scan.next();
            cliente.out.writeUTF(message);

            // Lee info sobre el protocolo a usar
            System.out.println(cliente.in.readUTF());

            // Empieza el programa cliente
            cliente.start();
            cliente.join();

        } catch (IOException | InterruptedException e) {
           e.printStackTrace();
        }
    }

    private int introduceNumero(Scanner scanner) {
        System.out.println("introduce un número entero: ");
        String next = scanner.next();

        try {
            return Integer.parseInt(next);
        }catch (NumberFormatException e ){
            System.out.println("introduce un numero valido!");
            return introduceNumero(scanner);
        }
    }
}
