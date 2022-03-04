
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteHilo extends Thread {

    private DataInputStream in;
    private DataOutputStream out;

    public ClienteHilo(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {

        Scanner sn = new Scanner(System.in);

        String mensaje;
        int opcion = 0;
        boolean salir = false;

        while (!salir) {

            try {
                System.out.println("1. Almacenar un par clave valor");
                System.out.println("2. Eliminar un par clave valor");
                System.out.println("3. Consultar el valor asoaciado a una clave");
                System.out.println("4. Modificar el valor asociado a una clave");
                System.out.println("5. Salir");

                opcion = sn.nextInt();
                out.writeInt(opcion);

                switch (opcion) {
                    case 1:

                        System.out.println("Que clave deseas enviar?");
                        String clave = sn.next();
                        out.writeUTF(clave);

                        System.out.println(in.readUTF());
                        System.out.println("Que valor deseas enviar?");
                        String valor = sn.next();
                        out.writeUTF(valor);
                        mensaje = in.readUTF();
                        System.out.println(mensaje);
                        break;
                    case 2:

                        System.out.println("Que par clave valor deseas eliminar? introduce la clave:");
                        String claveeliminar = sn.next();
                        out.writeUTF(claveeliminar);
                        System.out.println(in.readUTF());

                        break;
                    case 3:
                        System.out.println("Que clave deseas consultar?");
                        String claveconsulta = sn.next();
                        out.writeUTF(claveconsulta);
                        System.out.println(in.readUTF());
                        break;
                    case 4:
                        System.out.println("Que clave valor deseas modificar?");
                        String clavemodificar = sn.next();
                        out.writeUTF(clavemodificar);
                        System.out.println(in.readUTF());
                        String nuevovalor = sn.next();
                        out.writeUTF(nuevovalor);
                        System.out.println(in.readUTF());
                        break;
                    case 5:
                        salir = true;
                        break;
                    default:
                        mensaje = in.readUTF();
                        System.out.println(mensaje);

                }
            } catch (IOException ex) {
                Logger.getLogger(ClienteHilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
