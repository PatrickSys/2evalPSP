import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente extends Thread{
    protected DataInputStream in;
    protected DataOutputStream out;
    protected Socket sc;
    protected Scanner scanner;


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
    public void start() {


        String mensaje;
        boolean salir = false;

        while (!salir) {

            try {
                System.out.println("mensaje a enviar al servidor: \nescribe SALIR para terminar la conexión ");
                mensaje = scanner.next();
                if(mensaje.equalsIgnoreCase("salir")) {
                    salir = true;
                }

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
