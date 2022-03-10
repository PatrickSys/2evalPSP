package Protocols;

import java.util.AbstractMap;
import java.util.Map;
import Exception.BadRequestException;

/************************************************************************
 Made by        PatrickSys
 Date           03/03/2022
 Package        Protocols
 Description:
 ************************************************************************/
public enum RequestType {

    // Tipos de request definidos según protocolo
    SAVE_REQUEST("GUARDAR#key#value#"),
    DELETE_REQUEST("ELIMINAR#key"),
    GET_REQUEST("CONSULTAR#key"),
    UPDATE_REQUEST("MODIFICAR#key#value#");

    private final String requestType;

    RequestType(final String requestType) {
        this.requestType = requestType;
    }

    /**
     * Recoge una request de tipo "PROTOCOLO#CLAVE#VALOR# y de ella devuelve la entrada parseada que será tratada en el servidor
     * @param request
     * @return
     */
    public static Map.Entry<String, String> parseRequest(String request) {

        String requestMethod = request.split("#")[0];
        String key = request.split("#")[1];
        String value;
        // Si es delete o GET no tendrá valor
        if(requestMethod.equalsIgnoreCase(DELETE_REQUEST.getRequestMethod()) || requestMethod.equalsIgnoreCase(GET_REQUEST.getRequestMethod()))  {
            return new AbstractMap.SimpleEntry<>(key, null);
        }

        // Si no es ni delete ni get, tendrá valor
        try {
            value = request.split("#")[2];
        }
        //Si no es ni GET ni DELETE pero se ha "colado" la request hasta aquí, lanzará una excepción
        catch(IllegalArgumentException e) {
            e.printStackTrace();
            return new AbstractMap.SimpleEntry<>(new IllegalArgumentException("PROTOCOLO INVALeIDO").getMessage(), null);
        }
        return new AbstractMap.SimpleEntry<>(key, value);

    }

    @Override
    public String toString() {
        return this.requestType;
    }

    // obtenemos por ej "UPDATE" a partir de la update request
    public String getRequestMethod() {
        return this.requestType.split("#")[0];
    }

    // Según lo que haya pasado el cliente antes del primer hashtag separador, devolverá uno u otro requestType
    public static RequestType getRequestType(String requestType) {
        String method = requestType.split("#")[0];
        RequestType returnType;

        switch(method.toUpperCase()) {
            case "GUARDAR" :
                returnType = SAVE_REQUEST;
                break;
            case "CONSULTAR" :
                returnType =  GET_REQUEST;
                break;
            case "MODIFICAR" :
                returnType = UPDATE_REQUEST;
                break;
            case "ELIMINAR":
                returnType = DELETE_REQUEST;
                break;
            default:
                throw new BadRequestException();
        }
        return returnType;
    }
}
