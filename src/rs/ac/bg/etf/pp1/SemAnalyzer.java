package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemAnalyzer extends VisitorAdaptor {
	
	private boolean errorDetected = false;
	Logger log = Logger.getLogger(getClass());
	private Obj currProgram;
	private Struct currType;
	private int locConst;
	private Struct locConstType;
	private Struct boolType = Tab.find("bool").getType();
	private Obj mainMethod;
	private Obj currMethod;
	private Struct currEnum;
	private String currEnumName;
	Set<Integer> valuesInEnum;
	private int valEnum = 0;
	private Obj currEnumObj;
	private boolean returnHappend = false;
	private int petljeCnt = 0;
	private boolean insideFor = false;
	int nVars;  //da bi klasa Compile je dohvatila
	
	// findAny: skladiste skrivenih lokalnih promenljivih (brojac i mesto za pretragu) po AST cvoru,
	// da bi CodeGenerator mogao da ih pronadje bez ponovnog Tab.find (posto se scope u toj fazi vise ne otvara)
	static java.util.Map<Designator_findAny, Obj[]> findAnyTemps = new java.util.HashMap<>();
	private int findAnyTempCounter = 0;
	
	// map: skladiste skrivenih lokalnih promenljivih (brojac i referenca novog niza) po AST cvoru
	static java.util.Map<DesignatorMapBegin, Obj[]> mapTemps = new java.util.HashMap<>();
	private int mapTempCounter = 0;
	
	// count: skladiste skrivenih lokalnih promenljivih (indeks i brojac) po AST cvoru - NACRT, vidi komentar ispod
	// static java.util.Map<Designator_count, Obj[]> countTemps = new java.util.HashMap<>();
	// private int countTempCounter = 0;
	
	private boolean ourAssignableTo(Struct s1, Struct s2) { // gledamo da li su s2 = s1
    	if(s1.assignableTo(s2)) return true;
    	else if(s2.getKind() == Struct.Int && s1.getKind() == Struct.Enum) return true;
    	else if(s2.getKind() == Struct.Enum && s1.getKind() == Struct.Int) return true;
  
    	return false;
    	//ovu fju smo pravili zbog enuma, jer moze sa int da se poredi
    }
	
	

	/* LOG MESSAGES */
	public void report_error(String message, SyntaxNode info) {
		errorDetected   = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	/* SEMANTIC PASS CODE */
	
	// program
	@Override
	public void visit(ProgramName programName) {
		currProgram = Tab.insert(Obj.Prog, programName.getI1(), Tab.noType);
		Tab.openScope();
	}
	
	@Override
	public void visit(Program program) {
		nVars = Tab.currentScope().getnVars();   //prebrojavanje varijabli
		Tab.chainLocalSymbols(currProgram);
		Tab.closeScope();
		currProgram = null;
		
		if (mainMethod == null || mainMethod.getLevel() > 0) {  //da ne sme int main(int f1), nego samo void main()
			report_error("[Program] Nema adekvatnu main metodu", program);
		}
	}
	
	// const
	@Override
	public void visit(ConstAssign constAssign) {
		Obj objConst = Tab.find(constAssign.getI1());
		if (objConst != Tab.noObj) {
			report_error("[Const] Dva puta definicija: " + constAssign.getI1(), constAssign);
		}
		else {
			if (ourAssignableTo(locConstType, currType)) {
				objConst = Tab.insert(Obj.Con, constAssign.getI1(), currType);
				objConst.setAdr(locConst);
				report_info("Deklarisanje const: " + objConst.getName() + " preko obj cvora " + ObjToString.objToString(objConst), constAssign);
			}
			else report_error("[Const] Nekorektna dodela : " + constAssign.getI1(), constAssign);
		}
	}
	
	@Override
	public void visit(ConstValue_num constNum) {
		locConst = constNum.getN1();
		locConstType = Tab.intType;
	}
	
	@Override
	public void visit(ConstValue_char constChar) {
		locConst = constChar.getC1();
		locConstType = Tab.charType;
	}
	
	@Override
	public void visit(ConstValue_bool constBool) {
		locConst = constBool.getB1();
		locConstType = boolType;
	}
	
	// var
	@Override
	public void visit(VarVariable_arr varVariableArr) {
		Obj objVar = null;
		if (currMethod == null) objVar = Tab.find(varVariableArr.getI1());  //ako smo u glob delu
		else objVar = Tab.currentScope().findSymbol(varVariableArr.getI1());  //u metodi (da se ne preklope promenljive)
		
		if (objVar == null || objVar == Tab.noObj) {
			objVar = Tab.insert(Obj.Var, varVariableArr.getI1(), new Struct(Struct.Array, currType));
		}
		else report_error("[VarArr] Dva puta definicija: " + varVariableArr.getI1(), varVariableArr);
	}
	
	@Override
	public void visit(VarVariable_var varVariableVar) {
		Obj objVar = null;
		if (currMethod == null) objVar = Tab.find(varVariableVar.getI1());  //ako smo u glob delu
		else objVar = Tab.currentScope().findSymbol(varVariableVar.getI1());  //u metodi
		
		if (objVar == null || objVar == Tab.noObj) {  //ako je null nije dobro, jer prema gore mora da bude bar nesto
			objVar = Tab.insert(Obj.Var, varVariableVar.getI1(), currType);
			report_info("Deklarisanje globalne var promenljive: " + objVar.getName() + " preko objektnog cvora ." + ObjToString.objToString(objVar), varVariableVar);
		}
		else report_error("[VarVar] Dva puta definicija: " + varVariableVar.getI1(), varVariableVar);
	}
	
	// enum
	@Override
	public void visit(EnumName enumName) {
		Obj objEnum = Tab.find(enumName.getI1());  //da li vec postoji
		if (objEnum != Tab.noObj) {
			report_error("[Enum] Dva puta definicija: " + enumName.getI1(), enumName);
		}
		else {
			currEnum = new Struct(Struct.Enum);
			//currEnum = Tab.intType;
			currEnumName = enumName.getI1();
		    
			//da spreci duplikate vrednosti
			valuesInEnum = new HashSet<>();
			
			currEnumObj = Tab.insert(Obj.Type, enumName.getI1(), currEnum);  //u ts upisujemo 
			Tab.openScope();  //za taj enum:enum Boja { CRVENA, ZELENA }
		}
	}
	
	@Override
	public void visit(EnumDecl enumDecl) {
		Tab.chainLocalSymbols(currEnumObj);  //mora biti obj a ne struct jer Tab.dump ne vidi onda
		Tab.closeScope();
		
		currEnum = null;
		currEnumName = null;
		currEnumObj = null;
		valEnum  = 0;
		valuesInEnum.clear();
	}
	
	@Override
	public void visit(EnumAssign_var enumVar) {
		Obj objConstEnum = Tab.currentScope().findSymbol(enumVar.getI1());
		if (objConstEnum != Tab.noObj && objConstEnum != null) {
			report_error("[Enum] Dva puta definicija konstante: " + enumVar.getI1() + " za enum: " + currEnumName, enumVar);
		}
		else if (valuesInEnum.contains(valEnum)) {  //ne sme enum X { A=1, B=1 }
			report_error("[Enum] Vec postoji konstanta sa vrednoscu: " + valEnum + " za enum: " + currEnumName, enumVar);
		}
		else {
			objConstEnum = Tab.insert(Obj.Con, enumVar.getI1(), Tab.intType);
			objConstEnum.setAdr(valEnum);  //njena vrednost
			valuesInEnum.add(valEnum);
			valEnum = valEnum + 1;
		}
	}
	
	@Override
	public void visit(EnumAssign_assign enumAssign) {
		Obj objConstEnum = Tab.currentScope().findSymbol(enumAssign.getI1());
		if (objConstEnum != Tab.noObj && objConstEnum != null) {
			report_error("[Enum] Dva puta definicija konstante: " + enumAssign.getI1() +  " za enum: " + currEnumName, enumAssign);
		}
		else if (valuesInEnum.contains(enumAssign.getN2())) {
			report_error("[Enum] Vec postoji konstanta sa vrednoscu: " + enumAssign.getN2() + " za enum: " + currEnumName, enumAssign);
		}
		else {
			objConstEnum = Tab.insert(Obj.Con, enumAssign.getI1(), Tab.intType);
			objConstEnum.setAdr(enumAssign.getN2());
			valuesInEnum.add(enumAssign.getN2());
			valEnum = enumAssign.getN2() + 1;
		}
	}
	
	// method
	@Override
	public void visit(MethodTypeAndName_void methodName) {
		methodName.obj = currMethod = Tab.insert(Obj.Meth, methodName.getI1(), Tab.noType);
		Tab.openScope();
		
		if (methodName.getI1().equalsIgnoreCase("main")) mainMethod = currMethod;
	}
	
	@Override
	public void visit(MethodTypeAndName_type methodName) {
		methodName.obj = currMethod = Tab.insert(Obj.Meth, methodName.getI2(), currType);
		Tab.openScope();
	}
	
	@Override
	public void visit(MethodDecl method) {
		Tab.chainLocalSymbols(currMethod);
		Tab.closeScope();
		
		if (currMethod.getType() != Tab.noType && !returnHappend) {
			report_error("[MethodDecl] Nema return unutar metode: " + currMethod.getName(), method);
			
		}
		
		returnHappend = false;
		currMethod = null;
	}
	
	// formParam //prekopiraj od var isto je
	@Override
	public void visit(FormParsDeclaration_arr formParsArr) {
		Obj objVar = null;
		if (currMethod == null) report_error("[FromParsArr] Semanticka greska", formParsArr); //jer mora vec biti u nekom metodu
		else objVar = Tab.currentScope().findSymbol(formParsArr.getI2());  //u metodi (da se ne preklope promenljive)
		
		if (objVar == null || objVar == Tab.noObj) {
			objVar = Tab.insert(Obj.Var, formParsArr.getI2(), new Struct(Struct.Array, currType));
			//setupovanje tabele simbola bitno za sledecu fazu
			objVar.setFpPos(1);
			currMethod.setLevel(currMethod.getLevel() +  1);
		}
		else report_error("[VarArr] Dva puta definicija: " + formParsArr.getI2(), formParsArr);
	}
	
	@Override
	public void visit(FormParsDeclaration_var formParsVar) {
		Obj objVar = null;
		if (currMethod == null) report_error("[FromParsVar] Semanticka greska", formParsVar); //jer mora vec biti u nekom metodu
		else objVar = Tab.currentScope().findSymbol(formParsVar.getI2());  //u metodi
		
		if (objVar == null || objVar == Tab.noObj) {  //ako je null nije dobro, jer prema gore mora da bude bar nesto
			objVar = Tab.insert(Obj.Var, formParsVar.getI2(), currType);
			//setupovanje tabele simbola bitno za sledecu fazu
			objVar.setFpPos(1);
			currMethod.setLevel(currMethod.getLevel() +  1);
		}
		else report_error("[FromParsVar] Dva puta definicija: " + formParsVar.getI2(), formParsVar);
	}
	
	
	// type
	@Override
	public void visit(Type type) {
		Obj objType = Tab.find(type.getI1());
		if (objType == Tab.noObj) {
			report_error("[Type] Nepostojeci: " + type.getI1(), type);
			type.struct = currType = Tab.noType;
		}
		else if (objType.getKind() != Obj.Type) {
			report_error("[Type] Neadvekatan Kind: " + type.getI1(), type);
			type.struct = currType = Tab.noType;
		}
		else type.struct = currType = objType.getType();
	}
	
	
	/* CCONTECST CONDITIONSS */
	
	//////////////////////////// Designator
	@Override
	public void visit(Designator_var designatorVar) {
		Obj objVar = Tab.find(designatorVar.getI1()); //mora biti u tabeli simbola
		if (objVar == Tab.noObj) {
			report_error("[DesignatorVar] Pristupamo nedefinisanoj promenljivi: " + designatorVar.getI1(), designatorVar);
			designatorVar.obj = Tab.noObj;   //mora da ne bi puklo, da bi se ispisala greska
		}
		else if (objVar.getKind() != Obj.Var && objVar.getKind() != Obj.Con && objVar.getKind() != Obj.Meth) {  //elem svakako nikako nece dohvatitit iz ts
			report_error("[DesignatorVar] Neadekvatna promenljiva: " + designatorVar.getI1(), designatorVar);
			designatorVar.obj = Tab.noObj; 
		}
		else {
			designatorVar.obj = objVar;
			
			// za report info
			String stringType = null;
			switch(objVar.getKind()) {
			case Obj.Var:
				if(objVar.getLevel() == 0) stringType = "globalna prom";
				else if(objVar.getFpPos() == 1) stringType = "formalni argument f-je";
				else stringType = "lokalna prom";
				break;
			case Obj.Con:
				stringType = "konstanta";
				break;
			}
			
			if(stringType != null) report_info("Pristup tipu: (" + stringType + ") preko obj cvora " + ObjToString.objToString(objVar), designatorVar);
		}
	}
	
	@Override
	public void visit(DesignatorArrName designatorArrName) {  //index niza
		Obj objArr = Tab.find(designatorArrName.getI1()); //mora biti u tabeli simbola
		if (objArr == Tab.noObj) {
			report_error("[DesignatorArrName] Pristupamo nedefinisanoj promenljivi niza: " + designatorArrName.getI1(), designatorArrName);
			designatorArrName.obj = Tab.noObj;   //mora da ne bi puklo, da bi se ispisala greska
		}
		else if (objArr.getKind() != Obj.Var || objArr.getType().getKind() != Struct.Array) {  //za nizove je kind uvek Vars
			report_error("[DesignatorArrName] Neadekvatna promenljiva niza: " + designatorArrName.getI1(), designatorArrName);
			designatorArrName.obj = Tab.noObj; 
		}
		else designatorArrName.obj = objArr; //postoji u ts i sig je varijabla i sig nizovska prom
	}
	
	@Override
	public void visit(Designator_expr designatorArr) {  //index niza
		//moj sin je vec pitao da li to nesto postoji, pa ne idemo u ts
		Obj objArr = designatorArr.getDesignatorArrName().obj;
		if (objArr == Tab.noObj) {
			//samo prosledjujemo sinu
			designatorArr.obj = Tab.noObj;  	
		}
		else if (!designatorArr.getExpr().struct.equals(Tab.intType)
				&& !(designatorArr.getExpr().struct.getKind() == Struct.Enum)
		) {
			report_error("[DesignatorExpr] Nije int elem niza", designatorArr);
			designatorArr.obj = Tab.noObj; 
		}
		else {
			designatorArr.obj = new Obj(Obj.Elem, objArr.getName() + "[$]", objArr.getType().getElemType());
			report_info("Pristup elem niza: {" + objArr.getName()
						+ "} preko obj cvora " + ObjToString.objToString(objArr), designatorArr);
		}	
	}
	
	@Override
	public void visit(Designator_enumident designatorEnum) {
		Obj objEnum = Tab.find(designatorEnum.getI1()); //mora biti u tabeli simbola
		if (objEnum == Tab.noObj) {
			report_error("[DesignatorEnum] Pristupamo nedefinisanoj promenljivi: " + designatorEnum.getI1(), designatorEnum);
			designatorEnum.obj = Tab.noObj;   //mora da ne bi puklo, da bi se ispisala greska
		}
		else if (objEnum.getKind() != Obj.Type || objEnum.getType().getKind() != Struct.Enum) {  //elem svakako nikako nece dohvatitit iz ts
			report_error("[DesignatorEnum] Neadekvatna promenljiva: " + designatorEnum.getI1(), designatorEnum);
			designatorEnum.obj = Tab.noObj; 
		}
		else {
			String constEnum = designatorEnum.getI2();
			boolean found = false;
			//for (Obj k: objEnum.getType().getMembers()) { //ako je Struct
			for (Obj k: objEnum.getLocalSymbols()) {  //ako je Obj insertovan
				if (k.getName().equals(constEnum)) {
					designatorEnum.obj = k;
					found = true;
					break;
				}
			}
			if (!found) {
				report_error("[DesignatorEnum] Enum " + objEnum.getName() + " nema polje: " + constEnum, designatorEnum);
				designatorEnum.obj = Tab.noObj;	
			}
		}
	}
	
	@Override
	public void visit(DesignatorArrLength designatorLength) { 
		Obj objArr = Tab.find(designatorLength.getI1());
		if (objArr == Tab.noObj) {
			report_error("[DesignatorArrLength] Pristupamo nedefinisanoj promenljivi niza: " + designatorLength.getI1(), designatorLength);
			designatorLength.obj = Tab.noObj;
		}
		else if (objArr.getKind() != Obj.Var || objArr.getType().getKind() != Struct.Array) {  //za nizove je kind uvek Vars
			report_error("[DesignatorArrLength] Neadekvatna promenljiva niza: " + designatorLength.getI1(), designatorLength);
			designatorLength.obj = Tab.noObj; 
		}
		else designatorLength.obj = objArr; //postoji u ts i sig je varijabla i sig nizovska prom
	}
	
	@Override
	public void visit(Designator_length designatorLength) { 
		//Obj objArr = designatorLength.getDesignatorArrLength().obj;
		designatorLength.obj = new Obj(Obj.Con, "length", Tab.intType);
	}
	
	@Override
	public void visit(Designator_findAny findAny) {
		Obj arrObj = Tab.find(findAny.getI1());
		Struct elemType = null;
	
		if (arrObj == Tab.noObj) {
			report_error("[DesignatorFindAny] Pristupamo nedefinisanoj promenljivi niza: " + findAny.getI1(), findAny);
		}
		else if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
			report_error("[DesignatorFindAny] " + findAny.getI1() + " nije niz", findAny);
		}
		else {
			elemType = arrObj.getType().getElemType();
			if (!elemType.equals(Tab.intType) && !elemType.equals(Tab.charType) && !elemType.equals(boolType)) {
				report_error("[DesignatorFindAny] Niz " + findAny.getI1() + " nije ugradjenog tipa (int/char/bool)", findAny);
				elemType = null;
			}
			else if (!ourAssignableTo(findAny.getExpr().struct, elemType) && !ourAssignableTo(elemType, findAny.getExpr().struct)) {
				report_error("[DesignatorFindAny] Neodgovarajuci tip izraza za pretragu u nizu: " + findAny.getI1(), findAny);
				elemType = null;
			}
		}
	
		findAny.obj = new Obj(Obj.Con, "findAny", boolType);
	
		if (elemType != null) {
			Obj counterObj = Tab.insert(Obj.Var, "$fa$i$" + findAnyTempCounter, Tab.intType);
			Obj searchValObj = Tab.insert(Obj.Var, "$fa$v$" + findAnyTempCounter, elemType);
			findAnyTempCounter++;
			findAnyTemps.put(findAny, new Obj[]{ counterObj, searchValObj, arrObj });
		}
	}
	
	@Override
	public void visit(DesignatorMapBegin mapBegin) {
		Obj arrObj = Tab.find(mapBegin.getI1());
		Obj identObj = Tab.find(mapBegin.getI2());
		Struct elemType = null;
	
		if (arrObj == Tab.noObj) {
			report_error("[DesignatorMap] Pristupamo nedefinisanoj promenljivi niza: " + mapBegin.getI1(), mapBegin);
		}
		else if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
			report_error("[DesignatorMap] " + mapBegin.getI1() + " nije niz", mapBegin);
		}
		else {
			elemType = arrObj.getType().getElemType();
			if (!elemType.equals(Tab.intType) && !elemType.equals(Tab.charType) && !elemType.equals(boolType)) {
				report_error("[DesignatorMap] Niz " + mapBegin.getI1() + " nije ugradjenog tipa (int/char/bool)", mapBegin);
				elemType = null;
			}
		}
	
		if (identObj == Tab.noObj) {
			report_error("[DesignatorMap] Nepostojeca promenljiva: " + mapBegin.getI2(), mapBegin);
			elemType = null;
		}
		else if (identObj.getKind() != Obj.Var) {
			report_error("[DesignatorMap] " + mapBegin.getI2() + " nije promenljiva", mapBegin);
			elemType = null;
		}
		else if (elemType != null && !identObj.getType().equals(elemType)) {
			report_error("[DesignatorMap] Tip promenljive " + mapBegin.getI2() + " ne odgovara tipu elementa niza " + mapBegin.getI1(), mapBegin);
			elemType = null;
		}
	
		if (elemType != null) {
			Obj counterObj = Tab.insert(Obj.Var, "$map$i$" + mapTempCounter, Tab.intType);
			Obj newArrObj = Tab.insert(Obj.Var, "$map$arr$" + mapTempCounter, new Struct(Struct.Array, elemType));
			mapTempCounter++;
			mapTemps.put(mapBegin, new Obj[]{ counterObj, newArrObj, arrObj, identObj });
		}
	}
	
	@Override
	public void visit(Designator_map designatorMap) {
		Obj[] temps = mapTemps.get(designatorMap.getDesignatorMapBegin());
		if (temps == null) {
			designatorMap.obj = new Obj(Obj.Con, "map", Tab.noType);
			return;
		}
	
		Obj srcArrObj = temps[2];
		Struct elemType = srcArrObj.getType().getElemType();
	
		if (!ourAssignableTo(designatorMap.getExpr().struct, elemType) && !ourAssignableTo(elemType, designatorMap.getExpr().struct)) {
			report_error("[DesignatorMap] Neodgovarajuci tip izraza u map pozivu", designatorMap);
		}
	
		designatorMap.obj = new Obj(Obj.Con, "map", srcArrObj.getType());
	}
	
	//////////////////////////// Factor
	@Override
	public void visit(FactorReal_d factorDes) {
		factorDes.struct = factorDes.getDesignator().obj.getType();  //posto smo ga napravili kao obj, moramo ga pretvoriti u struct
	}
	
	@Override
	public void visit(FactorReal_method factorMethod) {
		int kindF = factorMethod.getDesignator().obj.getKind();
		
		if (kindF != Obj.Meth) {
			report_error("[FactorRealMethod] Poziv neadekvatne metode: " + factorMethod.getDesignator().obj.getName(), factorMethod);
			factorMethod.struct = Tab.noType;
		}
		else {
			factorMethod.struct = factorMethod.getDesignator().obj.getType();
		
			List<Struct> fpList = new ArrayList<>();  // m1(int fp1, int fp2), lista formalnih parametara
			for (Obj k: factorMethod.getDesignator().obj.getLocalSymbols()) {  //prelancani su jer je ova metoda vec izdefinisana, ovo je poziv metode u nekoj drugoj metodi
				if (k.getKind() == Obj.Var && k.getLevel() == 1 && k.getFpPos() == 1) {
					fpList.add(k.getType());  //structurni cvor smo dodali
				}
			}
			//report_error("[DesignatorStatementMethod] Broj FP " + fpList.size(), dsMethod);
			
			ActParsCounterr apc = new ActParsCounterr();  //actual params
			factorMethod.getActParsList().traverseBottomUp(apc);  //napunice listu bottom up obilaskom ovog podstabla
			
			List<Struct> apList = apc.getActParsList();
			if(fpList.size() != apList.size()
				|| factorMethod.getDesignator().obj.getLevel() != apList.size()
			) {
					report_error("[FactorRealMethod] BROJ PARAMS pri pozivu metode " + factorMethod.getDesignator().obj.getName() + " nije tacan!", factorMethod);
			}
			else {
				for (int i = 0; i < fpList.size(); i++) {
					Struct fps = fpList.get(i); //elem po elem
					Struct aps = apList.get(i);
					
					if(!ourAssignableTo(aps,fps)) {
						report_error("[FactorRealMethod] Greska kod poziva metode " + factorMethod.getDesignator().obj.getName() 
									+ " pokusaj dodele vrednosti parametra tipa: " + aps.getKind()
									+ " parametru koji je vec tipa: " + fps.getKind(), factorMethod);
					}
				}
				// za report info
				report_info("Poziv glob metode: {" + factorMethod.getDesignator().obj.getName() 
							+ "} preko obj cvora " + ObjToString.objToString(factorMethod.getDesignator().obj) , factorMethod);
			}
		}
	}
	
	@Override
	public void visit(FactorReal_num factorNum) {
		factorNum.struct = Tab.intType;
	}
	
	@Override
	public void visit(FactorReal_char factorChar) {
		factorChar.struct = Tab.charType;
	}
	
	@Override
	public void visit(FactorReal_bool factorBool) {
		factorBool.struct = boolType;
	}
	
	@Override
	public void visit(FactorReal_newarr factorNewArr) { //new int[5]
		if (!factorNewArr.getExpr().struct.equals(Tab.intType) ) {
			report_error("[FactorNewArr] Nije int velicina niza", factorNewArr);
			factorNewArr.struct = Tab.noType;
		}
		else factorNewArr.struct = new Struct(Struct.Array, currType);  // a = new int[2]
	}
	
	@Override
	public void visit(FactorReal_expr factorExpr) { //new int[5]
		factorExpr.struct = factorExpr.getExpr().struct;
	}
	
	@Override
	public void visit(FactorReal_at factorAt) { // (Expr1 @ Expr2)
		if (!factorAt.getExpr().struct.equals(Tab.intType) || !factorAt.getExpr1().struct.equals(Tab.intType)) {
			report_error("[FactorRealAt] Oba operanda '@' moraju biti int", factorAt);
			factorAt.struct = Tab.noType;
		}
		else factorAt.struct = Tab.intType;
	}
	
	@Override
	public void visit(Factor factor) {
		if (factor.getUnary() instanceof Unary_minus) { //desio se minus
			if (factor.getFactorReal().struct.equals(Tab.intType)) {
				factor.struct = Tab.intType;
			}
			else {
				report_error("[Factor] Negacija not int vrednosti", factor);
				factor.struct = Tab.noType;
			}
		}
		else factor.struct = factor.getFactorReal().struct;  //prosledjuje se
	}
	
	//////////////////////////// Expr
	@Override
	public void visit(MulopList_factor mulopFactor) {
		mulopFactor.struct = mulopFactor.getFactor().struct;
	}
	
	@Override
	public void visit(MulopList_mulop mulopList) { 
		//ima vec svoj tip jer se ide sleva nadesno i vec smo obisli stablo prvo ovim gornjim visit
		Struct left = mulopList.getMulopList().struct;  // onaj gore visit polje
		Struct right = mulopList.getFactor().struct;  // ono polje iz Factor
		
		if (left.equals(Tab.intType) && right.equals(Tab.intType)
			|| left.equals(Tab.intType) && right.getKind() == Struct.Enum
			|| left.getKind() == Struct.Enum && right.equals(Tab.intType)
			|| left.getKind() == Struct.Enum && right.getKind() == Struct.Enum
		){
			mulopList.struct = Tab.intType;
		}
		else {
			report_error("[MulopList] Mulop operacija not int vrednosti", mulopList);
			mulopList.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(Term term) {
		term.struct = term.getMulopList().struct;
	}
	
	@Override
	public void visit(ExpAddop_term addopTerm) {
		addopTerm.struct = addopTerm.getTerm().struct;
	}
	
	@Override
	public void visit(ExpAddop_addop addopList) { 
		//ima vec svoj tip jer se ide sleva nadesno i vec smo obisli stablo prvo ovim gornjim visit
		Struct left = addopList.getExpAddop().struct;  // onaj gore visit polje
		Struct right = addopList.getTerm().struct;  // ono polje iz Term
		
		if (left.equals(Tab.intType) && right.equals(Tab.intType)
			|| left.equals(Tab.intType) && right.getKind() == Struct.Enum
			|| left.getKind() == Struct.Enum && right.equals(Tab.intType)
			|| left.getKind() == Struct.Enum && right.getKind() == Struct.Enum
		) {
			addopList.struct = Tab.intType;
		}
		else {
			report_error("[ExprAddopList] Addop operacija not int vrednosti", addopList);
			addopList.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(ExprNonTernary exprNonT) {
		exprNonT.struct = exprNonT.getExpAddop().struct;
	}
	
	@Override
	public void visit(ExprTernary exprT) {
		//Struct condType = exprT.getCondition().struct;
		Struct left = exprT.getExpr().struct;  // onaj gore visit polje
		Struct right = exprT.getExpr1().struct;  // ono polje iz Term
		
		if (left.compatibleWith(right)) { //ne equals
			exprT.struct = left;
		}
		else {
			report_error("[ExprTernary] 2. i 3. operand ternarnog operatora nisu kompatibilni.", exprT);
			exprT.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(Expr_nonTernary exprNonT) {
		exprNonT.struct = exprNonT.getExprNonTernary().struct;
	}
	
	@Override
	public void visit(Expr_ternary exprT) {
		exprT.struct = exprT.getExprTernary().struct;
	}
	
	//////////////////////////// DesignatorStatements
	@Override
	public void visit(DesignatorStatement_assign dsAssign) {  // ne radi a = b; b je niz npr
		int kindDS = dsAssign.getDesignator().obj.getKind(); //sme samo Var i a[]
	
		if (kindDS != Obj.Var && kindDS != Obj.Elem) {
			report_error("[DesignatorStatementAssign] Dodela u neadekvatnu promenljivu: " + dsAssign.getDesignator().obj.getName(), dsAssign);
		}
		else if (!ourAssignableTo(dsAssign.getExpr().struct, dsAssign.getDesignator().obj.getType())) {
			report_error("[DesignatorStatementAssign] Dodela u neadekvatnu promenljivu: " + dsAssign.getDesignator().obj.getName(), dsAssign);
		}
	}
	
	@Override
	public void visit(DesignatorStatement_method dsMethod) {
		int kindDS = dsMethod.getDesignator().obj.getKind();
	
		if (kindDS != Obj.Meth) {
			report_error("[DesignatorStatementMethod] Poziv neadekvatne metode: " + dsMethod.getDesignator().obj.getName(), dsMethod);
		}
		else {
			List<Struct> fpList = new ArrayList<>();  // m1(int fp1, int fp2), lista formalnih parametara
			for (Obj k: dsMethod.getDesignator().obj.getLocalSymbols()) {  //prelancani su jer je ova metoda vec izdefinisana, ovo je poziv metode u nekoj drugoj metodi
				if (k.getKind() == Obj.Var && k.getLevel() == 1 && k.getFpPos() == 1) {
					fpList.add(k.getType());  //structurni cvor smo dodali
				}
			}
			//report_error("[DesignatorStatementMethod] Broj FP " + fpList.size(), dsMethod);
			
			ActParsCounterr apc = new ActParsCounterr();  //actual params
			dsMethod.getActParsList().traverseBottomUp(apc);  //napunice listu bottom up obilaskom ovog podstabla
			
			List<Struct> apList = apc.getActParsList();
			if(fpList.size() != apList.size() || dsMethod.getDesignator().obj.getLevel() != apList.size()) {
					report_error("[DesignatorStatementMethod] BROJ PARAMS pri pozivu metode " + dsMethod.getDesignator().obj.getName() + " nije tacan!", dsMethod);
			}
			else {
				for (int i = 0; i < fpList.size(); i++) {
					Struct fps = fpList.get(i); //elem po elem
					Struct aps = apList.get(i);
					
					if(!ourAssignableTo(aps, fps)) {
						report_error("[DesignatorStatementMethod] Greska kod poziva metode " + dsMethod.getDesignator().obj.getName() 
									+ " pokusaj dodele vrednosti parametra tipa: " + aps.getKind()
									+ " parametru koji je vec tipa: " + fps.getKind(), dsMethod);
					}
				}
				// za report info
				report_info("Poziv globalne metode: {" + dsMethod.getDesignator().obj.getName() 
							+ "} preko obj cvora " + ObjToString.objToString(dsMethod.getDesignator().obj) , dsMethod);
			}
			
		}
	}
	
	@Override
	public void visit(DesignatorStatement_inc dsInc) {
		int kindDS = dsInc.getDesignator().obj.getKind();
	
		if (kindDS != Obj.Var && kindDS != Obj.Elem) {
			report_error("[DesignatorStatementInc] Inkrementiranje neadekvatne promenljive: " + dsInc.getDesignator().obj.getName(), dsInc);
		}
		else if (!dsInc.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("[DesignatorStatementInc] Inkrementiranje not int promenljive: " + dsInc.getDesignator().obj.getName(), dsInc);
		}
	}
	
	@Override
	public void visit(DesignatorStatement_dec dsDec) {
		int kindDS = dsDec.getDesignator().obj.getKind();
	
		if (kindDS != Obj.Var && kindDS != Obj.Elem) {
			report_error("[DesignatorStatementDec] Dekrementiranje neadekvatne promenljive: " + dsDec.getDesignator().obj.getName(), dsDec);
		}
		else if (!dsDec.getDesignator().obj.getType().equals(Tab.intType)) {
			report_error("[DesignatorStatementDec] Dekrementiranje not int promenljive: " + dsDec.getDesignator().obj.getName(), dsDec);
		}
	}
	
	//////////////////////////// Statement
	@Override
	public void visit(OneStatement_read statementRead) {
		int kindDS = statementRead.getDesignator().obj.getKind();
		Struct tip = statementRead.getDesignator().obj.getType();
	
		if (kindDS != Obj.Var && kindDS != Obj.Elem) {
			report_error("[StatementRead] Read neadekvatne promenljive: " + statementRead.getDesignator().obj.getName(), statementRead);
		}
		else if (!tip.equals(Tab.intType) && !tip.equals(Tab.charType) && !tip.equals(boolType)) {
			report_error("[StatementRead] Read not int, char ili bool promenljive: " + statementRead.getDesignator().obj.getName(), statementRead);
		}
	}
	
	@Override
	public void visit(OneStatement_print1 statementPrint1) {
		Struct tip = statementPrint1.getExpr().struct;
	
		if (!tip.equals(Tab.intType) && !tip.equals(Tab.charType) && !tip.equals(boolType)) {
			report_error("[StatementPrint1] Print not int, char ili bool izraza", statementPrint1);
		}
	}
	
	@Override
	public void visit(OneStatement_print2 statementPrint2) {
		Struct tip = statementPrint2.getExpr().struct;
	
		if (!tip.equals(Tab.intType) && !tip.equals(Tab.charType) && !tip.equals(boolType)) {
			report_error("[StatementPrint2] Print not int, char ili bool izraza", statementPrint2);
		}
	}
	
	@Override
	public void visit(OneStatement_return statementReturn) {
		returnHappend = true;
		if (currMethod == null){
			report_error("[StatementReturn] Detektovan return van scope f-je!", statementReturn);
		} 
		else if (currMethod.getType() != Tab.noType) {
			report_error("[StatementReturn] Ne validan return unutar metode", statementReturn);
		}
	}
	
	@Override
	public void visit(OneStatement_returnExpr statementReturnExpr) {
		returnHappend = true;
		if (currMethod == null){
			report_error("[StatementReturnExpr] Detektovan return van scope f-je!", statementReturnExpr);
		} 
		else if (currMethod.getType() == Tab.noType) {
			report_error("[StatementReturnExpr] return sa Expr ne moze biti u f-ji sa povratnim tipom void!", statementReturnExpr);
		
		}
		else if (!currMethod.getType().equals(statementReturnExpr.getExpr().struct)) {
			if(ourAssignableTo(statementReturnExpr.getExpr().struct, currMethod.getType())) return;
			report_error("[StatementReturnExpr] Ne validan return unutar metode", statementReturnExpr);
		}
	}
	
	@Override
	public void visit(OneStatement_break statementBreak) {
		if(!insideFor) {
			report_error("[StatementBreak] Break nije unutar for petlje.", statementBreak);
		}
	}
	
	@Override
	public void visit(OneStatement_continue statementContinue) {
		if(!insideFor) {
			report_error("[statementContinue] Continue nije unutar for petlje.", statementContinue);
		}
	}
	
	//////////// For
	@Override
	public void visit(ForBegin forBegin) {
		insideFor = true;
		petljeCnt++;
	}
	
	@Override
	public void visit(OneStatement_for statementFor) {
		petljeCnt--;
		if (petljeCnt == 0) insideFor = false;
	}
	
	//////////////////////////// Condition
	@Override
	public void visit(CondFact_noting condFactExpr) {
		if(!condFactExpr.getExprNonTernary().struct.equals(boolType)) {
			report_error("[CondFactExpr] Expr operand nije bool.", condFactExpr);
			condFactExpr.struct = Tab.noType;
		}
		else condFactExpr.struct = boolType;
	}
	
	@Override
	public void visit(CondFact_relop condFactRelop) {
		Struct left = condFactRelop.getExprNonTernary().struct;
		Struct right = condFactRelop.getExprNonTernary1().struct;
		
		if(left.compatibleWith(right)) {  //kompatibilni
			if (left.isRefType() || right.isRefType()) {
				if (condFactRelop.getRelop() instanceof Relop_eq || condFactRelop.getRelop() instanceof Relop_ne) { //== ili !=
					condFactRelop.struct = boolType;
				}
				else {
					report_error("[CondFactRelop] Poredjenje ref tipova sa ne == ili !=", condFactRelop);
					condFactRelop.struct = Tab.noType;
				}
			}
			else condFactRelop.struct = boolType;
		}
		else {
			report_error("[CondFactRelop] Expr operandi nisu kompatibilni", condFactRelop);
			condFactRelop.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(CondFactList_nothing condFact) {
		condFact.struct = condFact.getCondFact().struct;
	}
	
	@Override
	public void visit(CondFactList_and condFactAnd) { 
		Struct left = condFactAnd.getCondFactList().struct;  // onaj gore visit polje
		Struct right = condFactAnd.getCondFact().struct;  // ono polje iz Term
		
		if (left.equals(boolType) && right.equals(boolType)) {
			condFactAnd.struct = boolType;
		}
		else {
			report_error("[CondFactAnd] AND operacija not bool vrednosti", condFactAnd);
			condFactAnd.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(CondTerm condTerm) {
		condTerm.struct = condTerm.getCondFactList().struct;
	}
	
	@Override
	public void visit(CondTermList_nothing condTerm) {
		condTerm.struct = condTerm.getCondTerm().struct;
	}
	
	@Override
	public void visit(CondTermList_or condTermOr) { 
		Struct left = condTermOr.getCondTermList().struct;  // onaj gore visit polje
		Struct right = condTermOr.getCondTerm().struct;  // ono polje iz Term
		
		if (left.equals(boolType) && right.equals(boolType)) {
			condTermOr.struct = boolType;
		}
		else {
			report_error("[CondTermListOr] OR operacija not bool vrednosti", condTermOr);
			condTermOr.struct = Tab.noType;
		}
	}
	
	@Override
	public void visit(Condition condition) {
		condition.struct = condition.getCondTermList().struct;
		if (!condition.struct.equals(boolType)) {
			report_error("[Condition] Uslov not bool vrednosti", condition);
		}
	}
}


/*
 
private int defaultCountInSwitch = 0;

@Override
public void visit(CaseBegin caseBegin) {
    insideCase = true;
    // ... postoji već caseCnt/insideCase logika
}

@Override
public void visit(DefaultBegin defaultBegin) {
    if (!insideCase) {
        report_error("[Default] Default nije unutar switch-a", defaultBegin);
    }
    defaultCountInSwitch++;
    if (defaultCountInSwitch > 1) {
        report_error("[Default] Više default grana u istom switch-u", defaultBegin);
    }
}

@Override
public void visit(OneStatement_switch statementSwitch) {
    // ... postojeca provera tipa
    defaultCountInSwitch = 0; // reset za sledeći switch
}
//da ne moze vise od 1
/// 
/// 
/////////////// label
private HashSet<String> setOfLabels = null;
private HashSet<String> setOfGotoLabels = null;
//Single Statements
	@Override
	public void visit(Label label) {
		if(!setOfLabels.add(label.getI1()))
			report_error("Dvostruka definicija labele: " + label.getI1(), label);
	}
	
	@Override
	public void visit(SingleStatement_goto singleStatement_goto) {
		setOfGotoLabels.add(singleStatement_goto.getI1());
	}
	
////////////// whiledo
/// (SingleStatement_do) DoNonterm Statement WhileNonterm LPAREN Condition RPAREN SEMI
/// WhileNonterm ::= (WhileNonterm) WHILE;
private int loopCnt = 0;
 	@Override
	public void visit(DoNonterm doNonterm) {
		loopCnt++;
	}
	
	@Override
	public void visit(SingleStatement_do singleStatement_do) {
		loopCnt--;
	}
	
	@Override
	public void visit(SingleStatement_break singleStatement_break) {
		if(loopCnt == 0)
			report_error("Break naredba se ne nalazi unutar tela petlje.", singleStatement_break);
	}
	
	@Override
	public void visit(SingleStatement_continue singleStatement_continue) {
		if(loopCnt == 0)
			report_error("Continue naredba se ne nalazi unutar tela petlje.", singleStatement_continue);
	}
 */

/* ============================================================================
   NACRTI ZA TRI NEZAVISNE JEZICKE MODIFIKACIJE (SEMANTICKA ANALIZA)
   Sve u ovom bloku je iskljucivo predlog/skica - nista odavde nije aktivno,
   ne dodaju se novi tokeni/produkcije/AST cvorovi niti se menja postojece
   ponasanje. Odgovarajuce gramaticke skice su u spec/mjparser.cup i spec/mjlexer.lex.
   ============================================================================

   ----------------------------------------------------------------------------
   1) FOREACH PETLJA:  for (elem : niz) Statement
   ----------------------------------------------------------------------------
   Skica koristi marker-cvorove (ForeachBegin2/ForeachColon iz mjparser.cup nacrta),
   po uzoru na vec aktivan ForBegin/OneStatement_for par (insideFor/petljeCnt), s tim
   da bi za ugnjezdene foreach-eve bio potreban stek umesto jednog flaga, jer svaka
   foreach petlja ima svoj interni indeks:

	// private java.util.Stack<Boolean> insideForeachStack = new java.util.Stack<>();
	//
	// @Override
	// public void visit(ForeachBegin2 foreachBegin) {
	//     insideForeachStack.push(true);
	//     petljeCnt++;        // foreach deli isti brojac sa "for" - break/continue provera ostaje ista
	//     insideFor = true;
	// }
	//
	// @Override
	// public void visit(OneStatement_foreach2 foreachStmt) {
	//     Obj destObj = foreachStmt.getDesignator().obj;         // odredisni designator (elem)
	//     Obj arrObj  = foreachStmt.getForeachArrName().obj;     // izvorni niz (ForeachArrName ~ isti obrazac kao DesignatorArrName)
	//
	//     if (destObj.getKind() != Obj.Var) {
	//         report_error("[Foreach] Odredisni designator mora biti obicna promenljiva: " + destObj.getName(), foreachStmt);
	//     }
	//     if (arrObj != null && arrObj != Tab.noObj) {
	//         Struct elemType = arrObj.getType().getElemType();
	//         if (!elemType.equals(Tab.intType) && !elemType.equals(Tab.charType) && !elemType.equals(boolType)) {
	//             report_error("[Foreach] Niz nije ugradjenog tipa (int/char/bool)", foreachStmt);
	//         }
	//         else if (!ourAssignableTo(elemType, destObj.getType())) {
	//             report_error("[Foreach] Tip odredisnog designatora ne odgovara tipu elementa niza", foreachStmt);
	//         }
	//     }
	//
	//     petljeCnt--;
	//     if (petljeCnt == 0) insideFor = false;
	//     insideForeachStack.pop();
	// }
	//
	// Napomena: skriveni interni indeks bi se, po uzoru na findAny/map (findAnyTemps/mapTemps
	// staticke mape kljucane po AST cvoru), alocirao ovde preko Tab.insert i cuvao u
	// slicnoj statickoj mapi, da bi ga CodeGenerator mogao pronaci u svom (odvojenom)
	// prolazu - u toj fazi je scope vec zatvoren pa se ne moze ponovo Tab.find-ovati.

   ----------------------------------------------------------------------------
   2) COUNT OPERACIJA:  odrediste = niz.count(Expr);
   ----------------------------------------------------------------------------
   TESTIRANO I POTVRDJENO DA RADI (privremeno aktivirano na test301_jul.mj, videlo se
   da tacno prebroji pojavljivanja trazene vrednosti u nizu, uz varijable niza sa
   0 i 1 pojavljivanjem; potom vraceno u komentar). Skoro identicno vec aktivnom
   visit(Designator_findAny) - ista struktura provere, samo je rezultat int umesto bool:

	// @Override
	// public void visit(Designator_count designatorCount) {
	//     Obj arrObj = Tab.find(designatorCount.getI1());
	//     Struct elemType = null;
	//
	//     if (arrObj == Tab.noObj) {
	//         report_error("[DesignatorCount] Pristupamo nedefinisanoj promenljivi niza: " + designatorCount.getI1(), designatorCount);
	//     }
	//     else if (arrObj.getKind() != Obj.Var || arrObj.getType().getKind() != Struct.Array) {
	//         report_error("[DesignatorCount] " + designatorCount.getI1() + " nije niz", designatorCount);
	//     }
	//     else {
	//         elemType = arrObj.getType().getElemType();
	//         if (!elemType.equals(Tab.intType) && !elemType.equals(Tab.charType) && !elemType.equals(boolType)) {
	//             report_error("[DesignatorCount] Niz nije ugradjenog tipa (int/char/bool)", designatorCount);
	//             elemType = null;
	//         }
	//         else if (!ourAssignableTo(designatorCount.getExpr().struct, elemType) && !ourAssignableTo(elemType, designatorCount.getExpr().struct)) {
	//             report_error("[DesignatorCount] Neodgovarajuci tip izraza za pretragu u nizu", designatorCount);
	//             elemType = null;
	//         }
	//     }
	//
	//     designatorCount.obj = new Obj(Obj.Con, "count", Tab.intType); // rezultat je uvek int
	//
	//     // skriveni indeks/brojac/searchVal - isti obrazac kao findAnyTemps (ovo je i stvarno
	//     // testirana verzija, ukljucuje treci skriveni slot za vrednost pretrage):
	//     // if (elemType != null) {
	//     //     Obj idxObj = Tab.insert(Obj.Var, "$cnt$i$" + countTempCounter, Tab.intType);
	//     //     Obj cntObj = Tab.insert(Obj.Var, "$cnt$c$" + countTempCounter, Tab.intType);
	//     //     Obj searchValObj = Tab.insert(Obj.Var, "$cnt$v$" + countTempCounter, elemType);
	//     //     countTempCounter++;
	//     //     countTemps.put(designatorCount, new Obj[]{ idxObj, cntObj, arrObj, searchValObj });
	//     // }
	// }
	//
	// Napomena: odrediste (mora biti int) se ne proverava posebno ovde - vec aktivan
	// visit(DesignatorStatement_assign) proverava da je levi designator Var/Elem i
	// tipski kompatibilan sa Expr-om (a Expr ovde ima struct = designatorCount.obj.getType() = int).

   ----------------------------------------------------------------------------
   3) ISPIS CELOG NIZA:  print(niz);  print(niz, width);
   ----------------------------------------------------------------------------
   Designator je vec jedan oblik Expr-a (FactorReal_d), pa postojece
   visit(OneStatement_print1)/visit(OneStatement_print2) VEC dobijaju niz kao
   Expr - nije potrebna nova produkcija. Prosirenje je samo u proveri tipa:

	// private boolean validatePrintType(Struct tip) {
	//     if (tip.equals(Tab.intType) || tip.equals(Tab.charType) || tip.equals(boolType)) return true;
	//     if (tip.getKind() == Struct.Array) {
	//         Struct elem = tip.getElemType();
	//         return elem.equals(Tab.intType) || elem.equals(Tab.charType) || elem.equals(boolType);
	//     }
	//     return false; // visedimenzionalni nizovi (niz od niza) ostaju odbijeni
	// }
	//
	// U aktivnim visit(OneStatement_print1)/visit(OneStatement_print2) bi provera
	// "!tip.equals(intType) && !tip.equals(charType) && !tip.equals(boolType)" bila
	// zamenjena pozivom "!validatePrintType(tip)" - aktivna implementacija ostaje
	// nepromenjena, ovo je samo nacrt.
============================================================================ */

