// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class EnumContinue_num extends EnumContinue {

    private EnumAssign EnumAssign;
    private EnumContinue EnumContinue;

    public EnumContinue_num (EnumAssign EnumAssign, EnumContinue EnumContinue) {
        this.EnumAssign=EnumAssign;
        if(EnumAssign!=null) EnumAssign.setParent(this);
        this.EnumContinue=EnumContinue;
        if(EnumContinue!=null) EnumContinue.setParent(this);
    }

    public EnumAssign getEnumAssign() {
        return EnumAssign;
    }

    public void setEnumAssign(EnumAssign EnumAssign) {
        this.EnumAssign=EnumAssign;
    }

    public EnumContinue getEnumContinue() {
        return EnumContinue;
    }

    public void setEnumContinue(EnumContinue EnumContinue) {
        this.EnumContinue=EnumContinue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumAssign!=null) EnumAssign.accept(visitor);
        if(EnumContinue!=null) EnumContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumAssign!=null) EnumAssign.traverseTopDown(visitor);
        if(EnumContinue!=null) EnumContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumAssign!=null) EnumAssign.traverseBottomUp(visitor);
        if(EnumContinue!=null) EnumContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumContinue_num(\n");

        if(EnumAssign!=null)
            buffer.append(EnumAssign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumContinue!=null)
            buffer.append(EnumContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumContinue_num]");
        return buffer.toString();
    }
}
