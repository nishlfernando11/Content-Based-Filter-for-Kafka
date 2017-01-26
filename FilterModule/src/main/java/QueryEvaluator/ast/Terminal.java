package QueryEvaluator.ast;

public abstract class Terminal  implements StringExpression{
    protected String sentence;

    // get string as terminal
    public Terminal(String sentence){
        this.sentence = sentence;
    }

    public String toString() {
        return String.format("%s", sentence);
    }
}
