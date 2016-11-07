package org.jnsgaii.operators;

import com.google.common.collect.Range;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jnsgaii.properties.AspectUser;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 11/1/16.
 */
public class AspectUserTest {

    @Test
    public void testMutateStartIndex() throws Exception {
        double[] aspects = new double[]{0, 1};
        double[] modifications = new double[]{10, 1, 0, 0};

        AspectUser.mutateAspect(modifications, aspects, 0, ThreadLocalRandom.current(), 0, 1);

        Assert.assertThat(aspects, new BaseMatcher<double[]>() {
            @Override
            public boolean matches(Object item) {
                if (!(item instanceof double[]))
                    return false;
                double[] castItem = (double[]) item;

                if (castItem[1] != 1)
                    return false;
                if (!Range.closed(0d, 1d).contains(castItem[0]))
                    return false;
                if (castItem[0] == 0)
                    return false;

                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        });
    }
}