// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class ExprNonTernary implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private ExpAddop ExpAddop;

    public ExprNonTernary (ExpAddop ExpAddop) {
        this.ExpAddop=ExpAddop;
        if(ExpAddop!=null) ExpAddop.setParent(this);
    }

    public ExpAddop getExpAddop() {
        return ExpAddop;
    }

    public void setExpAddop(ExpAddop ExpAddop) {
        this.ExpAddop=ExpAddop;
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
        if(ExpAddop!=null) ExpAddop.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExpAddop!=null) ExpAddop.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExpAddop!=null) ExpAddop.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprNonTernary(\n");

        if(ExpAddop!=null)
            buffer.append(ExpAddop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprNonTernary]");
        return buffer.toString();
    }
}
