package org.jnsgaii;

import org.jfree.data.statistics.HistogramDataset;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by skaggsm on 3/29/16.
 */
public class UpdatableHistogramDataset extends HistogramDataset {

    private Field list;

    public void removeAllSeries() {
        if (list == null) {
            try {
                list = HistogramDataset.class.getDeclaredField("list");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            list.setAccessible(true);
        }
        if (list != null)
            try {
                ((List) list.get(this)).clear();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        fireDatasetChanged();
    }

    public UpdatableHistogramDataset clone() throws CloneNotSupportedException {
        return (UpdatableHistogramDataset) super.clone();
    }
}
