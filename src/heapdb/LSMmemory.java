package heapdb;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LSMmemory implements ITable {
	
	private Schema schema;
	private TreeMap<Object, Tuple> level0;
	
	/**
	 * create a new LSM table
	 */
	public LSMmemory(String filename, Schema schema) {
		this.schema = schema;
		level0 = new TreeMap<>();
	}
	
	public LSMmemory(String filename) {
		this.level0 = new TreeMap<>();
	}

	@Override
	public void close() {
	}

	@Override
	public Schema getSchema() {
		return schema;
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("LSM Table does not support size().");
	}

	@Override
	public boolean insert(Tuple rec) {
		// TODO 
		// DO not check for duplicate key.  
		// Allow a duplicate key to replace current entry in the TreeMap.
		// use Tuple copy constructor to make a copy of rec.  put copy into TreeMap.
		Tuple t = new Tuple(rec); // copy constructor
		// prevent the user from modifying after its been added to the tree && constraints if any have been checked
		level0.put(t.getKey() ,t); // up-set -> if exits, adds into tree otherwise updates the key & value

		// Why a copy? 
		return true;
		
	}

	@Override
	public boolean delete(Object key) {
		// TODO remove an entry.  If the key does not exist, do nothing.
		Tuple t = level0.remove(key);
//		if (t==null) {return false;}
		
		return true;
	}

	@Override
	public Tuple lookup(Object key) {
		if(level0.get(key) != null ) {
			return level0.get(key);
		}
		return null;
	}

	@Override
	public ITable lookup(String colname, Object value) {
		
//		ITable t = new Table(schema);
//		// containsKey(key_element) & containsValue(Object Value)
//		
//		// used to get the key and value using index
//		Set<Map.Entry<Object, Tuple> >   tree = level0.entrySet();
//		Map.Entry<Object, Tuple>[] entryArray = tree.toArray( new Map.Entry[tree.size()]);
//		
//		for(int i=0; i < tree.size(); i++) {
//			if(entryArray[i].getKey() == colname && entryArray[i].getValue() == value) {
//				t.insert()
//			}
//		}
//		return t;
//		
		throw new UnsupportedOperationException("LSMmemory lookup(colname, value) not supported.");
	}
	
	@Override
	public String toString() {
		//TODO implement this method. Return a string that contains all tuples separated by new line "\n" character.
		if (schema.size() ==0) {
			return "Empty Table";
		}else {
			StringBuilder sb = new StringBuilder();
			for (Tuple t : this) {
				sb.append(t.toString());
				sb.append("\n");
			}
			return sb.toString();
		}
//		throw new UnsupportedOperationException("LSMmemory toString not implemented.");
	}
	
	@Override
	public Iterator<Tuple> iterator() {
		return new LSMmemoryIterator();
	}
	
	
	public class LSMmemoryIterator implements Iterator<Tuple> {
		Iterator<Map.Entry<Object,Tuple>> it0 = level0.entrySet().iterator();

		@Override
		public boolean hasNext() {
			return it0.hasNext();			
		}

		@Override
		public Tuple next() {
			return it0.next().getValue();
		}	
	}
}
