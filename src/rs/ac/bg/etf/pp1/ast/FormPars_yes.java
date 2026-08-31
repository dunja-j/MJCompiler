// generated with ast extension for cup
// version 0.8
// 31/7/2026 19:6:7


package rs.ac.bg.etf.pp1.ast;

public class FormPars_yes extends FormPars {

    private FormParsDeclaration FormParsDeclaration;
    private FormParsContinue FormParsContinue;

    public FormPars_yes (FormParsDeclaration FormParsDeclaration, FormParsContinue FormParsContinue) {
        this.FormParsDeclaration=FormParsDeclaration;
        if(FormParsDeclaration!=null) FormParsDeclaration.setParent(this);
        this.FormParsContinue=FormParsContinue;
        if(FormParsContinue!=null) FormParsContinue.setParent(this);
    }

    public FormParsDeclaration getFormParsDeclaration() {
        return FormParsDeclaration;
    }

    public void setFormParsDeclaration(FormParsDeclaration FormParsDeclaration) {
        this.FormParsDeclaration=FormParsDeclaration;
    }

    public FormParsContinue getFormParsContinue() {
        return FormParsContinue;
    }

    public void setFormParsContinue(FormParsContinue FormParsContinue) {
        this.FormParsContinue=FormParsContinue;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParsDeclaration!=null) FormParsDeclaration.accept(visitor);
        if(FormParsContinue!=null) FormParsContinue.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParsDeclaration!=null) FormParsDeclaration.traverseTopDown(visitor);
        if(FormParsContinue!=null) FormParsContinue.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParsDeclaration!=null) FormParsDeclaration.traverseBottomUp(visitor);
        if(FormParsContinue!=null) FormParsContinue.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormPars_yes(\n");

        if(FormParsDeclaration!=null)
            buffer.append(FormParsDeclaration.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsContinue!=null)
            buffer.append(FormParsContinue.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormPars_yes]");
        return buffer.toString();
    }
}
