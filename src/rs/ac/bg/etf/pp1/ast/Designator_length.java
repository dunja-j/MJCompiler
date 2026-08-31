// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class Designator_length extends Designator {

    private DesignatorArrLength DesignatorArrLength;

    public Designator_length (DesignatorArrLength DesignatorArrLength) {
        this.DesignatorArrLength=DesignatorArrLength;
        if(DesignatorArrLength!=null) DesignatorArrLength.setParent(this);
    }

    public DesignatorArrLength getDesignatorArrLength() {
        return DesignatorArrLength;
    }

    public void setDesignatorArrLength(DesignatorArrLength DesignatorArrLength) {
        this.DesignatorArrLength=DesignatorArrLength;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorArrLength!=null) DesignatorArrLength.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorArrLength!=null) DesignatorArrLength.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorArrLength!=null) DesignatorArrLength.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator_length(\n");

        if(DesignatorArrLength!=null)
            buffer.append(DesignatorArrLength.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator_length]");
        return buffer.toString();
    }
}
