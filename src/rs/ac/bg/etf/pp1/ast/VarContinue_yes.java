// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class VarContinue_yes extends VarContinue {

    private VarVariable VarVariable;
    private VarContinue VarContinue;

    public VarContinue_yes (VarVariable VarVariable, VarContinue VarContinue) {
        this.VarVariable=VarVariable;
        if(VarVariable!=null) VarVariable.setParent(this);
        this.VarContinue=VarContinue;
        if(VarContinue!=null) VarContinue.setParent(this);
    }

    public VarVariable getVarVariable() {
        return VarVariable;
    }

    public void setVarVariable(VarVariable VarVariable) {
        this.VarVariable=VarVariable;
    }

    public VarContinue getVarContinue() {
        return VarContinue;
    }

    public void setVarContinue(VarContinue VarContinue) {
        this.VarContinue=VarContinue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarVariable!=null) VarVariable.accept(visitor);
        if(VarContinue!=null) VarContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarVariable!=null) VarVariable.traverseTopDown(visitor);
        if(VarContinue!=null) VarContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarVariable!=null) VarVariable.traverseBottomUp(visitor);
        if(VarContinue!=null) VarContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarContinue_yes(\n");

        if(VarVariable!=null)
            buffer.append(VarVariable.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarContinue!=null)
            buffer.append(VarContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarContinue_yes]");
        return buffer.toString();
    }
}
