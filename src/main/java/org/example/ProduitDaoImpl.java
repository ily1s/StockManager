package org.example;

import java.sql.*;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ProduitDaoImpl implements Produit.ProduitDao {
    private static final Logger logger = Logger.getLogger(ProduitDaoImpl.class.getName());

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Stock";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    @Override
    public int add(Produit p) throws SQLException {
        String query = "INSERT INTO produit (nom, description, prix, commercial_id) VALUES (?, ?, ?, ?)";
        logger.info("Début de l'opération d'ajout du produit : " + p);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            if (p.getCommercial() != null) {
                ps.setString(4, p.getCommercial().getMatricule());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        p.setId(generatedId); // Mettre à jour l'objet Produit avec l'ID généré
                        logger.info("Produit ajouté avec succès avec ID généré : " + generatedId);
                        return generatedId;
                    }
                }
            }
            logger.warning("Aucun ID généré lors de l'ajout du produit.");
            return -1;

        } catch (SQLIntegrityConstraintViolationException e) {
            logger.log(Level.WARNING, "Échec de l'ajout : un produit avec un ID ou une clé unique existante existe déjà.", e);
            throw new SQLException("Un produit avec ce même ID ou contrainte unique existe déjà : " + e.getMessage(), e);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public int update(Produit p) throws SQLException {
        String sql = "Update produit set nom = ?, description = ?, prix = ?, commercial_id = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setString(4, p.getCommercial().getMatricule());
            if (p.getCommercial() != null) {
                ps.setString(4, p.getCommercial().getMatricule());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR); // Si aucun commercial n'est associé
            }
            ps.setInt(5, p.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Produit mis à jour avec succès : " + p);
            } else {
                logger.warning("Aucun produit trouvé avec ID : " + p.getId());
                throw new SQLException("Mise à jour échouée : aucun produit trouvé avec l'ID " + p.getId());
            }
            return rowsAffected;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la mise à jour du produit : " + p, e);
            throw e;
        }
    }
}
