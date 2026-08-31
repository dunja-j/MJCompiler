// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class CondFact_noting extends CondFact {

    private ExprNonTernary ExprNonTernary;

    public CondFact_noting (ExprNonTernary ExprNonTernary) {
        this.ExprNonTernary=ExprNonTernary;
        if(ExprNonTernary!=null) ExprNonTernary.setParent(this);
    }

    public ExprNonTernary getExprNonTernary() {
        return ExprNonTernary;
    }

    public void setExprNonTernary(ExprNonTernary ExprNonTernary) {
        this.ExprNonTernary=ExprNonTernary;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprNonTernary!=null) ExprNonTernary.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprNonTernary!=null) ExprNonTernary.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprNonTernary!=null) ExprNonTernary.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFact_noting(\n");

        if(ExprNonTernary!=null)
            buffer.append(ExprNonTernary.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFact_noting]");
        return buffer.toString();
    }
}
