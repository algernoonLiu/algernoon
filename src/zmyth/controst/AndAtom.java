package zmyth.controst;
/**
 * 与判断
 * 
 * @author hy
 * */
public class AndAtom implements Atom{
	/**左边、右边*/
	Atom left,right;
	/**设置元素*/
	public void setElement(Atom l,Atom r){
		left=l;
		right=r;
	}
	/**返回结果*/
	public boolean result() {
		return left.result()&&right.result();
	}
	
}