module com.mycompany.petAdoptionSystem {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    exports com.mycompany.petAdoptionSystem;
    opens com.mycompany.petAdoptionSystem.user to javafx.base;
    opens com.mycompany.petAdoptionSystem.admin to javafx.base;
}
