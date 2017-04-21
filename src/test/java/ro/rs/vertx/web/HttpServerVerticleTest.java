package ro.rs.vertx.web;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Remus Sinorchian
 */
@RunWith(VertxUnitRunner.class)
public class HttpServerVerticleTest {
    private Integer httpPort;

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    @Before
    public void setUp(TestContext context) throws InterruptedException {
        Vertx vertx = rule.vertx();
        vertx.deployVerticle(new HttpServerVerticle(), context.asyncAssertSuccess());
//delay until the http server is listening
        Thread.sleep(2000);
        httpPort = Constants.PORT;
    }

    @Test
    public void testServerUp(TestContext context) {
        Vertx vertx = rule.vertx();
        final Async async = context.async();
        HttpClient client = vertx.createHttpClient();
        HttpClientRequest req = client.get(httpPort, "localhost", "/", response -> response
                .handler(body -> {
                    //dummy assertion to test the http server responds
                    context.assertEquals(HttpResponseStatus.OK.code(), response.statusCode());
                    context.assertNotNull(body.toString());
                    async.complete();
                }));
        req.putHeader("Accept-Language", "en-US");
        req.end();
    }


    @After
    public void tearDown(TestContext context) {
        rule.vertx().close(context.asyncAssertSuccess());
    }
}
