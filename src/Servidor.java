
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Servidor {

    protected HashMap<String, String> map;
    ServerSocket serverSocket;
    Socket sc;

    DataInputStream in;
    DataOutputStream out;


    public Servidor(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.map = new HashMap<>();
        System.out.println("Servidor iniciado");
        this.sc = serverSocket.accept();
        in = new DataInputStream(sc.getInputStream());
        out = new DataOutputStream(sc.getOutputStream());
    }

    public static void main(String[] args) {


        try {

            Servidor servidor = new Servidor(5000);

            while(true){

                // Espero la conexion del cliente

                // Pido al cliente el nombre al cliente
                servidor.out.writeUTF("Identifícate: ");
                String nombreCliente = servidor.in.readUTF();

                servidor.out.writeUTF("Protocolo a usar: _clave_valor_");
                System.out.println("Creada la conexion con el cliente " + nombreCliente);

                // Arrancamos el servidor
                servidor.run(nombreCliente);

                sleep(500);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void run(String nombreCliente) {

        String strOpcion;
        int opcion;
        boolean salir = false;
        while (!salir) {

            try {
                strOpcion = in.readUTF();
                System.out.println(strOpcion+ " opcion");
                if(strOpcion.equals("s")) {
                    salir = true;
                    continue;
                }

                opcion = Integer.parseInt(strOpcion);
                switch (opcion) {
                    case 1:
                        // Recibo el numero aleatorio
                        out.writeUTF("Protocolo: _clave_valor_");
                        String entrada = in.readUTF();
                        String clave = entrada.split("_")[1];
                        String valor = entrada.split("_")[2];
                        if (map.containsKey(clave)){
                            out.writeUTF("La clave ya existe, vuelve a intentarlo");
                        }else{
                            map.put(clave, valor);
                            out.writeUTF("Entrada con clave " + clave + " y valor " + valor + " guardados correctamente");
                        }
                        break;

                    case 2:
                        out.writeUTF("Protocolo: _clave_");
                        String claveeliminar = in.readUTF().split("_")[1];
                        if (map.containsKey(claveeliminar))
                        {
                            map.remove(claveeliminar);
                            out.writeUTF("La entrada ha sido borrada");
                        }

                        else if(!map.containsKey(claveeliminar))
                            {
                            out.writeUTF("Dicha clave no existe");
                        }

                        break;

                    case 3:
                        out.writeUTF("Protocolo: _clave_");
                        String claveconsulta = in.readUTF().split("_")[1];
                        out.writeUTF(map.getOrDefault(claveconsulta, "Clave no encontrada"));
                        break;

                    case 4:
                        out.writeUTF("Protocolo: _clave_");
                        String clavemodificar = in.readUTF();
                        if (map.containsKey(clavemodificar))
                        {
                            out.writeUTF("indique un nuevo valor para la clave: ");
                            String valormodificado = in.readUTF();
                            map.replace(clavemodificar, valormodificado);
                            out.writeUTF("Modificado con exito, la clave " + clavemodificar + " tiene como nuevo valor " + valormodificado);
                        }
                        else if(!map.containsKey(clavemodificar))
                            {
                            out.writeUTF("Clave no encontrada");
                        }

                        break;

                    case 5:
                        salir = true;
                        break;
                    default:
                        out.writeUTF("Solo numero del 1 al 4");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // Cierro el socket
            sc.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

        System.out.println("Conexion cerrada con el cliente " + nombreCliente);

    }



}
