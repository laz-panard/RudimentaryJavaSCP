package SCPsolver;

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
