package com.sid.manage.entities;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;





@Entity
@DiscriminatorValue("admin")
public class Admin extends Utilisateur implements Serializable {

}
