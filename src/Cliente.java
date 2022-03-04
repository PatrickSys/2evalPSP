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
        String strOpcion;
        int opcion =  -1;
        boolean salir = false;

        while (!salir) {

            try {
                System.out.println("1. Almacenar un par clave valor\n2. Eliminar un par clave valor\n3. Consultar el valor asociado a una clave" +
                        "\n4. Modificar el valor asociado a una clave\ns. Salir");

                strOpcion = scanner.next();
                if(strOpcion.equals("s")) {
                    out.writeUTF("salir");
                    salir = true;
                    continue;
                }

                try {
                    opcion = Integer.parseInt(strOpcion);
                }catch(Exception e) {
                    opcion = introduceNumero(scanner);
                }

                out.writeUTF(String.valueOf(opcion));
                switch (opcion) {
                    case 1:

                        System.out.println("Servidor dice: " + in.readUTF());
                        System.out.println("Introduce _clave_valor_");
                        String entrada = scanner.next();

                        out.writeUTF(entrada);
                        System.out.println("Servidor dice: " + in.readUTF());
                        break;
                    case 2:

                        System.out.println("Servidor dice: " + in.readUTF());
                        System.out.println("Que entrada  deseas eliminar? introduce la _clave_:");
                        String claveeliminar = scanner.next();
                        out.writeUTF(claveeliminar);
                        System.out.println(in.readUTF());

                        break;
                    case 3:
                        System.out.println("Servidor dice: " + in.readUTF());
                        System.out.println("Que clave deseas consultar?");
                        String claveconsulta = scanner.next();
                        out.writeUTF(claveconsulta);
                        System.out.println(in.readUTF());
                        break;
                    case 4:
                        System.out.println("Servidor dice: " + in.readUTF());
                        System.out.println("Que entrada deseas modificar? introduce la _clave_ primero");
                        String clavemodificar = scanner.next();
                        out.writeUTF(clavemodificar);
                        System.out.println(in.readUTF());
                        String nuevovalor = scanner.next();
                        out.writeUTF(nuevovalor);
                        System.out.println(in.readUTF());
                        break;
                    default:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);

                }
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