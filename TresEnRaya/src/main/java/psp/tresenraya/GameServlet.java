package psp.tresenraya;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.lang.Integer.parseInt;
import static org.thymeleaf.web.servlet.JakartaServletWebApplication.buildApplication;
import static psp.tresenraya.GameServlet.Cell.*;
import static psp.tresenraya.GameServlet.PATH;
import static psp.tresenraya.IndexServlet.FIRST_PLAYER_NAME;
import static psp.tresenraya.IndexServlet.SECOND_PLAYER_NAME;
import static psp.tresenraya.ThymeleafInitializer.TEMPLATE_ENGINE_ATTRIBUTE;

@WebServlet(PATH)
public final class GameServlet extends HttpServlet {
    static final String PATH = "/game";

    protected enum Cell {Empty, FirstPlayer, SecondPlayer}

    private char firstPlayerChar;
    private char secondPlayerChar;

    private String boardAttributeName;
    private String turnAttributeName;

    @Override
    public void init() {
        var servletContext = getServletContext();
        firstPlayerChar = servletContext.getInitParameter("FirstPlayerChar").charAt(0);
        secondPlayerChar = servletContext.getInitParameter("SecondPlayerChar").charAt(0);
        boardAttributeName = servletContext.getInitParameter("BoardAttribute");
        turnAttributeName = servletContext.getInitParameter("TurnAttribute");
    }

    private Cell[][] createBoard() {
        Cell[][] b = new Cell[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) b[i][j] = Empty;
        return b;
    }

    private Cell checkWinner(Cell[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != Empty && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0];
            if (board[0][i] != Empty && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i];
        }
        if (board[0][0] != Empty && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0];
        if (board[0][2] != Empty && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2];
        return null;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var session = request.getSession();
        var firstPlayerNameParameter = request.getParameter(FIRST_PLAYER_NAME);
        firstPlayerNameParameter = firstPlayerNameParameter == null ? (String) session.getAttribute(FIRST_PLAYER_NAME) : firstPlayerNameParameter;
        var secondPlayerNameParameter = request.getParameter(SECOND_PLAYER_NAME);
        secondPlayerNameParameter = secondPlayerNameParameter == null ? (String) session.getAttribute(SECOND_PLAYER_NAME) : secondPlayerNameParameter;
        if (firstPlayerNameParameter == null || secondPlayerNameParameter == null) {
            response.setStatus(SC_BAD_REQUEST);
            return;
        }
        session.setAttribute(FIRST_PLAYER_NAME, firstPlayerNameParameter);
        session.setAttribute(SECOND_PLAYER_NAME, secondPlayerNameParameter);
        var turnAttribute = session.getAttribute(turnAttributeName);
        if (turnAttribute == null) {
            var firstTurn = 0;
            session.setAttribute(turnAttributeName, firstTurn);
            turnAttribute = firstTurn;
        }
        var turn = turnAttribute instanceof Integer i ? i : parseInt(turnAttribute.toString());
        if (turn < 0) {
            response.setStatus(SC_BAD_REQUEST);
            return;
        }

        var columnParameter = request.getParameter("column");
        var rowParameter = request.getParameter("row");
        var boardParameter = session.getAttribute(boardAttributeName);
        var board = boardParameter == null ? createBoard() : (Cell[][]) boardParameter;

        Cell winnerCell = checkWinner(board);
        boolean gameOver = winnerCell != null || turn >= 9;

        if (!gameOver) {
            if (columnParameter == null || rowParameter == null) {
                if (turn > 0) {
                    response.setStatus(SC_BAD_REQUEST);
                    response.sendRedirect("/");
                    return;
                }
            } else {
                var column = parseInt(columnParameter);
                var row = parseInt(rowParameter);
                if (board[row][column] == null || board[row][column] == Empty) {
                    board[row][column] = turn % 2 == 0 ? FirstPlayer : SecondPlayer;
                    session.setAttribute(boardAttributeName, board);
                    session.setAttribute(turnAttributeName, ++turn);
                    winnerCell = checkWinner(board);
                    gameOver = winnerCell != null || turn >= 9;
                }
            }
        }

        var currentPlayerName = turn % 2 == 0 ? firstPlayerNameParameter : secondPlayerNameParameter;
        var winnerName = winnerCell == FirstPlayer ? firstPlayerNameParameter : (winnerCell == SecondPlayer ? secondPlayerNameParameter : null);

        if (gameOver) session.invalidate();

        var servletContext = getServletContext();
        response.setStatus(SC_OK);
        response.setContentType(servletContext.getInitParameter("Content"));
        var exchange = buildApplication(servletContext).buildExchange(request, response);
        var webContext = new WebContext(exchange);
        webContext.setVariable(boardAttributeName, board);
        webContext.setVariable("currentPlayerName", currentPlayerName);
        webContext.setVariable("firstPlayerChar", firstPlayerChar);
        webContext.setVariable("secondPlayerChar", secondPlayerChar);
        webContext.setVariable("gameOver", gameOver);
        webContext.setVariable("winnerName", winnerName);
        var templateEngine = (TemplateEngine) servletContext.getAttribute(servletContext.getInitParameter(TEMPLATE_ENGINE_ATTRIBUTE));
        templateEngine.process(PATH, webContext, response.getWriter());
    }
}