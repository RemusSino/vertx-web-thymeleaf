package eu.rsino.vertx.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Calendar;

/**
 * Simple http server developed with Vertx Web and Thymeleaf.
 * The ClassLoaderTemplateResolver is used to resolve the Thymeleaf templates.
 * The html files are in the resources/webapp/html folder.
 * The message bundles are in the resources/webapp/i18n folder.
 * The static css and js files are in the resources/webapp/static/* folder.
 *
 * @author Remus Sinorchian
 */
public class HttpServerVerticle extends AbstractVerticle {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    private ThymeleafTemplateEngine engine;

    public HttpServerVerticle() {
        this.engine = ThymeleafTemplateEngine.create();
        configureThymeleafEngine(engine);
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route("/static/*").handler(StaticHandler.create("webapp/static"));
        router.route(HttpMethod.GET, "/").handler(rc -> {
            rc.put("today", Calendar.getInstance());
            rc.put("number", 212.42);
            engine.render(rc, "index", res -> {
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

    private void configureThymeleafEngine(ThymeleafTemplateEngine engine) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(Constants.TEMPLATE_PREFIX);
        templateResolver.setSuffix(Constants.TEMPLATE_SUFFIX);
        engine.getThymeleafTemplateEngine().setTemplateResolver(templateResolver);

        CustomMessageResolver customMessageResolver = new CustomMessageResolver();
        engine.getThymeleafTemplateEngine().setMessageResolver(customMessageResolver);
    }
}
