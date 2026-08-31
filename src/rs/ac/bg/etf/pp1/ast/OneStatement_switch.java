// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class OneStatement_switch extends OneStatement {

    private Expr Expr;
    private CaseStatement CaseStatement;

    public OneStatement_switch (Expr Expr, CaseStatement CaseStatement) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.CaseStatement=CaseStatement;
        if(CaseStatement!=null) CaseStatement.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public CaseStatement getCaseStatement() {
        return CaseStatement;
    }

    public void setCaseStatement(CaseStatement CaseStatement) {
        this.CaseStatement=CaseStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(CaseStatement!=null) CaseStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(CaseStatement!=null) CaseStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(CaseStatement!=null) CaseStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneStatement_switch(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseStatement!=null)
            buffer.append(CaseStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneStatement_switch]");
        return buffer.toString();
    }
}
