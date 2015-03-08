
public class MyInteger extends Element{

    public int myInt;

    public MyInteger(){
	this.myInt = 0;
    }

    public int Get(){
	return this.myInt;
    }

    public void Set(int val){
	this.myInt = val;
    }

    public void Print(){
	System.out.print(this.myInt);

    }

}
