package com.ssubijana.hellomicronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.reactivex.Flowable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EchoControllerTest {

    private static EmbeddedServer embeddedServer;

    private static RxHttpClient httpClient;

    @BeforeClass
    public static void setUp() {
        //Este embeddedServer nos permite ejecutar nuestro servicio dentro de la aplicación
        embeddedServer = ApplicationContext.run(EmbeddedServer.class);

        //Definimos un cliente que apunte al embeddedServer creado
        httpClient = embeddedServer.getApplicationContext()
                                   .createBean(RxHttpClient.class, embeddedServer.getURL());
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
    public void shouldReturnSyncContentBody() throws Exception {
        final String requestBody = "Hello world";
        HttpRequest request = HttpRequest.POST("/echo/sync", requestBody)
                                         .contentType(MediaType.TEXT_PLAIN_TYPE)
                                         .accept(MediaType.TEXT_PLAIN_TYPE);

        //Lanzamos la petición a través del cliente creado
        String response = httpClient.toBlocking()
                                    .retrieve(request);

        assertThat(response, is(requestBody));

    }

    @Test
    public void shouldReturnAsyncContentBody() throws Exception {
        final String requestText = "Hello world";
        final Flowable<String> requestBody = Flowable.just(requestText);
        HttpRequest request = HttpRequest.POST("/echo/async", requestBody)
                                         .contentType(MediaType.TEXT_PLAIN_TYPE)
                                         .accept(MediaType.TEXT_PLAIN_TYPE);
        String response = httpClient.toBlocking()
                                    .retrieve(request);

        assertThat(response, is(requestText));

    }
}
