package controllers;

import javafx.scene.control.Button;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import com.plexpt.chatgpt.ChatGPT;

import managers.SettingsManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class ChatbotController {
    private ChatGPT chatGPT;
    private Parser parserMarkdown;
    private HtmlRenderer renderer;
    private final StringBuilder chatHistory = new StringBuilder();

    @FXML
    private WebView output;

    @FXML
    private VBox vbox;

    @FXML
    private TextArea input;

    @FXML
    private Button send;

    @FXML
    private void initialize() {
        chatGPT = ChatGPT.builder()
                .apiKey(SettingsManager.getInstance().getProperty("chatgpt_api_key"))
                .build()
                .init();
        parserMarkdown = Parser.builder().build();
        renderer = HtmlRenderer.builder().build();
    }

    @FXML
    private void chat() {
        final String api_key = SettingsManager.getInstance().getProperty("chatgpt_api_key");
        if (api_key == null || api_key.isEmpty()) {
            output.getEngine().loadContent("Please add CHATGPT_API_KEY in settings.");
            return;
        }
        chatGPT.setApiKey(api_key);

        output.getEngine().loadContent("Loading...");
        final String query = input.getText().trim();
        input.clear();
        if (query.isEmpty()) {
            return;
        }

        final Thread thread = new Thread(() -> {
            try {
                final String userMessage = "<div><b>You</b>:<br/>" + query + "</div></br>";
                chatHistory.append(userMessage);

                Platform.runLater(() -> {
                    output.getEngine().loadContent(chatHistory.toString());
                    output.getEngine().loadContent(chatHistory.toString() + "<div><b>Bot</b>:<br/>Typing...</div>");
                });

                final String botResponse = chatGPT.chat(query);
                final Node document = parserMarkdown.parse(botResponse);
                final String botMessage = "<div><b>Bot</b>:" + renderer.render(document) + "</div><br/>";
                chatHistory.append(botMessage);

                Platform.runLater(() -> {
                    output.getEngine().loadContent(chatHistory.toString());
                });
            } catch (final Exception e) {
                Platform.runLater(() -> {
                    output.getEngine().loadContent("CHATGPT_API_KEY not valid!");
                });
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
