package com.sid.manage.entities;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("adminMTC")
public class AdminMTC extends Utilisateur implements Serializable {
}
