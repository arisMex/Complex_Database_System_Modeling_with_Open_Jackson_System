public class Requete {

    /*
     * Attributs de la requête.
     */
    private String id; //
    private double dateEntree;
    private double dateSortie;
    private String position; // sa position dans le systeme

    public Requete(String id, double dateEntree, double dateSortie, String position) {
        this.id = id;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public double getDateEntree() {
        return dateEntree;
    }

    public double getDateSortie() {
        return dateSortie;
    }

    public double getTempsTotal() {
        return (double) dateSortie - (double) dateEntree;
    }

    public String getPosition() {
        return position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDateEntree(double dateEntree) {
        this.dateEntree = dateEntree;
    }

    public void setDateSortie(double d) {
        this.dateSortie = d;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
