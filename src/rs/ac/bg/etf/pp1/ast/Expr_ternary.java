// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class Expr_ternary extends Expr {

    private ExprTernary ExprTernary;

    public Expr_ternary (ExprTernary ExprTernary) {
        this.ExprTernary=ExprTernary;
        if(ExprTernary!=null) ExprTernary.setParent(this);
    }

    public ExprTernary getExprTernary() {
        return ExprTernary;
    }

    public void setExprTernary(ExprTernary ExprTernary) {
        this.ExprTernary=ExprTernary;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprTernary!=null) ExprTernary.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprTernary!=null) ExprTernary.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprTernary!=null) ExprTernary.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr_ternary(\n");

        if(ExprTernary!=null)
            buffer.append(ExprTernary.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr_ternary]");
        return buffer.toString();
    }
}
