import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;



public class BDialogue extends JDialog {

    JTextField nbServersC = new JTextField();
    JTextField tempsC = new JTextField();
    JTextField lambdaC = new JTextField();
    JTextField muCC = new JTextField();

    JPanel panel = new JPanel();
    JPanel Pservers = new JPanel();

    private List<Double> mui = new ArrayList<Double>(); // Taux de traitement dans le systeme Fsi (µi)
    private List<Double> pi= new ArrayList<Double>(); // probas requette sortante du system i de se redériger vers le cordinateur
    private List<Double> qi= new ArrayList<Double>(); // probas requette de se redériger vers le serveur i
    private int n; // nombre de serveurs
    private int T; // temps de simulation
    private Double lambda; // temps de simulation
    private Double muC; // temps de simulation




    public BDialogue(JFrame fenetre) {
        super(fenetre, "Configuration", true);
        setSize(800, 600);
        creationComposants();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void creationComposants() {
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new GridLayout(5, 1));

        Pservers.setLayout(new GridLayout(1000, 1));

        JLabel nbServersL = new JLabel("nb Servers (N) ");
        JLabel tempsL = new JLabel("Temps de Simulation (T)");
        JLabel lambdaL = new JLabel("lambda");
        JLabel muCL = new JLabel("µ Cordinateur");



        JButton addServer = new JButton("Créer");
        JButton send = new JButton("Valider");

        JPanel p1 = new JPanel();
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(nbServersL);
        p1.setLayout(new GridLayout(1, 3));
        p1.add(labelPanel);
        nbServersC.setText(""+3);
        p1.add(nbServersC);
        p1.add(addServer);

        JPanel p2 = new JPanel();
        JPanel labelPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel2.add(tempsL);
        p2.setLayout(new GridLayout(1, 3));
        p2.add(labelPanel2);
        tempsC.setText(""+100000);
        p2.add(tempsC);
        p2.add(Box.createRigidArea(new Dimension(10, 10)));

        JPanel p3 = new JPanel();
        JPanel labelPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel3.add(lambdaL);
        p3.setLayout(new GridLayout(1, 3));
        p3.add(labelPanel3);
        lambdaC.setText(""+0.0095);
        p3.add(lambdaC);
        p3.add(Box.createRigidArea(new Dimension(10, 10)));

        JPanel p4 = new JPanel();
        JPanel labelPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel4.add(muCL);
        p4.setLayout(new GridLayout(1, 4));
        p4.add(labelPanel4);
        muCC.setText(""+(1./10.));
        p4.add(muCC);
        p4.add(Box.createRigidArea(new Dimension(10, 10)));

        p1.setSize(p1.getWidth(), p1.getHeight()/2);
        panel.add(p1);
        panel.add(p2);
        panel.add(p3);
        panel.add(p4);

        JScrollPane scrollPane = new JScrollPane(Pservers);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        

      // Configuration du gestionnaire de mise en page
      setLayout(new BorderLayout());

      // Ajout de la liste au panneau principal
      add(panel, BorderLayout.NORTH);
      add(scrollPane, BorderLayout.CENTER);
      add(send, BorderLayout.SOUTH);


        // Ajout d'un écouteur d'événements au bouton "Ajouter Champ de Saisie"
        addServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterChampSaisie();
            }
        });

  

        send.addActionListener(ae -> {
            try {

                if (nbServersC.getText().isEmpty() || tempsC.getText().isEmpty() || lambdaC.getText().isEmpty() ||  muCC.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(BDialogue.this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return; // Stop execution if any field is empty
                }
                this.n = Integer.parseInt(nbServersC.getText());
                this.T = Integer.parseInt(tempsC.getText());
                this.lambda = Double.parseDouble(lambdaC.getText());
                this.muC = Double.parseDouble(muCC.getText());

                mui = new ArrayList<Double>(); // Taux de traitement dans le systeme Fsi (µi)
                pi= new ArrayList<Double>(); // probas requette sortante du system i de se redériger vers le cordinateur
                qi= new ArrayList<Double>(); // probas requette de se redériger vers le serveur i
        
                // Traverse through the panels in Pservers
                for (int i = 0; i < n; i++) {
                    JPanel serverPanel = (JPanel) Pservers.getComponent(i);
        
                    // Retrieve values from JTextField in each panel
                    JTextField qField = (JTextField) serverPanel.getComponent(2); // Index 2 for 'q'
                    JTextField muField = (JTextField) serverPanel.getComponent(4); // Index 4 for 'µ'
                    JTextField pField = (JTextField) serverPanel.getComponent(6); // Index 6 for 'p'
                                // Check if any field is empty
                    if (qField.getText().isEmpty() || muField.getText().isEmpty() || pField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(BDialogue.this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return; // Stop execution if any field is empty
                    }
        
                    // Check if the text fields are not empty before parsing
                    if (!qField.getText().isEmpty()) {
                        qi.add(Double.parseDouble(qField.getText()));
                    }
                    if (!muField.getText().isEmpty()) {
                        mui.add(Double.parseDouble(muField.getText()));
                    }
                    if (!pField.getText().isEmpty()) {
                        pi.add(Double.parseDouble(pField.getText()));
                    }
                }
        
        
                dispose(); // Close the dialog
        
            } catch (NumberFormatException e) {
                System.err.println(e);
            }
        });
        
    }

    private void ajouterChampSaisie() {
        try {
            int nb = Integer.parseInt(nbServersC.getText());
        
            // Supprimer les anciens composants dynamiques du panneau dynamique
            Pservers.removeAll();
        
            // Ajouter les nouveaux champs de saisie en fonction de la valeur de nb
            
            JPanel[] p = new JPanel[nb];
           

            for (int i = 0; i < nb; i++) {
                p[i] = new JPanel(new GridLayout(0, 7));
                p[i].add(new JLabel("S"+i));
                p[i].add(new JLabel("q"));
                JTextField tmpq  =new JTextField();
                tmpq.setText(""+(1.0/(double)nb));
                p[i].add(tmpq);
    
                // Deuxième paire
                p[i].add(new JLabel("µ"));
                JTextField tmpmu  =new JTextField();
                tmpmu.setText(""+(1./100.));
                p[i].add(tmpmu);
    
                // Troisième paire
                p[i].add(new JLabel("p"));
                JTextField tmpp  =new JTextField();
                tmpp.setText(""+(1./3.));
                p[i].add(tmpp);
    
                Pservers.add(p[i]);  
        
            }
            // Revalider et redessiner le panneau dynamique
            Pservers.revalidate();
            Pservers.repaint();  
            
        } catch (NumberFormatException e) {
            System.err.println("La valeur dans le champ nbServersC n'est pas un entier valide.");
        }
    }
    

    public int getN() {
        return n;
    }
    
    public int getT() {
        return T;
    }
    
    public double getLambda() {
        return lambda;
    }
    
    public List<Double> getMui() {
        return mui;
    }
    
    public ArrayList<Double> getQi() {
        return (ArrayList<Double>) qi;
    }
    
    public List<Double> getPi() {
        return pi;
    }
    public double getMuC(){
        return muC;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame fenetre = new JFrame("Exemple de Test");
            fenetre.setSize(400, 300);
            fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenetre.setLocationRelativeTo(null);

            JButton openDialogButton = new JButton("Ouvrir la Boîte de Dialogue");
            openDialogButton.addActionListener(e -> {
                BDialogue bDialogue = new BDialogue(fenetre);
                bDialogue.setVisible(true);
            });

            JPanel mainPanel = new JPanel();
            mainPanel.add(openDialogButton);

            fenetre.add(mainPanel);
            fenetre.setVisible(true);
        });
    }
}
