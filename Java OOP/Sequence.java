
public class Sequence extends Element{

    Sequence next;
    Element data;
 
//FUNCTIONS////////////////////////////////////////////////////////////////////    

    public Sequence(){
	this.next = null;
	this.data = null;
    }


    public int length(){
	int counter = 0;
	Sequence current = this;
	while(current.next != null){
	    counter++;
	    current = current.next;
	}
	return counter;
    }

    public Element first(){
	return this.data;
    }

    public void add(Element elm, int pos){
	Sequence current = this;
/*
	System.out.print("Inserting: ");
	elm.Print();
	System.out.print(" @ pos " + pos);
	System.out.println(" The seq has " + length() + " elements");
//used for debugging
*/
	if(pos < 0 | pos > length()){//if adding to sequence index out of bounds
	    System.err.println("ADDING OUT OF BOUNDS " + pos + " "  + length());
	    System.exit(1);
	}
	for(int i = 0; i< pos; i++)
	    current = current.next;
	
	Sequence newNode = new Sequence();
	
	newNode.data = current.data;
	newNode.next = current.next;
	current.data = elm;
	current.next = newNode;
    }


    public void delete(int pos){
	Sequence current = this;
        if(pos < 0 | pos > this.length())
            System.out.println("DELETING OUT OF BOUNDS");

	for(int i = 0; i< pos; i++)
            current = current.next;

	if(current.next != null){
	    current.data = (current.next).data;
	    current.next = (current.next).next;
	}
	else{
	    current = null;
	}	    
	
    }//Deletes a sequence node at a certain position

    public Sequence rest(){
	return this.next;	
    }//returns a Sequence that contains everything except for the head

    public void Print(){
	Sequence current = this;
	System.out.print("[");
	while(current.next != null){
	    System.out.print(" ");
	    current.data.Print();
	    current = current.next;
	}	
	System.out.print(" ]");
    }

    public Element index(int pos){
	if(pos < 0 | pos > this.length())
	    System.out.println("INDEXING OUT OF BOUNDS");

	Sequence current = this;
	
    	for(int i = 0; i< pos; i++)
	    current = current.next;

	return current.data;
    }//returns the element at the sequence's index

    public Sequence flatten(){
	Sequence flatSeq = new Sequence();
        Sequence current = this;

        while(current.next != null){
            if(current.data instanceof Sequence){
		Sequence ns = (Sequence)current.data;
		ns = ns.flatten();
		Sequence temp = ns;
		//if the iterator is pointing to a element of type Sequence

		for(int i = 0; i< temp.length(); i++){
		    flatSeq.add(ns.data ,flatSeq.length());
		    ns = ns.next;
		}    
            }
            else{
		flatSeq.add(current.data, flatSeq.length());
            }//else element is not a sequence
	    current  = current.rest();
	}
        
	return flatSeq;
        	
    }

    public Sequence copy(){
	Sequence deepCopy = new Sequence();
	Sequence current = this;

	while(current.next != null){
	    if(current.data instanceof Sequence){
                deepCopy.add(((Sequence)current.data).copy(), deepCopy.length());	
	    }//if our current pointer element is a sequence, then make a recursive call to go inside the sequence to flatten that
	    else{
		if(current.data instanceof MyInteger){
		    MyInteger newInt = new MyInteger();
		    newInt.Set( ((MyInteger)current.data).Get() );
		    deepCopy.add(newInt, deepCopy.length());
		}//if the element is of type MyInteger
		else{
		    MyChar newChar = new MyChar();
		    newChar.Set( ((MyChar)current.data).Get() );
		    deepCopy.add(newChar, deepCopy.length());
		}//else the element is of type MyChar

	    }//else element is not a sequence
	    current = current.next;	
	}//while we havene't reached the end of the original sequence
	
	return deepCopy;
    }

    public SequenceIterator begin(){
	SequenceIterator seqit = new SequenceIterator(this);
	return seqit;
    }

    public SequenceIterator end(){
	SequenceIterator pastEnd = new SequenceIterator(this);
	Sequence dummy = new Sequence();
	dummy.next = null;
	dummy.data = null;
	Sequence endNode = this;
	while(endNode.next != null)
	    endNode = endNode.next;
	
	endNode.next = dummy;

	pastEnd.cur = dummy;	

	return pastEnd;
    }
}






