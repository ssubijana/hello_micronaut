package hello.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class HelloControllerTest {

    private static EmbeddedServer embeddedServer;

    private static HttpClient httpClient;

    @BeforeClass
    public static void setUp() {
        //Este embeddedServer nos permite ejecutar nuestro servicio dentro de la aplicación
        embeddedServer = ApplicationContext.run(EmbeddedServer.class);

        //Definimos un cliente que apunte al embeddedServer creado
        httpClient = embeddedServer.getApplicationContext()
                                   .createBean(HttpClient.class, embeddedServer.getURL());
    }

    @AfterClass
    public static void stopServer() {
        //Debemos cerrar las conexiones
        if (embeddedServer != null) {
            embeddedServer.stop();
        }
        if (httpClient != null) {
            httpClient.stop();
        }
    }

    @Test
    public void shouldReturnHelloWorld() {
        //Se crea la petición httpRequest
        HttpRequest request = HttpRequest.GET("/hello/sara");

        //Lanzamos la petición a través del cliente creado
        String response = httpClient.toBlocking()
                                    .retrieve(request);
        assertThat(response, is(notNullValue()));
        assertThat(response, is("Hello sara"));
    }
}
