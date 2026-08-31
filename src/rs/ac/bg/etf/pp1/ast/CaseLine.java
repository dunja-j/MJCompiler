// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class CaseLine implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private CaseBegin CaseBegin;
    private Integer N2;
    private StatementContinue StatementContinue;

    public CaseLine (CaseBegin CaseBegin, Integer N2, StatementContinue StatementContinue) {
        this.CaseBegin=CaseBegin;
        if(CaseBegin!=null) CaseBegin.setParent(this);
        this.N2=N2;
        this.StatementContinue=StatementContinue;
        if(StatementContinue!=null) StatementContinue.setParent(this);
    }

    public CaseBegin getCaseBegin() {
        return CaseBegin;
    }

    public void setCaseBegin(CaseBegin CaseBegin) {
        this.CaseBegin=CaseBegin;
    }

    public Integer getN2() {
        return N2;
    }

    public void setN2(Integer N2) {
        this.N2=N2;
    }

    public StatementContinue getStatementContinue() {
        return StatementContinue;
    }

    public void setStatementContinue(StatementContinue StatementContinue) {
        this.StatementContinue=StatementContinue;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CaseBegin!=null) CaseBegin.accept(visitor);
        if(StatementContinue!=null) StatementContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CaseBegin!=null) CaseBegin.traverseTopDown(visitor);
        if(StatementContinue!=null) StatementContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CaseBegin!=null) CaseBegin.traverseBottomUp(visitor);
        if(StatementContinue!=null) StatementContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CaseLine(\n");

        if(CaseBegin!=null)
            buffer.append(CaseBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+N2);
        buffer.append("\n");

        if(StatementContinue!=null)
            buffer.append(StatementContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CaseLine]");
        return buffer.toString();
    }
}
