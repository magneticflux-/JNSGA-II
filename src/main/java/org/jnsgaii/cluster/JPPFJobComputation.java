package org.jnsgaii.cluster;

import org.jnsgaii.computations.DefaultComputation;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Properties;
import org.jppf.JPPFException;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.node.protocol.AbstractTask;
import org.jppf.node.protocol.Task;

import java.util.List;

/**
 * Created by Mitchell on 5/20/2016.
 */
public class JPPFJobComputation<E, F> extends DefaultComputation<E, F> {

    private final DefaultComputation<E, F> wrapped;
    private transient final JPPFClient client; //transient so it isn't sent to nodes

    private JPPFJobComputation(DefaultComputation<E, F> wrapped, JPPFClient client) {
        this.wrapped = wrapped;
        this.client = client;
    }

    public static <E, F> JPPFJobComputation<E, F> wrapOptimizationFunction(DefaultComputation<E, F> wrapped, JPPFClient client) {
        return new JPPFJobComputation<>(wrapped, client);
    }

    @Override
    public F[] compute(List<Individual<E>> individuals, Properties properties) {
        JPPFJob job = new JPPFJob("Computation \"" + wrapped.getComputationID() + "\"");

        try {
            for (Individual<E> individual : individuals) {
                job.add(new AbstractTask<F>() {
                    @Override
                    public void run() {
                        try {
                            setResult(computeIndividual(individual, properties));
                        } catch (Throwable t) {
                            setThrowable(t);
                        }
                    }
                });
            }
        } catch (JPPFException e) {
            e.printStackTrace();
        }

        job.setBlocking(true);
        List<Task<?>> tasks = client.submitJob(job);

        //noinspection unchecked
        F[] computations = (F[]) new Object[individuals.size()];

        for (int i = 0; i < individuals.size(); i++) {
            //noinspection ThrowableResultOfMethodCallIgnored
            if (tasks.get(i).getThrowable() != null)
                throw new Error(tasks.get(i).getThrowable());
            computations[i] = (F) tasks.get(i).getResult();
        }

        return computations;
    }

    @Override
    public F computeIndividual(Individual<E> individual, Properties properties) {
        return wrapped.computeIndividual(individual, properties);
    }

    @Override
    public String getComputationID() {
        return wrapped.getComputationID();
    }

    @Override
    public boolean isDeterministic() {
        return wrapped.isDeterministic();
    }
}
