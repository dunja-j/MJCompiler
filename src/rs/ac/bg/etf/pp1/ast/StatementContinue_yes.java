// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class StatementContinue_yes extends StatementContinue {

    private StatementContinue StatementContinue;
    private Statement Statement;

    public StatementContinue_yes (StatementContinue StatementContinue, Statement Statement) {
        this.StatementContinue=StatementContinue;
        if(StatementContinue!=null) StatementContinue.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public StatementContinue getStatementContinue() {
        return StatementContinue;
    }

    public void setStatementContinue(StatementContinue StatementContinue) {
        this.StatementContinue=StatementContinue;
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
        if(StatementContinue!=null) StatementContinue.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementContinue!=null) StatementContinue.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementContinue!=null) StatementContinue.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementContinue_yes(\n");

        if(StatementContinue!=null)
            buffer.append(StatementContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementContinue_yes]");
        return buffer.toString();
    }
}
