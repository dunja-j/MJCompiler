// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class Statement_OneStatement extends Statement {

    private OneStatement OneStatement;

    public Statement_OneStatement (OneStatement OneStatement) {
        this.OneStatement=OneStatement;
        if(OneStatement!=null) OneStatement.setParent(this);
    }

    public OneStatement getOneStatement() {
        return OneStatement;
    }

    public void setOneStatement(OneStatement OneStatement) {
        this.OneStatement=OneStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OneStatement!=null) OneStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OneStatement!=null) OneStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OneStatement!=null) OneStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Statement_OneStatement(\n");

        if(OneStatement!=null)
            buffer.append(OneStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Statement_OneStatement]");
        return buffer.toString();
    }
}
