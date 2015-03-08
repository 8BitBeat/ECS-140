
public class Map extends Sequence{

    Map next;
    Pair pair;

    public Map(){
	this.next = null;
	this.pair = null;
    }

    public MapIterator begin(){
        MapIterator mapit = new MapIterator(this);
        return mapit;
    }

    public MapIterator end(){
        MapIterator pastEnd = new MapIterator(this);
        Map dummy = new Map();
        dummy.next = null;
        dummy.data = null;
        Map endNode = this;
        while(endNode.next != null)
            endNode = endNode.next;

        endNode.next = dummy;

        pastEnd.cur = dummy;

        return pastEnd;
    }

    public void add(Pair inval){
	Map itr = this.next;
	while(itr.next != null)
	    itr = itr.next;
	
	Pair newPair = new Pair(inval);
	itr.next = newPair;
    }

    public void Print(){
	Map itr = this.next;
    }

}
