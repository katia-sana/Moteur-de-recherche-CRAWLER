import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.*;

public class crawler {
	
	//Dossier qui va contenir l'ensemble des textes récuperés
	public static String DOSSIER_TXT = "C:/Users/Sakher/crawler/txt/";
	
	//Fichier qui contient 120 liens initiaux
	public static String LINKS_FILE = "C:/Users/Sakher/liens.txt";
	
	public static int MAX_LIENS=2000000;
	
	//Un set qui contient les empreites des fichiers sauvegardés
	public static Set <byte []> Empreintes= new HashSet<byte []>();
	
	
	//Fonction qui permet de sauvegarder un lien après le nettoyage
	static void sauvegarder_fichier_nettoyer(String text,  int k, String domaine) throws IOException, BoilerpipeProcessingException, NoSuchAlgorithmException
	{
			//Calculer l'enmpeinte du contenue du lien
        	byte[] bytesOfMessage = text.getBytes("UTF-8");
        	MessageDigest md = MessageDigest.getInstance("MD5");
        	byte[] thedigest = md.digest(bytesOfMessage);

        	
        	if(!Empreintes.contains(thedigest))
        	{
        		//Sauvegarder le fichier dans l'arboriscence : domaine/fichier.txt
        		String arbo=DOSSIER_TXT+domaine;
        		File dir = new File(arbo);
        		dir.mkdirs();
        		
            	File ff=new File(arbo+"/url"+k+""+".txt"); // d�finir l'arborescence
        		ff.createNewFile();
        		FileWriter ffw=new FileWriter(ff);
        		ffw.write(text);
        		ffw.close();
        		
        		//ajouter l'empreinte
        		Empreintes.add(thedigest);
        	}
        
	}
	
	public static void main(String[] args) throws Exception {

 
		//Une liste tabou des liens à ne pas traiter 
		Set <String> tabou= new HashSet<String>();
		
		// Key: nom du domaine, Value: un set contenant les liens à traiter de ce dernier
        HashMap <String,Set<String>> liste_liens= new HashMap <String,Set<String>> ();
        
        Double fichiers_non_telecharges=0.0;
         
        // Première phase de filtrage des 120 liens	
		try{
			BufferedReader buff1 = new BufferedReader(new FileReader(LINKS_FILE));
			
			try {
				//pour les 100 liens 
				String line1=buff1.readLine();
				while ((line1 = buff1.readLine()) != null)
				{
					if(new URL(line1).getProtocol().equals("http"))
					{
						 try
		                    {
							 	//Definir l'agent et faire la connection
		                    	Connection connection = Jsoup.connect(line1).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(3000);
		                    	Connection.Response reponse=connection.execute();
		                        if(reponse.statusCode() == 200)// si la page existe, et de type html/text
		                    	{
									Set <String> temp=new HashSet <String>();
									temp.add(line1);
									liste_liens.put(new URL(line1).getHost(), temp);
		                    	}
		                    }
							 catch(IOException e) {
			                        System.out.println("io - "+e);
			                }
						 
					}
				}
				
			} finally 
			{
				// dans tous les cas, on ferme nos flux
				buff1.close();

			}
			} catch (IOException ioe) {
				// erreur de fermeture des flux
				System.out.println("Erreur --" + ioe.toString());
			}

		
		
		ArrayList <String> domaines_list=new ArrayList<String>(liste_liens.keySet());
		int domindex=0;
		String Domainecourrant;
		
		int k=0;
		
		while (domindex<domaines_list.size() && k<MAX_LIENS)
		{
			Domainecourrant=domaines_list.get(domindex);
				System.err.println("########"+Domainecourrant+" Domaine N "+(domindex+1));
				Set <String> liens= liste_liens.get(Domainecourrant);
				
		        Iterator<String> link_list_iterator = liens.iterator();
		    	String url = "";
		    	int quota=0;
		    	while(link_list_iterator.hasNext() && quota<100) {
		    		//
		    		url = link_list_iterator.next();
		    		System.out.println(k+"- Fetching "+url);
            		k++;
            		quota++;
		        	if (!tabou.contains(url))//verifier si on a déjà vu le lien
		        	{       
		                    try
		                    {//.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
		                    	Connection connection = Jsoup.connect(url).timeout(3000);
		                    	Connection.Response reponse=connection.execute();
		                        if(reponse.statusCode() == 200)// si la page existe, et de type html/text
		                    	{
		                    		Document doc = connection.get(); // la page HTML
		                    		Element body=doc.body();
		                    		if (body!=null)
		                    		{
		                    			String text=doc.body().text();
		                    			if (text.length()!=0)
		                    				sauvegarder_fichier_nettoyer(text,k,new URL(url).getHost());
		                    		}
		                        
		                        	Elements links = doc.select("a[href]");

		                            System.out.println("Liens ajoutés: "+links.size());
		                            for (Element link : links) 
		                            {
		                            	//Si le lien a une taille>0 et protocole = http on l'insert
		                            	if ((link.attr("abs:href").length()!=0)&&(new URL(link.attr("abs:href")).getProtocol().equals("http")))
		                            	{
		                            		if(liste_liens.containsKey(new URL(link.attr("abs:href")).getHost()))
		                            		{
		                            			//si le domaine existe, on insert le lien dans son set
		                            			liste_liens.get(new URL(link.attr("abs:href")).getHost()).add(link.attr("abs:href"));
		                            		}
		                            		else
		                            		{
		                            			//sinon, on insert le domaine, et son nouveau set 
		                            			Set <String> temp=new HashSet <String>();
		                    					temp.add(link.attr("abs:href"));
		                    					liste_liens.put(new URL(link.attr("abs:href")).getHost(), temp);
		                    					domaines_list.add(new URL(link.attr("abs:href")).getHost());
		                            		}
		                            	}
		                            		
		                            }
		                            
		                            //Ajouter le lien à la liste tabou
		                            tabou.add(url);
		                            liste_liens.get(new URL(url).getHost()).remove(url);
		                            liens= liste_liens.get(Domainecourrant);
		                            link_list_iterator = liens.iterator();
		                            System.out.println("Liens internes ajoutés "+liens.size());
		                        }
		                        else //if the website does not exist
		                        {
		                     		liste_liens.get(new URL(url).getHost()).remove(url);
		                        	tabou.add(url);
		                        	liens= liste_liens.get(Domainecourrant);
		                        	link_list_iterator = liens.iterator();
		                        }
		                        
		                    }catch(IOException e) {
		                    	fichiers_non_telecharges++;
		                        // Site inaccessible
		                        //System.out.println("io - "+e);
		                    	
		                    }
		                    	            		
		        	}
		        }
		domindex++;
		System.err.println("Err ==== "+(fichiers_non_telecharges/(k*1.0)));
		}
    	    	
    }
}
