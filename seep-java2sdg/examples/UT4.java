import java.util.Vector;
import java.util.List;
import uk.ac.imperial.lsds.seep.api.Partial;
import uk.ac.imperial.lsds.seep.api.Global;
import uk.ac.imperial.lsds.seep.api.Collection;
import uk.ac.imperial.lsds.seep.api.DriverProgram;
import uk.ac.imperial.lsds.seep.api.largestateimpls.SeepMap;

public class UT4 implements DriverProgram{

	@Partial
	public SeepMap<String, Integer> counter = new SeepMap<String, Integer>();

	public void main(){
		String newword = "testupdate"; // get data somehow
		// just update word in distributed fashion
		update(newword); // call function -> implies this is an entry point
		String word = "testread";
		// just returns an accurate counting of that word
		count(word);
	}

	public void update(String key){
		
		updateAll(@Global counter, key);
	}

	private void updateAll(SeepMap<String, Integer> counter, String key){
		int newCounter = 0;
		if(counter.containsKey(key)){
			newCounter = ((Integer)counter.get(key)) + 1;
		}
		counter.put(key, newCounter);
	}

	public int count(String key){
		int counts = (Integer)counter.get(key);
		return counts;
	}
}