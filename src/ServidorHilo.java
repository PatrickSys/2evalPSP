import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorHilo extends Thread {

    private Socket sc;
    private DataInputStream in;
    private DataOutputStream out;
    private String nombreCliente;
    private HashMap<String, String> map;

    public ServidorHilo(Socket sc, DataInputStream in, DataOutputStream out, String nombreCliente, HashMap map) {
        this.sc = sc;
        this.in = in;
        this.out = out;
        this.nombreCliente = nombreCliente;
        this.map = map;
    }


    @Override
    public void run() {

        int opcion;
        boolean salir = false;
        while (!salir) {

            try {
                opcion = in.readInt();
                switch (opcion) {
                    case 1:
                        // Recibo el numero aleatorio
                        String clave = in.readUTF();
                        if (map.containsKey(clave)){
                                out.writeUTF("La clave ya existe, vuelve a intentarlo");
                        }else{
                            out.writeUTF("Clave valida, indica el valor");
                            String valor = in.readUTF();
                            map.put(clave, valor);
                            out.writeUTF("clave" + clave + " y valor " + valor + " guardados correctamente");
                        }
                        break;

                    case 2:
                        String claveeliminar = in.readUTF();
                        if (map.containsKey(claveeliminar)){
                            out.writeUTF("La clave " + claveeliminar + " con valor " + map.get(claveeliminar) + " han sido eliminados");
                            map.remove(claveeliminar);
                        }else{
                            out.writeUTF("Dicha clave no existe");
                        }

                        break;

                    case 3:
                        String claveconsulta = in.readUTF();
                       if (map.containsKey(claveconsulta)){
                           out.writeUTF(map.get(claveconsulta));
                       }else{
                           out.writeUTF("Clave no encontrada");
                       }

                        break;

                    case 4:


                        break;

                    case 5:
                        salir = true;
                        break;
                    default:
                        out.writeUTF("Solo numero del 1 al 5");
                }

            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            // Cierro el socket
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Conexion cerrada con el cliente " + nombreCliente);

    }
}
