package QueryEvaluator.ast;

public abstract class NonTerminal   implements StringExpression{
    protected StringExpression left,right;

	public void setLeft(StringExpression left){
		this.left = left;
	}

	public void setRight(StringExpression right){
		this.right = right;
	}
}
