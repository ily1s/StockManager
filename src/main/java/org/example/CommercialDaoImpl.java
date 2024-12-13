package org.example;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class CommercialDaoImpl implements Commercial.CommercialDao {
    private static final Logger logger = Logger.getLogger(CommercialDaoImpl.class.getName());

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Stock";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    @Override
    public Set<Commercial> selectAll() throws SQLException {
        String sql = "select * from commercial";
        Set<Commercial> commercials = new HashSet<>();
        logger.info("Début de l'opération de récupération de tous les commerciaux.");

        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            while(resultSet.next()){
                String Matricule = resultSet.getString("Matricule");
                String Nom = resultSet.getString("Nom");
                String Prenom = resultSet.getString("Prenom");
                commercials.add(new Commercial(Matricule,Nom,Prenom));
                logger.info("Commercial récupéré : " + Matricule + " - " + Nom + " " + Prenom);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        logger.info("Fin de l'opération de récupération de tous les commerciaux.");
        return commercials;
    }

    @Override
    public void delete(String matricule) throws SQLException {
        String sql = "delete from commercial where Matricule = ?";
        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, matricule);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Commercial avec matricule " + matricule + " supprimé avec succès.");
            } else {
                logger.warning("Aucun commercial trouvé avec le matricule : " + matricule);
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}
