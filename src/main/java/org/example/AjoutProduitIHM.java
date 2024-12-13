package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class AjoutProduitIHM {
    private JFrame frame;
    private JTextField nomField, prixField;
    private JTextArea descriptionArea;
    private JComboBox<String> commercialComboBox;
    private JLabel errorLabel;

    private final CommercialDaoImpl commercialDao;
    private final ProduitDaoImpl produitDao;

    public AjoutProduitIHM(CommercialDaoImpl commercialDao, ProduitDaoImpl produitDao) {
        this.commercialDao = commercialDao;
        this.produitDao = produitDao;

        // Frame setup
        frame = new JFrame("Ajout Nouveau Produit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));

        // Components
        JLabel nomLabel = new JLabel("Nom:");
        nomField = new JTextField();

        JLabel prixLabel = new JLabel("Prix:");
        prixField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea();

        JLabel commercialLabel = new JLabel("Commercial:");
        commercialComboBox = new JComboBox<>();

        JButton ajouterButton = new JButton("Ajouter");
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        // Populate commercial combo box
        loadCommercials();

        // Add components to the frame
        frame.add(nomLabel);
        frame.add(nomField);

        frame.add(prixLabel);
        frame.add(prixField);

        frame.add(descriptionLabel);
        frame.add(new JScrollPane(descriptionArea));

        frame.add(commercialLabel);
        frame.add(commercialComboBox);

        frame.add(ajouterButton);
        frame.add(errorLabel);

        // Add button action
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterProduit();
            }
        });

        frame.setVisible(true);
    }

    private void loadCommercials() {
        try {
            Set<Commercial> commercials = commercialDao.selectAll(); // Use CommercialDao
            for (Commercial commercial : commercials) {
                commercialComboBox.addItem(commercial.getMatricule());
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur de chargement des commerciaux.");
            e.printStackTrace();
        }
    }

    private void ajouterProduit() {
        String nom = nomField.getText().trim();
        String prixText = prixField.getText().trim().replace(',', '.'); // Replace ',' with '.'
        String description = descriptionArea.getText().trim();
        String selectedCommercial = (String) commercialComboBox.getSelectedItem();

        // Input validation
        if (nom.isEmpty() || prixText.isEmpty() || description.isEmpty() || selectedCommercial == null) {
            errorLabel.setText("Tous les champs sont obligatoires.");
            return;
        }

        try {
            double prix = Double.parseDouble(prixText); // Validate price

            // Create a new product object
            Produit produit = new Produit();
            produit.setNom(nom);
            produit.setDescription(description);
            produit.setPrix(prix);

            Commercial commercial = new Commercial();
            commercial.setMatricule(selectedCommercial);
            produit.setCommercial(commercial);

            // Use ProduitDao to add the product
            int generatedId = produitDao.add(produit);
            if (generatedId > 0) {
                JOptionPane.showMessageDialog(frame, "Produit ajouté avec succès! ID: " + generatedId);
            } else {
                errorLabel.setText("Erreur lors de l'ajout du produit.");
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Prix invalide. Utilisez le format ##,## ou ##.##.");
        } catch (Exception e) {
            errorLabel.setText("Erreur lors de l'ajout du produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Initialize DAO implementations
        CommercialDaoImpl commercialDao = new CommercialDaoImpl(); // Replace with your implementation
        ProduitDaoImpl produitDao = new ProduitDaoImpl(); // Replace with your implementation

        new AjoutProduitIHM(commercialDao, produitDao);
    }
}
