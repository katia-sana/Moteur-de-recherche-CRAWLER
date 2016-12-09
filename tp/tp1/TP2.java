package tp.tp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;


/**
 * TP 2
 * @author Sakher
 *
 */
public class TP2 {
	
	//Le répertoire du corpus, fichiers data
	protected static String DIRNAME = "C:/Users/Sakher/Desktop/2015/";

	//Le fichier contenant les mots vides
	private static String STOPWORDS_FILENAME = "C:/Users/Sakher/Google Drive/Etudes/TC3 RI/Ma Solution TP/frenchST.txt";

	//le dossier qui va contenir les fichiers poids temporaires ( sera supprimé après )
	public static String WEIGHTS_DIR = "C:/Users/Sakher/sorties";
	
	//Ce dossier va contenir l'ensemble des fichier de sous index
	public static String SUB_DIR = "C:/Users/Sakher/sub/";

	//Ce dossier va contenir les fichiers inverses temporaires
	protected static String outFile = "C:/Users/Sakher/inverse/";
	
	//Le dossier final, contient le fichier inverse, le fichier CODE, et le fichier des poids
	protected static String FinalFiles = "C:/Users/Sakher/FinalFiles/";

	//Sauvegarder les codes générés dans ce fichier
	protected static String CodesFile = "C:/Users/Sakher/FinalFiles/codes";
	
	//HashMap key:id du fichier, valeur: code associé
	public static HashMap <String,Integer> code_id = new HashMap <String,Integer>();

	
	//Calcule du DF 
	public static HashMap<String, Integer> getDocumentFrequency(File dir, Normalizer normalizer) throws IOException {
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		if (dir.isDirectory()) {
			// Liste des fichiers du répertoire
			File[] files = dir.listFiles();
			// Parcours des fichiers et remplissage de la table			
			for (File file : files) {
				// appler la méthode de calcule des fréquences 
					HashMap<String, Integer> tfs = getTermFrequencies(file, normalizer);
					
					for (Map.Entry<String, Integer> tf : tfs.entrySet()) {
						
						if(hits.containsKey(tf.getKey()))
						{
							int r=hits.get(tf.getKey());
							r+=1;
							hits.put(tf.getKey(), r);
						}
						else hits.put(tf.getKey(), 1);
								
					}
			}
		}
		return hits;	
	}
	
	public static HashMap<String, Integer>  getTermFrequencies(File file, Normalizer normalizer) throws IOException {
		// Création de la table des mots
		HashMap<String, Integer> hits = new HashMap<String, Integer>();
		int r;
		// Appel de la méthode de normalisation
		ArrayList<String> words = normalizer.normalize(file);
		// Pour chaque mot de la liste, on remplit un dictionnaire
		// du nombre d'occurrences pour ce mot
		for (String word : words) {
			word = word.toLowerCase();
			// on récupère le nombre d'occurrences pour ce mot
			// Si ce mot n'était pas encore présent dans le dictionnaire,
			// on l'ajoute (nombre d'occurrences = 1)
			// Sinon, on incrémente le nombre d'occurrence
			if(hits.containsKey(word))
			{
				
				r=hits.get(word);
				r+=1;
				hits.put(word, r);
			}
			else hits.put(word, 1);
		}
		return hits;
	}
	
	public static void create_weights2(File dir) throws IOException
	{
		//Cette méthodes permet de créer un fichiers contenant la somme carré des poids:
		// Ex:  fichier1 654,6 fichier2 148 .....
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			
			File ff=new File(FinalFiles+"weights.txt"); 
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
		
			for (File file : files) {
				Double somme=0.0;
				try{
					BufferedReader fichier_poids = new BufferedReader(new FileReader(file));
					try {
						String line;
						//Pour chaque fichier poids, calcule la somme des poids de ses terms au carré
						while ((line = fichier_poids.readLine()) != null) 
						{
							String[] term_weight=line.split("\t");
							somme+=Math.pow(Double.parseDouble(term_weight[1]),2);
						}
					}
				finally 
				{
					fichier_poids.close();
					
					// on insert le nom du fichier et la somme dans le fichier weights.txt
					ffw.write(file.getName()+"\t"+somme+"\n");
				    
				}	
				}catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			ffw.close();
		}
	}


	//Calcule le tf.idf des mots d'un fichier 
	public static HashMap<String, Double> getTfIdf(File file, HashMap<String, Integer> dfs, int documentNumber, Normalizer normalizer) throws IOException {
		HashMap<String, Double> tfIdfs = new HashMap<String, Double>();
		double idf;
		//construire la table TF:
		HashMap<String, Integer> tfs = getTermFrequencies(file, normalizer);
		
		for (Map.Entry<String, Integer> tf : tfs.entrySet()) {
			//calculer le idf, puis ajouter la valeur dans le tableau
			idf=Math.log((float)documentNumber/(dfs.get(tf.getKey())*1.0));
			tfIdfs.put(tf.getKey(), tf.getValue() * idf);
		}
		return tfIdfs;
	}
	
	/**
	 * Crée, pour chaque fichier d'un répertoire, un nouveau
	 * fichier contenant les poids de chaque mot.
	 */
	private static void getWeightFiles(File inDir, File outDir, Normalizer normalizer) throws IOException {
		if (inDir.isDirectory()) {
			File[] files = inDir.listFiles();
			//calcule de df
			HashMap<String, Integer> dfs=getDocumentFrequency(inDir,normalizer);
			int documentNumber=files.length;
			for (File file : files) {
					//calcule des poids de chaque fichier
					HashMap<String, Double> tf_idf=getTfIdf(file,dfs,documentNumber,normalizer);
			
					//on crée un fichier . poids pour ce fichier
					File dir = outDir;
					dir.mkdirs();
					
					File ff=new File(outDir+"/"+code_id.get(file.getName())+".poids"); // d�finir l'arborescence
					ff.createNewFile();
					FileWriter ffw=new FileWriter(ff);
					ArrayList<String> v = new ArrayList<String>(tf_idf.keySet());
				    Collections.sort(v);
				    for (String str : v) {
				    	if (str!="   ")
				    		ffw.write(str+"\t"+tf_idf.get(str)+"\n");
				    }
					ffw.close();
				
			}
		}
			
	}
	
	//Fonction qui analyse l’ensemble des fichiers d’un r´epertoire et qui collecte, pour chaque mot, la liste des
	//fichiers dans lesquels ce mot apparaˆıt ainsi que le nombre d’occurrences de ce mot dans le fichier.
	
	public static TreeMap<String, TreeMap<String, Integer>> getInvertedFileWithWeights(File dir, Normalizer normalizer,Set<String> files_list) throws IOException
	{
		TreeMap<String, TreeMap<String, Integer>> InvertedWithWeights = new TreeMap<String, TreeMap<String, Integer>> ();
		
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (files_list.contains(file.getName()))
				{
					//File source =file;
					//String path = (SUB_DIR);
					//Files.copy(source.toPath(), (new File(path + source.getName())).toPath(), StandardCopyOption.REPLACE_EXISTING);
					
					
					TreeMap<String, Integer> temp=new TreeMap<String, Integer>();
					// appeler la m�thode de calcule des fr�quences 
					HashMap<String, Integer> tfs = getTermFrequencies(file, normalizer);
					for (Map.Entry<String, Integer> tf : tfs.entrySet()) {
						if(InvertedWithWeights.containsKey(tf.getKey()))
						{
							temp=InvertedWithWeights.get(tf.getKey());
							temp.put(""+code_id.get(file.getName()),tfs.get(tf.getKey()));//modification ici
							InvertedWithWeights.put(tf.getKey(), temp);
						}
						else {
							temp=new TreeMap<String, Integer>();
							temp.put(""+code_id.get(file.getName()),tf.getValue());
							InvertedWithWeights.put(tf.getKey(), temp);
						}	
					}
				}
			}
		}

		return InvertedWithWeights;
	}
	
	
	

	public static void saveInvertedFileWithWeights(TreeMap<String, TreeMap<String, Integer>> invertedFile, File outFile, File Path) throws IOException
	{
		if (invertedFile.isEmpty()==false)
		{
			//String = code, integer = TF
			TreeMap<String, Integer> temp=new TreeMap<String, Integer>();
			if (Path.exists())
			{
				File ff=new File(outFile.getPath()); // d�finir l'arborescence
				ff.createNewFile();
				FileWriter ffw=new FileWriter(ff);
				ArrayList<String> v = new ArrayList<String>(invertedFile.keySet());
			    Collections.sort(v);
			    for (String str : v) {
			    	temp=invertedFile.get(str);
					String files="";
			    	Iterator<String> it = temp.keySet().iterator();
			    	int i = 0;
			    	String current = "";
			    	while(it.hasNext() && i < temp.size()) {
			    	   current = it.next();
			    	   files+=current+":"+temp.get(current)+",";
			    	   }
			    	ffw.write(str+"\t"+temp.size()+"\t"+files+"\n");
			    }
				ffw.close();
			}
			else {
				//créer le dossier
				//System.out.println(outFile.getParent());
				File dir = Path;
				dir.mkdirs();
				
				File ff=new File(outFile.getPath()); // d�finir l'arborescence
				ff.createNewFile();
				FileWriter ffw=new FileWriter(ff);
				ArrayList<String> v = new ArrayList<String>(invertedFile.keySet());
			    Collections.sort(v);
			    for (String str : v) {
			    	temp=invertedFile.get(str);
					String files="";
			    	Iterator<String> it = temp.keySet().iterator();
			    	int i = 0;
			    	String current = "";
			    	while(it.hasNext() && i < temp.size()) {
			    	   current = it.next();
			    	   files+=current+":"+temp.get(current)+",";
			    	   }
			    	ffw.write(str+"\t"+temp.size()+"\t"+files+"\n");
			    }
				ffw.close();
			}
			
		}
	}
		
	//Fct qui prend en param`etres les deux fichiers inverses des sous-corpus (deux premiers param`etres), et
	//g´en`ere un nouveau fichier inverse (troisi`eme param`etre) correspondant `a la fusion des deux premiers,
	//au mˆeme format
	public static void mergeInvertedFiles(File invertedFile1, File invertedFile2,File mergedInvertedFile) throws IOException
	{
		//lire les deux fichiers
		String filepath1=invertedFile1.getPath();
		String filepath2=invertedFile2.getPath();
		
		//la cr�ation du fichier de sortie
		File merged=new File(mergedInvertedFile.getPath()); 
		merged.createNewFile();
		FileWriter merged_writer=new FileWriter(merged);
		
		try{
			BufferedReader buff1 = new BufferedReader(new FileReader(filepath1));
			BufferedReader buff2 = new BufferedReader(new FileReader(filepath2));
			
			try {
				String line1=buff1.readLine();
				String line2=buff2.readLine();
				
				while ((line1 != null) && (line2 != null))
				{
					String list1[]=line1.split("\t");
					String list2[]=line2.split("\t");
					// premier cas:
					if (list1[0].compareTo(list2[0])==0)
					{
						//ins�rer une des deux, avec la concatination						
						Set<String> first_file_items = new HashSet<String>(Arrays.asList(list1[2].split(",")));
						Set<String> second_file_items = new HashSet<String>(Arrays.asList(list2[2].split(",")));
						
						first_file_items.addAll(second_file_items);

						String str_1 = StringUtils.join(first_file_items, ",");
						
						merged_writer.write(list2[0]+"\t"+first_file_items.size()+"\t"+str_1+"\n");
						
						line1=buff1.readLine();
						line2=buff2.readLine();	
						
					}
					//deuxi�me cas
					else if (list1[0].compareTo(list2[0])<0)
					{	
						merged_writer.write(line1+"\n");
						line1=buff1.readLine();
					}
					//3�me Cas
					else 
					{
						merged_writer.write(line2+"\n");
						line2=buff2.readLine();
					}
				}
				//sortie de a boucle :
				if(line1==null)
				{
					merged_writer.write(line2+"\n");
					//inserer le restant de Fichier 2 dans la sortie
					while ((line2 = buff1.readLine()) != null)
					{
						merged_writer.write(line2+"\n");
					}
				}
				else //line2==null
				{
					merged_writer.write(line1+"\n");
					//inserer le restant de Fichier 1 dans la sortie
					while ((line1 = buff1.readLine()) != null)
					{
						merged_writer.write(line1+"\n");
					}
				}
			} finally 
			{
				// dans tous les cas, on ferme nos flux
				buff1.close();
				buff2.close();
				merged_writer.close();
			}
			} catch (IOException ioe) {
				// erreur de fermeture des flux
				System.out.println("Erreur --" + ioe.toString());
			}
	}
	
	/**
	 * 
	 * Main, appels de toutes les méthodes . 
	 */
	public static void main(String[] args) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String outDirName = "C:\\Users\\Sakher\\sorties\\";
		String temp_poid;
		int code=0;
		try {
			Normalizer stemmerAllWords = new FrenchStemmer();
			Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));
			Normalizer tokenizerAllWords = new FrenchTokenizer();
			Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));
			//Normalizer[] normalizers = {stemmerAllWords, stemmerNoStopWords, 
			//		tokenizerAllWords, tokenizerNoStopWords};
			Normalizer[] normalizers = {stemmerNoStopWords};

			for (Normalizer normalizer : normalizers) {
				//Pour chaque type de normalizer,
				
				String name = normalizer.getClass().getName();
				if (!normalizer.getStopWords().isEmpty()) {
					name += "_noSW";
				}
				System.out.println("Normalisation avec " + name);
			    final DocumentBuilder builder = factory.newDocumentBuilder();
			    
			    //un set qui va contenir l'ensemble des IDs des fichiers qu'on doit traiter 
			    Set<String> liste_fichiers;
			    int k=0;
			    //On commence par ouvrir le fichier 'code', qui va associer un code à chaque id d'un fichier
			    File ff=new File(CodesFile); 
				ff.createNewFile();
				FileWriter ffw=new FileWriter(ff);
				
			    for (int mois=1;mois<=4;mois++)
			    {
			      	for (int jour=1;jour<=31;jour++)
				    {
			      		//Reinitialiser le set liste_fichiers
			       		liste_fichiers = new HashSet<String>();
			          	File xml_file=new File("C:/Users/Sakher/Desktop/subindex/2015/"+String.format("%02d", mois)+"/2015"+String.format("%02d", mois)+""+String.format("%02d", jour)+".xml");
			       		if(xml_file.exists())
				       	{
			       			final Document document= builder.parse(xml_file);							
						    final Element racine = document.getDocumentElement();					   
						    final NodeList racineNoeuds = racine.getChildNodes();
						    final int nbRacineNoeuds = racineNoeuds.getLength();
									
							
						    for (int i = 0; i<nbRacineNoeuds; i++) 
						    {
						    	if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) 
						    	{
						            final Element fichier = (Element) racineNoeuds.item(i);
						            //on insert l'id à la liste des IDs qu'on doit traiter
								    liste_fichiers.add(racine.getAttribute("date")+"_"+fichier.getAttribute("id")+".txt");
								    //On insert l'ID et le code assosié dans la HashMap code_id, puis dans le fichier CODE
								    code_id.put(racine.getAttribute("date")+"_"+fichier.getAttribute("id")+".txt", code);							    
								    ffw.write(racine.getAttribute("date")+"_"+fichier.getAttribute("id")+".txt"+"\t"+code+"\n");
								    code++;
							    }				
							}
							
						    // Se rendre dans le dossier 2015/mois/jour
							temp_poid=DIRNAME+"/"+String.format("%02d", mois)+"/"+String.format("%02d", jour);
														
							k++;
							
							//Construire le fichier inverse du dossier 2015/mois/jour, pour les fichiers qui sont dans liste_fichiers uniquement
							TreeMap<String, TreeMap<String, Integer>> InvertedWithWeights=getInvertedFileWithWeights(new File(temp_poid), normalizer,liste_fichiers);					
							
							//Sauvegarder le fichier inverse du dossier 2015/mois/jour
							saveInvertedFileWithWeights(InvertedWithWeights, new File(outFile+"/"+k+".txt"),new File(outFile));
				       	}
				    }
			    }
			    ffw.close();
			    
			    //Construire les fichiers .poids temporaires
			    getWeightFiles(new File(SUB_DIR), new File(outDirName), normalizer);
			    
			    //Utiliser les fichiers .poids créés pour construire un seul fichier
			    create_weights2(new File(outDirName));
			    
			    
			    //Fusion des fichiers inverses de chaque 'jour'  afin d'avoir un seul
				String invertedFile1 = outFile+"1.txt";
				String invertedFile2 = outFile+"2.txt";
				String mergedInvertedFile = outFile+"2_2.txt";
				mergeInvertedFiles(new File(invertedFile1), new File(invertedFile2),new File(mergedInvertedFile));
				for (int i=3;i<=k-1;i++)
				{
					invertedFile1= outFile+i+".txt";
					invertedFile2 = outFile+(i-1)+"_"+(i-1)+".txt";
					mergedInvertedFile = outFile+i+"_"+i+".txt";
					mergeInvertedFiles(new File(invertedFile1), new File(invertedFile2),new File(mergedInvertedFile));
				}
				{
					//sauvegarder le fichier inverse final 
					invertedFile1= outFile+k+".txt";
					invertedFile2 = outFile+(k-1)+"_"+(k-1)+".txt";
					mergedInvertedFile = FinalFiles+"InvFile.txt";
					mergeInvertedFiles(new File(invertedFile1), new File(invertedFile2),new File(mergedInvertedFile));
				}

			}
		}
		catch (final ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
		catch (final SAXException e) 
		{
			e.printStackTrace();
		}	
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
	}
}
