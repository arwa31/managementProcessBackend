package com.sid.manage.entities;
import java.io.Serializable;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Data;


@Entity
@Data 
@DiscriminatorValue("membre")
public class Membre extends Utilisateur implements Serializable{

}
