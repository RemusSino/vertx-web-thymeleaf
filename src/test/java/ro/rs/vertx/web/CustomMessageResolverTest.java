package ro.rs.vertx.web;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.thymeleaf.context.ITemplateContext;

import java.util.Locale;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Remus Sinorchian
 */
public class CustomMessageResolverTest {
    private static final String H2_MESSAGE_DEFAULT = "Hello! Vertx is here!";
    private static final String H2_MESSAGE_EN = "Hello! Vertx is here!";
    private static final String H2_MESSAGE_DE = "Hello! Vertx is here!";
    private static CustomMessageResolver resolver;

    @BeforeClass
    public static void setUpClass() {
        resolver = new CustomMessageResolver();
    }

    @Test
    public void testResolveMessage() {
        ITemplateContext context = PowerMockito.mock(ITemplateContext.class);
        PowerMockito.when(context.getLocale()).thenReturn(Locale.ENGLISH);
        String resolvedMessage = resolver.resolveMessage(context, null, "index.h2", null);
        assertEquals(H2_MESSAGE_EN, resolvedMessage);

        PowerMockito.when(context.getLocale()).thenReturn(Locale.GERMAN);
        resolvedMessage = resolver.resolveMessage(context, null, "index.h2", null);
        assertEquals(H2_MESSAGE_DE, resolvedMessage);

        PowerMockito.when(context.getLocale()).thenReturn(Locale.KOREAN);
        resolvedMessage = resolver.resolveMessage(context, null, "index.h2",
                null);
        assertEquals(H2_MESSAGE_DEFAULT, resolvedMessage);

        PowerMockito.when(context.getLocale()).thenReturn(Locale.US);
        resolvedMessage = resolver.resolveMessage(context, null, "index.h2", null);
        assertEquals(H2_MESSAGE_EN, resolvedMessage);

        PowerMockito.when(context.getLocale()).thenReturn(Locale.GERMANY);
        resolvedMessage = resolver.resolveMessage(context, null, "index.h2", null);
        assertEquals(H2_MESSAGE_DE, resolvedMessage);

        PowerMockito.when(context.getLocale()).thenReturn(Locale.KOREA);
        resolvedMessage = resolver.resolveMessage(context, null, "index.h2",
                null);
        assertEquals(H2_MESSAGE_DEFAULT, resolvedMessage);
    }

    @AfterClass
    public static void tearDown() {
        resolver = null;
    }
}
