import java.util.Vector;
import uk.ac.imperial.lsds.seep.api.Partitioned;
import uk.ac.imperial.lsds.seep.api.DriverProgram;
import uk.ac.imperial.lsds.seep.api.largestateimpls.SeepMap;

public class UT2 implements DriverProgram{

	@Partitioned
	public SeepMap<String, Integer> kvstore = new SeepMap<String, Integer>();

	public void main(){
		String keyupdate = "testupdate"; // get data somehow
		count(keyupdate); // call function -> implies this is an entry point
		String keyread = "testread";
		read(keyread);
	}

	public void count(String key){
		int newCounter = 0;
		if(kvstore.containsKey(key)){
			newCounter = ((Integer)kvstore.get(key)) + 1;
		}
		kvstore.put(key, newCounter);
	}

	public int read(String key){
		int counter = 0;
		if(kvstore.containsKey(key)){
			counter = (Integer)kvstore.get(key);
		}
		return counter;
	}
}
