package cn.riversky.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author riversky
 * @date 2020/12/11
 **/
@Component
public class SpringFXMLLoader {
    private final ResourceBundle resourceBundle;
    private final ApplicationContext applicationContext;

    @Autowired
    public SpringFXMLLoader(ResourceBundle resourceBundle, ApplicationContext applicationContext) {
        this.resourceBundle = resourceBundle;
        this.applicationContext = applicationContext;
    }
    public Parent load(String fxlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        fxmlLoader.setResources(resourceBundle);
        fxmlLoader.setLocation(getClass().getResource(fxlPath));
        return fxmlLoader.load();
    }
}
