package org.example;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ProduitDaoImpl produitDao = new ProduitDaoImpl();
        try {
            // Create a new product and add it to the database
            Produit newProduit = new Produit();
            newProduit.setNom("Laptop");
            newProduit.setDescription("High-end gaming laptop");
            newProduit.setPrix(1500.99);

            // Optional: Attach a Commercial if needed
            Commercial commercial = new Commercial();
            commercial.setMatricule("C000");
            commercial.setNom("Dupont");
            commercial.setPrenom("Jean");
            newProduit.setCommercial(commercial);

            // Test the add method
            int generatedId = produitDao.add(newProduit);
            if (generatedId > 0) {
                System.out.println("Produit ajouté avec succès : " + newProduit);
            } else {
                System.out.println("Échec de l'ajout du produit.");
            }

            // Test the update method
            newProduit.setDescription("Updated description for the gaming laptop");
            int rowsUpdated = produitDao.update(newProduit);
            if (rowsUpdated > 0) {
                System.out.println("Produit mis à jour avec succès : " + newProduit);
            } else {
                System.out.println("Échec de la mise à jour du produit.");
            }

            // Test error handling for duplicate entries
            try {
                Produit duplicateProduit = new Produit();
                duplicateProduit.setNom("Laptop");
                duplicateProduit.setDescription("Duplicate entry test");
                duplicateProduit.setPrix(1500.99);
                produitDao.add(duplicateProduit);
            } catch (SQLException e) {
                System.out.println("Exception attendue : " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors des opérations sur le produit : " + e.getMessage());
        }
    }
}
