package org.ahocorasick.trie.handler;

import org.ahocorasick.trie.Emit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultEmitHandler implements EmitHandler {

    private List<Emit> emits = new ArrayList<Emit>();


    @Override
    public void emit(Emit emit) {
        this.emits.add(emit);
    }

    @Override
    /**
     *   emits matches that are not duplicated
     */
    public void emitNoneDuplicate(Emit emit) {
        List<String> emittedStrings =  this.getKeywords();
        boolean isContained = emittedStrings.contains(emit.toString());
        if(!isContained)
            this.emits.add(emit);
    }

    /**
     * get a list of matched keywords
     * @return a keyword list
     */
    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<String>();
        for(Emit emit : this.emits){
            keywords.add(emit.toString());
        }
        return keywords;
    }

    public List<Emit> getEmits() {
        return this.emits;
    }

    /**
     * gets the emit count in emit handler
     * @return emit count
     */
    public int getEmitCount(){
        return this.emits.size();
    }

}
