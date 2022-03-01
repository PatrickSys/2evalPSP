import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {

    protected Socket cs;
    protected OutputStream salidaServidor;

    public Cliente() throws IOException {
    }

  /**
   * Segunda clase cliente para comprobar que se pueden conectar 2 clientes diferentes al mismo
   * servidor.
   */
  public void startClient() //Método para iniciar el cliente
  {
      try
      {

          cs = new Socket("localhost", 1234); //Socket para el cliente en localhost en puerto 1234

          //Flujo de datos hacia el servidor
          Scanner scanner = new Scanner(System.in);
          while(true) {
              OutputStream os = cs.getOutputStream();
              InputStream is = cs.getInputStream();
              PrintWriter pw = new PrintWriter(os, true);
              pw.println("Hi Server");

              InputStreamReader isr = new InputStreamReader(is);
              BufferedReader br = new BufferedReader(isr);
              System.out.println("Waiting for server reply..");
              System.out.println(br.readLine());
              System.out.println("Got reply from server..");

          System.out.println("""
                  Elige una opción:
                   1.Almacenar un par clave-valor
                   2.Eliminar un par clave-valor
                   3.Consultar el valor asociado a una clave
                   4.Modificar el valor asociado a una clave
                   0.Terminar la conexión""");
          int choice = scanner.nextInt();



          switch (choice) {
              case 1 -> {
                  System.out.println("Clave a almacenar: ");
                  String clave = scanner.next();
                  System.out.println("Valor a almacenar: ");
                  String valor = scanner.next();

                  // tenemos clave valor
                  salidaServidor.write(clave.getBytes(StandardCharsets.UTF_8));
                  salidaServidor.write('#');
                  salidaServidor.write(valor.getBytes(StandardCharsets.UTF_8));
                  salidaServidor.flush();

              }
              case 2 -> {
                  System.out.println("Clave de la entrada a borrar: ");
                  String clave = scanner.nextLine();
                  salidaServidor.write(clave.getBytes(StandardCharsets.UTF_8));
              }
              case 3 -> {
                  System.out.println("Que clave deseas consultar?");
                  String clave = scanner.nextLine();
                  salidaServidor.write(clave.getBytes(StandardCharsets.UTF_8));
              }
              case 4 -> {
                  System.out.println("Que clave valor deseas modificar?");
                  String clave = scanner.nextLine();
                  salidaServidor.write(clave.getBytes(StandardCharsets.UTF_8));
                  String nuevovalor = scanner.nextLine();
                  salidaServidor.write(nuevovalor.getBytes(StandardCharsets.UTF_8));
              }
              default -> {
              }

          }

          }
          //Fin de la conexión

      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
  }
  public static void main(String[] args) {}
}
