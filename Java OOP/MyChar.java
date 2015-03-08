
public class MyChar extends Element{

    public char c;  

    public MyChar(){
        this.c = 0;
    }

    public char Get(){
        return this.c;
    }

    public void Set(char val){
	this.c = val;   
    }

    public void Print(){
	System.out.print("'" + this.c +"'");
    }
  
}
