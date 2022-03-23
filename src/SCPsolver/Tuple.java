package SCPsolver;

//It's a tuple, really not much to say about it except that Java could do it native.

public class Tuple<K,V> {
	K k;
	V v;
	
	public Tuple(K k, V v) {
		this.k = k;
		this.v = v;
	}
	
	public K get1() {
		return this.k;
	}
	
	public V get2() {
		return this.v;
	}
	
}
