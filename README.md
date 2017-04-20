# vertx-web-thymeleaf
Shows an example of a web server developed with Vertx and Thymeleaf.

Uses a custom message resolver extends the AbstractMessageResolver class.
It loads the properties files from the resources/webapp/i18n folder, into a map.
The keys are taken from the file name and should be {language} or {language}_{country}.
The values are instances of Properties class.
The message bundles must have the name pattern "messages_{language}.properties"
or "messages_{language}_{country}.properties" to be loaded by this resolver.
The default message bundle must have the name "messages.properties"
