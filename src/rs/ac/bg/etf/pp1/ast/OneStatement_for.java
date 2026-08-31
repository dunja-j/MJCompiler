// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class OneStatement_for extends OneStatement {

    private ForBegin ForBegin;
    private ForDS1 ForDS1;
    private ForCondition ForCondition;
    private ForDS2 ForDS2;
    private Statement Statement;

    public OneStatement_for (ForBegin ForBegin, ForDS1 ForDS1, ForCondition ForCondition, ForDS2 ForDS2, Statement Statement) {
        this.ForBegin=ForBegin;
        if(ForBegin!=null) ForBegin.setParent(this);
        this.ForDS1=ForDS1;
        if(ForDS1!=null) ForDS1.setParent(this);
        this.ForCondition=ForCondition;
        if(ForCondition!=null) ForCondition.setParent(this);
        this.ForDS2=ForDS2;
        if(ForDS2!=null) ForDS2.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ForBegin getForBegin() {
        return ForBegin;
    }

    public void setForBegin(ForBegin ForBegin) {
        this.ForBegin=ForBegin;
    }

    public ForDS1 getForDS1() {
        return ForDS1;
    }

    public void setForDS1(ForDS1 ForDS1) {
        this.ForDS1=ForDS1;
    }

    public ForCondition getForCondition() {
        return ForCondition;
    }

    public void setForCondition(ForCondition ForCondition) {
        this.ForCondition=ForCondition;
    }

    public ForDS2 getForDS2() {
        return ForDS2;
    }

    public void setForDS2(ForDS2 ForDS2) {
        this.ForDS2=ForDS2;
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
        if(ForBegin!=null) ForBegin.accept(visitor);
        if(ForDS1!=null) ForDS1.accept(visitor);
        if(ForCondition!=null) ForCondition.accept(visitor);
        if(ForDS2!=null) ForDS2.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForBegin!=null) ForBegin.traverseTopDown(visitor);
        if(ForDS1!=null) ForDS1.traverseTopDown(visitor);
        if(ForCondition!=null) ForCondition.traverseTopDown(visitor);
        if(ForDS2!=null) ForDS2.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForBegin!=null) ForBegin.traverseBottomUp(visitor);
        if(ForDS1!=null) ForDS1.traverseBottomUp(visitor);
        if(ForCondition!=null) ForCondition.traverseBottomUp(visitor);
        if(ForDS2!=null) ForDS2.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneStatement_for(\n");

        if(ForBegin!=null)
            buffer.append(ForBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForDS1!=null)
            buffer.append(ForDS1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForCondition!=null)
            buffer.append(ForCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForDS2!=null)
            buffer.append(ForDS2.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneStatement_for]");
        return buffer.toString();
    }
}
