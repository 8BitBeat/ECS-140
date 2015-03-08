
public class Matrix extends Sequence{
    int rowAmt;
    int colAmt;

    public Matrix(int rowsize, int colsize){
        this.rowAmt = rowsize;
	this.colAmt = colsize;
	Sequence itr = this;
	for(int i = 0; i<rowsize * colsize; i++){
	    MyInteger myInt = new MyInteger();
	    itr.data = myInt;
	    if(i == rowsize*colsize -1)
	        itr.next = null;
	    else{
		Sequence nextNode = new Sequence();
		itr.next = nextNode;
		itr = itr.next;
	    }
	}	
    }//Constructor fills the matrix of rowsize x colsize of type MyInteger which are initialized to 0
    

    public void Set(int row, int col, int value){
	Sequence itr = this;
	int access = row * colAmt + col;
	for(int i = 0; i < access; i++)
	    itr = itr.next;    
	
	((MyInteger)itr.data).Set(value);
    }//sets Matrix[row, col] to value

    public int Get(int row, int col){
        Sequence itr = this;
        int access = row * colAmt + col;
        for(int i = 0; i < access; i++)
            itr = itr.next;

         return ((MyInteger)itr.data).Get();
    }// returns value at Matrix[row,col]

    public Matrix Sum(Matrix mat){
	Matrix sumMat = new Matrix(this.rowAmt, this.colAmt);
	for(int i = 0; i <this.rowAmt; i++){
	    for(int j = 0; j < this.colAmt; j++){
		int sum = this.Get(i,j) + mat.Get(i,j);
		sumMat.Set(i, j, sum);
	    } 
	}
	return sumMat;
    }//returns the a matrix that contains the sum of each element between two matrics
 
    public Matrix Product(Matrix mat){
	if(this.colAmt != mat.rowAmt){
	    System.out.println("Matrix dimensions incompatible for Product");
	    System.exit(0);
	}	
	
	Matrix productMat = new Matrix(this.rowAmt, mat.colAmt);

	for(int i = 0; i< this.rowAmt; i++){
	    for(int j = 0; j<mat.colAmt; j++){
		for(int k = 0; k<this.colAmt; k++){
		    int product = productMat.Get(i,j) + this.Get(i,k) * mat.Get(k,j);
		    productMat.Set(i,j,product);
		}//special matrix multiplication rules
	    }
	}	
	
	return productMat;
    }//returns the matrix that contains the product between the elements of first and second matrix

    public void Print(){
	Sequence itr = this;
	for(int i = 0; i< this.rowAmt; i++){
	    System.out.print("[");
	    for(int j = 0; j< this.colAmt; j++){
		System.out.print(" ");
		itr.data.Print();
		itr = itr.next;
	    }//traverse columns
	    System.out.println(" ]");
	}//traverse rows
    }





}
