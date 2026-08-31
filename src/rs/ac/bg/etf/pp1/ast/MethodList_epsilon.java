// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class MethodList_epsilon extends MethodList {

    public MethodList_epsilon () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodList_epsilon(\n");

        buffer.append(tab);
        buffer.append(") [MethodList_epsilon]");
        return buffer.toString();
    }
}
