package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPC;
	
	public int getMainPc() {
		return this.mainPC;
	}
	
	private Stack<Integer> ternaryEndIf = new Stack<>();
	private Stack<Boolean> ternaryPendingBeginElse = new Stack<>();

	private void fixupZaTernarniZaPocetakNaExpr2() {
		if (!ternaryPendingBeginElse.isEmpty() && ternaryPendingBeginElse.peek()) {
			ternaryPendingBeginElse.pop();
			
			//ovde za expr2 kaze gde da skace
			//ato ce biti poceci Expr, a to su Factori i Designatori
			if (!skipIf.isEmpty()) {
				Code.fixup(skipIf.pop()); 
				//daje pc, ovom jmp koji je cekao nakon neispunjenog Condition
			}
		}
	}
	
	private void printtNewLine() {
		int c = (int)('\n');
		Code.loadConst(c);
		Code.loadConst(1);
		Code.put(Code.bprint);
	}
	
	private void initPredeclaredMethods() {
        //'ord' i 'chr' su isti kodovi, primaju broj i ostavljaju ga an stack
        Obj ordMethod = Tab.find("ord");
        Obj chrMethod = Tab.find("chr");
        ordMethod.setAdr(Code.pc);
        chrMethod.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);  //fp
        Code.put(1);  //locp
        Code.put(Code.load_n);  //load_0 satavlja na stack
        Code.put(Code.exit);
        Code.put(Code.return_);

        Obj lenMethod = Tab.find("len");
        lenMethod.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.arraylength);  //ostavlja duzinu niza na ExprStack
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

	public CodeGenerator() {
		initPredeclaredMethods();
	}
	
	
	// MethodDecl	
	@Override
	public void visit(MethodTypeAndName_void methodName) {
		methodName.obj.setAdr(Code.pc);  //pre enter, njena pocetna adr
		if (methodName.getI1().equalsIgnoreCase("main")) this.mainPC = Code.pc;
		
		Code.put(Code.enter);  //sad se nalazi 1B u nasem programu, tj OC
		Code.put(methodName.obj.getLevel()); //b1
		Code.put(methodName.obj.getLocalSymbols().size()); //b2 - locals
		
	}
	
	@Override
	public void visit(MethodTypeAndName_type methodName) {
		methodName.obj.setAdr(Code.pc);  //pre enter, njena pocetna adr
		if (methodName.getI2().equalsIgnoreCase("main")) this.mainPC = Code.pc;
		
		Code.put(Code.enter);  //sad se nalazi 1B u nasem programu, tj OC
		Code.put(methodName.obj.getLevel()); //b1
		Code.put(methodName.obj.getLocalSymbols().size()); //b2 - locals
		
	}
	
	@Override
	public void visit(MethodDecl methodDecl) {
		if(methodDecl.getMethodTypeAndName().obj.getType() != Tab.noType) { // trap
			Code.put(Code.trap);
			Code.put(0); // porukaa greskee
		}
		
		Code.put(Code.exit);  //uz enter
		Code.put(Code.return_); 		
	}
	
	//Factor
	@Override
	public void visit(FactorReal_num factor) {
		fixupZaTernarniZaPocetakNaExpr2();
		Code.loadConst(factor.getN1());		
	}
	
	@Override
	public void visit(FactorReal_char factor) {
		fixupZaTernarniZaPocetakNaExpr2();
		Code.loadConst(factor.getC1());		
	}
	
	@Override
	public void visit(FactorReal_bool factor) {
		fixupZaTernarniZaPocetakNaExpr2();
		Code.loadConst(factor.getB1());		
	}
	
	@Override
	public void visit(FactorReal_d factor) {
		fixupZaTernarniZaPocetakNaExpr2();
		if (factor.getDesignator() instanceof Designator_length || factor.getDesignator() instanceof Designator_findAny
				|| factor.getDesignator() instanceof Designator_map)
	        return; //  !!!da uradi skip loading array.length / findAny / map rezultat je vec na stacku!!!
		
		//za ovo pravimo visit, jer smo ga ovde tek pozvali
		//ali npr gde radimo Designator = nesto, tu necemo jer to je store neki
		Code.load(factor.getDesignator().obj);	//sve moguce varijable ovaku su zadovoljene	
	}

	@Override
	public void visit(FactorReal_newarr factor) {
		fixupZaTernarniZaPocetakNaExpr2();
		Code.put(Code.newarray);  //expr vec je stavio na stack
		if (factor.getType().struct.equals(Tab.charType)) {
			Code.put(0); //b=0
		}
		else Code.put(1); //b=1
	}
	
	@Override
	public void visit(FactorReal_method factor) {
		fixupZaTernarniZaPocetakNaExpr2();
		// mora offset jer call inc pc, i mora se pre call racunati
		int offsett = factor.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);   
		Code.put2(offsett);  //s - short 2B
	}
	
	@Override
	public void visit(Factor factor) {
		if (factor.getUnary() instanceof Unary_minus) {
			Code.put(Code.neg);	
		}
	}
	
	//Designator
	@Override
	public void visit(DesignatorArrName designatorArrName) {
		fixupZaTernarniZaPocetakNaExpr2();
		Code.load(designatorArrName.obj);  // obj cvor niza, nebitno da li sa leve ili desne strane
	}
	
	@Override
	public void visit(DesignatorArrLength designatorArrLength) {
		fixupZaTernarniZaPocetakNaExpr2();
		Code.load(designatorArrLength.obj); 
	}
	
	@Override
	public void visit(Designator_length designatorLength) {
		fixupZaTernarniZaPocetakNaExpr2();
		Code.put(Code.arraylength); 
	}
	
	@Override
	public void visit(Designator_expr designatorExpr) {
	}
	
	@Override
	public void visit(Designator_findAny findAny) {
		fixupZaTernarniZaPocetakNaExpr2();
	
		Obj[] temps = SemAnalyzer.findAnyTemps.get(findAny);
		if (temps == null) return; // semanticka greska je vec prijavljena, kod se ionako ne generise pri gresci
	
		Obj counterObj = temps[0];
		Obj searchValObj = temps[1];
		Obj arrObj = temps[2];
	
		// stack: [ EXPRVAL ] (vrednost za pretragu, vec je izracunata)
		Code.store(searchValObj);	// searchVal = Expr
		Code.loadConst(0);
		Code.store(counterObj);	// i = 0
	
		int loopStart = Code.pc;
		Code.load(counterObj);		// i
		Code.load(arrObj);			// niz
		Code.put(Code.arraylength);	// niz.length      stack: [i, length]
		Code.putFalseJump(Code.lt, 0);	// ako i >= length -> notFound
		int notFoundJmp = Code.pc - 2;
	
		Code.load(arrObj);			// niz
		Code.load(counterObj);		// i               stack: [niz, i]
		Code.load(new Obj(Obj.Elem, "$fa$elem", arrObj.getType().getElemType())); // niz[i]
		Code.load(searchValObj);	// stack: [niz[i], searchVal]
		Code.putFalseJump(Code.eq, 0);	// ako nije jednako -> nastavi (increment)
		int notEqualJmp = Code.pc - 2;
	
		// pronadjeno
		Code.loadConst(1);
		Code.putJump(0);
		int foundJmp = Code.pc - 2;
	
		// nije jednako, i++ pa nazad na pocetak petlje
		Code.fixup(notEqualJmp);
		Code.load(counterObj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(counterObj);
		Code.putJump(loopStart);
	
		// nije pronadjeno (petlja zavrsena)
		Code.fixup(notFoundJmp);
		Code.loadConst(0);
	
		// zajednicka tacka - rezultat (0/1) je na stacku
		Code.fixup(foundJmp);
	}
	
	// map: DesignatorMapBegin generise sve pre Expr-a (alokacija novog niza, brojac, provera i < length,
	// ident = niz[i], i priprema stek za store na kraju), tako da se Expr-ov kod izvrsava PONOVO svaki put
	// kad se skoci nazad na loopStart (Expr fizicki sedi u petlji, izmedju ova dva visit-a)
	private Stack<int[]> mapLoopInfo = new Stack<>();
	
	@Override
	public void visit(DesignatorMapBegin mapBegin) {
		fixupZaTernarniZaPocetakNaExpr2();
	
		Obj[] temps = SemAnalyzer.mapTemps.get(mapBegin);
		if (temps == null) return; // semanticka greska je vec prijavljena
	
		Obj counterObj = temps[0];
		Obj newArrObj = temps[1];
		Obj srcArrObj = temps[2];
		Obj identObj = temps[3];
		Struct elemType = srcArrObj.getType().getElemType();
	
		// noviNiz = new elemType[srcNiz.length]
		Code.load(srcArrObj);
		Code.put(Code.arraylength);
		Code.put(Code.newarray);
		Code.put(elemType.equals(Tab.charType) ? 0 : 1);
		Code.store(newArrObj);
	
		Code.loadConst(0);
		Code.store(counterObj);	// i = 0
	
		int loopStart = Code.pc;
		Code.load(counterObj);
		Code.load(srcArrObj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.lt, 0);	// ako i >= length -> gotovo
		int doneJmp = Code.pc - 2;
	
		// ident = srcNiz[i]
		Code.load(srcArrObj);
		Code.load(counterObj);
		Code.load(new Obj(Obj.Elem, "$map$read", elemType));
		Code.store(identObj);
	
		// priprema za store noviNiz[i] = Expr posle sto se Expr izracuna (sledeci u obilasku)
		Code.load(newArrObj);
		Code.load(counterObj);
	
		mapLoopInfo.push(new int[]{ loopStart, doneJmp });
	}
	
	@Override
	public void visit(Designator_map designatorMap) {
		Obj[] temps = SemAnalyzer.mapTemps.get(designatorMap.getDesignatorMapBegin());
		if (temps == null) return; // semanticka greska je vec prijavljena
	
		Obj counterObj = temps[0];
		Obj newArrObj = temps[1];
		Obj srcArrObj = temps[2];
		Struct elemType = srcArrObj.getType().getElemType();
	
		int[] loopInfo = mapLoopInfo.pop();
		int loopStart = loopInfo[0];
		int doneJmp = loopInfo[1];
	
		// stack: [ noviNiz, i, ExprValue ] -> noviNiz[i] = ExprValue
		Code.store(new Obj(Obj.Elem, "$map$write", elemType));
	
		Code.load(counterObj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(counterObj);	// i++
		Code.putJump(loopStart);
	
		Code.fixup(doneJmp);
		Code.load(newArrObj);	// rezultat map poziva - referenca na novi niz
	}
	
	
	//DesignatorStatements
	@Override
	public void visit(DesignatorStatement_assign dsAssign) {
		Code.store(dsAssign.getDesignator().obj); //znaci sa stacka store u des
	}
	
	@Override
	public void visit(DesignatorStatement_inc dsInc) {
		if (dsInc.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		} //da bi duplirao gde treba da vrati inc vrednost, za store da bi imalii
//		else if(dsInc.getDesignator().obj.getKind() == Obj.Fld) {
//			Code.put(Code.dup);  //???
//		}
		
		Code.load(dsInc.getDesignator().obj); 
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(dsInc.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorStatement_dec dsDec) {
		if (dsDec.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
//		else if(dsDec.getDesignator().obj.getKind() == Obj.Fld) {
//			Code.put(Code.dup);
//		}
		
		Code.load(dsDec.getDesignator().obj); 
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(dsDec.getDesignator().obj);
	}
	
	@Override
	public void visit(DesignatorStatement_method dsMethod) {
		// mora offset jer call inc pc, i mora se pre call racunati
		int offsett = dsMethod.getDesignator().obj.getAdr() - Code.pc;
		Code.put(Code.call);  //ovo inc pc  
		Code.put2(offsett);  //s = relativni pomeraj na pc
		
		//za izraz f(); sam sa sobom, ostavice djubre na steku
		//jer samo void moze ovako da radi, a ostali tipovi moraju da assignuju u neku prom
		if(dsMethod.getDesignator().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}
	
	//Statement
	@Override
	public void visit(OneStatement_print1 statementPrint1) {
		Code.loadConst(0); //mora pre print, jer bi pukao print jer se nije loadovalo
		if (statementPrint1.getExpr().struct.equals(Tab.charType)) {
			Code.put(Code.bprint); 
		}
		else Code.put(Code.print);
		printtNewLine();
	}
	
	@Override
	public void visit(OneStatement_print2 statementPrint2) {
		Code.loadConst(statementPrint2.getN2());  //number
		if (statementPrint2.getExpr().struct.equals(Tab.charType)) {
			Code.put(Code.bprint); 
		}
		else Code.put(Code.print); 
	}
	
	@Override
	public void visit(OneStatement_return statementReturn) {
		Code.put(Code.exit);
		Code.put(Code.return_); 
	}
	
	@Override
	public void visit(OneStatement_returnExpr statementReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_); 
	}
	
	@Override
	public void visit(OneStatement_read statementRead) {
		if (statementRead.getDesignator().obj.getType().equals(Tab.charType)) {
			Code.put(Code.bread);
		} 
		else Code.put(Code.read);  //na Expr Sack se nesto ucitava
		
		Code.store(statementRead.getDesignator().obj);  //pa ga u des
	}
	
	//Addop i Mulop
	@Override
	public void visit(ExpAddop_addop expAddopAddop) {
		if (expAddopAddop.getAddop() instanceof Addop_p) { //ako je +
			Code.put(Code.add);
		}
		else if (expAddopAddop.getAddop() instanceof Addop_m) { //ako je -
			Code.put(Code.sub);
		}
	}
	
	@Override
	public void visit(MulopList_mulop mulopListMulop) {
		if(mulopListMulop.getMulop() instanceof Mulop_m) Code.put(Code.mul);
		else if(mulopListMulop.getMulop() instanceof Mulop_d) Code.put(Code.div);
		else if(mulopListMulop.getMulop() instanceof Mulop_pr) Code.put(Code.rem); // %mod
	}

	//Ternarni Expr
	@Override
	public void visit(Expr_nonTernary exprNonTernary) {  //znaci za obican Expr pita da li je deo tern op
		if (exprNonTernary.getParent() instanceof ExprTernary) {
			ExprTernary owner = (ExprTernary) exprNonTernary.getParent();
			if (owner.getExpr() == exprNonTernary) {  //ako je u expr1
				Code.putJump(0);
				ternaryEndIf.push(Code.pc - 2);
				//zavsili smo expr1, pa mora da preskocimo expr2
				
				ternaryPendingBeginElse.push(true);
				//ovime smo zavrsili expr1, i rekli da mozemo da fixupujemo expr2
				//pa to radi u pomocnoj nasoj fji fixupZaTernarniZaPocetakNaExpr2
				//pa ih postavljamo na pocetke svega sto moze biti poc Expr
				//a to su Factor, ili onda i Designator
			}
		}
	}

	@Override
	public void visit(Expr_ternary exprTernary) { //za ugnjezdeni tern u Expr delu
		if (exprTernary.getParent() instanceof ExprTernary) {
			ExprTernary owner = (ExprTernary) exprTernary.getParent();
			if (owner.getExpr() == exprTernary) {  //ako je u expr1
				Code.putJump(0);
				ternaryEndIf.push(Code.pc - 2);
				
				ternaryPendingBeginElse.push(true);
				//ovime smo zavrsili expr1, i rekli da mozemo da fixupujemo expr2
			}
		}
	}
	
	@Override
	public void visit(ExprTernary exprTernary) {
		//expr2 ovde se zavrsava
		
		//za expr1 da zna na kraj ovoga da skoci
		if (!ternaryEndIf.isEmpty()) {
			Code.fixup(ternaryEndIf.pop());
		}
	}
	
	//DesignatorArrName // obj cvor niza
	//Designator_expr //obj cvor elem niza
	
	
	//Condition
	private Stack<Integer> skipCondFact = new Stack<>();
	private Stack<Integer> skipCondition = new Stack<>();
	private Stack<Integer> skipIf = new Stack<>(); //za ugnjezdene if
	private Stack<Integer> skipElse = new Stack<>();
	private int whatRelop(Relop r) {
		if(r instanceof Relop_eq) return Code.eq;
		else if(r instanceof Relop_ne) return Code.ne;
		else if(r instanceof Relop_gt) return Code.gt;
		else if(r instanceof Relop_ge) return Code.ge;
		else if(r instanceof Relop_lt) return Code.lt;
		else if(r instanceof Relop_le) return Code.le;
		else return -1; //greskaa
	}
	
	@Override
	public void visit(CondFact_noting condFactExpr) {
		Code.loadConst(0);  //mora sa necim da stavimo da se poredi
		//ako je Expr=1 - tacan ne, u suprotnom netacan eq
		//znaci falseJmp ako nije ispunjeno ne sto znaci ispunjeno eq onda skaci
		Code.putFalseJump(Code.ne, 0);  //nzm gde skacemo za netacn
		skipCondFact.push(Code.pc - 2);
		
		//za tacan = Code.ne, tj !=0 koji smo loadovali
		
	} 
	
	@Override
	public void visit(CondFact_relop condFactRelop) {
		Code.putFalseJump(whatRelop(condFactRelop.getRelop()), 0); //netacno
		skipCondFact.push(Code.pc - 2);
		
		//za tacan
	}
	
	@Override
	public void visit(CondTerm condTerm) {		
		//tacne
		Code.putJump(0);  //bacamo ih na then zbog OR
		skipCondition.push(Code.pc - 2);
		
		//ovde netacne vracamo za OR
		while (!skipCondFact.empty()) Code.fixup(skipCondFact.pop());
		
		//netacne nastavljaju na OR
	}
	
	@Override
	public void visit(Condition condition) {
		//znaci svi su bili netacni na kraju OR
		Code.putJump(0);  //bacamo ih na posle if, tj nista ili else, jedan jedini jump ovde
		skipIf.push(Code.pc - 2);
		//ovo gore je i za expr2 ceka da zna gde da ga baci
		
		//tacni - then
		while (!skipCondition.empty()) Code.fixup(skipCondition.pop());
		
		//tacni nastavljaju
	}
	
	@Override
	public void visit(ElseStatement_epsilon elseStatementNO) {
		//posle tacnih da skoci, tj posle if
		Code.fixup(skipIf.pop()); //iz perspektive compilera on ce vec znati gde da skoci
		//ne radimo ovde while, jer samo poslednji skidamo, za ugnjezdeni if samo
		//tacne+netacne
	}
	
	@Override
	public void visit(ElseBegin elseBegin) {
		//tacne treba ovo da preskoce
		Code.putJump(0);
		skipElse.push(Code.pc - 2);
		//za netacne
		Code.fixup(skipIf.pop());
		//netacne
	}
	
	@Override
	public void visit(ElseStatement_yes elseStatement) {
		//netacne 
		//kraj else, vracamo i tacne
		Code.fixup(skipElse.pop());
		//netacne+tacne
	}
	
	//DO WHILE
//	private Stack<Integer> doBegin = new Stack<>();
//	
//	// u .cup (OneStatement_do) DoNonTerm Statement WhileNonterm LPARENTH Condition RPARENTH SEMI
//	// DoNonterm ::= (DoNonterm) DO;
//	// WhileNonterm ::= (WhileNonterm) WHILE;
//	@Override
//	public void visit(DoNonterm doNonterm) {
//		doBegin.push(Code.pc);
//		breakJmp.push(new ArrayList<Integer>());
//		continueJmp.push(new ArrayList<Integer>());
//	}
//	
//	@Override
//	public void visit(OneStatement_do statementDo) {
//		Code.putJump(doBegin.pop());
//		Code.fixup(doBegin.pop());
//		
//		while (!breakJmp.peek().isEmpty()) Code.fixup(breakJmp.peek().remove(0));
//		breakJmp.pop(); //da skine i tu listu samo
//	}
//	
//	@Override
//	public void visit(WhileNonterm whileNonterm) {
//		while (!continueJmp.peek().isEmpty()) Code.fixup(continueJmp.peek().remove(0));
//		continueJmp.pop();
//		
//	}
	
	//Break i continue
	
//	private Stack<List<Integer>> breakJmp = new Stack<>();
//	private Stack<List<Integer>> continueJmp = new Stack<>();
//	
	@Override
	public void visit(OneStatement_break statementBreak) {
		Code.putJump(0);  
		int adr = Code.pc - 2;
		
		if (!forBreakJmp.isEmpty()) {
	    	forBreakJmp.peek().add(adr);
	    }
	}
	
	@Override
	public void visit(OneStatement_continue statementContinue) {
		//za for
		if (!forContinueJmp.isEmpty()) {
			Code.putJump(0);  
			forContinueJmp.peek().add(Code.pc - 2); 
		}
	}
	
	
	
	// FOR
	
	private Stack<Integer> forCondBegin = new Stack<>();
	private Stack<Integer> forIncBegin = new Stack<>();
	private Stack<Integer> forBodyJmp = new Stack<>();
	private Stack<Boolean> forHasCond = new Stack<>();
	private Stack<Boolean> forHasInc = new Stack<>();
	
	private Stack<List<Integer>> forBreakJmp = new Stack<>();
	private Stack<List<Integer>> forContinueJmp = new Stack<>();
	private Stack<String> breakTargets = new Stack<>(); //za sada samo "for"
	
	@Override
	public void visit(ForBegin forBegin) {
		//za init break i continue lista za ovu for petlju 
		forBreakJmp.push(new ArrayList<Integer>());
		forContinueJmp.push(new ArrayList<Integer>());
		breakTargets.push("for");
	}
	
	@Override
	public void visit(ForDS1_epsilon forDS1NO) {
		//nema init, pamtimo pocetak cond
		forCondBegin.push(Code.pc);
	}
	
	@Override
	public void visit(ForDS1_yes forDS1) {
		//nakon init, pamtimo pocetak cond
		forCondBegin.push(Code.pc);
	}
	
	@Override
	public void visit(ForCondition_epsilon ForConditionNO) {
		//nema cond, skaci na body, preskaci inc
		Code.putJump(0);
		forBodyJmp.push(Code.pc - 2);
		
		//pamtimo pocetak inc
		forIncBegin.push(Code.pc);
		
		//nema condition
	    forHasCond.push(false);
	}
	
	@Override
	public void visit(ForCondition_yes ForCondition) {
		//kraj cond
		//ako je netacno
		//skipIf ima adr za fixup false jmp to exit
		//i treba da preskocimo inc PRVI PUT, skaci na body
		Code.putJump(0);
		forBodyJmp.push(Code.pc - 2);
		
		//za tacno, pamtimo pocetak inc
		forIncBegin.push(Code.pc);
		
		//ima condition
	    forHasCond.push(true);
	}
	
	@Override
	public void visit(ForDS2_yes forDS1NO) {
		//kraj inc
		//SAD SE VRACA U CONDITION, da proveri za novi step uslov
		Code.putJump(forCondBegin.peek());  //ovo smo vec prosli pa znamo gde da skacemo
		
		//pocetak body, za one koji preskacu inc
		Code.fixup(forBodyJmp.pop());
		
		//ovo ne mora jer nakon inc svakako ide body
		//forBodyBegin.push(Code.pc);
		
		//nema inc
	    forHasInc.push(true);
	}
	
	@Override
	public void visit(ForDS2_epsilon forDS2) {
		//kraj inc
		//pocetak body, za one koji preskacu inc
		Code.fixup(forBodyJmp.pop());
		
		//ima inc
	    forHasInc.push(false);
	}
	
	@Override
	public void visit(OneStatement_for statementFor) {  //razresava break i continue i krajeve for
		//kraj body
		boolean hasCond = forHasCond.pop();
	    boolean hasInc = forHasInc.pop();
		
	    if (hasInc && !forContinueJmp.isEmpty()) {
	    	//continue da razresi, da pokazuje na inc
	    	List<Integer> continuesThisFor = forContinueJmp.pop();
		    for (Integer c : continuesThisFor) {
		        Code.fixup(c);
		    }
		    
		    //skoci na inc, koji posle skace na cond
		    if(!forIncBegin.isEmpty()) Code.putJump(forIncBegin.pop());
	    }
	    else if(!forContinueJmp.isEmpty()) {
	    	//continue da razresi, da pokazuje na cond
	    	List<Integer> continuesThisFor = forContinueJmp.pop();
		    for (Integer c : continuesThisFor) {
		        Code.fixup(c);
		    }
		    
		    //skoci na cond
		    if(!forCondBegin.isEmpty()) Code.putJump(forCondBegin.pop());
		    if(!forContinueJmp.isEmpty()) forIncBegin.pop(); //pop i ne iskoristi
	    }
	    
	    
		//ovde fixup false jmp, tj na kraj body i nigde ne skace vise
	    //zato ga stavljamo na kraj da nebi imao neki jmp izmedju
	    //znaci ovo je za false cond -> exitFor
	    if (hasCond && !skipIf.empty()) { //skipIf nema ako nema Cond
	        Code.fixup(skipIf.pop());
	    }
	    
	    //break-ove isto ovde razresava na kraju body
	    List<Integer> breaksThisFor = forBreakJmp.pop();
	    for (Integer b : breaksThisFor) {
	        Code.fixup(b);  //na ovu adr ide i stavlja kod jmp thisPC
	    }
	    
	    //kraj for i sklanjamo cond
	    //forCondBegin.pop();
	    if (!breakTargets.isEmpty() && "for".equals(breakTargets.peek())) {
	    	breakTargets.pop();
	    }
	}
	
}

/*
 (OneStatement_foreach) ForeachBegin LPARENT ForeachDS1 COLON ForeachDS2 RPARENT Statement
 ForeachBegin ::= (ForeachBegin) FOREACH;
 ForeachDS1 ::= (ForeachDS1_yes) DesignatorStatement 
 				| 
 				(ForeachDS1_epsilon) ;
 ForeachDS2 ::= (ForeachDS2_yes) DesignatorStatement 
 				| 
 				(ForeachDS2_epsilon) ;
 				
 				
 //////////////////////////
 
private Stack<Integer> foreachCondBegin = new Stack<>();
private Stack<Integer> foreachArrayAdr = new Stack<>();
private Stack<Integer> foreachBodyJmp = new Stack<>();

private Stack<List<Integer>> foreachBreakJmp = new Stack<>();
private Stack<List<Integer>> foreachContinueJmp = new Stack<>();

//nema generisanja koda kod ds1

@Override
public void visit(ForBegin forBegin) {

    foreachBreakJmp.push(new ArrayList<>());
    foreachContinueJmp.push(new ArrayList<>());

    breakTargets.push("foreach");
}

@Override
public void visit(ForEachDS2_yes node) {
    Code.load(node.getDesignator().obj); // arr
    Code.loadConst(0); // index
    
    foreachCondBegin.push(Code.pc);
}

@Override
public void visit(ForEachDS1_yes node) {
    Code.put(Code.dup2);
    Code.put(Code.arraylength);
    
    Code.putFalseJump(Code.ge, 0);  znaci ako nema vise onda skaci van for
    skipIf.push(Code.pc - 2);
	
	Code.put(Code.dup2);
    Code.put(Code.aload);
    Code.store(node.getDesignator().obj);
}

@Override
public void visit(OneStatement_continue statementContinue) {
    if (!foreachContinueJmp.isEmpty()) {
        Code.putJump(0);
        foreachContinueJmp.peek().add(Code.pc - 2);
    }
}

//u break
else if (!foreachBreakJmp.isEmpty()) {
    foreachBreakJmp.peek().add(adr);
}

@Override
public void visit(OneStatement_foreach foreachStmt) {
    // continue fixup
    List<Integer> continues = foreachContinueJmp.pop();
    for (Integer c : continues) {
        Code.fixup(c);
    }

    // index++
    Code.loadConst(1);
    Code.put(Code.add);

    Code.putJump(foreachCondBegin.peek());

    // exit
    Code.fixup(skipIf.pop());

    // break fixup
    List<Integer> breaks = foreachBreakJmp.pop();
    for (Integer b : breaks) {
        Code.fixup(b);
    }

    foreachCondBegin.pop();

    // skini array i index
    Code.put(Code.pop);
    Code.put(Code.pop);

    if (!breakTargets.isEmpty() && "foreach".equals(breakTargets.peek())) {
        breakTargets.pop();
    }
}


////Default za switch
DefaultBegin ::= (DefaultBegin) DEFAULT;
DefaultLine  ::= (DefaultLine) DefaultBegin COLON StatementContinue;

CaseStatement ::= (CaseStatement_yes) CaseStatement CaseLine
                | (CaseStatement_default) CaseStatement DefaultLine
                | (CaseStatement_epsilon)
                ;

//////////////////////////
private Stack<Integer> switchDefaultAdr = new Stack<>();

//u visitor(CaseBegin)
//posle owners switchNextCaseJmp inita dodaj 
switchDefaultAdr.push(-1);

@Override
public void visit(DefaultBegin defaultBegin) {
    // fiksiraj prethodni case mismatch da ide na default labelu
    if (!switchNextCaseJmp.isEmpty()) {
        int adr = switchNextCaseJmp.pop();
        if (adr != -1) {
            Code.fixup(adr);
        }
    }
    // default label adresa
    switchDefaultAdr.pop();
    switchDefaultAdr.push(Code.pc);

    // tretiraj default kao "case" label za fallthrough, ali bez poređenja:
    switchNextCaseJmp.push(-1);
}

////////
private Map<String, Integer> labels = new HashMap<>();
private Map<String, List<Integer>> patchAddrs = new HashMap<>();
//Label
	
	@Override
	public void visit(Label label) {
		labels.put(label.getI1(), Code.pc);
		
		if(patchAddrs.containsKey(label.getI1()))
			while(!patchAddrs.get(label.getI1()).isEmpty())
				Code.fixup(patchAddrs.get(label.getI1()).remove(0));
	}
	
	//Goto
	public void visit(SingleStatement_goto singleStatement_goto) {
		if(labels.containsKey(singleStatement_goto.getI1()))
			Code.putJump(labels.get(singleStatement_goto.getI1()));
		else {
			Code.putJump(0);
			int patchAddr = Code.pc - 2;
			List<Integer> l;
			if(patchAddrs.containsKey(singleStatement_goto.getI1()))
				l = patchAddrs.get(singleStatement_goto.getI1());
			else {
				l = new ArrayList<>();
				patchAddrs.put(singleStatement_goto.getI1(), l);
			}
			l.add(patchAddr);
		}
		
	}
	
	
///////////
//Do while
	private Stack<Integer> doBegin = new Stack<>();
	@Override
	public void visit(DoNonterm doNonterm) {
		doBegin.push(Code.pc);
		breakJump.push(new ArrayList<Integer>());
		continueJumps.push(new ArrayList<Integer>());
	}
	
	@Override
	public void visit(SingleStatement_do singleStatement_do) {
		Code.putJump(doBegin.pop());
		Code.fixup(skipThen.pop());
		
		while(!breakJump.peek().isEmpty())
			Code.fixup(breakJump.peek().remove(0));
		breakJump.pop();
	}
	
	//Break i Continue
	private Stack<List<Integer>> breakJump = new Stack<>();
	private Stack<List<Integer>> continueJumps = new Stack<>();
	
	@Override
	public void visit(SingleStatement_break singleStatement_break) {
		Code.putJump(0);
		breakJump.peek().add(Code.pc - 2);
	}
	
	@Override
	public void visit(SingleStatement_continue singleStatement_continue) {
		Code.putJump(0);
		continueJumps.peek().add(Code.pc - 2);
	}
	
	@Override
	public void visit(WhileNonterm whileNonterm) {
		while(!continueJumps.peek().isEmpty())
			Code.fixup(continueJumps.peek().remove(0));
		continueJumps.pop();
	}

 */
