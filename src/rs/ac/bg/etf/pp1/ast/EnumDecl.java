// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class EnumDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private EnumName EnumName;
    private EnumAssign EnumAssign;
    private EnumContinue EnumContinue;

    public EnumDecl (EnumName EnumName, EnumAssign EnumAssign, EnumContinue EnumContinue) {
        this.EnumName=EnumName;
        if(EnumName!=null) EnumName.setParent(this);
        this.EnumAssign=EnumAssign;
        if(EnumAssign!=null) EnumAssign.setParent(this);
        this.EnumContinue=EnumContinue;
        if(EnumContinue!=null) EnumContinue.setParent(this);
    }

    public EnumName getEnumName() {
        return EnumName;
    }

    public void setEnumName(EnumName EnumName) {
        this.EnumName=EnumName;
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
        if(EnumName!=null) EnumName.accept(visitor);
        if(EnumAssign!=null) EnumAssign.accept(visitor);
        if(EnumContinue!=null) EnumContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumName!=null) EnumName.traverseTopDown(visitor);
        if(EnumAssign!=null) EnumAssign.traverseTopDown(visitor);
        if(EnumContinue!=null) EnumContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumName!=null) EnumName.traverseBottomUp(visitor);
        if(EnumAssign!=null) EnumAssign.traverseBottomUp(visitor);
        if(EnumContinue!=null) EnumContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumDecl(\n");

        if(EnumName!=null)
            buffer.append(EnumName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

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
        buffer.append(") [EnumDecl]");
        return buffer.toString();
    }
}
