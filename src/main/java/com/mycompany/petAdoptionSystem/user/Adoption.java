/**
 * Represents an adoption record in the system.
 * Contains information such as adoption ID, pet details, adopter details, and adoption date.
 * 
 * @author Chew Sin Ai (finalize, and debug)
 * @version 2.0
 */
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

    public Adoption(int id, String realName, String petName,
            String adoptTime, String status, String userName,
            String telephone, String email, String sex, int age,
            String address, int userState, int petHave, int experience,
            String pic, String petType, String remark) {
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

    /**
     * Returns the unique identifier of this adoption.
     *
     * @return the ID as an integer
     */
    public int getId() {
        return id.get();
    }

    /**
     * Returns the IntegerProperty representing the ID of this adoption.
     *
     * @return the ID property
     */
    public javafx.beans.property.IntegerProperty idProperty() {
        return id;
    }

    /**
     * Returns the user name of the user who made this adoption.
     *
     * @return the user name as a string
     */
    public String getUserName() {
        return userName.get();
    }

    /**
     * Returns the StringProperty representing the user name of the user who
     * made this adoption.
     *
     * @return the user name property
     */
    public javafx.beans.property.StringProperty userNameProperty() {
        return userName;
    }

    /**
     * Returns the name of the pet which was adopted in this adoption.
     *
     * @return the name of the pet as a string
     */
    public String getPetName() {
        return petName.get();
    }

    /**
     * Returns the StringProperty representing the name of the pet which was
     * adopted in this adoption.
     *
     * @return the pet name property
     */
    public javafx.beans.property.StringProperty petNameProperty() {
        return petName;
    }

    /**
     * Returns the time at which the adoption was made.
     *
     * @return the time as a string
     */
    public String getAdoptTime() {
        return adoptTime.get();
    }

    /**
     * Returns the StringProperty representing the time at which the adoption
     * was made.
     *
     * @return the adopt time property
     */
    public javafx.beans.property.StringProperty adoptTimeProperty() {
        return adoptTime;
    }

    /**
     * Returns the status of this adoption as a string, which can be "Applied",
     * "Rejected", "Adopted", or "Cancelled".
     *
     * @return the status of this adoption
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Returns the StringProperty representing the status of this adoption as a
     * string, which can be "Applied", "Rejected", "Adopted", or "Cancelled".
     *
     * @return the status of this adoption
     */
    public javafx.beans.property.StringProperty statusProperty() {
        return status;
    }

    /**
     * Returns the real name of the user who made this adoption.
     *
     * @return the real name of the user
     */
    public String getRealName() {
        return realName;
    }

    /**
     * Sets the real name of the user who made this adoption.
     *
     * @param realName the real name of the user
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * Returns the telephone number of the user who made this adoption.
     *
     * @return the telephone number of the user
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Sets the telephone number of the user who made this adoption.
     *
     * @param telephone the telephone number of the user
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Returns the email of the user who made this adoption.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user who made this adoption.
     *
     * @param email the email of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the address of the user who made this adoption.
     *
     * @return the address of the user
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user who made this adoption.
     *
     * @param address the address of the user
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the sex of the user who made this adoption.
     *
     * @return the sex of the user
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the sex of the user who made this adoption.
     *
     * @param sex the sex of the user
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * Returns the age of the user who made this adoption.
     *
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the user who made this adoption.
     *
     * @param age the age of the user
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Returns the state of the user who made this adoption.
     *
     * @return the state of the user
     */
    public int getUserState() {
        return userState;
    }

    /**
     * Returns the number of pets the user currently has.
     *
     * @return the number of pets the user currently has
     */
    public int getPetHave() {
        return petHave;
    }

    /**
     * Sets the number of pets the user currently has.
     *
     * @param petHave the number of pets the user currently has
     */
    public void setPetHave(int petHave) {
        this.petHave = petHave;
    }

    /**
     * Returns the user's experience with pet adoption.
     *
     * @return the user's experience with pet adoption
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Sets the user's experience with pet adoption.
     *
     * @param experience the user's experience with pet adoption
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * Gets the user state string.
     * <p>
     * The user state is currently a boolean value (1/0) indicating whether the
     * user is willing to adopt pets or not. This method will return "Yes" if
     * the user is willing to adopt, and "No" if they are not.
     *
     * @return the user state string
     */
    public String getUserStateString() {
        return userState == 1 ? "Yes" : "No";
    }

    /**
     * Gets the user's profile picture path.
     *
     * @return the user's profile picture path
     */
    public String getPic() {
        return pic;
    }

    /**
     * Gets the type of pet the user is willing to adopt.
     *
     * @return the type of pet the user is willing to adopt, e.g. "Dog", "Cat"
     */
    public String getPetType() {
        return petType;
    }

    /**
     * Gets the remark of the user who made this adoption.
     *
     * @return the remark of the user
     */
    public String getRemark() {
        return remark;
    }
}
