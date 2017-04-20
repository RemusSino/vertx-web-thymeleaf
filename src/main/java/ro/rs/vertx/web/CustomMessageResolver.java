package ro.rs.vertx.web;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;

/**
 * @author Remus Sinorchian; created on 4/20/2017
 */
public class CustomMessageResolver extends AbstractMessageResolver {
    @Override
    public String resolveMessage(ITemplateContext context, Class<?> origin, String key, Object[] messageParameters) {
        return null;
    }

    @Override
    public String createAbsentMessageRepresentation(ITemplateContext context, Class<?> origin, String key, Object[] messageParameters) {
        return null;
    }
}
