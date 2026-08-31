package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class Compiler {

	// Configure the MJ program to compile here (matches test/<PROGRAM_NAME>.mj).
	private static final String PROGRAM_NAME = "test301_jul";

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {
			File sourceCode = new File("test/" + PROGRAM_NAME + ".mj");
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			/* Formiranje AST */
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();
	        
	        Program prog = (Program)(s.value);
	        
			/* Ispis AST */
			log.info(prog.toString(""));
			log.info("=====================================================================");
			
			/* Inicijalizacija tabele simbola */
			Tab.init();
			Struct boolType = new Struct(Struct.Bool);
			Obj boolObj = Tab.insert(Obj.Type, "bool", boolType);
			boolObj.setAdr(-1);
			boolObj.setLevel(-1);
			List<String> libMethods = new ArrayList<>();
			libMethods.add("chr");
			libMethods.add("ord");
			libMethods.add("len");
			
			for (String m: libMethods) 
				for (Obj fp: Tab.find(m).getLocalSymbols())
					fp.setFpPos(1);
			
			/* Semanticka analiza */
			SemAnalyzer sa = new SemAnalyzer();
			prog.traverseBottomUp(sa);
			
			/* Ispis tabele simbola */
			new Compiler().tsdump();
			
			if(!p.errorDetected && sa.passed()){
				/* Generisanje koda */ 
				File objFile = new File("test/" + PROGRAM_NAME + ".obj");
				if(objFile.exists()) objFile.delete();
				
				CodeGenerator cg = new CodeGenerator();
				prog.traverseBottomUp(cg);
				Code.dataSize = sa.nVars;  //u SemAnalazeru u program
				Code.mainPc = cg.getMainPc();
				Code.write(new FileOutputStream(objFile));
				
				log.info("Generisanje uspesno zavrseno!");
			}else{
				log.error("Generisanje NIJE uspesno zavrseno!");
			}
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	public void tsdump() {
		Logger log = Logger.getLogger(Compiler.class);
		log.info("=====================================================================");
		Tab.dump();
	}
	
	
}