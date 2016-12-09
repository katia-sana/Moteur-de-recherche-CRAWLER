package tp.tp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ExpesStructures {

	private static final int MAX = 10000000;
	
	private static void constructionTableauInt() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de type int");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		int[] tab = new int[MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab[i] = i;
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void constructionTableauInteger() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de type Integer");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		Integer[] tab = new Integer[MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab[i] = new Integer(i);
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void constructionArrayListInteger() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'une ArrayList de type Integer");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		ArrayList<Integer> tab = new ArrayList<Integer>();
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab.add(i);
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void constructionTableauChar() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de char");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		char[] tab = new char[MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab[i] = 'a';
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void constructionString() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'un String");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		String tab = "";
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab.concat("a");
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}

	private static void constructionStringBuilder() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'un StringBuilder");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		StringBuilder tab = new StringBuilder();
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab.append("a");
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void constructionHashMap() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'une HashMap<Integer, String>");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		HashMap<Integer, String> tab = new HashMap<Integer, String>();
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab.put(i, "test");
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void constructionTableauString() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de String");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		String[] tab = new String[MAX];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab[i] = "test";
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void constructionTableauTableauChar() {
		int chronoId = Utils.startChrono();
		System.out.println("Construction d'un tableau de tableaux de char");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		char[][] tab = new char[MAX][];
		System.out.println("  Liste allouée");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		for (int i = 0 ; i < MAX ; i++) {
			tab[i] = new char[4];
			tab[i][0] = 't';
			tab[i][1] = 'e';
			tab[i][2] = 's';
			tab[i][3] = 't';
		}
		System.out.println("  Liste remplie");
		System.out.println("    Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("------------------------------------------------------");
	}
	
	private static void testArrayList() {
		ArrayList<Integer> tab = new ArrayList<Integer>();
		System.out.println("Test ArrayList");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Ajout d'éléments");
		int chronoId = Utils.startChrono();
		for (int i = 0 ; i < MAX ; i++) {
			tab.add(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Recherche d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0 ; i < 100000 ; i++) {
			tab.contains(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Suppression d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0 ; i < 1000 ; i++) {
			tab.remove(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("------------------------------------------------------");
	}
	
	private static void testHashSet() {
		HashSet<Integer> tab = new HashSet<Integer>();
		System.out.println("Test HashSet");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Ajout d'éléments");
		int chronoId = Utils.startChrono();
		for (int i = 0 ; i < MAX ; i++) {
			tab.add(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Recherche d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0 ; i < 100000 ; i++) {
			tab.contains(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("  Suppression d'éléments");
		chronoId = Utils.startChrono();
		for (int i = 0 ; i < 1000 ; i++) {
			tab.remove(new Integer(i));
		}
		System.out.println("    Terminé en " + Utils.endChrono(chronoId) + " secondes");
		System.out.println("      Mémoire utilisée : " + Utils.getUsedMemory() / 1024 + " Ko");
		System.out.println("------------------------------------------------------");
	}
	

	public static void main(String[] args) {
		// Tableau vs Liste d'entiers
		/////////////////////////////////
//		System.out.println("On démarre...");
////		Utils.waitKeyPressed(); // Commande à utiliser pour lancer ou consulter la jconsole
//		constructionTableauInt();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
////		Utils.waitKeyPressed(); 
//		constructionTableauInteger();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
////		Utils.waitKeyPressed(); 
//		constructionArrayListInteger();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
//		Utils.waitKeyPressed(); 
		// Tableau vs Chaîne de caractères vs StringBuilder
		/////////////////////////////////
//		constructionTableauChar();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
////		Utils.waitKeyPressed(); 
//		constructionString();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
////		Utils.waitKeyPressed();
//		constructionStringBuilder();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();

		// HashMap vs Tableau vs Tableau de Tableaux
		/////////////////////////////////
//		constructionHashMap();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
////		Utils.waitKeyPressed(); 
//		constructionTableauString();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
////		Utils.waitKeyPressed(); 
//		constructionTableauTableauChar();
////		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
		// ArrayList vs HashSet
		/////////////////////////////////
//		testArrayList();
//		Utils.waitKeyPressed(); 
//		System.out.println("Lancement du gc");
//		System.gc();
//		Utils.waitKeyPressed(); 
//		testHashSet();
//		Utils.waitKeyPressed(); 
	}

}
