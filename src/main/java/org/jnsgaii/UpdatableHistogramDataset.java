package org.jnsgaii;

import org.jfree.data.statistics.HistogramDataset;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by skaggsm on 3/29/16.
 */
public class UpdatableHistogramDataset extends HistogramDataset {

    public void removeAllSeries() {
        try {
            Field list = HistogramDataset.class.getDeclaredField("list");
            list.setAccessible(true);
            ((Collection) list.get(this)).clear();
            fireDatasetChanged();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
