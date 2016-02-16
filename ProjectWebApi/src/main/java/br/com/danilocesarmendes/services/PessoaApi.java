package br.com.danilocesarmendes.services;

import br.com.danilocesarmendes.dao.PessoaDAO;
import br.com.danilocesarmendes.model.Pessoa;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import org.json.JSONObject;

@Path("/pessoas")
public class PessoaApi {

    @Context
    protected UriInfo uriInfo;

    private String json;

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response salvar(String objJson) {
        try {
            Pessoa pessoa = new ObjectMapper().readValue(objJson, Pessoa.class);
            PessoaDAO pessoaDAO = new PessoaDAO();
            pessoa = pessoaDAO.save(pessoa);
            URI uri = uriInfo.getAbsolutePathBuilder().path(pessoa.getId().toString()).build();
            return Response.created(uri).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(404).entity("Error").build();
        }
    }

    @Path("{id}")
    @DELETE
    public Response excluir(@PathParam("id") String id) {
        try {
            PessoaDAO pessoaDAO = new PessoaDAO();
            Pessoa pessoa =  pessoaDAO.getPessoa(Integer.parseInt(id));
            
            pessoaDAO.remove(pessoa);
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET
    @Produces("application/json")
    public Response getAllPessoas() throws JSONException, SQLException {
        try {
            List<Pessoa> pessoas = new ArrayList<Pessoa>();
            PessoaDAO pessoaDAO = new PessoaDAO();
            pessoas = pessoaDAO.getAllPessoas();
            Gson gson = new Gson();
            json = gson.toJson(pessoas);
            String result = "" + json;
            return Response.status(200).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").entity(result).build();
        } catch (Throwable e) {

            return Response.status(404).entity("Error").build();

        }

    }

    @Path("{pessoaId}")
    @GET
    @Produces("application/json")
    public Response getPessoa(@PathParam("pessoaId") Integer pessoaId) throws Throwable {

        //String user_login, String user_senha 
        try {

            Pessoa pessoa = new Pessoa();
            PessoaDAO usuarioDao = new PessoaDAO();
            pessoa = usuarioDao.getPessoa(pessoaId);
            Gson gson = new Gson();
            json = gson.toJson(pessoa);
            String result = "" + json;
            return Response.status(200).entity(result).build();

        } catch (Throwable e) {

            return Response.status(404).entity("Error").build();

        }

    }
    

}
