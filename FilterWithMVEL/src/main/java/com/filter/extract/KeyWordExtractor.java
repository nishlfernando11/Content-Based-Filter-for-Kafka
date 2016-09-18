package main.java.com.filter.extract;


import com.sree.textbytes.jtopia.Configuration;
import com.sree.textbytes.jtopia.TermDocument;
import com.sree.textbytes.jtopia.TermsExtractor;

import java.util.Map;

public class KeyWordExtractor {

    public static Map<String,String> extract(String message, Map<String,String> tags){

        Configuration.setTaggerType("default");// "default" for lexicon POS tagger and "openNLP" for openNLP POS     tagger
        Configuration.setSingleStrength(2);
        Configuration.setNoLimitStrength(5);
        // If tagger type is "default" , then set model location as "model/default/english-lexicon.txt"
        // If tagger type is "openNLP" , then set model location as "model/openNLP/en-pos-maxent.bin"
        Configuration.setModelFileLocation("D:/Project/KeywordExtract/jtopia/model/default/english-lexicon.txt");
        TermsExtractor termExtractor = new TermsExtractor();
        TermDocument termDocument = termExtractor.extractTerms(message);

        for(int i=0; i<termDocument.getFinalFilteredTerms().keySet().size(); i++){
            tags.put(termDocument.getFinalFilteredTerms().keySet().toArray()[i].toString(),"true");
        }

        return tags;
    }
}
