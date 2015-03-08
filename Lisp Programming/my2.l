;Christopher Chan (998005560)

;PART 1
(defun all-length (mylist)
    (cond
	((null mylist) 0) ;if mylist is empty, return null
	((atom mylist) 1) ;if mylist is an atom, return 1
	(t (+ (all-length (first mylist)) ;if the first of my list is a sublist, then do a recursive call
	      (all-length (rest mylist))))))

(defun range (mylist)
    (list (apply 'min mylist) (apply 'max mylist)))     


(defun before(start end mylist)
    (cond
	((null mylist) nil) ;if mylist is empty
	((and (null start) (equal (first mylist) end)) (list (first mylist)))
;if our start == null and the head of mylist is == end, then return the "last" element between our start and end
	((null start) (cons (first mylist) (before nil end (rest mylist))))
;if we passed in null into start during recursion, cons the front of mylist and with a recursive call before
	((equal (first mylist) start) (cons (first mylist)(before nil end(rest mylist))))
	(t (before start end (rest mylist))) ;else call it recursively since either we havent found the start or have gone past our end
    )	
)

(defun split-if (myfunc mylist)
    (list (remove-if myfunc mylist) (remove-if-not myfunc mylist))) ;remove-if removes all elements that satisfy the condition

(defun group (mylist myamt)
    (labels ((rec (mylist acc)
	(let ((rest (nthrest myamt mylist)))
	    (if(consp rest)
		(rec rest (cons (subseq mylist 0 myamt) acc))
		(nreverse (cons mylist acc))))))
    (if mylist (rec mylist nil) nil))
) ;groups mylist into sublists of at most n elements 

(defun mostn (myfunc mylist) 
    (mostn_helper myfunc mylist nil 0))

(defun mostn_helper (myfunc mylist itrm n)
    (cond
	((null mylist) (append (list itrm) (list n) ))
	((> (funcall myfunc(first mylist)) n) (mostn_helper myfunc (rest mylist) (list(first mylist)) (funcall myfunc(first mylist))))
	((equal (funcall myfunc(first mylist)) n) (mostn_helper myfunc (rest mylist) (append itrm (list(first mylist))) (funcall myfunc(first mylist))))
	(t(mostn_helper myfunc (rest mylist) itrm n))
    )
) ;mostn helper that returns sublists that satisfy our function condition the most 

;PART 2
(defun match (mylist1 mylist2)
    (cond
	((or (null mylist1) (null mylist2)) (equal mylist1 mylist2))
	((equal (first mylist1) '?) (match (rest mylist1) (rest mylist2)))
	((and (equal (first mylist1) '!) (match (rest mylist1) (rest mylist2))) t)
	((equal (first mylist1) '!) (match mylist1 (rest mylist2)))
	(t (and (equal (first mylist1) (first mylist2)) (match (rest mylist1) (rest mylist2))))
    )
) ;allows us to see whether or not the 2 lsits that we pass in to match. Details of what each thing does are in Phoung Pham's slides

;PART 3
(defun match-var (mylist1 mylist2)
    (cond
	((or (null mylist1) (null mylist2)) (equal mylist1 mylist2))
	((and (atom (first mylist1)) (atom (first mylist2))) (and (equal (first mylist1) (first mylist2)) (match-var (rest mylist1) (rest mylist2))))
	((equal (first (first mylist1)) '?) (setf (symbol-value (first (last( first mylist1)))) (first mylist2)) (match-var (rest mylist1) (rest mylist2)))
	((and (and (listp (first mylist1)) (equal (caar mylist1) '!))
	    (cond
        	((not (boundp (cadar mylist1))) (set (cadar mylist1) (list(first mylist2))) t)
        	((listp (eval (cadar mylist1))) (set (cadar mylist1) (append (eval (cadar mylist1)) (list(first mylist2)))) t)
        	(t (set (cadar mylist1) (list(first mylist2))) t)
	    )
      	    (match-var (rest mylist1) (rest mylist2))) t) 
	((and (listp (first mylist1)) (equal (caar mylist1) '!))   
	    (cond
        	((not (boundp (cadar mylist1))) (set (cadar mylist1) (list(first mylist2))))
        	((listp (eval (cadar mylist1))) (set (cadar mylist1) (append (eval (cadar mylist1)) (list(first mylist2)))))
      	    )
	  (match-var mylist1 (rest mylist2)))
    )
) ;same as match, except for the fact that we can bound patterns to variables

