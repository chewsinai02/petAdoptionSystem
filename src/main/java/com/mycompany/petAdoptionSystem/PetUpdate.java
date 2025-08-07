package com.mycompany.petAdoptionSystem;

public class PetUpdate {
    private final javafx.beans.property.IntegerProperty id;
    private final javafx.beans.property.StringProperty petName;
    private final javafx.beans.property.StringProperty userName;
    private final javafx.beans.property.StringProperty updateTime;
    private final javafx.beans.property.StringProperty updateContent;
    private final javafx.beans.property.StringProperty updatePic;
    private final javafx.beans.property.StringProperty remark;

    public PetUpdate(int id, String petName, String userName, java.sql.Date updateTime,
                     String updateContent, String updatePic, String remark) {
        this.id = new javafx.beans.property.SimpleIntegerProperty(id);
        this.petName = new javafx.beans.property.SimpleStringProperty(petName);
        this.userName = new javafx.beans.property.SimpleStringProperty(userName);
        this.updateTime = new javafx.beans.property.SimpleStringProperty(updateTime.toString());
        this.updateContent = new javafx.beans.property.SimpleStringProperty(updateContent);
        this.updatePic = new javafx.beans.property.SimpleStringProperty(updatePic);
        this.remark = new javafx.beans.property.SimpleStringProperty(remark);
    }

    // Getters and property getters
    public int getId() { return id.get(); }
    public javafx.beans.property.IntegerProperty idProperty() { return id; }
    public String getPetName() { return petName.get(); }
    public javafx.beans.property.StringProperty petNameProperty() { return petName; }
    public String getUserName() { return userName.get(); }
    public javafx.beans.property.StringProperty userNameProperty() { return userName; }
    public String getUpdateTime() { return updateTime.get(); }
    public javafx.beans.property.StringProperty updateTimeProperty() { return updateTime; }
    public String getUpdateContent() { return updateContent.get(); }
    public javafx.beans.property.StringProperty updateContentProperty() { return updateContent; }
    public String getUpdatePic() { return updatePic.get(); }
    public javafx.beans.property.StringProperty updatePicProperty() { return updatePic; }
    public String getRemark() { return remark.get(); }
    public javafx.beans.property.StringProperty remarkProperty() { return remark; }
}