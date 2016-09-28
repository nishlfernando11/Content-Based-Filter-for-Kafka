package test.java.com.filter;


import junit.framework.Assert;
import main.java.com.filter.Coder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CoderTest {

    @Test
    public void encodeTest() {
        Map<String,String> tags = new HashMap<String, String>() {{
            put("entity", "Sri Lanka");
            put("order", "true");
            put("long", "1283102390123");
        }};
        String testMessage = "SLFP Minister Wijith Wijayamuni Zoysa said yesterday that it was the former president Mahinda Rajapaksa \" +\n" +
                "                \"who governed the country using executive powers sidelining the premier and going against democratic values. He made \" +\n" +
                "                \"these remarks in response to a statement made by former President and Kurunegala District MP Mahinda Rajapaksa that the\" +\n" +
                "                \" country should be governed either by the President or the Prime Minister without contradicting each other.";

        Coder coder = new Coder();
        Assert.assertNotNull(coder.encode(testMessage.getBytes(), tags));
    }

    @Test
    public void decodeTest() {
        Map<String,String> tags = new HashMap<String, String>() {{
            put("entity", "Sri Lanka");
            put("order", "true");
            put("long", "1283102390123");
        }};
        String testMessage = "SLFP Minister Wijith Wijayamuni Zoysa said yesterday that it was the former president Mahinda Rajapaksa \" +\n" +
                "                \"who governed the country using executive powers sidelining the premier and going against democratic values. He made \" +\n" +
                "                \"these remarks in response to a statement made by former President and Kurunegala District MP Mahinda Rajapaksa that the\" +\n" +
                "                \" country should be governed either by the President or the Prime Minister without contradicting each other.";

        Coder coder = new Coder();
        byte [] encoded = coder.encode(testMessage.getBytes(), tags);
        Assert.assertNotNull(coder.decode(encoded));
    }
}
