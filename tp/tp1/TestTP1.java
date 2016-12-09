package tp.tp1;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tp.tp1.TP1;

public class TestTP1 {

	@Test
	public void testStemming() throws IOException {
		File file = new File(TP1.FILENAME);
		ArrayList<String> words = TP1.stemming(file);
		assertEquals(words.toString(), "[le, déleg, israélien, et, palestinien, ont, décid, de, report, sin, di, leur, séanc, de, négoci, prévu, lund, 21, octobr, ,, à, eilat, ,, sur, le, littoral, israélien, de, la, mer, roug, ., cet, décis, a, été, pris, par, \", le, chef, de, déleg, palestinien, et, israélien, saëb, erakat, et, dan, shomron, (...), dan, la, nuit, de, dimanch, à, lund, \",, a, précis, ,, lund, matin, 21, octobr, ,, mosh, vogel, ,, respons, du, bureau, de, press, gouvernemental, à, jérusalem, ., ce, discuss, ont, été, engag, le, 6, octobr, à, la, suit, de, la, flamb, de, violenc, consécut, à, l', ouvertur, par, le, israélien, d', un, tunnel, archéolog, dan, la, vieil, vill, de, jérusalem, ., le, gouvern, de, benyamin, nétanyahou, souhait, modifi, le, plan, de, partag, d', hébron, défin, par, le, accord, de, washington, sign, en, septembr, 1995, pour, proteg, 450, colon, extrem, install, au, coeur, de, cet, vill, palestinien, de, 120, 000, habit, .]");
	}
	
	@Test
	public void testGetTermFrequenciesWithTokenizer1() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, tokenizer);
		
		// Vérification taille résultat
		assertEquals(100, tfs.size());
	}

	@Test
	public void testGetTermFrequenciesWithTokenizer2() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, tokenizer);
		
		// Toujours tester avec un mot avec accent !
		assertEquals(new Integer(5), tfs.get("à"));
	}

	@Test
	public void testGetTermFrequenciesWithTokenizer3() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, tokenizer);
		
		// Mot non racinisé
		assertEquals(new Integer(1), tfs.get("négociations"));
	}
	
	@Test
	public void testGetTermFrequenciesWithTokenizer4() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, tokenizer);
		
		// Mot racinisé
		assertEquals(null, tfs.get("engag"));
	}
	
	
	
	@Test
	public void testGetTermFrequenciesWithStemmer1() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, stemmer);
		
		// Vérification taille résultat
		assertEquals(95, tfs.size());
	}

	@Test
	public void testGetTermFrequenciesWithStemmer2() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, stemmer);
		
		// Toujours tester avec un mot avec accent !
		assertEquals(new Integer(5), tfs.get("à"));
	}

	@Test
	public void testGetTermFrequenciesWithStemmer3() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, stemmer);
		
		// Mot non racinisé
		assertEquals(null, tfs.get("négociations"));
	}	
	
	@Test
	public void testGetTermFrequenciesWithStemmer4() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File file = new File(TP1.FILENAME);
		
		HashMap<String, Integer> tfs = TP1.getTermFrequencies(file, stemmer);
		
		// Mot racinisé
		assertEquals(new Integer(1), tfs.get("engag"));
	}

	@Test
	public void testGetCollectionFrequenciesWithTokenizer1() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, tokenizer);
		
		// Vérification taille résultat
		assertEquals(3346, cfs.size());
	}

	@Test
	public void testGetCollectionFrequenciesWithTokenizer2() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, tokenizer);
		
		assertEquals(new Integer(4), cfs.get("ministère"));
	}
	
	@Test
	public void testGetCollectionFrequenciesWithTokenizer3() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, tokenizer);
		
		assertEquals(new Integer(226), cfs.get("à"));
	}	
	
	@Test
	public void testGetCollectionFrequenciesWithTokenizer4() throws IOException {
		FrenchTokenizer tokenizer = new FrenchTokenizer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, tokenizer);
		
		assertEquals(null, cfs.get("minister"));
	}	
	
	@Test
	public void testGetCollectionFrequenciesWithStemmer1() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, stemmer);
		
		// Vérification taille résultat
		assertEquals(2701, cfs.size());
	}

	@Test
	public void testGetCollectionFrequenciesWithStemmer2() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, stemmer);
		
		assertEquals(null, cfs.get("ministère"));
	}
	
	@Test
	public void testGetCollectionFrequenciesWithStemmer3() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, stemmer);
		
		assertEquals(new Integer(226), cfs.get("à"));
	}	
	
	@Test
	public void testGetCollectionFrequenciesWithStemmer4() throws IOException {
		FrenchStemmer stemmer = new FrenchStemmer();
		File dir = new File(TP1.DIRNAME);
		
		HashMap<String, Integer> cfs = TP1.getCollectionFrequency(dir, stemmer);
		
		assertEquals(new Integer(4), cfs.get("minister"));
	}	
}
