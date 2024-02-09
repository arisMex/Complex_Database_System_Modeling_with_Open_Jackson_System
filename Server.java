import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server extends MED {
    private double p;
    private double q;
    private String id;
    private Cordinateur cordinateur;
    private BDD bdd;
    private boolean en_cours_de_traitement; // en cours de traitement ou pas
    private double tempsDebutTrait = -1;
    private double tempsTraitement = -1;
    // Temps moyen que passe une requete dans le system en


    public Server(BDD bdd, String id, double mu, double p, double q, double D) {
        super(mu, D);
        this.p = p;
        this.q = q;
        this.id = id;
        this.cordinateur = bdd.getCordinateur();
        this.bdd = bdd;
    }

    /* Getters */
    public double getP() {
        return p;
    }

    public double getQ() {
        return q;
    }

    public String getId() {
        return id;
    }

    public boolean getStatut() {
        return en_cours_de_traitement;
    }

    /* Setters */

    public void setP(double p) {
        this.p = p;
    }

    public void setQ(double q) {
        this.q = q;
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

    private int direction() {
        double choixAleatoire = Math.random();
        
        return (choixAleatoire <= p) ? 1 : 0;
    }

    private void redirection() throws InterruptedException {
        int direction = direction();
        Requete r = file.retirerRequete();
        // System.out.println(id + " fin de traitement :" + (tempsTraitement +
        // tempsDebutTrait));
        if (direction == 1) {
            cordinateur.ajouterRequete(r);
            System.out.println(id + " -- (" + r.getId() + ") --> Fc ");
            cordinateur.traitement();
            r.setPosition(cordinateur.getId());

        } else {
            r.setDateSortie(tempsTraitement + tempsDebutTrait);
            r.setPosition("OUT");
            bdd.getGenerateur().addReqSoritie(r);
            bdd.decrementNbRequetesSystem();
            bdd.incrementNbRequetesSorties();
            this.nbRequetesSorties++;
            System.out.println(id + " -- (" + r.getId() + ") --> out time: " + r.getDateSortie());
        }
        en_cours_de_traitement = false;
        nbRequetesTraitees++;
        //traitement();
    }

    public void traitement() throws InterruptedException {
        if (!file.estVide() && bdd.getCount() < D) {

            if (en_cours_de_traitement == true) {
                // System.out.println(id + " en cours de traitement ");

                if ((bdd.getCount() >= Math.floor(tempsTraitement + tempsDebutTrait) && bdd.getCount() < D
                        && tempsDebutTrait >= 0 && tempsTraitement >= 0)) {
                    redirection();
                }

            } else {

                // Random rand = new Random();
                // tempsTraitement = -Math.log(1 - rand.nextDouble()) / mu;
                tempsTraitement = 1.0 / (double) mu;
                tempsDebutTrait = bdd.getCount();
                en_cours_de_traitement = true;

                /*
                 * System.out.println(id + " avant traitement :" + bdd.getCount() +
                 * " temps traitement :" + tempsTraitement
                 * + " temps fin de traitement prévu à  :" + (tempsTraitement +
                 * tempsDebutTrait));
                 */

                if (tempsTraitement < 1 && tempsTraitement >= 0) {
                    redirection();
                }

            }
        } else if (file.estVide()) {
            // System.out.println(id + " file d'attente Vide !");
        }
    }

}
