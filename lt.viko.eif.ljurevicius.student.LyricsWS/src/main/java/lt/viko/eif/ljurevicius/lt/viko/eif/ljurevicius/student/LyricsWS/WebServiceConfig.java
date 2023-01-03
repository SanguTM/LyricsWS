package lt.viko.eif.ljurevicius.lt.viko.eif.ljurevicius.student.LyricsWS;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/* @EnableWs enables SOAP Web Service features in this Spring Boot application.
The WebServiceConfig class extends the WsConfigurerAdapter base class,
which configures the annotation-driven Spring-WS programming model. */

//https://spring.io/guides/gs/producing-web-service/
//src/main/java/com/example/producingwebservice/WebServiceConfig.java

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }
    /*
    There are several ways to configure beans in a Spring container. Firstly, we can declare them using XML configuration.
    We can also declare beans using the @Bean annotation in a configuration class.

    Configuration classes can contain bean definition methods annotated with @Bean:
     */
    @Bean(name = "lyrics")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema lyricsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("LyricsPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        wsdl11Definition.setSchema(lyricsSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema lyricsSchema() { return new SimpleXsdSchema(new ClassPathResource("getlyrics.xsd"));
    }
}