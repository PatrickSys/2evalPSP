
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Server {

    protected HashMap<String, String> map;
    ServerSocket serverSocket;
    DataInputStream in;
    DataOutputStream out;


    public Server() throws IOException {
        this.serverSocket = new ServerSocket(Connection.PORT);
        this.map = new HashMap<>();
        System.out.println("Servidor iniciado");
    }

    public static void start() {


        try {

                //Instanciamos el servidor que nos servirá para tener la caché en memoria y aceptar las conexiones con los clientes
                Server server = new Server();
                //bucle infinito para crear un socket y esperar a un nuevo cliente
            while (true) {

        Thread t =
            new Thread(
                () -> {
                  Socket clientSocket = null;
                  String nombreCliente = "";
                  boolean exit = false;
                  try {
                    clientSocket = server.serverSocket.accept();

                    server.in = new DataInputStream(clientSocket.getInputStream());
                    server.out = new DataOutputStream(clientSocket.getOutputStream());
                    // Pido al cliente el nombre al cliente
                    server.out.writeUTF("Identifícate: ");
                    nombreCliente = server.in.readUTF();
                    System.out.println("Creada la conexion con el cliente " + nombreCliente);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }

                  while (!exit) {

                    try {

                      String valor;
                      String entrada;

                      // Obtenemos la entrada desde el cliente y separamos el método y clave para
                      // tratarlos

                      entrada = server.in.readUTF();

                      if (entrada.equalsIgnoreCase("salir")) {
                        System.out.println(nombreCliente + " ha abandonado el clúster");
                        server.out.writeUTF(nombreCliente + " hasta luego!");
                        clientSocket.close();
                        exit = true;
                        continue;
                      }
                      String metodo = entrada.split("_")[0];
                      String clave = entrada.split("_")[1];

                      if (metodo.equalsIgnoreCase("MODIFICAR")
                          || metodo.equalsIgnoreCase("GUARDAR")) {
                        valor = entrada.split("_")[2];

                        // Si se quiere modificar, se comprueba primero que la clave exista y luego
                        // se actualiza en el mapa-caché
                        if (metodo.equalsIgnoreCase("MODIFICAR")) {
                          if (!server.map.containsKey(clave)) {
                            server.out.writeUTF("La clave no existe");
                          } else {
                            server.map.put(clave, valor);
                            server.out.writeUTF("Entrada " + clave + ", " + valor + " modificada");
                          }
                        } else // Si no es modificar, será guardar y directamente se guardará
                        {
                          server.map.put(clave, valor);
                          server.out.writeUTF("Entrada " + clave + ", " + valor + " guardada");
                        }
                      }

                      // Si se quiere consultar, se comprobará primero que exista
                      else if (metodo.equalsIgnoreCase("CONSULTAR")) {
                        String valorRecibido = server.map.get(clave);
                        if (valorRecibido == null) {
                          server.out.writeUTF("La clave " + clave + " no existe");
                        } else {
                          server.out.writeUTF(
                              "valor de la entrada con clave " + clave + ": " + valorRecibido);
                        }
                      } else if (metodo.equalsIgnoreCase("ELIMINAR")) {
                        String valorBorrado = server.map.remove(clave);

                        if (valorBorrado == null) {
                          server.out.writeUTF("La entrada con la clave " + clave + " no existe");
                        } else {
                          server.out.writeUTF(
                              " Entrada con clave "
                                  + clave
                                  + " y valor "
                                  + valorBorrado
                                  + " ha sido borrada");
                        }
                      } else {
                        server.out.writeUTF("PROTCOLO INVÁLIDO");
                      }
                      sleep(500);

                    }
                    catch (SocketException e) {
                      System.out.println(nombreCliente + " ha abandonado el clúster");
                      try {
                        System.out.println();
                          assert clientSocket != null;
                          clientSocket.close();
                        exit = true;
                        continue;
                      }
                      catch (IOException ex) {
                      }
                    }
                    catch (ArrayIndexOutOfBoundsException ei) {
                      try {
                        server.out.writeUTF("PROTOCLO INVÁLIDO");
                      }
                      catch (IOException e) {
                          e.printStackTrace();
                      }
                    }
                    catch (IOException | InterruptedException e) {
                      e.printStackTrace();
                    }
                  }
                });
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
