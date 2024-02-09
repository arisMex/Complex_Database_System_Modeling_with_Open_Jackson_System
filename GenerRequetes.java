import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerRequetes {

    private BDD bdd;
    private boolean en_attente; // en cours de traitement ou pas
    private double tempsDebutTrait = -1;
    private double tempsEntreArrivees = -1;
    private double lambda;
    private int compteur;
    private Cordinateur cordinateur;
    private List<Requete> requetes;
    private List<Requete> requetesSorties;

    public GenerRequetes(BDD bdd) {
        this.bdd = bdd;
        this.lambda = bdd.getLambda();
        this.cordinateur = bdd.getCordinateur();
        en_attente = false;
        this.compteur = 0;
        this.requetes = new ArrayList<Requete>();
        this.requetesSorties = new ArrayList<Requete>();

    }

    public int getCompteur() {
        return compteur;
    }

    public int getNbRequetes() {
        return requetes.size();
    }

    public List<Requete> getRequetes() {
        return requetes;
    }

    /* Autres Methodes */

    private void ajouter() throws InterruptedException {
        Requete r = new Requete("R" + compteur, (tempsEntreArrivees + tempsDebutTrait), -1, "Fc");
        System.out.println(" * (" + r.getId() + " ) --> Fc \t time : " + (tempsEntreArrivees + tempsDebutTrait));
        cordinateur.ajouterRequete(r);
        en_attente = false;
        compteur++;
        requetes.add(r);
        bdd.incrementNbRequetesSystem();
        bdd.incrementNbRequetesTot();
        generer();
        // cordinateur.traitement();
    }

    public void generer() throws InterruptedException {
        if (bdd.getCount() < bdd.getD()) {

            if (en_attente == true) {
                // si date d'arrivée prévue du client
                if ((bdd.getCount() >= Math.floor(tempsEntreArrivees + tempsDebutTrait) && bdd.getCount() < bdd.getD()
                        && tempsDebutTrait >= 0 && tempsEntreArrivees >= 0)) {
                    ajouter();

                }

            } else {

                tempsEntreArrivees = -Math.log(1 - Math.random()) / (double) lambda;
                // tempsEntreArrivees = 1. / (double)lambda;
                tempsDebutTrait = bdd.getCount();
                en_attente = true;
                // System.out.println(" Requete prévu à :" + (tempsEntreArrivees
                // +tempsDebutTrait));

                if (tempsEntreArrivees < 1 && tempsEntreArrivees >= 0) {
                    ajouter();
                }

            }
        }
    }

    public double tempsMoyen() {
        double sum = 0;
        for (int i = 0; i < requetesSorties.size(); i++) {
            if (requetesSorties.get(i).getTempsTotal() >= 0) { // requetesSorties
                // sorties
                sum += requetesSorties.get(i).getTempsTotal();
            }
        }
        return (requetesSorties.size() > 0) ? ((double) sum / (double) requetesSorties.size()) : 0;

    }

    public double tempsMoyenRegimeStationnaire() {
        double sum = 0;
        int nb = 0;
        for (int i = 0; i < requetesSorties.size(); i++) {
            if (requetesSorties.get(i).getTempsTotal() >= 0
                    && requetesSorties.get(i).getDateEntree() >= bdd.getD() / 2) { // requetesSorties
                // sorties & entrées apres T/2
                sum += (double) requetesSorties.get(i).getTempsTotal();
                nb++;
            }
        }
        return (requetesSorties.size() > 0 && nb != 0) ? ((double) sum / (double) nb) : 0;

    }

    public synchronized void addReqSoritie(Requete r) {
        requetesSorties.add(r);
    }

    public synchronized int getNbRequetesSorties() {
        return requetesSorties.size();
    }

    // Requetes arrivées dans le R S
    public void saveToFile2(String filename, String filename2) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            int k = 0;
            for (Requete r : requetes) {
                writer.write(k + "  " + r.getTempsTotal() + "\n");
                k++;

            }

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    // Requetes arrivées dans le R S
    public void saveToFile(String filename, String filename2) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                BufferedWriter writer2 = new BufferedWriter(new FileWriter(filename2))) {
            // Écrire les données
            int k = 0;
            for (Requete r : requetesSorties) {
                writer2.write(k + "  " + r.getTempsTotal() + "\n");
                if (r.getDateEntree() >= bdd.getD() / 2) {
                    writer.write(k + "  " + r.getTempsTotal() + "\n");

                }
                k++;
            }

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    public static void genererFichierPLT(String filename, String titreImage, int id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            writer.write("# Nom du fichier de sortie de l'image\n" +
                    "set terminal png\n" +
                    "set output 'Graphs/" + titreImage + ".png'\n" +
                    "\n" +
                    "# Titre du graphique\n" +
                    "set title ' Temps que passe la requete i dans le system (i>= T/2) '\n" +
                    "set xlabel 'Requete i '\n" +
                    "set ylabel 'Temps (ms)'\n" +
                    "set grid\n" +
                    "\n" +
                    "# Plots\n" +
                    "plot 'Data/" + id + "_Req2TempsTraitement.dat' using 1:2 with boxes title 'all'," +
                    "'Data/" + id + "_ReqTempsTraitement.dat' using 1:2 with boxes title 'Regime stationnaire',");

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

}
