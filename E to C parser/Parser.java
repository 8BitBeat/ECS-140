/* *** This file is given as part of the programming assignment. *** */

//any variables declared and assigned to 69 denotes that it needed to be initialized for compiling purposes even though the value will be changed for sure later on
import java.util.Vector;
import java.util.Stack;
public class Parser {
    Vector<String> symbolTable = new Vector<String>();
    Stack<Vector<String> > scope = new Stack<Vector<String> >();
    Stack<Vector<String> > tempSymbol = new Stack<Vector<String> >(); 
    //used for keeping track which vars are in the symbol table

    Vector<Integer> symScopeTable = new Vector<Integer>();
    Stack<Vector<Integer> > scopeDecd = new Stack<Vector<Integer> >();
    Stack<Vector<Integer> > tempScope = new Stack<Vector<Integer> >();
    //used for keeping track which scopes each symbol was declared in
    static int currentScope = -1;
    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan() {
	tok = scanner.scan();
    }

    private void stack_push(){
	Vector<String> tempSymTable = new Vector<String>(symbolTable);
	Vector<Integer>tempScopeTable = new Vector<Integer>(symScopeTable);
	scope.push(tempSymTable);
	scopeDecd.push(tempScopeTable);
	currentScope++;	
    }
    
    private void stack_pop(){
	symbolTable = scope.pop();
	symScopeTable = scopeDecd.pop();
	currentScope--;
    }

    private int symbolSearch(int scopeLevel){
	if(scopeLevel > currentScope)
	    return -1;

        for(int i = 0; i< scopeLevel - 1; i++)
	    tempSymbol.push(scope.pop());  

	int pos = 69;

	if(scopeLevel == 0)
	    pos = searchSTable(symbolTable);
	else
	    pos = searchSTable(scope.peek());

	while(tempSymbol.size() > 0)
	    scope.push(tempSymbol.pop());

	return pos;
	 
    }

    private boolean searchGlobal(){
	while(scope.size() > 2)
	    tempSymbol.push(scope.pop());
         
	int found = 69;
	if (scope.size() == 1)
	    found = searchSTable(symbolTable);
	else
	    found = searchSTable(scope.peek());
	
	while(tempSymbol.size() > 0)
	    scope.push(tempSymbol.pop());

	if(found != -1)
	    return true;	
	else 
	    return false;
    }

    private boolean scopeSearch(int scopeLevel, int pos){
	int temp = currentScope;

	if(scopeLevel > currentScope| pos == -1)
	    return false;

        if(scopeLevel == 0)//if we're checking current scope
        {
            if(symScopeTable.get(pos) == currentScope)
                return true;
            else
                return false;
        }


	for(int i = 0; i< scopeLevel - 1; i++)
	{
            tempScope.push(scopeDecd.pop());
	    temp--;
	}	

	boolean found;
        temp--;


	if((scopeDecd.peek()).get(pos) == temp)
	    found = true;
	else
	    found = false;

        for(int i = 0; i< scopeLevel - 1; i++)
            scopeDecd.push(tempScope.pop());
	
	return found;		

    }


    private Scan scanner;
    Parser(Scan scanner) {
	this.scanner = scanner;
	scan();
	program();
	if( tok.kind != TK.EOF )
	    parse_error("junk after logical end of program");
    }

    private void program() {
	block();
    }

    private void block(){
//	System.out.println("Entering Scope# " + (currentScope+1) + " " + symbolTable + "\n");
	stack_push();
	declaration_list();
//	System.out.println("Scope # " + currentScope + " variables: ");
//        for(int i =0; i< symbolTable.size(); i++)
//                System.out.println(symbolTable.get(i) + symScopeTable.get(i));
//        System.out.println("Entering scope # " + (currentScope + 1) + "\n");

	statement_list();
//	System.out.println("Scope # " + currentScope + " variables: ");
//	for(int i =0; i< asd.size(); i++)
//		System.out.println(asd.get(i)+ );
	stack_pop();
//	System.out.println("Exiting scope # " + currentScope + " " + symbolTable+ scope.size() + "\n");
//	stack_pop();
    }

    private void declaration_list() {
	// below checks whether tok is in first set of declaration.
	// here, that's easy since there's only one token kind in the set.
	// in other places, though, there might be more.
	// so, you might want to write a general function to handle that.
	while( is(TK.DECLARE) ) {
	    declaration();
	}
    }

    private void declaration() {
	mustbe(TK.DECLARE);
	int pos = searchSTable(symbolTable);
	if(pos != -1)
	{
	    if(searchScopeTable(symScopeTable,pos) == true)
	        redecError();
	}
	else
	{
	    symbolTable.add(tok.string); 
	    symScopeTable.add(currentScope);
	}
	mustbe(TK.ID);
	while( is(TK.COMMA) ) {
	    scan();
	    pos = searchSTable(symbolTable);
	    if(pos != -1)
	    {
		if(searchScopeTable(symScopeTable, pos) == true)
		    redecError();
	    }
	    else
	    {
		symbolTable.add(tok.string);
		symScopeTable.add(currentScope);
	    }
	    mustbe(TK.ID);
	}
    }

    private void statement_list() {
	while(is(TK.TILDE) | is(TK.ID) | is(TK.PRINT) | is(TK.DO) | is(TK.IF))
	    statement();
    }

    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    private void statement(){
	if(is(TK.TILDE) | is(TK.ID))
	    assignment();
	else if(is(TK.PRINT))
	    print();
	else if(is(TK.DO))
	    e_do();
	else if(is(TK.IF))
	    e_if();
	else
	    parse_error("statement");
    }

    private void print(){
	mustbe(TK.PRINT);
	expr();
    }

    private void assignment(){
	ref_id();
	mustbe(TK.ASSIGN);
	expr();
    }

    private void ref_id(){
	int tempScope;
	if(is(TK.TILDE))
	{
	    scan();
	    if(is(TK.NUM))
	    {
		tempScope = Integer.parseInt(tok.string);
	        scan();
		if(scopeSearch(tempScope, symbolSearch(tempScope)) == false)
		    noSuchError(tempScope);
	    }
	    else
	    {
		if(searchGlobal() == false)
		    noSuchError(-1); 	
	    }
	}

	if(searchSTable(symbolTable) == -1 )
	    dneError();	
	mustbe(TK.ID);
    }

    private void e_do(){
	mustbe(TK.DO);
	guarded_command();
	mustbe(TK.ENDDO);
    }

    private void e_if(){
	mustbe(TK.IF);
	guarded_command();
	while(is(TK.ELSEIF))
	{
	    scan();
	    guarded_command();
	}	
	if(is(TK.ELSE))
	{
	    scan();
	    block();
	}
	mustbe(TK.ENDIF);
    }

    private void guarded_command(){
	expr();
	mustbe(TK.THEN);
	block();   
    }

    private void expr(){
	term();
	while(is(TK.PLUS) | is(TK.MINUS))
	{
	    scan();
	    term();
	}
    }  

    private void term(){
	factor();
	while(is(TK.TIMES)| is(TK.DIVIDE))
	{
	    scan();
	    factor();
	}
    }

    private void factor(){
        if(is(TK.LPAREN))
	{
	    mustbe(TK.LPAREN);
	    expr();
	    mustbe(TK.RPAREN);
	}
	else if(is(TK.TILDE)| is(TK.ID))
	    ref_id();
	else if(is(TK.NUM))
	    scan();
	else
	    parse_error("factor");	    

    }

    private int searchSTable(Vector <String> sTable){
	for(int i = 0; i< sTable.size(); i++)
	{
	    if((sTable.get(i)).equals(tok.string))
		return i;
	}
	return -1; //false, symbol is NOT in symbolTable
    }//searches symbol table and returns position of the symbol in the symbol table which we'll use to search in scope table

    private void redecError(){
	System.err.println("redeclaration of variable " + tok.string);	
    }

    private void dneError(){
	System.err.println(tok.string + " is an undeclared variable on line " + tok.lineNumber);
	System.exit(1);
    }//does not exist in symbol table error

    // ensure current token is tk and skip over it.

    private boolean searchScopeTable(Vector <Integer> scopeTable, int pos){
	if(pos <= scopeTable.size())
	{
	    if ((scopeTable.get(pos)).equals(currentScope))
	        return true;//redeclared variable is in same scope as previous
	}
	scopeTable.set(pos, currentScope);
	return false;//new declaration
    }
/*
    private boolean checkScope(int tempScope){
	int pos = searchSTable(symbolTable);
	int scopeDeclared = 69;//value here doesn't matter, just needs to be intialized

	if(pos != -1)
	    scopeDeclared = symScopeTable.get(pos);

	if(tempScope == -1){
	    if(scopeDeclared == 0)
		return true;
	    else
		return false;			
	}

	if(tempScope > currentScope | pos != -1 )
        {
//	    System.out.println("scope declared: "+scopeDeclared);
//	    System.out.println("current Scope: " + currentScope);
//	    System.out.println("tempScope: " + tempScope);
//	    if(scopeDeclared <= currentScope - tempScope)
//		System.out.println("scope declared <= currentScope - tempScope "+ "scope declared = "+ scopeDeclared + "currentScope - tempScope = " + (currentScope -tempScope));
//	    System.out.println();

	    if(scopeDeclared <= currentScope - tempScope)
	        return true;	    
	}

	    return false;
		 	

    }
*/
    private void mustbe(TK tk){ 
	if( tok.kind != tk ) {
	    System.err.println( "mustbe: want " + tk + ", got " +
				    tok);
	    parse_error( "missing token (mustbe)" );
	}
	scan();
    }

    private void noSuchError(int tempScope){
	if(tempScope == -1)//if we're checking for global variable and DNE
            System.err.println("no such variable ~" + tok.string + " on line "+tok.lineNumber);

	else
	    System.err.println("no such variable ~" + tempScope + tok.string + " on line "+tok.lineNumber);

	System.exit(1);
    }

    private void parse_error(String msg) {
	System.err.println( "can't parse: line "
			    + tok.lineNumber + " " + msg );
	System.exit(1);
    }
}
