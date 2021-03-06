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
package org.apache.iotdb.tsfile.read.filter.basic;

import java.io.Serializable;

/**
 * Definition for binary filter operations.
 */
public abstract class BinaryFilter implements Filter, Serializable {

    private static final long serialVersionUID = 1039585564327602465L;

    protected final Filter left;
    protected final Filter right;

    protected BinaryFilter(Filter left, Filter right) {
        this.left = left;
        this.right = right;
    }

    public Filter getLeft() {
        return left;
    }

    public Filter getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "( " + left + "," + right + " )";
    }
}
