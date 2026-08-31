// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class OneStatement_foreach extends OneStatement {

    private ForeachBegin ForeachBegin;
    private ForeachDS1 ForeachDS1;
    private ForeachDS2 ForeachDS2;
    private Statement Statement;

    public OneStatement_foreach (ForeachBegin ForeachBegin, ForeachDS1 ForeachDS1, ForeachDS2 ForeachDS2, Statement Statement) {
        this.ForeachBegin=ForeachBegin;
        if(ForeachBegin!=null) ForeachBegin.setParent(this);
        this.ForeachDS1=ForeachDS1;
        if(ForeachDS1!=null) ForeachDS1.setParent(this);
        this.ForeachDS2=ForeachDS2;
        if(ForeachDS2!=null) ForeachDS2.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ForeachBegin getForeachBegin() {
        return ForeachBegin;
    }

    public void setForeachBegin(ForeachBegin ForeachBegin) {
        this.ForeachBegin=ForeachBegin;
    }

    public ForeachDS1 getForeachDS1() {
        return ForeachDS1;
    }

    public void setForeachDS1(ForeachDS1 ForeachDS1) {
        this.ForeachDS1=ForeachDS1;
    }

    public ForeachDS2 getForeachDS2() {
        return ForeachDS2;
    }

    public void setForeachDS2(ForeachDS2 ForeachDS2) {
        this.ForeachDS2=ForeachDS2;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ForeachBegin!=null) ForeachBegin.accept(visitor);
        if(ForeachDS1!=null) ForeachDS1.accept(visitor);
        if(ForeachDS2!=null) ForeachDS2.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForeachBegin!=null) ForeachBegin.traverseTopDown(visitor);
        if(ForeachDS1!=null) ForeachDS1.traverseTopDown(visitor);
        if(ForeachDS2!=null) ForeachDS2.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForeachBegin!=null) ForeachBegin.traverseBottomUp(visitor);
        if(ForeachDS1!=null) ForeachDS1.traverseBottomUp(visitor);
        if(ForeachDS2!=null) ForeachDS2.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneStatement_foreach(\n");

        if(ForeachBegin!=null)
            buffer.append(ForeachBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForeachDS1!=null)
            buffer.append(ForeachDS1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForeachDS2!=null)
            buffer.append(ForeachDS2.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneStatement_foreach]");
        return buffer.toString();
    }
}
