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
// package org.apache.iotdb.db.query.externalsort;
//
//
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
//
/// **
// * This class represents an external sort job. Every job will use a separated directory.
// */
// public class ExternalSortJob {
// private long jobId;
// private List<ExternalSortJobPart> partList;
//
// public ExternalSortJob(long jobId, List<ExternalSortJobPart> partList) {
// this.jobId = jobId;
// this.partList = partList;
// }
//
// public List<PrioritySeriesReader> executeWithGlobalTimeFilter() throws IOException {
// List<PrioritySeriesReader> readers = new ArrayList<>();
// for (ExternalSortJobPart part : partList) {
// readers.add(part.executeWithGlobalTimeFilter());
// }
// return readers;
// }
// }
