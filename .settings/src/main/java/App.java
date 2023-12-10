import java.io.IOException;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(final Stage stage) throws IOException {
        stage.setTitle("JavaFX-ChatGPT");
        stage.setResizable(false);

        SceneController.getInstance().init(stage);
        SceneController.getInstance().switchScene("/views/main.fxml");
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
