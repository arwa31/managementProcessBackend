package com.sid.manage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.sid.manage.entities.*;

import java.util.ArrayList;
import java.util.Optional;

@RepositoryRestResource
public interface UtilisateurRepo extends JpaRepository<Utilisateur,Long>{

    public Utilisateur findByUsername(String username);
    Optional<Utilisateur> findUSerByEmail(String email);
    //Optional <Utilisateur> findByNomOrganisation(String username);
    ArrayList<AdminMTC> findByTypeU(String typeU);

}
