import java.io.IOException;

/************************************************************************
 Made by        PatrickSys
 Date           27/02/2022
 Package        PACKAGE_NAME
 Description:
 ************************************************************************/


public class MainClient {

    public static void main(String[] args) throws IOException
    {
        Cliente cli = new Cliente(); //Se crea el cliente

        System.out.println("Iniciando cliente\n");
        cli.startClient(); //Se inicia el cliente
    }
}

