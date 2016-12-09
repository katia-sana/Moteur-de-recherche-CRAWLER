package tp.tp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collections;
import java.util.Comparator;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

public class TP3 {
	
	//Le fichier contenant les mots vides	
	private static String STOPWORDS_FILENAME = "C:/Users/Sakher/Google Drive/Etudes/TC3 RI/Ma Solution TP/frenchST.txt";
	
	//Le fichier inverse
	public static String fichier_inverse = "C:/Users/Sakher/FinalFiles/InvFile.txt";
	
	//Le fichier des poids
	private static String WEIGHTS2 = "C:/Users/Sakher/FinalFiles/weights.txt";
	
	//Le fichier contenant pour chaque ID, le code associé
	private static String CODES = "C:/Users/Sakher/FinalFiles/codes";
	
	private static HashMap <String, String> code_id=new HashMap<String, String>();
	
	//Le fichier contenant la requete
	public static String req_pond = "C:/Users/Sakher/req_pond.poids";
	
	//Fonction qui calcule la similarité entre une requete et un fichier
	public static double getSimilarity(HashMap <String,Double> terms_req, String file, HashMap <String,String> inverted_hach_file, Double fileweight)
	{
		HashMap<String, Double> doc_freq ;
		double sommeUp=0;
		double sommeDown2=0;
		
		for (Entry<String, Double> req : terms_req.entrySet()) 
		{
			if(inverted_hach_file.containsKey(req.getKey()))
			{
				doc_freq = new HashMap<String, Double>();
				
				//table d'hachage : clé term ... valeur poids 
				String doc_tf=inverted_hach_file.get(req.getKey()); //  doc:tf,doc:tf,doc:tf,doc:tf
				
				String [] liste_docs=doc_tf.split(","); // [doc:tf] [doc:tf] [doc:tf] 
				int df=liste_docs.length;
				for(int i=0;i<df;i++)
				{
					String [] temp=liste_docs[i].split(":"); // [doc] [tf] 
					doc_freq.put(temp[0], Double.parseDouble(temp[1]));
				}
				Double w;
				// term:  df   doc:tf,doc:tf,doc:tf,doc:tf  w(i,f)
				if (doc_freq.containsKey(file))
				{
					double idf=Math.log(9714.0/(df*1.0));
					w=doc_freq.get(file)*idf;
				}
				else w=0.0;
				
				sommeUp+=w*req.getValue(); // w * w
			}
			sommeDown2+=Math.pow(req.getValue(), 2);
		}
		return (sommeUp)/(Math.sqrt(fileweight)*Math.sqrt(sommeDown2)); 
	}
	
	//Focntion qui calcule la similarité entre la requete et un ensemble de fichiers
	public static HashMap<String, Double>  getSimilarDocuments(HashMap <String,Double> terms_req, Set<String> fileList,HashMap <String,String> inverted_hach_file) throws IOException
	{
		HashMap<String, Double> choisit = new HashMap<String, Double>();
		HashMap<String, Double> weights_of_docs=new HashMap<String,Double>();
		try{
			BufferedReader fichier_poids = new BufferedReader(new FileReader(WEIGHTS2));
			try {
				String line;
				//commencer par enregistrer l'ensemble des termes de Fichier 1 dans une HTable
				while ((line = fichier_poids.readLine()) != null) 
				{
					String[] temp_line=line.split("\t");
					weights_of_docs.put(temp_line[0], Double.parseDouble(temp_line[1]));				
				}
		}
		finally 
		{
			// dans tous les cas, on ferme nos flux
			fichier_poids.close();
		}	
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		Iterator<String> it = fileList.iterator();
		while (it.hasNext()) {
			// pour chaque fichier dans la liste de fusion
			String f=it.next();
			choisit.put( code_id.get(f), getSimilarity(terms_req,  f, inverted_hach_file,weights_of_docs.get(f+".poids")));
			
		}
		
		return choisit;
	}
	
	
	//Trier et afficher les K premiers résultats 
	public static void Afficher_resultats(HashMap<?, Double> first, int k){
		ArrayList as = new ArrayList( first.entrySet() );
        Collections.sort( as , new Comparator() {
            public int compare( Object o1 , Object o2 )
            {
                Map.Entry e1 = (Map.Entry)o1 ;
                Map.Entry e2 = (Map.Entry)o2 ;
                Double first = (Double)e1.getValue();
                Double second = (Double)e2.getValue();
                return second.compareTo( first );
            }
        });
        int j=0;
        Iterator i = as.iterator();
        System.out.println("Document\t\tPertinence");
        while ( (i.hasNext()) && (j<k))
        {
            System.out.println( (Map.Entry)i.next() );
            j++;
        }
	}


	//Main
	public static void main(String[] args) {
		try {
			
			HashMap <String, Double> terms_req=new HashMap<String, Double>();	
			
			Normalizer stemmerAllWords = new FrenchStemmer();
			Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));
			Normalizer tokenizerAllWords = new FrenchTokenizer();
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));
			
			Scanner e=new Scanner(System.in);
			System.out.println("Entrer la requête");
			String requete=e.nextLine();
			requete=requete.replace("'", " ");
			
			//temps d'execution
			long startTime = System.currentTimeMillis();
			 
			//Commencer par traiter la requete et supprimer les mots vides
			ArrayList<String> words = stemmerNoStopWords.normalize(requete);
			 for (String word : words) {
				   word = word.toLowerCase();
				   terms_req.put(word, 1.0);
				   System.err.println(word);
			 }
			 
			//Un set qui va contenir l'ensemble des fichiers dans lesquels les terms de la requete appartient 
			Set<String> fileList = new HashSet<String>();
			//Clé va contenir un term, valeur la chaine[DOC:TF, DOC:TF .....]
			HashMap<String, String> inverted_hach_file = new HashMap<String, String>();

			//lire le fichier inverse , le fichier qui va contenir les termes de la requete, et le fichier des CODES
			try{
				BufferedReader buff_f_inv = new BufferedReader(new FileReader(fichier_inverse));
				BufferedReader buff_req = new BufferedReader(new FileReader(req_pond));
				BufferedReader cd_id = new BufferedReader(new FileReader(CODES));
				
				try {
					String line;
					while ((line = buff_f_inv.readLine()) != null) 
					{
						String[] temp_line=line.split("\t");
						if (temp_line.length==3)
						{
							if(terms_req.containsKey(temp_line[0]))
							{
								inverted_hach_file.put(temp_line[0],temp_line[2]);//Term - doc:tf , doc:tf .....
								
								//Mettre à jour le poid du terme dans la requete
								Double idf=Math.log(9714.0/Double.parseDouble(temp_line[1]));
								terms_req.replace(temp_line[0], idf);
							}
						}
					}
					
					
					
					for (Entry<String, String> ihf : inverted_hach_file.entrySet()) 
					{
						String temporary=ihf.getValue(); // doc:tf,doc:tf....
						String[] temp_doc_tf=temporary.split(","); //doc:tf
						for (int i=0;i<temp_doc_tf.length;i++)
						{
							String[] temp_doc_tf2=temp_doc_tf[i].split(":"); 
							fileList.add(temp_doc_tf2[0]);
						}
					}
					
					while ((line = cd_id.readLine()) != null) 
					{
						String[] temp_line=line.split("\t");
						if(fileList.contains(temp_line[1]))
							code_id.put(temp_line[1], temp_line[0]);				
					}
					
						
			} finally 
			{
				// dans tous les cas, on ferme nos flux
				buff_f_inv.close();
				buff_req.close();
				cd_id.close();
			}
			} catch (IOException ioe) {
				// erreur de fermeture des flux
				System.out.println("Erreur --" + ioe.toString());
		}
			long stopTime1 = System.currentTimeMillis();
			System.out.println(fileList.size()+ " fichiers trouvées en "+(stopTime1-startTime)+" ms");
			
			// Calculer la perminance de chaque document de fileList avec la requete
			Afficher_resultats(getSimilarDocuments(terms_req,fileList,inverted_hach_file),20);
			long stopTime2 = System.currentTimeMillis();
			System.out.println("Opération terminée en "+(stopTime2-startTime)+" ms");
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
