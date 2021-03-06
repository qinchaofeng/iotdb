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
package org.apache.iotdb.tsfile.read.controller;

import org.apache.iotdb.tsfile.file.metadata.*;
import org.apache.iotdb.tsfile.read.TsFileSequenceReader;
import org.apache.iotdb.tsfile.read.common.Path;
import org.apache.iotdb.tsfile.common.cache.LRUCache;
import org.apache.iotdb.tsfile.file.metadata.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class MetadataQuerierByFileImpl implements MetadataQuerier {

    private static final int CHUNK_METADATA_CACHE_SIZE = 100000;

    private TsFileMetaData fileMetaData;

    private LRUCache<Path, List<ChunkMetaData>> chunkMetaDataCache;

    private TsFileSequenceReader tsFileReader;

    public MetadataQuerierByFileImpl(TsFileSequenceReader tsFileReader) throws IOException {
        this.tsFileReader = tsFileReader;
        this.fileMetaData = tsFileReader.readFileMetadata();
        chunkMetaDataCache = new LRUCache<Path, List<ChunkMetaData>>(CHUNK_METADATA_CACHE_SIZE) {
            @Override
            public List<ChunkMetaData> loadObjectByKey(Path key) throws IOException {
                return loadChunkMetadata(key);
            }
        };
    }

    @Override
    public List<ChunkMetaData> getChunkMetaDataList(Path path) throws IOException {
        return chunkMetaDataCache.get(path);
    }

    @Override
    public TsFileMetaData getWholeFileMetadata() {
        return fileMetaData;
    }

    @Override
    public void loadChunkMetaDatas(List<Path> paths) throws IOException {

        // group measurements by device
        TreeMap<String, Set<String>> device_measurementsMap = new TreeMap<>();
        for (Path path : paths) {
            if (!device_measurementsMap.containsKey(path.getDevice()))
                device_measurementsMap.put(path.getDevice(), new HashSet<>());
            device_measurementsMap.get(path.getDevice()).add(path.getMeasurement());
        }

        Map<Path, List<ChunkMetaData>> tempChunkMetaDatas = new HashMap<>();

        int count = 0;
        boolean enough = false;

        // get all TsDeviceMetadataIndex by string order
        for (Map.Entry<String, Set<String>> device_measurements : device_measurementsMap.entrySet()) {

            if (enough)
                break;

            // d1
            String selectedDevice = device_measurements.getKey();
            // s1, s2, s3
            Set<String> selectedMeasurements = device_measurements.getValue();

            // get the index information of TsDeviceMetadata
            TsDeviceMetadataIndex index = fileMetaData.getDeviceMetadataIndex(selectedDevice);
            TsDeviceMetadata tsDeviceMetadata = tsFileReader.readTsDeviceMetaData(index);

            // d1
            for (ChunkGroupMetaData chunkGroupMetaData : tsDeviceMetadata.getChunkGroups()) {// TODO make this function
                                                                                             // better

                if (enough)
                    break;

                // s1, s2
                for (ChunkMetaData chunkMetaData : chunkGroupMetaData.getChunkMetaDataList()) {

                    String currentMeasurement = chunkMetaData.getMeasurementUID();

                    // s1
                    if (selectedMeasurements.contains(currentMeasurement)) {

                        // d1.s1
                        Path path = new Path(selectedDevice, currentMeasurement);

                        // add into tempChunkMetaDatas
                        if (!tempChunkMetaDatas.containsKey(path))
                            tempChunkMetaDatas.put(path, new ArrayList<>());
                        tempChunkMetaDatas.get(path).add(chunkMetaData);

                        // check cache size, stop when reading enough
                        count++;
                        if (count == CHUNK_METADATA_CACHE_SIZE) {
                            enough = true;
                            break;
                        }
                    }
                }
            }
        }

        for (Map.Entry<Path, List<ChunkMetaData>> entry : tempChunkMetaDatas.entrySet())
            chunkMetaDataCache.put(entry.getKey(), entry.getValue());

    }

    private List<ChunkMetaData> loadChunkMetadata(Path path) throws IOException {

        if (!fileMetaData.containsDevice(path.getDevice()))
            return new ArrayList<>();

        // get the index information of TsDeviceMetadata
        TsDeviceMetadataIndex index = fileMetaData.getDeviceMetadataIndex(path.getDevice());

        // read TsDeviceMetadata from file
        TsDeviceMetadata tsDeviceMetadata = tsFileReader.readTsDeviceMetaData(index);

        // get all ChunkMetaData of this path included in all ChunkGroups of this device
        List<ChunkMetaData> chunkMetaDataList = new ArrayList<>();
        for (ChunkGroupMetaData chunkGroupMetaData : tsDeviceMetadata.getChunkGroups()) {
            List<ChunkMetaData> chunkMetaDataListInOneChunkGroup = chunkGroupMetaData.getChunkMetaDataList();
            for (ChunkMetaData chunkMetaData : chunkMetaDataListInOneChunkGroup) {
                if (path.getMeasurement().equals(chunkMetaData.getMeasurementUID())) {
                    chunkMetaDataList.add(chunkMetaData);
                }
            }
        }
        return chunkMetaDataList;
    }

}
