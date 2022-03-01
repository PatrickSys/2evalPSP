import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/************************************************************************
 Made by        PatrickSys
 Date           27/02/2022
 Package        utils
 Description:
 ************************************************************************/


public class Servidor {

    protected Socket cs;
    protected ServerSocket ss;
    protected OutputStream salidaCliente;
    protected OutputStream salidaServidor;
    protected String mensajeServidor;
    public Servidor() throws IOException {
    }

  public void start() throws IOException // Método para iniciar el servidor
      {
          ss = new ServerSocket(1234);

          try {
      System.out.println("Esperando..."); // Esperando conexión

      cs = ss.accept(); // Accept comienza el socket y espera una conexión desde un cliente

      System.out.println("Cliente en línea");


      Map<String, String> cacheMap = new HashMap<>();


      while(true) {

          // Se obtiene el flujo de salida del cliente para enviarle mensajes
          salidaCliente = cs.getOutputStream();
          PrintWriter printCliente = new PrintWriter(salidaCliente, true);

          // Se le envía un mensaje al cliente usando su flujo de salida
          printCliente.write("Petición recibida y aceptada");

          // Se obtiene el flujo entrante desde el cliente
          BufferedReader entrada = new BufferedReader(new InputStreamReader(cs.getInputStream()));

          System.out.println(entrada.readLine());
          System.out.println("reply sent to client");
              System.out.println(mensajeServidor);

              String[] valores = mensajeServidor.split("#");
              String clave = valores[0];
              String valor = valores[1];
              // Mientras haya mensajes desde el cliente


              // Se muestra por pantalla el mensaje recibido
              System.out.println("clave: " + clave + " valor: " + valor);
              cacheMap.put(clave, valor);


      }





    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
        }


}
