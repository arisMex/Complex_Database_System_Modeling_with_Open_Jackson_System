import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

/**
 * BDD
 */
public class BDD {

    private double lambda; // Taux d'arrivées dans le cordinateur
    private double muc; // Taux de traitement dans le cordinateur (µc)
    private List<Double> mui; // Taux de traitement dans le systeme Fsi (µi)
    private List<Double> pi; // probas requette sortante du system i de se redériger vers le cordinateur
    private List<Double> qi; // probas requette de se redériger vers le serveur i
    private int n; // nombre de serveurs
    private Server[] fsi; // mes serveurs Fsi
    private Cordinateur Fc; // mon cordinateur
    private GenerRequetes generateurR;
    private int D; // Période de simulation
    public int count = 0;
    public int nbRequetesSorties = 0;
    public int nbRequetesSystem = 0;
    public int nbRequetesTot = 0;

    public int id;

    // Mesures
    private List<Double> tempsMoyenReq = new ArrayList<Double>(); // Temps moyen que passe une requete dans le system en
                                                                  // fonction du temps
    private List<Integer> nbReq = new ArrayList<Integer>(); // nb de requetes dans le system en fonction du temps
    private List<Integer> nbReSort = new ArrayList<Integer>(); // nb de requetes sorties dans le system en fonction du
                                                               // temps
    private List<Integer> nbReEntr = new ArrayList<Integer>(); // nb de requetes sorties dans le system en fonction du
    // temps

    // * Constructeur */
    public BDD(int id, int n, int D, double lambda, double mu, List<Double> mui, List<Double> qi,
            List<Double> pi) throws Exception {
        if (lambda < 0 || mu < 0)
            throw new Exception("Erreur paramètre");
        this.id = id;
        this.lambda = lambda;
        this.muc = mu;
        this.mui = mui;
        this.n = n;
        this.D = D;
        this.fsi = new Server[n];
        this.pi = pi;
        this.qi = qi;
        this.Fc = new Cordinateur(this, mu, qi, D);
        System.out.println("Creation des serveurs Fsi...");
        fsi = new Server[n];
        for (int i = 0; i < n; i++) {
            fsi[i] = new Server(this, "Fs" + i, mui.get(i), pi.get(i), qi.get(i), D);
        }
        this.Fc.setFsi(fsi);
        generateurR = new GenerRequetes(this);
        System.out.println("Done .");
    }

    public BDD(int id, int n, int D, double lambda,  double mu, 
            double p,   int nbServRapides) throws Exception {

        this.id = id;
        this.lambda = lambda;
        this.muc = mu;
        this.n = n;
        this.D = D;
        this.fsi = new Server[n];
        qi = new ArrayList<Double>();
        pi = new ArrayList<Double>();
        for (int i = 0; i < fsi.length; i++) {
            qi.add(1./(double)n);
            pi.add(p);
        }
        
        this.Fc = new Cordinateur(this, mu, qi ,D);
        System.out.println("Creation des serveurs Fsi...");
        fsi = new Server[n];
        for (int i = 0; i < n; i++) {
            double muf= 1./200.;
            if (i < nbServRapides)mu = 1./100.;
            fsi[i] = new Server(this, "Fs" + i, muf, p, 1./(double)n, D);
        }
        this.Fc.setFsi(fsi);
        generateurR = new GenerRequetes(this);
        System.out.println("Done .");
    }

    // * Set */
    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public void setMuC(double mu) {
        this.muc = mu;
    }

    public void setMui(List<Double> mui) {
        this.mui = mui;
    }

    public void setPi(List<Double> pi) {
        this.pi = pi;
    }

    public void setQi(List<Double> qi) {
        this.qi = qi;
    }

    public void setElmentMui(int index, double m) {
        this.mui.set(index, m);
    }

    public void setElmentPi(int index, Double p) {
        this.pi.set(index, p);
    }

    public void setElmentQi(int index, double q) {
        this.qi.set(index, q);
    }

    public void setD(int d) {
        D = d;
    }

    public void setFc(Cordinateur fc) {
        Fc = fc;
    }

    public synchronized void setCount(int count) {
        this.count = count;
    }

    // *Getters */

    public int getId() {
        return id;
    }

    public synchronized int getCount() {
        return count;
    }

    public synchronized int getNbRequetesSorties() {
        return nbRequetesSorties;
    }

    public synchronized int getNbRequetesSystem() {
        return nbRequetesSystem;
    }

    public synchronized int getNbRequetesTot() {
        return nbRequetesTot;
    }

    public GenerRequetes getGenerateur() {
        return generateurR;
    }

    public Server[] getFsi() {
        return fsi;
    }

    public Cordinateur getCordinateur() {
        return Fc;
    }

    public double getLambda() {
        return lambda;
    }

    public double getMuC() {
        return muc;
    }

    public List<Double> getMui() {
        return mui;
    }

    public int getN() {
        return n;
    }

    public List<Double> getPi() {
        return pi;
    }

    public List<Double> getQi() {
        return qi;
    }

    public int getD() {
        return D;
    }

    // * To Do */
    // |- system stable ?
    // |- nombre moyen de clients dans le system ?
    // |- temps moyen de présence dans le system (temps total passé par les
    // | requettes dans le réseau)
    // |- ...

    public void record() {
        tempsMoyenReq.add(generateurR.tempsMoyen());
        nbReq.add(nbRequetesSystem);
        nbReSort.add(generateurR.getNbRequetesSorties());
        nbReEntr.add(this.getNbRequetesTot());
    }

    public synchronized void incrementCount() {
        count++;
    }

    public synchronized void incrementNbRequetesSystem() {
        nbRequetesSystem++;
    }

    public synchronized void decrementNbRequetesSystem() {
        nbRequetesSystem--;
    }

    public synchronized void incrementNbRequetesSorties() {
        nbRequetesSorties++;
    }

    public synchronized void incrementNbRequetesTot() {
        nbRequetesTot++;

    }

    // sauvegarde des données, generation des scripts plot et generation des graphs.
    private void generateGraphs() {
        // Requetes et leurs temps de traitement 
        this.generateurR.saveToFile("Data/" + id + "_ReqTempsTraitement.dat", "Data/" + id + "_Req2TempsTraitement.dat");
        GenerRequetes.genererFichierPLT("Data/" + id + "_ReqTempsTraitement.plt",    id + "_ReqTempsTraitement", id);
        MED.genererGraphe("Data/" + id + "_ReqTempsTraitement.plt");

        // Nombre de requetes (entrées/ dans le systeme/ sorties )
        MED.saveToFile("Data/" + id + "_nbRequetes.dat", this.nbReq);
        MED.saveToFile("Data/" + id + "_nbRequetesSorties.dat", nbReSort);
        MED.saveToFile("Data/" + id + "_nbRequetesEntrees.dat", nbReEntr);
        MED.genererFichierPLTNbClients("Data/" + id + "_nbRequetes.plt",
                "Nombre de requetes / ms",
                id + "_NombreRequetes", id);
        MED.genererGraphe("Data/" + id + "_nbRequetes.plt");

        // Temps moyen que passe une requette dans le systeme
        MED.saveToFileD("Data/" + id + "_tempsMoyenReq.dat", tempsMoyenReq);

        MED.genererFichierPLTTempsMoy("Data/" + id + "_tempsMoy.plt",
                "Temps moyen que passe une requete dans le systeme à l-instant i",
                id + "_TempsMoyen", id);

        MED.genererGraphe("Data/" + id + "_tempsMoy.plt");

        // Mesures (file d'attente / sorties / traitement) dans chaque
        // serveur/cordinateur
        Fc.nbRequetesToFile("Data/" + id + "_Fc_file.dat");
        MED.saveToFile("Data/" + id + "_Fc_traitement.dat",
                Fc.getNbRequetesTraiteesList());
        MED.saveToFile("Data/" + id + "_" + Fc.getId() + "_sorties.dat",
                Fc.getnbRequetesSorties());
        MED.genererFichierPLT("Data/" + id + "_" + Fc.getId() + ".plt", "Fc", id +
                "_Fc", Fc.getId(), id);
        MED.genererGraphe("Data/" + id + "_Fc.plt");

        for (Server s : fsi) {
            s.nbRequetesToFile("Data/" + id + "_" + s.getId() + "_file.dat");
            MED.saveToFile("Data/" + id + "_" + s.getId() + "_traitement.dat",
                    s.getNbRequetesTraiteesList());
            MED.saveToFile("Data/" + id + "_" + s.getId() + "_sorties.dat",
                    s.getnbRequetesSorties());
            MED.genererFichierPLT("Data/" + id + "_" + s.getId() + ".plt", s.getId(), id
                    + "_" + s.getId(),
                    s.getId(),
                    id);
            MED.genererGraphe("Data/" + id + "_" + s.getId() + ".plt");
        }
    }


    // L : Nombre moyen de clients en régime stationnaire : 
    public double  getL() {
        int sum = 0;
        for (Server s : fsi) {
            sum += s.getNbClientsMoyen();
        }
        sum += Fc.getNbClientsMoyen();
        return sum;
    }

        // W : temps moyen que passe un client en régime stationnaire : 
    public double  getW() {
        return generateurR.tempsMoyenRegimeStationnaire();
    }
    
    public void traitement(boolean generate) throws InterruptedException {
        System.out.println("Traitement des données BDD:  " + id);

        // test

        while (count <= D) {
            record();
            generateurR.generer();
            Fc.record();
            Fc.traitement();
            for (Server s : fsi) {
                s.record();
                s.traitement();
            }
            // System.out.println("temps : " + getCount());
            incrementCount();
        }


        if (generate)generateGraphs();

    }

    public static void main2(String[] args) throws Exception {
        int T = 100000;
        int n = 3;

        double lambda = 1.0 / 100.0;
        double muC = 1.0 / 10.0;

        List<Double> mui = new ArrayList<Double>();
        mui.add(1.0 / 100.0);
        mui.add(1.0 / 200.0);
        List<Double> pi = new ArrayList<Double>();
        pi.add(0.25);
        pi.add(0.25);
        List<Double> qi = new ArrayList<Double>();
        qi.add(0.5);
        qi.add(0.5);

         BDD b = new BDD(100, 2, T, lambda, muC, mui, qi, pi);
         b.traitement(true);
         Test.test(b);


        // BDD2

        List<Double> pi2 = new ArrayList<Double>();
        pi2.add(1.0 / 3.0);
        pi2.add(1.0 / 3.0);
        pi2.add(1.0 / 3.0);
        List<Double> mui2 = new ArrayList<Double>();
        mui2.add(1.0 / 100.0);
        mui2.add(1.0 / 100.0);
        mui2.add(1.0 / 200.0);
        List<Double> qi2 = new ArrayList<Double>();
        qi2.add(1.0 / 3.0);
        qi2.add(1.0 / 3.0);
        qi2.add(1.0 / 3.0);

         BDD b2 = new BDD(2, n, T, 0.0095, muC, mui2, qi2, pi2);
         b2.traitement(true);
        Test.test(b2);
        // test(b);
        // test(b2);

        int nbBDD = 10;

        //Test.tempsMoyenLambda(nbBDD, n, T, muC, mui2, qi2, pi2);

    }
    public static void main(String[] args) throws Exception {
        JFrame mainFrame = new JFrame("Main Frame");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);        BDialogue bDialogue = new BDialogue(mainFrame);
        bDialogue.setVisible(true); // Show the dialog

        // Retrieve the values after the dialog is closed
        int n = bDialogue.getN();
        int T = bDialogue.getT();
        double lambda = bDialogue.getLambda();
        double muC = bDialogue.getMuC();
        List<Double> mui = bDialogue.getMui();
        List<Double> qi = bDialogue.getQi();
        List<Double> pi = bDialogue.getPi();

         BDD b = new BDD(100, n, T, lambda, muC, mui, qi, pi);
         b.traitement(true);
        // Test.test(b);


        // BDD2

        List<Double> pi2 = new ArrayList<Double>();
        pi2.add(1.0 / 3.0);
        pi2.add(1.0 / 3.0);
        pi2.add(1.0 / 3.0);
        List<Double> mui2 = new ArrayList<Double>();
        mui2.add(1.0 / 100.0);
        mui2.add(1.0 / 100.0);
        mui2.add(1.0 / 200.0);
        List<Double> qi2 = new ArrayList<Double>();
        qi2.add(1.0 / 3.0);
        qi2.add(1.0 / 3.0);
        qi2.add(1.0 / 3.0);

        // BDD b2 = new BDD(2, 3, T, 0.0095, muC, mui2, qi2, pi2);
         //b2.traitement();
        //Test.test(b2);


        int nbBDD = 10;

        //Test.tempsMoyenLambda(nbBDD, n, T, muC, mui2, qi2, pi2);

    }

}