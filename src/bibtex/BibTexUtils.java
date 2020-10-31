package bibtex;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.BibTeXParser;
import org.jbibtex.Key;
import org.jbibtex.Value;

/**
 * @author reinaqu
 * Utilities to work with BibTeX databases created thanks to the jbibtex library (https://github.com/jbibtex/jbibtex) 
 *
 */
public class BibTexUtils {

	private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );
	
	/**
	 * @param parser BibTexParser used to parse the file
	 * @param path BibTex file path. 
	 * @return A BibTeXDatabase object. 
	 * @throws Exception
	 */
	public static BibTeXDatabase  createDatabase(BibTeXParser parser, String path) throws Exception{
		BibTeXDatabase res = parseFully(parser, path);
		List<Exception> exceptions = parser.getExceptions();
		LOGGER.log( Level.INFO, "Cargados...{0}", res.getEntries().size());
		LOGGER.log( Level.INFO, "Excepciones...{0}", exceptions );
		ensureSerializability(res);
	//	ensureJsonSerializability(database);

		return res;
	}
	/**
	 * @param db1 BibTeXDatabase
	 * @param db2 BibTeXDatabase
	 * @return A new BibTeXDatabase that contains all the entries from the db2 database that 
	 * are not included in the db1 database. 
	 */
	public static BibTeXDatabase removeAll(BibTeXDatabase db1, BibTeXDatabase db2) {
		Map<Key, BibTeXEntry> entries1 = db1.getEntries();
		Map<Key, BibTeXEntry> entries2 = db2.getEntries();
		
		Set<Key> diff = new HashSet<>(entries2.keySet());
		diff.removeAll(entries1.keySet());	
		LOGGER.log( Level.INFO, "{0}: {1}...", new Object[]{diff.size(), diff} );

		BibTeXDatabase res = new BibTeXDatabase();	
		for (Key k : diff){
			res.addObject(entries2.get(k));
		}
		return res;
	}

	/**
	 * @param db BibTeXDatabase
 	 * @param path BibTex file path. 
	 * @throws IOException
	 * It generates a BibTeX file that includes all the database entries.
	 */
	public static void toBibTexFile(BibTeXDatabase db, String path) throws IOException{
		FileWriter writer = new FileWriter(path);
		BibTeXFormatter bibtexFormatter = new BibTeXFormatter();
		bibtexFormatter.format(db, writer);
	}
	
	/**
	 * @param db BibTeXDatabase
	 * @param key Entry key
	 * @return All the database entries with the given key.
	 */
	public static List<String> getAllValuesWithKey (BibTeXDatabase db, Key key){
		List<String> res =  db.getEntries().entrySet().stream()
							 .map(entry->entry.getValue().getField(key).toUserString())
							 .collect(Collectors.toList());
		return res;
	}
	
	/**
	 * @param parser BibTeXParser
	 * @param path BibTex file path
	 * @return A BibTexDatabase obtained as a result of the file parse.
	 * @throws Exception
	 */
	static private BibTeXDatabase parseFully(BibTeXParser parser, String path) throws Exception {
		//	InputStream is = (TestBibText.class).getResourceAsStream(path);
			InputStream is = new FileInputStream(path);
			try {
				//Reader reader = new InputStreamReader(is, "US-ASCII");
				Reader reader =  new InputStreamReader(is,StandardCharsets.UTF_8);
				try {
					return parser.parseFully(reader);
				} finally {
					reader.close();
				}
			} finally {
				is.close();
			}
		}

		static private void ensureSerializability(BibTeXDatabase database) {
			BibTeXDatabase clonedDatabase;

			try {
				clonedDatabase = SerializationUtil.clone(database);
			} catch (Exception e) {
				throw new AssertionError();
			}

		}

		static private void ensureJsonSerializability(BibTeXDatabase database) {
			BibTeXDatabase clonedDatabase;

			try {
//				clonedDatabase = SerializationUtil.jsonClone(database);
			} catch (Exception e) {
				throw new AssertionError();
			}

		}

}
