
import Protocols.RequestType;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import Exception.*;
import static Protocols.RequestType.*;
import static java.lang.Thread.sleep;

public class Server {

    // variables de clase
    protected Map<String, String> cacheMap;
    protected Map<RequestType, BiConsumer<Map.Entry, DataOutputStream>> callbackMap;
    ServerSocket serverSocket;


    public Server() throws IOException {
        this.serverSocket = new ServerSocket(Connection.PORT);
        this.cacheMap = new HashMap<>();
    }

    // cierra la conexión con el cliente
    private void terminateConnection(String nombreCliente, Socket clientSocket, DataOutputStream out) throws IOException {
        System.out.println(nombreCliente + " ha abandonado el clúster");
        out.writeUTF(nombreCliente + " hasta luego!");
        clientSocket.close();
    }

    private void writeOutException(String message, DataOutputStream out) {
        try {
            out.writeUTF(message);
        } catch (IOException ignored) {

        }
    }

    /**
     * Tenemos un mapa para gestionar las diferentes "request" siguiendo el protocolo definido
     * Según la request del cliente, hará lo propio
     */
    private void initCallbackMap() {
        this.callbackMap = new HashMap<>();
        this.callbackMap.put(SAVE_REQUEST, (entry, out) -> {
            try {
                insertEntry(entry,out );
            } catch (IOException | EntryNotFoundException e) {
               writeOutException("La entrada con clave " + e.getMessage() + " ya existe", out);
            }
        });
        this.callbackMap.put(GET_REQUEST, (entry, out) -> {
            try {
                getEntry(entry, out );
            } catch (IOException | EntryNotFoundException e) {
                writeOutException("La entrada con clave " + e.getMessage() + " no existe", out);
            }
        });
        this.callbackMap.put(DELETE_REQUEST, (entry, out) -> {
            try {
                deleteEntry(entry, out );
            } catch (IOException | EntryNotFoundException e) {
                writeOutException("La entrada con clave " + e.getMessage() + " no existe", out);
            }
        });
        this.callbackMap.put(UPDATE_REQUEST, (entry, out) -> {
            try {
                updateEntry(entry,out );
            } catch (IOException | EntryNotFoundException e) {
                writeOutException("La entrada con clave " + e.getMessage() + " no existe", out);
            }
        });
    }

    /**
     * Métodos para gestionar la caché
     * @param entry la entrada parseada por la request
     * @param out el outputstream a escribir la respuesta si es válida
     * @throws IOException si el outputstream está cerrado
     * @throws EntryNotFoundException si la entrada consultada no existe
     */
    private void deleteEntry(Map.Entry<String, String> entry,  DataOutputStream out) throws IOException, EntryNotFoundException {

        String value = this.cacheMap.remove(entry.getKey());
        if (value == null) throw new EntryNotFoundException(entry.getKey());
        out.writeUTF("Entrada " + entry.getKey() + ", " + value + " borrada con éxito");
    }

    private void getEntry(Map.Entry<String, String> entry, DataOutputStream out) throws IOException, EntryNotFoundException {
        String value =  this.cacheMap.get(entry.getKey());
        if(value == null) throw new EntryNotFoundException(entry.getKey());
        out.writeUTF("Entrada encontrada: " + entry.getKey() + ", " + value);
    }

    private void updateEntry(Map.Entry<String, String> entry, DataOutputStream out) throws IOException, EntryNotFoundException {
        if(!this.cacheMap.containsKey(entry.getKey())) throw new EntryNotFoundException(entry.getKey());
        this.cacheMap.put(entry.getKey(), entry.getValue());
        out.writeUTF("Entrada " + entry.getKey() + ", " + entry.getValue() + " actualizada con éxito");
    }

    private void insertEntry(Map.Entry<String, String> entry, DataOutputStream out) throws IOException, EntryNotFoundException {
       if(this.cacheMap.containsKey(entry.getKey())) throw  new EntryNotFoundException(entry.getKey());
        this.cacheMap.put(entry.getKey(), entry.getValue());
        out.writeUTF("Entrada creada con éxito");
    }


    public void start() {
        System.out.println("Servidor iniciado");
        this.initCallbackMap();

        //bucle infinito para crear un hilo y esperar a un nuevo cliente entrante
        while (true) {

            Thread t =
                    new Thread(
                            () -> {


                        try {
                            Socket clientSocket;
                            String nombreCliente = "";
                            DataInputStream in;
                            DataOutputStream out;
                            boolean exit = false;

                            // inicializamos streams de entrada y salida
                            clientSocket = this.serverSocket.accept();
                            in = new DataInputStream(clientSocket.getInputStream());
                            out = new DataOutputStream(clientSocket.getOutputStream());


                            // Pido al cliente el nombre al cliente
                            out.writeUTF("Identifícate: ");
                            nombreCliente = in.readUTF();
                            System.out.println("Creada la conexion con el cliente " + nombreCliente);


                        while (!exit) {

                            try {

                                String entrada;
                                RequestType requestType = null;
                                String requestMethod = "";
                                // Obtenemos la entrada desde el cliente y separamos el método y clave para
                                // tratarlos

                                entrada = in.readUTF();

                                if (entrada.equalsIgnoreCase("salir")) {
                                    this.terminateConnection(nombreCliente, clientSocket, out);
                                    exit = true;
                                    continue;
                                }

                                try {
                                    requestMethod = entrada.split("#")[0];
                                    requestType = RequestType.getRequestType(requestMethod);
                                }catch(BadRequestException e) {
                                    out.writeUTF("PROTOCOLO INVALIDO");
                                }


                                // obtenemos el BiConsumer para realizar el callback según el tipo de Request que haya realizado el cliente
                                BiConsumer<Map.Entry, DataOutputStream> consum = this.callbackMap.get(requestType);

                                try {
                                    //lo ejecuta con los parámetros que le hayamos pasado
                                    consum.accept(parseRequest(entrada), out);
                                }
                                catch (Exception e) {
                                    out.writeUTF("PROTOCOLO INVALIDO");
                                }

                            }
                            // dará la excepción si un cliente cierra la conexión unilateralmente
                            catch (SocketException e) {
                                System.out.println(nombreCliente + " ha abandonado el clúster");
                                try {
                                    System.out.println();
                                    assert clientSocket != null;
                                    clientSocket.close();
                                    exit = true;
                                    continue;
                                } catch (IOException ex) {
                                }
                            } catch (ArrayIndexOutOfBoundsException ei) {
                                try {
                                    out.writeUTF("PROTOCOLO INVÁLIDO");
                                }
                                catch (IOException ignored) {
                                }
                            }
                        }


                    }catch (Exception e) {
                        }
        });
        t.start();
        }
    }


}
