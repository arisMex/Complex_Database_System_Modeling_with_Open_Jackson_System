import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MED {
    protected double mu; // Taux de service
    protected double D; // Période de simulation
    protected List<Double> arrivees; // Instants d'arrivées
    protected List<Double> debutTraitement; // Instants de début de traitement
    protected List<Double> departs; // Instants de sortie
    protected List<Integer> NbRequetesFile; // nombre de clients dans la file d'attente
    protected List<Integer> NbRequetesSorties; // nombre de clients servis
    protected List<Integer> NbRequetesTraitees; // nombre de clients servis

    protected FileAttente file; // File d'attente
    protected int nbRequetesTraitees;
    protected int nbRequetesSorties;

    public MED(double mu, double D) {
        this.mu = mu;
        this.D = D;
        this.arrivees = new ArrayList<>();
        this.debutTraitement = new ArrayList<>();
        this.departs = new ArrayList<>();
        this.NbRequetesFile = new ArrayList<>();
        this.NbRequetesSorties = new ArrayList<>();
        this.NbRequetesTraitees = new ArrayList<>();
        this.file = new FileAttente();
        this.nbRequetesTraitees = 0;
        this.nbRequetesSorties = 0;
    }

    /* Getters */

    public double getD() {
        return D;
    }

    public double getMu() {
        return mu;
    }

    public int getNbRequetesTraitees() {
        return nbRequetesTraitees;
    }

    public List<Integer> getNbRequetesTraiteesList() {
        return NbRequetesTraitees;
    }

    public FileAttente getFile() {
        return file;
    }

    public List<Double> getArrivees() {
        return arrivees;
    }

    public List<Double> getDeparts() {
        return departs;
    }

    public List<Integer> getNbRequetesFile() {
        return NbRequetesFile;
    }

    public List<Integer> getnbRequetesSorties() {
        return NbRequetesSorties;
    }

    public List<Double> getStartTimes() {
        return debutTraitement;
    }

    /* Setters */

    public void setMu(double mu) {
        this.mu = mu;
    }

    // Autres Methodes

    public void record() {// le nombre de requetes dans la file d'attente à l'instant i
        NbRequetesFile.add(file.getSize());
        NbRequetesTraitees.add(nbRequetesTraitees);
        NbRequetesSorties.add(nbRequetesSorties);

    }

    public void ajouterRequete(Requete r) {
        file.ajouterRequete(r);
    }

    public void retirerRequete() {
        file.retirerRequete();
    }

    // * */ sauvegarder dans un fichier le nombre de requestes dans la file
    // d'attente en fct du temps
    public void nbRequetesToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            int k = 0;
            for (int nb : NbRequetesFile) {

                writer.write(k + "  " + nb + "\n"); // nb arrivées en fct des instants d'arrivée
                k++;
            }
            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }

    }

    // * */ sauvegarder dans un fichier les arrivées (nombre en fonction des
    // instants)
    public void clientsArriveesToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            int k = 0;
            for (double instantArrivee : getArrivees()) {
                k++;
                if (instantArrivee > 1000)
                    break;
                writer.write(instantArrivee + "  " + k + "\n"); // nb arrivées en fct des instants d'arrivée
            }
            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }

    }

    // * */ sauvegarder dans un fichier les departs (nombre en fonction des
    // instants)
    public void clientsDepartsToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            int k = 0;
            for (double instantDepart : getDeparts()) {
                k++;
                if (instantDepart > 1000)
                    break;
                writer.write(instantDepart + "  " + k + "\n"); // nb departs en fct des instants d'arrivée
            }

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }

    }

    // * */ sauvegarde les instants d'arrivees et de depart dans un fichier
    public static void saveToFile(String filename, List<Integer> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            int k = 0;
            for (double d : data) {
                writer.write(k + "  " + d + "\n");
                k++;
            }

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    public static void saveToFileD(String filename, List<Double> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            int k = 0;
            for (double d : data) {
                writer.write(k + "  " + d + "\n");
                k++;
            }

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    public static void saveToFile(String filename, HashMap<Double, Double> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            for (Map.Entry<Double, Double> m : data.entrySet()) {
                writer.write(m.getKey() + "  " + m.getValue() + "\n");

            }

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    // * */ generer un fichier plt pour une simulation
    public static void genererFichierPLT(String filename, String titre, String titreImage, String med, int id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            writer.write("# Nom du fichier de sortie de l'image\n" +
                    "set terminal png\n" +
                    "set output 'Graphs/" + titreImage + ".png'\n" +
                    "\n" +
                    "# Titre du graphique\n" +
                    "set title '" + titre + "'\n" +
                    "set xlabel 'T'\n" +
                    "set ylabel 'Nombre de Requetes'\n" +
                    "set grid\n" +
                    "\n" +
                    "# Plots\n" +
                    "plot 'Data/" + id + "_" + med + "_file.dat' using 1:2 with linespoints title 'en File attente', " +
                    "'Data/" + id + "_" + med + "_traitement.dat' using 1:2 with linespoints title 'Traitées'," +
                    "'Data/" + id + "_" + med + "_sorties.dat' using 1:2 with linespoints title 'Sorties'");

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    // * */ generer un fichier plt pour une simulation
    public static void genererFichierPLTNbClients(String filename, String titre, String titreImage, int id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            writer.write("# Nom du fichier de sortie de l'image\n" +
                    "set terminal png\n" +
                    "set output 'Graphs/" + titreImage + ".png'\n" +
                    "\n" +
                    "# Titre du graphique\n" +
                    "set title '" + titre + "'\n" +
                    "set xlabel 'T'\n" +
                    "set ylabel 'Nombre de Requetes'\n" +
                    "set grid\n" +
                    "\n" +
                    "# Plots\n" +
                    "plot 'Data/" + id + "_nbRequetes.dat' using 1:2 with linespoints title ' dans le system', " +
                    "'Data/" + id + "_nbRequetesEntrees.dat' using 1:2 with linespoints title 'Total Entrees'," +
                    "'Data/" + id + "_nbRequetesSorties.dat' using 1:2 with linespoints title ' Sorties'");

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    // * */ generer un fichier plt pour une simulation (temps Moyen)
    public static void genererFichierPLTTempsMoy(String filename, String titre, String titreImage, int id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            writer.write("# Nom du fichier de sortie de l'image\n" +
                    "set terminal png\n" +
                    "set output 'Graphs/" + titreImage + ".png'\n" +
                    "\n" +
                    "# Titre du graphique\n" +
                    "set title '" + titre + "'\n" +
                    "set xlabel 'T'\n" +
                    "set ylabel 'Temps Moyen'\n" +
                    "set grid\n" +
                    "\n" +
                    "# Plots\n" +
                    "plot 'Data/" + id + "_tempsMoyenReq.dat' using 1:2 with linespoints title ' temps moyen '");

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    public static void genererFichierPLTtempsMoyrnLambda(String filename, String titre, String titreImage, String med) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            writer.write("# Nom du fichier de sortie de l'image\n" +
                    "set terminal png\n" +
                    "set output 'Graphs/" + titreImage + ".png'\n" +
                    "\n" +
                    "# Titre du graphique\n" +
                    "set title '" + titre + "'\n" +
                    "set xlabel 'λ'\n" +
                    "set ylabel 'temps Moyen dans le system W'\n" +
                    "set grid\n" +
                    "\n" +
                    "# Plots\n" +
                    "plot 'Data/" + med + ".dat' using 1:2 with linespoints title 'en File attente' ");

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    public static void genererFichierPLTtempsMoyenLambdaN(String filename, String titre, String titreImage, String med) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Écrire les données
            writer.write("# Nom du fichier de sortie de l'image\n" +
                    "set terminal png\n" +
                    "set output 'Graphs/" + titreImage + ".png'\n" +
                    "\n" +
                    "# Titre du graphique\n" +
                    "set title '" + titre + "'\n" +
                    "set xlabel 'λ'\n" +
                    "set ylabel 'temps Moyen dans le system W'\n" +
                    "set grid\n" +
                    "\n" +
                    "# Plots\n" +
                    "plot 'Data/" + med + "_1.dat' using 1:2 with linespoints title '1 Servers',"+
                    "'Data/" + med + "_2.dat' using 1:2 with linespoints title '2 Servers',"+
                    "'Data/" + med + "_3.dat' using 1:2 with linespoints title '3 Servers',"+
                    "'Data/" + med + "_4.dat' using 1:2 with linespoints title '4 Servers'");

            // System.out.println("Data has been saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + filename);
        }
    }

    // * */ executer un fichier plt et générer un graphe
    public static void genererGraphe(String nomFichier) {
        try {
            String command = "gnuplot " + nomFichier;
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
