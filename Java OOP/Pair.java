public class Pair{

    Sequence next;
    MyChar pairChar;
    MyInteger pairInt;
   
    public Pair(Pair node){
	this.pairChar = node.pairChar;
	this.pairInt = node.pairInt;
    } 
}
