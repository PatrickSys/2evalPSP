import tutonio.Client;

import java.io.IOException;



/************************************************************************
 Made by        PatrickSys
 Date           09/03/2022
 Package        PACKAGE_NAME
 Description:
 ************************************************************************/


public class ClientMain {
  public static void main(String[] args) throws IOException {
      Cliente cliente = new Cliente(Connection.HOST, Connection.PORT);
      cliente.run();
  }
}
