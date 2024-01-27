module com.vedruna.proyectoservicios2ev {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.vedruna.proyectoservicios2ev to javafx.fxml;
    exports com.vedruna.proyectoservicios2ev;
    exports com.vedruna.proyectoservicios2ev.client;
    opens com.vedruna.proyectoservicios2ev.client to javafx.fxml;
    exports com.vedruna.proyectoservicios2ev.server;
    opens com.vedruna.proyectoservicios2ev.server to javafx.fxml;
}