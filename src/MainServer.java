import java.io.IOException;

/************************************************************************
 Made by        PatrickSys
 Date           26/02/2022
 Package        main.java
 Description:
 ************************************************************************/


public class MainServer {

  public static void main(String[] args) throws IOException {
    Servidor serv = new Servidor(); //Se crea el servidor

    System.out.println("Iniciando servidor\n");
    serv.start(); //Se inicia el servidor
  }



}