package com.mycompany.petAdoptionSystem;

import java.sql.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Pet {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty type;
    private final StringProperty sex;
    private final ObjectProperty<Date> birthday;
    private final StringProperty pic;
    private final IntegerProperty state;
    private final StringProperty remark;

    public Pet(int id, String name, String type, String sex, Date birthday, String pic, int state, String remark) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.sex = new SimpleStringProperty(sex);
        this.birthday = new SimpleObjectProperty<>(birthday);
        this.pic = new SimpleStringProperty(pic);
        this.state = new SimpleIntegerProperty(state);
        this.remark = new SimpleStringProperty(remark);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getType() { return type.get(); }
    public String getSex() { return sex.get(); }
    public Date getBirthday() { return birthday.get(); }
    public String getPic() { return pic.get(); }
    public int getState() { return state.get(); }
    public String getRemark() { return remark.get(); }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty typeProperty() { return type; }
    public StringProperty sexProperty() { return sex; }
    public ObjectProperty<Date> birthdayProperty() { return birthday; }
    public StringProperty picProperty() { return pic; }
    public IntegerProperty stateProperty() { return state; }
    public StringProperty remarkProperty() { return remark; }

    // Setters
    public void setName(String name) { this.name.set(name); }
    public void setType(String type) { this.type.set(type); }
    public void setSex(String sex) { this.sex.set(sex); }
    public void setBirthday(Date birthday) { this.birthday.set(birthday); }
    public void setPic(String pic) { this.pic.set(pic); }
    public void setState(int state) { this.state.set(state); }
    public void setRemark(String remark) { this.remark.set(remark); }
} 