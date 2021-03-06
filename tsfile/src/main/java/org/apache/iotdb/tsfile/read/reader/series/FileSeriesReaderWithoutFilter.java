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
package org.apache.iotdb.tsfile.read.reader.series;

import org.apache.iotdb.tsfile.file.metadata.ChunkMetaData;
import org.apache.iotdb.tsfile.read.common.Chunk;
import org.apache.iotdb.tsfile.read.controller.ChunkLoader;
import org.apache.iotdb.tsfile.read.reader.chunk.ChunkReaderWithoutFilter;
import org.apache.iotdb.tsfile.file.metadata.ChunkMetaData;
import org.apache.iotdb.tsfile.read.controller.ChunkLoader;
import org.apache.iotdb.tsfile.read.reader.chunk.ChunkReaderWithoutFilter;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Series reader is used to query one series of one tsfile, this reader has no filter.
 */
public class FileSeriesReaderWithoutFilter extends FileSeriesReader {

    public FileSeriesReaderWithoutFilter(ChunkLoader chunkLoader, List<ChunkMetaData> chunkMetaDataList) {
        super(chunkLoader, chunkMetaDataList);
    }

    @Override
    protected void initChunkReader(ChunkMetaData chunkMetaData) throws IOException {
        Chunk chunk = chunkLoader.getChunk(chunkMetaData);
        this.chunkReader = new ChunkReaderWithoutFilter(chunk);
        this.chunkReader.setMaxTombstoneTime(chunkMetaData.getMaxTombstoneTime());
    }

    @Override
    protected boolean chunkSatisfied(ChunkMetaData chunkMetaData) {
        return true;
    }

}
