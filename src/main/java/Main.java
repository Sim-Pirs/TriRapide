public class Main {

    public static void main(String[] args){


        float gains;



            TriRapideParallele.lancer(42);
            TriRapideParalleleOptimal.lancer(42, 1);
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

            gains = (float) TriRapideParallele.duréeDuTri / (float) TriRapideParalleleOptimal.duréeDuTri;

       System.out.println(gains);

    }
}
