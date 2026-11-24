package com.example.SistemaHistoriasClinicas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class Main extends Application {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(
                SistemaHistoriasClinicasApplication.class
        )
                .web(WebApplicationType.NONE)
                .run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Vistas/Inicio.fxml")
        );

        loader.setControllerFactory(springContext::getBean);

        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Sistema de Historias Clínicas");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((hilo, error) ->
                log.error("Error no controlado en el hilo {}", hilo.getName(), error)
        );
        launch(args);
    }
}