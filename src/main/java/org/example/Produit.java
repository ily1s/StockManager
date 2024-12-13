package org.example;

import lombok.Data;

import java.sql.SQLException;
import java.util.Objects;

@Data
public class Produit {
    private int id;
    private String nom;
    private String description;
    private double prix;
    private Commercial commercial;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Produit produit = (Produit) obj;
        return this.id == produit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public interface ProduitDao {
        int add(Produit p) throws SQLException;
        int update(Produit p) throws SQLException;
    }
}
