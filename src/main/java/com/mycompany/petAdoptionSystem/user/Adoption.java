package com.mycompany.petAdoptionSystem.user;

public class Adoption {
    private final javafx.beans.property.IntegerProperty id;
    private final javafx.beans.property.StringProperty userName;
    private final javafx.beans.property.StringProperty petName;
    private final javafx.beans.property.StringProperty adoptTime;
    private final javafx.beans.property.StringProperty status;
    private String realName;
    private String telephone;
    private String email;
    private String address;
    private String sex;
    private int age;
    private int petHave;
    private int experience;
    private final int userState;
    private final String pic;
    private final String petType;
    private final String remark;

    public Adoption(int id,
            String realName,
            String petName,
            String adoptTime,
            String status,
            String userName,
            String telephone,
            String email,
            String sex,
            int age,
            String address,
            int userState,
            int petHave,
            int experience,
            String pic, String petType, String remark
    ) {
        this.id = new javafx.beans.property.SimpleIntegerProperty(id);
        this.realName = realName;
        this.petName = new javafx.beans.property.SimpleStringProperty(petName);
        this.adoptTime = new javafx.beans.property.SimpleStringProperty(adoptTime);
        this.status = new javafx.beans.property.SimpleStringProperty(status);
        this.userName = new javafx.beans.property.SimpleStringProperty(userName);
        this.telephone = telephone;
        this.email = email;
        this.sex = sex;
        this.age = age;
        this.address = address;
        this.userState = userState;
        this.petHave = petHave;
        this.experience = experience;
        this.pic = pic;
        this.petType = petType;
        this.remark = remark;
    }

    public int getId() { return id.get(); }
    public javafx.beans.property.IntegerProperty idProperty() { return id; }
    public String getUserName() { return userName.get(); }
    public javafx.beans.property.StringProperty userNameProperty() { return userName; }
    public String getPetName() { return petName.get(); }
    public javafx.beans.property.StringProperty petNameProperty() { return petName; }
    public String getAdoptTime() { return adoptTime.get(); }
    public javafx.beans.property.StringProperty adoptTimeProperty() { return adoptTime; }
    public String getStatus() { return status.get(); }
    public javafx.beans.property.StringProperty statusProperty() { return status; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public int getUserState() { return userState; }
    public void setUserState(int state) { } // This field is no longer used
    public int getPetHave() { return petHave; }
    public void setPetHave(int petHave) { this.petHave = petHave; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public String getUserStateString() {
        return userState == 1 ? "Yes" : "No";
    }
    public String getPic() { return pic; }
    public String getPetType() { return petType; }
    public String getRemark() { return remark; }
}
