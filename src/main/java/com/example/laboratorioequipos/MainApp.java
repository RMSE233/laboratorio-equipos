package com.example.laboratorioequipos;

import com.example.laboratorioequipos.database.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/equipo_view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Gestión de Equipos de Laboratorio - ORM");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        JPAUtil.shutdown();
    }
}