public class Main {

    public static void main(String[] args){

        float gain;

        TriRapide.lancer(42);
        TriRapideParallele.lancer(42);
        boolean identique = true;

        for (int i = 0; i < TriRapideParalleleOptimal.taille; i++) {
            if (TriRapideParalleleOptimal.tableau[i] != TriRapideParalleleOptimal.tableau[i]) {
                identique = false;
                break;
            }
        }

        System.out.println("");

        if (!identique)
            System.out.println("Les tableaux ne sont pas identiques");
        else
            System.out.println("Les tableaux sont triés et identiques");

        gain = (float) TriRapideParalleleOptimal.duréeDuTri / (float) TriRapideParalleleOptimal.duréeDuTri;

       System.out.println("Gain : " + gain);

    }
}
