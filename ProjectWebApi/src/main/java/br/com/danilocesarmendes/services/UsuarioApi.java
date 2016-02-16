package br.com.danilocesarmendes.services;

import br.com.danilocesarmendes.dao.UsuarioDAO;
import br.com.danilocesarmendes.model.Usuario;
import br.com.danilocesarmendes.util.Mensagem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import com.google.gson.Gson;

@Path("/usuario")
public class UsuarioApi {

    private String json;

    @GET
    @Produces("application/json")
    public Response getAllUsuarios() throws JSONException, SQLException {

        try {

            List<Usuario> arrUsuarios = new ArrayList<Usuario>();
            UsuarioDAO userDao = new UsuarioDAO();
            arrUsuarios = userDao.getAllUsuarios();
            Gson gson = new Gson();
            json = gson.toJson(arrUsuarios);
            String result = "" + json;
            return Response.status(200).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").entity(result).build();

        } catch (Throwable e) {

            return Response.status(404).entity("Error").build();

        }

    }

    @Path("{user_id}")
    @GET
    @Produces("application/json")
    public Response getUsuario(@PathParam("user_id") Integer id) throws Throwable {

        //String user_login, String user_senha 
        try {

            Usuario usuario = new Usuario();
            UsuarioDAO usuarioDao = new UsuarioDAO();
            usuario = usuarioDao.getUsuario(id);
            Gson gson = new Gson();
            json = gson.toJson(usuario);
            String result = "" + json;
            return Response.status(200).entity(result).build();

        } catch (Throwable e) {

            return Response.status(404).entity("Error").build();

        }

    }

    @Path("{user_login}/{user_senha}")
    @GET
    @Produces("application/json")
    public Response getUsuario(@PathParam("user_login") String user_login, @PathParam("user_senha") String user_senha) throws Throwable {

        //String user_login, String user_senha 
        try {

            Usuario usuario = new Usuario();
            UsuarioDAO usuarioDao = new UsuarioDAO();
            usuario = usuarioDao.getValidaUsuario(user_login, user_senha);
            if (usuario == null) {
                Mensagem mensagem = new Mensagem();
                mensagem.setCod_msg(-1);
                mensagem.setMensagem("Usu�rio ou senha inv�lidos!");

                Gson gson = new Gson();
                json = gson.toJson(mensagem);

                return Response.status(404).entity(json).build();
            }
            Gson gson = new Gson();
            json = gson.toJson(usuario);
            String result = "" + json;
            return Response.status(200).entity(result).build();

        } catch (Throwable e) {

            return Response.status(404).entity("Error").build();

        }

    }

}
