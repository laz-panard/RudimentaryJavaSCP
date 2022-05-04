package SCPsolver;

//It's a triple, really not much to say about it except that Java could do it native.

public class Triple<A,B,C> {
	A a;
	B b;
	C c;
	
	public Triple(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public A get1() {
		return this.a;
	}
	
	public B get2() {
		return this.b;
	}
	
	public C get3() {
		return this.c;
	}
	
}
