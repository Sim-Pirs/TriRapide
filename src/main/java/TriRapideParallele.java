// -*- coding: utf-8 -*-

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TriRapideParallele implements Runnable {
    static final int taille = 100_000_000 ;                   // Longueur du tableau à trier
    static final int [] tableau = new int[taille] ;         // Le tableau d'entiers à trier
    static final int borne = 10 * taille ;                  // Valeur maximale dans le tableau

    private int début;
    private int fin;

    static int nbThreads = Runtime.getRuntime().availableProcessors();

    static volatile ExecutorService executeur = Executors.newFixedThreadPool(nbThreads);

    static volatile AtomicInteger nbTaches = new AtomicInteger(0);

    private TriRapideParallele(int début, int fin) {
        this.début = début;
        this.fin = fin;
    }

    private static void echangerElements(int m, int n) {
        int temp = tableau[m] ;
        tableau[m] = tableau[n] ;
        tableau[n] = temp ;
    }

    private static int partitionner(int début, int fin) {
        int v = tableau[fin] ;                               // Choix (arbitraire) du pivot : t[fin]
        int place = début ;                            // Place du pivot, à droite des éléments déplacés
        for (int i = début ; i<fin ; i++) {            // Parcours du *reste* du tableau
            if (tableau[i] < v) {                            // Cette valeur t[i] doit être à droite du pivot
                echangerElements(i, place) ;        // On le place à sa place
                place++ ;                              // On met à jour la place du pivot
            }
        }
        echangerElements(place, fin) ;              // Placement définitif du pivot
        return place ;
    }

    private static void trierRapidementSequentiel(int début, int fin) {
        if (début < fin) {                             // S'il y a un seul élément, il n'y a rien à faire!
            int p = partitionner(début, fin) ;
            nbTaches.addAndGet(1);
            trierRapidementSequentiel(début, p-1); ;
            nbTaches.addAndGet(1);
            trierRapidementSequentiel(p+1, fin); ;
        }
        nbTaches.decrementAndGet();
    }

    private void trierRapidement() {
        if (fin - début > 1000 && fin - début > 0.01 * taille) {                             // S'il y a un seul élément, il n'y a rien à faire!
            int p = partitionner(début, fin) ;

            nbTaches.addAndGet(1);
            TriRapideParallele tri1 = new TriRapideParallele(début, p-1);

            nbTaches.addAndGet(1);
            TriRapideParallele tri2 = new TriRapideParallele( p+1, fin);

            executeur.submit(tri1);
            executeur.submit(tri2);
        }
        else if(début < fin){
            int p = partitionner( début, fin) ;

            nbTaches.addAndGet(1);
            trierRapidementSequentiel( début, p-1);
            nbTaches.addAndGet(1);
            trierRapidementSequentiel( p+1, fin);
        }
        nbTaches.decrementAndGet();
    }

    private static void afficher(int[] t, int début, int fin) {
        for (int i = début ; i <= début+3 ; i++) {
            System.out.print(" " + t[i]) ;
        }
        System.out.print("...") ;
        for (int i = fin-3 ; i <= fin ; i++) {
            System.out.print(" " + t[i]) ;
        }
        System.out.print("\n") ;
    }

    public static void main(String[] args) {
        Random alea = new Random(42) ;

        for (int i=0 ; i<taille ; i++) {                          // Remplissage aléatoire du tableau
            tableau[i] = alea.nextInt(2*borne) - borne ;
        }
        System.out.print("Tableau initial : ") ;
        afficher(tableau, 0, taille -1) ;                         // Affiche le tableau à trier

        System.out.println("Démarrage du tri rapide.") ;
        long débutDuTri = System.nanoTime();

        TriRapideParallele tri = new TriRapideParallele( 0, taille-1);

        executeur.execute(tri);

        while(nbTaches.get() != 0){

        }
        executeur.shutdown();
        // Il faut maintenant attendre la fin des calculs

        try {
            while (!executeur.awaitTermination(1, TimeUnit.SECONDS)) {
                System.out.print("#");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         // Il n'y a plus aucune tâche à soumettre

        long finDuTri = System.nanoTime();
        long duréeDuTri = (finDuTri - débutDuTri) / 1_000_000 ;
        System.out.print("Tableau trié : ") ;
        afficher(tableau, 0, taille -1) ;                         // Affiche le tableau obtenu
        System.out.println("obtenu en " + duréeDuTri + " millisecondes.") ;
    }

    @Override
    public void run() {
        trierRapidement();
    }
}


/*
  $ make
  javac *.java
  $ java TriRapide
  Tableau initial :  4967518 -8221265 -951337 4043143... -4807623 -1976577 -2776352 -6800164
  Démarrage du tri rapide.
  Tableau trié :  -9999981 -9999967 -9999957 -9999910... 9999903 9999914 9999947 9999964
  obtenu en 85 millisecondes.
*/