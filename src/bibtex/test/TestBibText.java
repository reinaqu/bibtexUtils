package bibtex.test;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import javax.xml.ws.handler.Handler;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.Value;

import bibtex.BibTexUtils;


//NOTA 1:
//No se traga bien las referencias que dentro de las llaves tienen otras llaves,
//por ejemplo:
//@inproceedings{Liu:2018:STS:3238147.3240728,
//	 author = {Liu, Han and Liu, Chao and Zhao, Wenqi and Jiang, Yu and Sun, Jiaguang},
//	 title = {S-gram: Towards Semantic-aware Security Auditing for Ethereum Smart Contracts},
//	 booktitle = {Proceedings of the 33rd ACM/IEEE International Conference on Automated Software Engineering},
//	 series = {ASE 2018},
//	 year = {2018},
//	 isbn = {978-1-4503-5937-5},
//	 location = {Montpellier, France},
//	 pages = {814--819},
//	 numpages = {6},
//	 url = {http://doi.acm.org/10.1145/3238147.3240728},
//	 doi = {10.1145/3238147.3240728},
//	 acmid = {3240728},
//	 publisher = {ACM},
//	 address = {New York, NY, USA},
//	 keywords = {language modeling, security auditing, static semantic labeling, {Smart contracts},
//	}
//En la clave keywords, Smart contracts está encerrado entre comilla doble
public class TestBibText {

	 private final static Logger logger = Logger.getLogger(TestBibText.class.getName());
	 private static ConsoleHandler ch = null;
	 
	public static void main(String[] args) throws Exception {
		setup();
		BibTeXParser parser = new BibTeXParser();

		BibTeXDatabase database1 = BibTexUtils.createDatabase(parser, "./res/scopus_SEARCH10.bib");
		BibTeXDatabase database2 = BibTexUtils.createDatabase(parser, "./res/scopus_SEARCH10_bis.bib");
		
		BibTeXDatabase database3 = BibTexUtils.removeAll(database1, database2);
		List<String> lista = BibTexUtils.getAllValuesWithKey(database3, new Key("year"));
		System.out.println(lista);
		System.out.println(lista.stream()
				                .map(Integer::valueOf)
				                .allMatch(n->n>=2018));
		
		BibTexUtils.toBibTexFile(database3, "./res/scopus_SEARCH10_dif.bib");
		
		
	}
	
	static public void setup() throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
    //    FileHandler fileTxt = new FileHandler("Logging.txt");
    //    SimpleFormatter formatterTxt = new SimpleFormatter();
    //    fileTxt.setFormatter(formatterTxt);
    //    logger.addHandler(fileTxt);
	}
}
