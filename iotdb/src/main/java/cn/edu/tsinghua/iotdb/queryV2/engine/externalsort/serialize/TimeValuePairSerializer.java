package cn.edu.tsinghua.iotdb.queryV2.engine.externalsort.serialize;


import cn.edu.tsinghua.iotdb.utils.TimeValuePair;
import java.io.IOException;


public interface TimeValuePairSerializer {

    void write(TimeValuePair timeValuePair) throws IOException;

    void close() throws IOException;
}
