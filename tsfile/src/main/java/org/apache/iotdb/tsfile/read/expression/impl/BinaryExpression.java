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
package org.apache.iotdb.tsfile.read.expression.impl;

import org.apache.iotdb.tsfile.read.expression.IBinaryExpression;
import org.apache.iotdb.tsfile.read.expression.IExpression;
import org.apache.iotdb.tsfile.read.expression.ExpressionType;

public abstract class BinaryExpression implements IBinaryExpression {

    protected static class AndExpression extends BinaryExpression {
        public IExpression left;
        public IExpression right;

        public AndExpression(IExpression left, IExpression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public IExpression getLeft() {
            return left;
        }

        @Override
        public IExpression getRight() {
            return right;
        }

        @Override
        public ExpressionType getType() {
            return ExpressionType.AND;
        }

        @Override
        public String toString() {
            return "[" + left + " && " + right + "]";
        }
    }

    protected static class OrExpression extends BinaryExpression {
        public IExpression left;
        public IExpression right;

        public OrExpression(IExpression left, IExpression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public IExpression getLeft() {
            return left;
        }

        @Override
        public IExpression getRight() {
            return right;
        }

        @Override
        public ExpressionType getType() {
            return ExpressionType.OR;
        }

        @Override
        public String toString() {
            return "[" + left + " || " + right + "]";
        }
    }

    public static AndExpression and(IExpression left, IExpression right) {
        return new AndExpression(left, right);
    }

    public static OrExpression or(IExpression left, IExpression right) {
        return new OrExpression(left, right);
    }
}
