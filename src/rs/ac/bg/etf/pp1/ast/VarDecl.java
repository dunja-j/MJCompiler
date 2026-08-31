// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:6


package rs.ac.bg.etf.pp1.ast;

public class VarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private VarVariable VarVariable;
    private VarContinue VarContinue;

    public VarDecl (Type Type, VarVariable VarVariable, VarContinue VarContinue) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.VarVariable=VarVariable;
        if(VarVariable!=null) VarVariable.setParent(this);
        this.VarContinue=VarContinue;
        if(VarContinue!=null) VarContinue.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
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
        if(Type!=null) Type.accept(visitor);
        if(VarVariable!=null) VarVariable.accept(visitor);
        if(VarContinue!=null) VarContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarVariable!=null) VarVariable.traverseTopDown(visitor);
        if(VarContinue!=null) VarContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarVariable!=null) VarVariable.traverseBottomUp(visitor);
        if(VarContinue!=null) VarContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

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
        buffer.append(") [VarDecl]");
        return buffer.toString();
    }
}
