import javax.swing.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Test {
    public static void tempsMoyenLambda(int nbBDD, int n, int T, double muC) throws Exception {
        BDD[] bdds = new BDD[nbBDD];
        HashMap<Double, Double> map = new LinkedHashMap<>();
        for (int i = 0; i < nbBDD; i++) {
            bdds[i] = new BDD(i, n, T, 0.001 * (i), muC,  1./3. , i/2);
            bdds[i].traitement(false);
            double tmp = bdds[i].getGenerateur().tempsMoyenRegimeStationnaire();
            map.put(0.001 * i, tmp);
            // test(bdds[i]);
        }
        MED.saveToFile("Data/tempsMoyenLambda.dat", map);
        MED.genererFichierPLTtempsMoyrnLambda("Data/WLambda.plt", "W en fonction de λ", "W_En_Fct_de_Lambda",
                "tempsMoyenLambda");
        MED.genererGraphe("Data/WLambda.plt");

    }

    public static void tempsMoyenN(int nMax, int T, double lambda, double muC) throws Exception {
        BDD[] bdds = new BDD[nMax];
        HashMap<Double, Double> map = new LinkedHashMap<>();
        for (int i = 1; i <= nMax; i++) {
            bdds[i-1] = new BDD(i, i, T, lambda, muC,  1./3. , i/2);
            bdds[i-1].traitement(false);
            double tmp = bdds[i-1].getGenerateur().tempsMoyenRegimeStationnaire();
            map.put((double) i, tmp);
        }
        MED.saveToFile("Data/tempsMoyen_N.dat", map);
        MED.genererFichierPLTtempsMoyrnLambda("Data/W_N.plt", "W en fonction de N", "W_En_Fct_de_N",
                "tempsMoyen_N");
        MED.genererGraphe("Data/W_N.plt");
    }

    public static void tempsMoyen_N_Lambda(int nbBDD, int nMax, int T, double lambda, double muC) throws Exception {
        BDD[] bdds = new BDD[nbBDD * nMax];
    
        for (int i = 1; i <= nMax; i++) {
            HashMap<Double, Double> map = new LinkedHashMap<>();
            for (int j = 0; j < 10; j++) {
                bdds[j] = new BDD(j, i, T, 0.001 * j, muC, 1. / 3., i / 2);
                bdds[j].traitement(false);
                double tmp = bdds[j].getGenerateur().tempsMoyenRegimeStationnaire();
                map.put(0.001 * j, tmp);
            }
            MED.saveToFile("Data/TempsMoyen_Lambda_N/tempsMoyen_N_lambda_" + i + ".dat", map);
        }
        MED.genererFichierPLTtempsMoyenLambdaN("Data/TempsMoyen_Lambda_N/W_N_Lambda.plt", "W en fonction de λ et n",
                "W_En_Fct_de_N_et_Lambda",
                "TempsMoyen_Lambda_N/tempsMoyen_N_lambda");
        MED.genererGraphe("Data/TempsMoyen_Lambda_N/W_N_Lambda.plt");
    }
    

    public static void test(BDD b) {
        System.out.println("\n*******************************************************");
        System.out.println("*-*-*-*--*- BDD_" + b.getId() + " *-*-*-*-*-*");
        System.out.println();
        System.out.println("BDD :");
        int nbReqTot =  b.getNbRequetesTot();
        int nbReqSort = b.getNbRequetesSorties();
        System.out.println(" * nb requetes total entrées : " +nbReqTot);
        System.out.println(" * nb requetes encore dans le system : " +
                b.getNbRequetesSystem());
        System.out.println(" * nb requetes sorties du system : " +nbReqSort);
        double efficiency = ((double) nbReqSort * 100 / (double) nbReqTot);
        DecimalFormat df = new DecimalFormat("#.##");
        String eff = df.format(efficiency);
        System.out.println(" * * Efficacité : "+ eff+"%");
         efficiency = ((double)nbReqTot   / (double)nbReqSort );
         eff = df.format(efficiency);
         System.out.println(" * * E/S : "+ eff+"%");

        System.out.println();
        System.out.println("Cordinateur :");
        Cordinateur c = b.getCordinateur();
        System.out.println(c.getId() + "\n * nb rqts en file d'attente : " + c.getFile().getSize() +
                "\n * nb rqts en traitées : " + c.getNbRequetesTraitees() + " \n * rqts en cours de traitement " +
                ((c.getStatut()) ? 1 : 0));

        System.out.println();

        System.out.println("Servers :");

        for (Server s : b.getFsi()) {
            System.out.println(s.getId() + "\n * nb rqts en file d'attente : " + s.getFile().getSize() +
                    "\n * nb rqts traitées : " + s.getNbRequetesTraitees() + " \n * rqts en cours de traitement " +
                    ((s.getStatut()) ? 1 : 0));

        }

        System.out.println("Le temps moyen que passe une requete dans le système(RS) est : "
                + b.getGenerateur().tempsMoyenRegimeStationnaire() + " ms");
        System.out.println("nb moyen de clients L (RS) = " + b.getL());

        System.out.println();
    }

    public static void clear() {
        try {
            // For Windows
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                // For Unix/Linux/Mac
                new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            // Handle exceptions if any
            e.printStackTrace();
        }
    }

    public void system1() throws Exception {
        JFrame mainFrame = new JFrame("Main Frame");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        BDialogue bDialogue = new BDialogue(mainFrame);
        bDialogue.setVisible(true); // Show the dialog

        // Retrieve the values after the dialog is closed
        int n = bDialogue.getN();
        int T = bDialogue.getT();
        double lambda = bDialogue.getLambda();
        double muC = bDialogue.getMuC();
        List<Double> mui = bDialogue.getMui();
        List<Double> qi = bDialogue.getQi();
        List<Double> pi = bDialogue.getPi();

        BDD b = new BDD(0, n, T, lambda, muC, mui, qi, pi);
        b.traitement(true);
        Test.test(b);
    }

    public void system1Default() throws Exception {
        int T = 100000;
        int n = 2;

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

        //BDD b = new BDD(0, n, T, lambda, muC, mui, qi, pi);
        BDD b = new BDD(0, n, T, lambda, muC,  1./4. , 1);
        b.traitement(true);
        Test.test(b);

    }

    public void system2() throws Exception {
        JFrame mainFrame = new JFrame("Main Frame");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);
        BDialogue bDialogue = new BDialogue(mainFrame);
        bDialogue.setVisible(true); // Show the dialog

        int n = bDialogue.getN();
        int T = bDialogue.getT();
        double lambda = bDialogue.getLambda();
        double muC = bDialogue.getMuC();
        List<Double> mui = bDialogue.getMui();
        List<Double> qi = bDialogue.getQi();
        List<Double> pi = bDialogue.getPi();

        BDD b = new BDD(1, n, T, lambda, muC, mui, qi, pi);
        b.traitement(true);
        Test.test(b);
    }

    public void system2Default() throws Exception {

        int T = 100000;
        int n = 3;

        double lambda = 0.0095;
        double muC = 1.0 / 10.0;

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

        //BDD b2 = new BDD(1, n, T, lambda, muC, mui2, qi2, pi2);
        BDD b2 = new BDD(0, n, T, lambda, muC,  1./3. , 2);

        b2.traitement(true);
        Test.test(b2);
    }

    public static void main(String[] args) throws Exception {
        Test t = new Test();
        //t.system1Default(); // Serveur non-stable
        t.system1();
        // t.system2Default(); // Serveur stable
        //t.system2();

        int nbBDD = 10; 
        int nMax = 4;
        int T = 100000;


        //* 4
        // tempsMoyenLambda(nbBDD, 3, T, 1./10.);
         //tempsMoyenN(10, T, 0.0095, 1. / 10.);
         //tempsMoyen_N_Lambda( nbBDD, nMax, T,0.0095, 1./10.);

         //Test.tempsMoyenLambda(nbBDD, nMax, T, 0.0095);

    }
}
