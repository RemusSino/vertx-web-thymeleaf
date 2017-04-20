package ro.rs.vertx;

import io.vertx.core.Vertx;
import ro.rs.vertx.web.HttpServerVerticle;

/**
 * @author Remus Sinorchian
 */
public class MainApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpServerVerticle());
    }
}
