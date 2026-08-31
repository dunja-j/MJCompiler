package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class ActParsCounterr extends VisitorAdaptor {
	private List<Struct> ActParsList1 = new ArrayList<>();
	private Stack<List<Struct>> actParsLists = new Stack<>();

	@Override
	public void visit(ActParsBegin actParsBegin) {
		actParsLists.push(new ArrayList<>());
	}
	
	@Override
	public void visit(ActPars actPars) {  //lista formalnih parametara metode
		actParsLists.peek().add(actPars.getExpr().struct);  //peek - dohvati sa vrha
	}
	
	@Override
	public void visit(ActParsList_expr actParsListExpr) {
		ActParsList1 = actParsLists.pop();  
		//poslednje napravljenu
	}
	
	@Override
	public void visit(ActParsList_epsilon actParsList) {
		ActParsList1 = actParsLists.pop();
		//pa ce se zadnje ubaciti prva napravljena
	}
	
	public List<Struct> getActParsList() {
		return ActParsList1;
	}
}
