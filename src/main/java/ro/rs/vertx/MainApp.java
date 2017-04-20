package ro.rs.vertx;

import io.vertx.core.Vertx;
import ro.rs.vertx.web.HttpServerVerticle;

/**
 * @author Remus Sinorchian; created on 4/20/2017
 */
public class MainApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpServerVerticle());
    }
}
