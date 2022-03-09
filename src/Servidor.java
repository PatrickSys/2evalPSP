
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Servidor {

    protected HashMap<String, String> map;
    ServerSocket serverSocket;
    DataInputStream in;
    DataOutputStream out;


    public Servidor() throws IOException {
        this.serverSocket = new ServerSocket(5000);
        this.map = new HashMap<>();
        System.out.println("Servidor iniciado");
    }

    public static void main(String[] args) {


        try {

                //Instanciamos el servidor que nos servirá para tener la caché en memoria y aceptar las conexiones con los clientes
                Servidor servidor = new Servidor();
                //bucle infinito para crear un socket y esperar a un nuevo cliente
            while (true) {


                Thread t = new Thread(() -> {
                    Socket clientSocket = null;
                    String nombreCliente = "";
                    try {
                        clientSocket = servidor.serverSocket.accept();

                    servidor.in = new DataInputStream(clientSocket.getInputStream());
                    servidor.out = new DataOutputStream(clientSocket.getOutputStream());
                    // Pido al cliente el nombre al cliente
                    servidor.out.writeUTF("Identifícate: ");
                    nombreCliente = servidor.in.readUTF();
                    System.out.println("Creada la conexion con el cliente " + nombreCliente);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    while (!clientSocket.isClosed()) {

                        try {

                            String valor;
                            String entrada;

                            // Obtenemos la entrada desde el cliente y separamos el método y clave para tratarlos

                            entrada = servidor.in.readUTF();


                            if(entrada.equalsIgnoreCase("salir")) {
                                System.out.println(nombreCliente + " ha abandonado el clúster");
                                servidor.out.writeUTF(nombreCliente + " hasta luego!");
                                clientSocket.close();
                                continue;
                            }
                            String metodo = entrada.split("_")[0];
                            String clave = entrada.split("_")[1];


                            if(metodo.equalsIgnoreCase("MODIFICAR") || metodo.equalsIgnoreCase("GUARDAR"))
                            {
                                valor = entrada.split("_")[2];

                                // Si se quiere modificar, se comprueba primero que la clave exista y luego se actualiza en el mapa-caché
                                if(metodo.equalsIgnoreCase("MODIFICAR")) {
                                    if(!servidor.map.containsKey(clave)) {
                                       servidor.out.writeUTF("La clave no existe");
                                    }
                                    else{
                                        servidor.map.put(clave, valor);
                                        servidor.out.writeUTF("Entrada " + clave + ", " + valor + " modificada");

                                    }
                                }
                                else // Si no es modificar, será guardar y directamente se guardará
                                {
                                    servidor.map.put(clave, valor);
                                    servidor.out.writeUTF("Entrada " + clave + ", " + valor + " guardada");
                                }
                            }

                            // Si se quiere consultar, se comprobará primero que exista
                            else if(metodo.equalsIgnoreCase("CONSULTAR")) {
                                String valorRecibido = servidor.map.get(clave);
                                if(valorRecibido == null ) {
                                    servidor.out.writeUTF("La clave " + clave + " no existe");
                                }
                                else {
                                    servidor.out.writeUTF("valor de la entrada con clave " + clave + ": " + valorRecibido);
                                }
                            }

                            else if(metodo.equalsIgnoreCase("ELIMINAR")) {
                                String valorBorrado = servidor.map.remove(clave);

                                if(valorBorrado == null) {
                                    servidor.out.writeUTF("La entrada con la clave " + clave + " no existe");
                                }
                                else {
                                    servidor.out.writeUTF(" Entrada con clave " + clave + " y valor " + valorBorrado + " ha sido borrada");
                                }
                            }
                            else {
                                servidor.out.writeUTF("PROTCOLO INVÁLIDO");
                            }
                            sleep(500);

                        }catch (SocketException e) {
                            System.out.println(nombreCliente + " ha abandonado el clúster");
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
                            }
                        }
                        catch(ArrayIndexOutOfBoundsException ei) {
                            try {
                                servidor.out.writeUTF("PROTOCLO INVÁLIDO");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        catch(IOException | InterruptedException e) {
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
