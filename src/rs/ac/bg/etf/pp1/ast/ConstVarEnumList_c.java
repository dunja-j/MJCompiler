// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:6


package rs.ac.bg.etf.pp1.ast;

public class ConstVarEnumList_c extends ConstVarEnumList {

    private ConstDecl ConstDecl;
    private ConstVarEnumList ConstVarEnumList;

    public ConstVarEnumList_c (ConstDecl ConstDecl, ConstVarEnumList ConstVarEnumList) {
        this.ConstDecl=ConstDecl;
        if(ConstDecl!=null) ConstDecl.setParent(this);
        this.ConstVarEnumList=ConstVarEnumList;
        if(ConstVarEnumList!=null) ConstVarEnumList.setParent(this);
    }

    public ConstDecl getConstDecl() {
        return ConstDecl;
    }

    public void setConstDecl(ConstDecl ConstDecl) {
        this.ConstDecl=ConstDecl;
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
        if(ConstDecl!=null) ConstDecl.accept(visitor);
        if(ConstVarEnumList!=null) ConstVarEnumList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDecl!=null) ConstDecl.traverseTopDown(visitor);
        if(ConstVarEnumList!=null) ConstVarEnumList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDecl!=null) ConstDecl.traverseBottomUp(visitor);
        if(ConstVarEnumList!=null) ConstVarEnumList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstVarEnumList_c(\n");

        if(ConstDecl!=null)
            buffer.append(ConstDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstVarEnumList!=null)
            buffer.append(ConstVarEnumList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstVarEnumList_c]");
        return buffer.toString();
    }
}
