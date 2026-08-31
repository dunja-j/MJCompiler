// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class CondFact_relop extends CondFact {

    private ExprNonTernary ExprNonTernary;
    private Relop Relop;
    private ExprNonTernary ExprNonTernary1;

    public CondFact_relop (ExprNonTernary ExprNonTernary, Relop Relop, ExprNonTernary ExprNonTernary1) {
        this.ExprNonTernary=ExprNonTernary;
        if(ExprNonTernary!=null) ExprNonTernary.setParent(this);
        this.Relop=Relop;
        if(Relop!=null) Relop.setParent(this);
        this.ExprNonTernary1=ExprNonTernary1;
        if(ExprNonTernary1!=null) ExprNonTernary1.setParent(this);
    }

    public ExprNonTernary getExprNonTernary() {
        return ExprNonTernary;
    }

    public void setExprNonTernary(ExprNonTernary ExprNonTernary) {
        this.ExprNonTernary=ExprNonTernary;
    }

    public Relop getRelop() {
        return Relop;
    }

    public void setRelop(Relop Relop) {
        this.Relop=Relop;
    }

    public ExprNonTernary getExprNonTernary1() {
        return ExprNonTernary1;
    }

    public void setExprNonTernary1(ExprNonTernary ExprNonTernary1) {
        this.ExprNonTernary1=ExprNonTernary1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprNonTernary!=null) ExprNonTernary.accept(visitor);
        if(Relop!=null) Relop.accept(visitor);
        if(ExprNonTernary1!=null) ExprNonTernary1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprNonTernary!=null) ExprNonTernary.traverseTopDown(visitor);
        if(Relop!=null) Relop.traverseTopDown(visitor);
        if(ExprNonTernary1!=null) ExprNonTernary1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprNonTernary!=null) ExprNonTernary.traverseBottomUp(visitor);
        if(Relop!=null) Relop.traverseBottomUp(visitor);
        if(ExprNonTernary1!=null) ExprNonTernary1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFact_relop(\n");

        if(ExprNonTernary!=null)
            buffer.append(ExprNonTernary.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Relop!=null)
            buffer.append(Relop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprNonTernary1!=null)
            buffer.append(ExprNonTernary1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFact_relop]");
        return buffer.toString();
    }
}
