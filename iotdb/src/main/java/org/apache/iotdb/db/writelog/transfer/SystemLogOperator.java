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
package org.apache.iotdb.db.writelog.transfer;

/**
 * To avoid conflict with org.apache.iotdb.tsfiledb.qp.constant.SQLConstant.Operator.
 */
public class SystemLogOperator {
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;
    public static final int OVERFLOWFLUSHSTART = 3;
    public static final int OVERFLOWFLUSHEND = 4;
    public static final int BUFFERFLUSHSTART = 5;
    public static final int BUFFERFLUSHEND = 6;
}
