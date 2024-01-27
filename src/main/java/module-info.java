module com.vedruna.proyectoservicios2ev {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;

    exports com.vedruna.proyectoServicios2ev.client;
    opens com.vedruna.proyectoServicios2ev.client to javafx.fxml;
    exports com.vedruna.proyectoServicios2ev.server;
    opens com.vedruna.proyectoServicios2ev.server to javafx.fxml;
}