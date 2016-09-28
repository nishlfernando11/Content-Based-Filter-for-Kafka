package main.java.com.filter.extract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class KeyWordExtractor {

    public static MaxentTagger tagger = new MaxentTagger(
            "D:/Project/KeywordExtract/stanford-postagger-2015-12-09/model/stanford/2015/english-left3words-distsim.tagger");

    public static Map<String,String> extract(String message, Map<String,String> tags){

        // Initialize the tagger



        // The tagged string
        String tagged = tagger.tagString(message);

        String[] x = tagged.split(" ");
        ArrayList<String> list = new ArrayList<String>();

        //System.out.println(tagged);
        for(int i=0;i<x.length;i++){
            //Check weather given word is proper noun and insert to header
            if (x[i].substring(x[i].lastIndexOf("_")+1).equals("NNP")){
                if(i!= x.length-1 && x[i+1].substring(x[i+1].lastIndexOf("_")+1).contains("NNP")){
                    list.add(x[i].split("_")[0]+" "+x[i+1].split("_")[0]);
                    i++;
                }else{
                    list.add(x[i].split("_")[0]);
                }
            }
        }
        if(list.size() > 0){
            tags = new HashMap<String, String>();
            for(int i=0; i<list.size(); i++){
                tags.put(list.get(i),"true");
            }
        }
        //System.out.println(tags);
        return tags;
    }
}
