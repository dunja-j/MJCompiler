// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class CaseStatement_yes extends CaseStatement {

    private CaseStatement CaseStatement;
    private CaseLine CaseLine;

    public CaseStatement_yes (CaseStatement CaseStatement, CaseLine CaseLine) {
        this.CaseStatement=CaseStatement;
        if(CaseStatement!=null) CaseStatement.setParent(this);
        this.CaseLine=CaseLine;
        if(CaseLine!=null) CaseLine.setParent(this);
    }

    public CaseStatement getCaseStatement() {
        return CaseStatement;
    }

    public void setCaseStatement(CaseStatement CaseStatement) {
        this.CaseStatement=CaseStatement;
    }

    public CaseLine getCaseLine() {
        return CaseLine;
    }

    public void setCaseLine(CaseLine CaseLine) {
        this.CaseLine=CaseLine;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CaseStatement!=null) CaseStatement.accept(visitor);
        if(CaseLine!=null) CaseLine.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CaseStatement!=null) CaseStatement.traverseTopDown(visitor);
        if(CaseLine!=null) CaseLine.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CaseStatement!=null) CaseStatement.traverseBottomUp(visitor);
        if(CaseLine!=null) CaseLine.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CaseStatement_yes(\n");

        if(CaseStatement!=null)
            buffer.append(CaseStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseLine!=null)
            buffer.append(CaseLine.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CaseStatement_yes]");
        return buffer.toString();
    }
}
