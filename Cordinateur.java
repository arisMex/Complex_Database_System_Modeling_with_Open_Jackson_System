import java.util.List;
import java.util.Random;

public class Cordinateur extends MED {

    private List<Double> q; // probas requette de se redériger vers le serveur i
    private Server[] fsi; // mes serveurs Fsi
    private BDD bdd;
    private boolean en_cours_de_traitement = false; // en cours de traitement ou pas
    private double tempsDebutTrait = -1;
    private double tempsTraitement = -1;
    private String id = "Fc";

    public Cordinateur(BDD bdd, double mu, double D) {
        super(mu, D);
        this.fsi = bdd.getFsi();
        this.bdd = bdd;
        this.q = bdd.getQi();
    }

    public Cordinateur(BDD bdd, double mu, List<Double> q, double D) {
        super(mu, D);
        this.fsi = bdd.getFsi();
        this.q = q;
        this.bdd = bdd;

    }

    public void setFsi(Server[] fsi) {
        this.fsi = fsi;
    }

    public String getId() {
        return id;
    }

    public boolean getStatut() {
        return en_cours_de_traitement;
    }

    public double getNbClientsMoyen(){
        int sum = 0;
        int nb = 0;
        try {
            for (int i = bdd.getD()/2; i< bdd.getD() ; i++) {
                if (NbRequetesFile.get(i) != null) {
                    sum += NbRequetesFile.get(i);
                    nb++;
                }
            }
            return (double)sum / (double)nb;
        } catch (Exception e) {
           System.err.println("erreur ! ");
           return -1;
        }
        
        
    }

    /* Autres Methodes */

    private Server direction() {
        double choixAleatoire = Math.random();
        double sommeProbabilites = 0.0;
        if (fsi != null) {
            for (int i = 0; i < bdd.getFsi().length; i++) {
                sommeProbabilites += q.get(i);
                if (choixAleatoire < sommeProbabilites) {
                    return bdd.getFsi()[i];
                }
            }
        }
        throw new AssertionError("Erreur dans la fonction 'direction' Cordinateur.java");
    }

    private void redirection() throws InterruptedException {
        Server direction = direction();
        Requete r = file.retirerRequete();
        // System.out.println(" CORDINATEUR fin de traitement :" + (tempsTraitement +
        // tempsDebutTrait));

        direction.ajouterRequete(r);
        r.setPosition(direction.getId());
        // System.out.println("Fc -- (" + r.getId() + ") --> " + direction.getId());
        en_cours_de_traitement = false;
        nbRequetesTraitees++;
        //traitement();
        //direction.traitement();
    }

    public void traitement() throws InterruptedException {
        if (!file.estVide() && bdd.getCount() < D) {

            if (en_cours_de_traitement == true) {
                // System.out.println("CORDINATEUR en cours de traitement ");

                if ((bdd.getCount() >= Math.floor(tempsTraitement + tempsDebutTrait) && bdd.getCount() < D
                        && tempsDebutTrait >= 0 && tempsTraitement >= 0)) {
                    redirection();

                }

            } else {

                // Random rand = new Random();
                // tempsTraitement = -log(1 - rand.nextDouble()) / mu;
                tempsTraitement = 1.0 / (double) mu;
                tempsDebutTrait = bdd.getCount();
                en_cours_de_traitement = true;
                /*
                 * System.out.println(
                 * "CORDINATEUR Debut traitement :" + bdd.getCount() + " temps traitement :" +
                 * tempsTraitement
                 * + " temps fin de traitement prévu à  :" + (tempsTraitement +
                 * tempsDebutTrait));
                 */

                if (tempsTraitement < 1 && tempsTraitement >= 0) {
                    redirection();
                }

            }
        } else if (file.estVide()) {
            // System.out.println("CORDINATEUR : file d'attente Vide !");
        }
    }
}
