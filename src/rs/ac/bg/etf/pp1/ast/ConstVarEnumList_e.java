// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:6


package rs.ac.bg.etf.pp1.ast;

public class ConstVarEnumList_e extends ConstVarEnumList {

    private EnumDecl EnumDecl;
    private ConstVarEnumList ConstVarEnumList;

    public ConstVarEnumList_e (EnumDecl EnumDecl, ConstVarEnumList ConstVarEnumList) {
        this.EnumDecl=EnumDecl;
        if(EnumDecl!=null) EnumDecl.setParent(this);
        this.ConstVarEnumList=ConstVarEnumList;
        if(ConstVarEnumList!=null) ConstVarEnumList.setParent(this);
    }

    public EnumDecl getEnumDecl() {
        return EnumDecl;
    }

    public void setEnumDecl(EnumDecl EnumDecl) {
        this.EnumDecl=EnumDecl;
    }

    public ConstVarEnumList getConstVarEnumList() {
        return ConstVarEnumList;
    }

    public void setConstVarEnumList(ConstVarEnumList ConstVarEnumList) {
        this.ConstVarEnumList=ConstVarEnumList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumDecl!=null) EnumDecl.accept(visitor);
        if(ConstVarEnumList!=null) ConstVarEnumList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumDecl!=null) EnumDecl.traverseTopDown(visitor);
        if(ConstVarEnumList!=null) ConstVarEnumList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumDecl!=null) EnumDecl.traverseBottomUp(visitor);
        if(ConstVarEnumList!=null) ConstVarEnumList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstVarEnumList_e(\n");

        if(EnumDecl!=null)
            buffer.append(EnumDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstVarEnumList!=null)
            buffer.append(ConstVarEnumList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstVarEnumList_e]");
        return buffer.toString();
    }
}
