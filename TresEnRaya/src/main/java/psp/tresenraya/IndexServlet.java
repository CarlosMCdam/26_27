package psp.tresenraya;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.thymeleaf.web.servlet.JakartaServletWebApplication.buildApplication;
import static psp.tresenraya.ThymeleafInitializer.TEMPLATE_ENGINE_ATTRIBUTE;

@WebServlet("")
public final class IndexServlet extends HttpServlet {
    public static final String FIRST_PLAYER_NAME = "firstPlayerName";
    public static final String SECOND_PLAYER_NAME = "secondPlayerName";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var servletContext = getServletContext();
        response.setStatus(SC_OK);
        response.setContentType(servletContext.getInitParameter("Content"));

        var exchange = buildApplication(servletContext).buildExchange(request, response);
        var webContext = new WebContext(exchange);
        var templateEngine = (TemplateEngine) servletContext.getAttribute(servletContext.getInitParameter(TEMPLATE_ENGINE_ATTRIBUTE));
        templateEngine.process("index", webContext, response.getWriter());
    }
}