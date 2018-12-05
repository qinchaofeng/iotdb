package cn.edu.tsinghua.tsfile.timeseries.filter.operator;

import cn.edu.tsinghua.tsfile.timeseries.filter.DigestForFilter;
import cn.edu.tsinghua.tsfile.timeseries.filter.basic.UnaryFilter;
import cn.edu.tsinghua.tsfile.timeseries.filter.factory.FilterType;

/**
 * less than
 *
 * @param <T> comparable data type
 */
public class Lt<T extends Comparable<T>> extends UnaryFilter<T> {

    private static final long serialVersionUID = -2088181659871608986L;

    public Lt(T value, FilterType filterType) {
        super(value, filterType);
    }

    @Override
    public boolean satisfy(DigestForFilter digest) {
        if (filterType == FilterType.TIME_FILTER) {
            return ((Long) value) > digest.getMinTime();
        } else {
            return value.compareTo(digest.getMinValue()) > 0;
        }
    }

    @Override
    public boolean satisfy(long time, Object value) {
        Object v = filterType == FilterType.TIME_FILTER ? time : value;
        return this.value.compareTo((T) v) > 0;
    }

    @Override
    public String toString() {
        return getFilterType() + " < " + value;
    }
}
