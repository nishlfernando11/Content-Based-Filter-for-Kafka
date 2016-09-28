package main.java.com.filter;


import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import main.java.com.filter.google.flatbuffers.FlatBufferBuilder;
import main.java.com.filter.fds.FB_FilterWrap;
import main.java.com.filter.fds.FB_Tuple;
import main.java.com.filter.model.FilterWrapper;

public class Coder {

    public byte[] encode(byte[] serializedWithoutWrap, Map<String, String> tags) {


        //get flatbuffers to encode message
        FlatBufferBuilder fbb = new FlatBufferBuilder(1024);
        int[] tagsArr = new int[tags.size()];

        int i = 0;
        //get tags and add to buffer
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            tagsArr[i] = _encodeEntry(entry, fbb);
            i++;
        }

        //integrate the data with the buffer
        int tagsRef = FB_FilterWrap.createTagsVector(fbb, tagsArr);
        int dataRef = FB_FilterWrap.createDataVector(fbb, serializedWithoutWrap);
        int filterWrapRef = FB_FilterWrap.createFB_FilterWrap(fbb, tagsRef, dataRef);
        FB_FilterWrap.finishFB_FilterWrapBuffer(fbb, filterWrapRef);
        int buffer_start = fbb.dataBuffer().position();
        int buffer_length = fbb.offset();
        byte[] full_buffer = fbb.dataBuffer().array();

        return Arrays.copyOfRange(full_buffer, buffer_start, buffer_start + buffer_length);
    }

    private static int _encodeEntry(Map.Entry<String, String> entry, FlatBufferBuilder fbb) {
        //insert into flatbuffer
        int k_ref =  fbb.createString(entry.getKey());
        int v_ref = fbb.createString(entry.getValue());
        return FB_Tuple.createFB_Tuple(fbb, k_ref, v_ref);
    }

    public FilterWrapper decode(byte[] serializedWithWrap) {
        //wrap with bytebuffer
        ByteBuffer bb = ByteBuffer.wrap(serializedWithWrap);
        FB_FilterWrap fbWrapper = FB_FilterWrap.getRootAsFB_FilterWrap(bb);
        FilterWrapper wrapper = new FilterWrapper();
        byte[] dataBuffer = new byte[fbWrapper.dataLength()];
        fbWrapper.dataAsByteBuffer().get(dataBuffer, 0, fbWrapper.dataLength());
        //getting tags and data separately
        wrapper.setData(dataBuffer);
        wrapper.setTags(getTags(fbWrapper));
        return wrapper;
    }

    public static Map<String, String> getTags(FB_FilterWrap filterWrapper) {
        //getting tags from the full message
        int len = filterWrapper.tagsLength();
        Map<String, String> tags = new HashMap<String, String>();
        for ( int i = 0; i < len; i++ ) {
            FB_Tuple tagTuple = filterWrapper.tags(i);
            tags.put(tagTuple.k(), tagTuple.v());
        }
        return tags;
    }
}
