package cn.riversky;

import cn.riversky.config.StageManager;
import cn.riversky.view.FxmlView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author riversky
 * @date 2020/12/11
 **/
@SpringBootApplication
public class FxApp extends Application {
    protected ConfigurableApplicationContext applicationContext;
    protected StageManager stageManager;
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stageManager = applicationContext.getBean(StageManager.class, primaryStage);
        displayInitialScene();
    }
    @Override
    public void init() throws Exception {
        applicationContext = springBootApplicationContext();
    }
    @Override
    public void stop() throws Exception {
        applicationContext.close();
    }
    /**
     * Useful to override this method by sub-classes wishing to change the first
     * Scene to be displayed on startup. Example: Functional tests on main
     * window.
     */
    protected void displayInitialScene() {
        stageManager.switchScene(FxmlView.LOGIN);
    }
    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(FxApp.class);
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        return builder.run(args);
    }

}
