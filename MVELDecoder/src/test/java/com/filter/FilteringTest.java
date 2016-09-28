package test.java.com.filter;


import junit.framework.Assert;
import main.java.com.filter.Coder;
import main.java.com.filter.exp.FilterExpression;
import main.java.com.filter.exp.MvelExpression;
import main.java.com.filter.model.FilterWrapper;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FilteringTest {

    @Test
    public void filterTest() {
        Map<String,String> tags = new HashMap<String, String>() {{
            put("entity", "true");
            put("order", "true");
            put("long", "1283102390123");
        }};
        FilterExpression expression = new MvelExpression("entity==true && order==true");
        String testMessage = "SLFP Minister Wijith Wijayamuni Zoysa said yesterday that it was the former president Mahinda Rajapaksa \" +\n" +
                "                \"who governed the country using executive powers sidelining the premier and going against democratic values. He made \" +\n" +
                "                \"these remarks in response to a statement made by former President and Kurunegala District MP Mahinda Rajapaksa that the\" +\n" +
                "                \" country should be governed either by the President or the Prime Minister without contradicting each other.";

        Coder coder = new Coder();
        byte [] encoded = coder.encode(testMessage.getBytes(), tags);
        FilterWrapper decoded = coder.decode(encoded);
        Assert.assertTrue(expression.isInteresting(decoded));
    }
}
