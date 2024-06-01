package com.sid.manage.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("adminCNI")
public class AdminCNI extends Utilisateur implements Serializable {
}
