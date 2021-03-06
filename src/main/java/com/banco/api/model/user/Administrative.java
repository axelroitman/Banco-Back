package com.banco.api.model.user;

import com.banco.api.utils.DateUtils;
import com.banco.api.adapter.Externalizable;
import com.banco.api.dto.user.AdministrativeUserDTO;
import com.banco.api.dto.user.UserType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@DiscriminatorValue("Administrative")
public class Administrative extends User implements Externalizable<AdministrativeUserDTO> {

    private String dni;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;
    private String firstName;
    private String lastName;
    private String mobilePhone;

    public Administrative() {
    	super();
        this.userTypeNumber = UserType.ADMINISTRATIVE.getValue();
    }

    public Administrative(int id, String cuitCuilCdi, String username, String address, String phone, boolean active,
                          String dni, Date birthdate, String firstName, String lastName, String mobilePhone) {
        super(id, cuitCuilCdi, username, address, phone, active);
        this.dni = dni;
        this.birthday = birthdate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobilePhone = mobilePhone;
        this.userTypeNumber = UserType.ADMINISTRATIVE.getValue();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Date getBirthdate() {
        return birthday;
    }

    public void setBirthdate(Date birthdate) {
        this.birthday = birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public String toString() {
        return "Administrative{" +
                "dni='" + dni + '\'' +
                ", birthdate=" + birthday +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                '}';
    }

    @Override
    public AdministrativeUserDTO toView() {
        AdministrativeUserDTO view = new AdministrativeUserDTO();
        view.setId(this.getId());
        view.setActive(this.isActive());
        view.setAddress(this.getAddress());
        view.setCuitCuilCdi(this.getCuitCuilCdi());
        view.setPhone(this.getPhone());
        view.setUsername(this.getUsername());
        view.setUserType(UserType.valueOf(this.getUserType()).toString());
        view.setBirthDate(DateUtils.format(this.getBirthdate()));
        view.setDni(this.getDni());
        view.setMobilePhone(this.getMobilePhone());
        view.setFirstName(this.getFirstName());
        view.setLastName(this.getLastName());
        view.setFirstLogin(this.isFirstLogin());
        return view;
    }
}
