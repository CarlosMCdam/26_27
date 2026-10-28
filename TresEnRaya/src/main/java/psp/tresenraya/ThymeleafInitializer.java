package psp.tresenraya;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;

import static org.thymeleaf.web.servlet.JakartaServletWebApplication.buildApplication;

@WebListener
public final class ThymeleafInitializer implements ServletContextListener {
    public static final String TEMPLATE_ENGINE_ATTRIBUTE = "TemplateEngineAttribute";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        var servletContext = servletContextEvent.getServletContext();
        var application = buildApplication(servletContext);
        var templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        servletContext.setAttribute(servletContext.getInitParameter(TEMPLATE_ENGINE_ATTRIBUTE), templateEngine);
    }
}
