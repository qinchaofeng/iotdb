/**
 * Copyright © 2019 Apache IoTDB(incubating) (dev@iotdb.apache.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.iotdb.db.utils;

import org.apache.iotdb.db.conf.IoTDBConstant;
import org.apache.iotdb.tsfile.utils.Binary;
import org.apache.iotdb.tsfile.write.record.datapoint.DataPoint;
import org.apache.iotdb.tsfile.write.record.TSRecord;
import org.apache.iotdb.tsfile.write.record.datapoint.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Notice : methods in this class may not be accurate.
public class MemUtils {
    private static Logger logger = LoggerFactory.getLogger(MemUtils.class);

    public static long getRecordSize(TSRecord record) {
        long memSize = 0;
        for (DataPoint dataPoint : record.dataPointList) {
            memSize += getPointSize(dataPoint);
        }
        return memSize;
    }

    private static long getPointSize(DataPoint dataPoint) {
        switch (dataPoint.getType()) {
        case INT32:
            return 8 + 4;
        case INT64:
            return 8 + 8;
        case FLOAT:
            return 8 + 4;
        case DOUBLE:
            return 8 + 8;
        case BOOLEAN:
            return 8 + 1;
        case TEXT:
            return 8 + dataPoint.getValue().toString().length() * 2;
        default:
            return 8 + 8;
        }
    }

    // TODO : move this down to TsFile ?
    /**
     * Calculate how much memory will be used if the given record is written to Bufferwrite.
     * 
     * @param record
     * @return
     */
    public static long getTsRecordMemBufferwrite(TSRecord record) {
        long memUsed = 8; // time
        memUsed += 8; // deviceId reference
        memUsed += getStringMem(record.deviceId);
        for (DataPoint dataPoint : record.dataPointList) {
            memUsed += 8; // dataPoint reference
            memUsed += getDataPointMem(dataPoint);
        }
        return memUsed;
    }

    /**
     * Calculate how much memory will be used if the given record is written to Bufferwrite.
     * 
     * @param record
     * @return
     */
    public static long getTsRecordMemOverflow(TSRecord record) {
        return getTsRecordMemBufferwrite(record);
    }

    public static long getStringMem(String str) {
        // wide char (2 bytes each) and 64B String overhead
        return str.length() * 2 + 64;
    }

    // TODO : move this down to TsFile
    public static long getDataPointMem(DataPoint dataPoint) {
        // type reference
        long memUsed = 8;
        // measurementId and its reference
        memUsed += getStringMem(dataPoint.getMeasurementId());
        memUsed += 8;

        if (dataPoint instanceof FloatDataPoint) {
            memUsed += 4;
        } else if (dataPoint instanceof IntDataPoint) {
            memUsed += 4;
        } else if (dataPoint instanceof BooleanDataPoint) {
            memUsed += 1;
        } else if (dataPoint instanceof DoubleDataPoint) {
            memUsed += 8;
        } else if (dataPoint instanceof LongDataPoint) {
            memUsed += 8;
        } else if (dataPoint instanceof StringDataPoint) {
            StringDataPoint stringDataPoint = (StringDataPoint) dataPoint;
            memUsed += 8 + 20; // array reference and array overhead
            memUsed += ((Binary) stringDataPoint.getValue()).values.length;
            // encoding string reference and its memory
            memUsed += 8;
            memUsed += getStringMem(((Binary) stringDataPoint.getValue()).getTextEncodingType());
        } else {
            logger.error("Unsupported data point type");
        }

        return memUsed;
    }

    public static String bytesCntToStr(long cnt) {
        long GBs = cnt / IoTDBConstant.GB;
        cnt = cnt % IoTDBConstant.GB;
        long MBs = cnt / IoTDBConstant.MB;
        cnt = cnt % IoTDBConstant.MB;
        long KBs = cnt / IoTDBConstant.KB;
        cnt = cnt % IoTDBConstant.KB;
        return GBs + " GB " + MBs + " MB " + KBs + " KB " + cnt + " B";
    }
}
