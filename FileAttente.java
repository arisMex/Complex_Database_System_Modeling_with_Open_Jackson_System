
/**
 * FileAttente
 */
import java.util.LinkedList;

public class FileAttente {
    private LinkedList<Requete> file;

    public FileAttente() {
        file = new LinkedList<>();
    }

    public boolean estVide() {
        return (file.size() == 0);
    }

    public void ajouterRequete(Requete r) {
        file.add(r);
    }

    public Requete getRequeteIndex(int i) {
        if (!estVide()) {
            return file.get(i);
        } else {
            return null;
        }
    }

    public Requete retirerRequete() {
        if (!estVide()) {
            return file.poll();
        } else {
            return null;
        }
    }

    public LinkedList<Requete> getFile() {
        return file;
    }

    public int getSize() {
        return file.size();
    }

}
