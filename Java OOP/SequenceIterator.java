
public class SequenceIterator{

    Sequence head;
    Sequence cur;

    public SequenceIterator(Sequence node){
	this.head = node;
	this.cur = node;
    }

    public boolean equal(SequenceIterator other){
	if(this.cur.data == other.cur.data)
	    return true;
	return false;
    }

    public SequenceIterator advance(){
	this.cur = cur.rest();
	return this;
    }

    public Element get(){
	return (this.cur).data;
    }





}
