// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:6


package rs.ac.bg.etf.pp1.ast;

public class ConstContinue_yes extends ConstContinue {

    private ConstAssign ConstAssign;
    private ConstContinue ConstContinue;

    public ConstContinue_yes (ConstAssign ConstAssign, ConstContinue ConstContinue) {
        this.ConstAssign=ConstAssign;
        if(ConstAssign!=null) ConstAssign.setParent(this);
        this.ConstContinue=ConstContinue;
        if(ConstContinue!=null) ConstContinue.setParent(this);
    }

    public ConstAssign getConstAssign() {
        return ConstAssign;
    }

    public void setConstAssign(ConstAssign ConstAssign) {
        this.ConstAssign=ConstAssign;
    }

    public ConstContinue getConstContinue() {
        return ConstContinue;
    }

    public void setConstContinue(ConstContinue ConstContinue) {
        this.ConstContinue=ConstContinue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstAssign!=null) ConstAssign.accept(visitor);
        if(ConstContinue!=null) ConstContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstAssign!=null) ConstAssign.traverseTopDown(visitor);
        if(ConstContinue!=null) ConstContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstAssign!=null) ConstAssign.traverseBottomUp(visitor);
        if(ConstContinue!=null) ConstContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstContinue_yes(\n");

        if(ConstAssign!=null)
            buffer.append(ConstAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstContinue!=null)
            buffer.append(ConstContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstContinue_yes]");
        return buffer.toString();
    }
}
