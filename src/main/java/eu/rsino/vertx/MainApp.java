package eu.rsino.vertx;

import eu.rsino.vertx.web.HttpServerVerticle;
import io.vertx.core.Vertx;

/**
 * @author Remus Sinorchian
 */
public class MainApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpServerVerticle());
    }
}
