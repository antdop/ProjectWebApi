
import br.com.danilocesarmendes.model.Pessoa;
import br.com.danilocesarmendes.util.Servidor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Danilo
 */
public class PessoaRestTest extends TestCase {

    private HttpServer server;
    private WebTarget target;
    private Client client;

    @Before
    public void before() {
        server = Servidor.startServer();
        ClientConfig config = new ClientConfig();
        config.register(new LoggingFilter());
        this.client = ClientBuilder.newClient((Configuration) config);
    }

    @After
    public void after() {
        Servidor.stopServer(server);
    }

    @Test
    public void testNewPessoa() {
        WebTarget target = client.target("http://localhost:8080/ProjectWebApi");
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Danilo Cesar Mendes");
        pessoa.setLocalidade("Franca");
        pessoa.setAtivo(true);

        Entity<Pessoa> entity = Entity.entity(pessoa, MediaType.APPLICATION_JSON);
        javax.ws.rs.core.Response response = target.path("/api/pessoa").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
    }
    
    @Test
    public void getPessoa() {
        WebTarget target = client.target("http://localhost:8080");
        Pessoa pessoa = target.path("/api/pessoa/1").request().get(Pessoa.class);
        Assert.assertEquals("Danilo Cesar Mendes", pessoa.getNome());
    }


}
