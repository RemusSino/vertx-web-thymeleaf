package ro.rs.vertx.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

/**
 * @author Remus Sinorchian; created on 4/20/2017
 */
public class HttpServerVerticle extends AbstractVerticle {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private ThymeleafTemplateEngine engine;

    public HttpServerVerticle() {
        this.engine = ThymeleafTemplateEngine.create();
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route("/static/*").handler(StaticHandler.create("webapp/static"));
        router.route(HttpMethod.GET, "/").handler(rc -> {
            engine.render(rc, "webapp/html/index.html", res -> {
                if (res.succeeded()) {
                    rc.response().end(res.result());
                } else {
                    rc.fail(res.cause());
                }
            });
        });
        vertx.createHttpServer().requestHandler(router::accept).listen(Constants.PORT);
        LOGGER.info("Listening on http://localhost:" + Constants.PORT);
    }
}
