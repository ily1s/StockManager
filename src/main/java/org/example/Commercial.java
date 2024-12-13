package org.example;

import lombok.Data;

import java.sql.SQLException;
import java.util.Set;

@Data
public class Commercial {
    private String matricule;
    private String nom;
    private String prenom;
    private Set<Produit> produits;

    public Commercial(String matricule, String nom, String prenom) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Commercial() {
    }

    public interface CommercialDao {
        Set<Commercial> selectAll() throws SQLException;
        void delete(String matricule) throws SQLException;
    }
}
