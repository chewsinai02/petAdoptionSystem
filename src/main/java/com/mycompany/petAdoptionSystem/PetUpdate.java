/**
 * Represents an update for a pet's information.
 * Stores details about changes such as health condition or availability.
 * 
 * @author Chew Sin Ai (finalize, and debug)
 * @version 2.0
 */
package com.mycompany.petAdoptionSystem;

public class PetUpdate {

    private final javafx.beans.property.IntegerProperty id;
    private final javafx.beans.property.StringProperty petName;
    private final javafx.beans.property.StringProperty userName;
    private final javafx.beans.property.StringProperty updateTime;
    private final javafx.beans.property.StringProperty updateContent;
    private final javafx.beans.property.StringProperty updatePic;

    public PetUpdate(int id, String petName, String userName, java.sql.Date updateTime,
            String updateContent, String updatePic, String remark) {
        this.id = new javafx.beans.property.SimpleIntegerProperty(id);
        this.petName = new javafx.beans.property.SimpleStringProperty(petName);
        this.userName = new javafx.beans.property.SimpleStringProperty(userName);
        this.updateTime = new javafx.beans.property.SimpleStringProperty(updateTime.toString());
        this.updateContent = new javafx.beans.property.SimpleStringProperty(updateContent);
        this.updatePic = new javafx.beans.property.SimpleStringProperty(updatePic);
    }

    // Getters and property getters
    public int getId() {
        return id.get();
    }

    /**
     * Returns the IntegerProperty representing the ID of this pet update.
     *
     * @return the ID property
     */
    public javafx.beans.property.IntegerProperty idProperty() {
        return id;
    }

    /**
     * Returns the name of the pet that this update is for.
     *
     * @return the name of the pet
     */
    public String getPetName() {
        return petName.get();
    }

    /**
     * Returns the StringProperty representing the name of the pet that this
     * update is for.
     *
     * @return the pet name property
     */
    public javafx.beans.property.StringProperty petNameProperty() {
        return petName;
    }

    /**
     * Returns the user name associated with this pet update.
     *
     * @return the user name as a string
     */
    public String getUserName() {
        return userName.get();
    }

    /**
     * Returns the StringProperty representing the user name associated with
     * this pet update.
     *
     * @return the user name property
     */
    public javafx.beans.property.StringProperty userNameProperty() {
        return userName;
    }

    /**
     * Retrieves the update time for this pet update.
     *
     * @return the update time as a string
     */
    public String getUpdateTime() {
        return updateTime.get();
    }

    /**
     * Returns the StringProperty representing the update time for this pet
     * update.
     *
     * @return the update time property
     */
    public javafx.beans.property.StringProperty updateTimeProperty() {
        return updateTime;
    }

    /**
     * Retrieves the content of this pet update.
     *
     * @return the update content as a string
     */
    public String getUpdateContent() {
        return updateContent.get();
    }

    /**
     * Retrieves the file name of the image associated with this pet update, if
     * any.
     *
     * @return the file name of the image, or an empty string if none
     */
    public String getUpdatePic() {
        return updatePic.get();
    }
}
