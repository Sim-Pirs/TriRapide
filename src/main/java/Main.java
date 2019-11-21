public class Main {

    public static void main(String[] args){


        float gain;

        TriRapideParallele.lancer(42);
        TriRapideParalleleOptimal.lancer(42, 5);
        boolean identique = true;

        for (int i = 0; i < TriRapideParallele.taille; i++) {
            if (TriRapideParallele.tableau[i] != TriRapideParalleleOptimal.tableau[i]) {
                identique = false;
                break;
            }
        }

        System.out.println("");

        if (!identique)
            System.out.println("Les tableaux ne sont pas identiques");
        else
            System.out.println("Les tableaux sont triés et identiques");

        gain = (float) TriRapideParallele.duréeDuTri / (float) TriRapideParalleleOptimal.duréeDuTri;

       System.out.println("Gain : " + gain);

    }
}
