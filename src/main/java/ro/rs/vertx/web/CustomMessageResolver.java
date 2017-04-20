package ro.rs.vertx.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.util.StringUtils;
import org.thymeleaf.util.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static ro.rs.vertx.web.Constants.*;

/**
 * This custom message resolver extends the {@link AbstractMessageResolver} class.
 * It loads the properties files from the resources/webapp/i18n folder, into a map.
 * The keys are taken from the file name and should be {language} or {language}_{country}.
 * The values are instances of {@link Properties} class.
 * <p>
 * The message bundles must have the name pattern "messages_{language}.properties" or
 * "messages_{language}_{country}.properties" to be loaded by this resolver.
 * The default message bundle must have the name "messages.properties"
 *
 * @author Remus Sinorchian
 */
public class CustomMessageResolver extends AbstractMessageResolver {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomMessageResolver.class);
    private Map<String, Properties> localizedMessages;

    public CustomMessageResolver() {
        super();
        readMessageBundles();
    }

    private void readMessageBundles() {
        localizedMessages = new LinkedHashMap<>();
        try {
            URL resourceUrl = this.getClass().getClassLoader().getResource(BUNDLES_DIR);
            if (resourceUrl == null) {
                LOGGER.error("Unable to access the default bundle directory: {}", BUNDLES_DIR);
                return;
            }

            Files.list(Paths.get(resourceUrl.toURI()))
                    .filter(f -> f.toString().contains(".properties"))
                    .filter(f -> f.toString().contains("messages_"))
                    .forEach(this::readResourceBundle);

            Properties properties = new Properties();
            InputStream iss = this.getClass().getClassLoader().getResourceAsStream
                    (DEFAULT_BUNDLE_FILE);
            if (iss == null) {
                LOGGER.warn("Unable to find the default resource bundle: {}", DEFAULT_BUNDLE_FILE);
            } else {
                properties.load(iss);
                localizedMessages.put(DEFAULT_BUNDLE_KEY, properties);
                LOGGER.info("Loaded {} resource bundle", DEFAULT_BUNDLE_FILE);
            }
        } catch (IOException e) {
            LOGGER.error("IOException occurred:", e);
        } catch (URISyntaxException e) {
            LOGGER.error("URISyntaxException occurred:", e);
        }
    }

    private void readResourceBundle(Path f) {
        try {
            if (!f.toFile().exists()) {
                LOGGER.error("File {} does not exist", f.toFile().getAbsolutePath());
                return;
            }
            Properties properties = new Properties();
            // extract the {language} or {language}_{country} from the file name
            String languageCountry = f.getFileName()
                    .toString()
                    .substring(f.getFileName().toString().indexOf('_') + 1, f.getFileName()
                            .toString().indexOf('.'));
            if (f.toFile().exists()) {
                InputStream iss = this.getClass().getClassLoader().getResourceAsStream
                        (BUNDLES_DIR + "/" + f.toFile().getName());
                if (iss != null) {
                    properties.load(iss);
                    localizedMessages.put(languageCountry, properties);
                }
            }
            LOGGER.info("Loaded {} resource bundle", f.getFileName().toString());
        } catch (IOException e) {
            LOGGER.error("IOException occurred:", e);
        }
    }

    @Override
    public String resolveMessage(ITemplateContext context, Class<?> origin, String key, Object[] messageParameters) {
        Validate.notNull(context, "Context cannot be null");
        Validate.notNull(context.getLocale(), "Locale in context cannot be null");
        Validate.notNull(key, "Message key cannot be null");

        String languageContry = StringUtils.isEmptyOrWhitespace(context.getLocale().getCountry()
        ) ? context.getLocale().getLanguage() : context.getLocale().getLanguage() + "_" + context.getLocale()
                .getCountry();

        //get into account the {language} and {language}_{country} (de and de_DE) type of
        // localized resource bundles
        Properties messages = localizedMessages.entrySet()
                .stream()
                .filter(e -> e.getKey().contains(languageContry))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseGet(() -> localizedMessages.get(DEFAULT_BUNDLE_KEY));

        if (messages != null && messages.get(key) != null)
            return messages.get(key).toString();
        return null;
    }

    @Override
    public String createAbsentMessageRepresentation(ITemplateContext context, Class<?> origin, String key, Object[] messageParameters) {
        Validate.notNull(key, "Message key cannot be null");
        if (context.getLocale() != null) {
            return "??" + key + "_" + context.getLocale().toString() + "??";
        }
        return "??" + key + "_" + "??";
    }
}
