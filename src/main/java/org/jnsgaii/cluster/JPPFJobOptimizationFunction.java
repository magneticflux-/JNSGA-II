package org.jnsgaii.cluster;

import org.jnsgaii.DefaultOptimizationFunction;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;
import org.jppf.JPPFException;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.node.protocol.AbstractTask;

import java.util.List;

/**
 * Created by Mitchell on 5/20/2016.
 */
public class JPPFJobOptimizationFunction<E> extends DefaultOptimizationFunction<E> {

    private final DefaultOptimizationFunction<E> wrapped;
    private final JPPFClient client;

    private JPPFJobOptimizationFunction(DefaultOptimizationFunction<E> wrapped, JPPFClient client) {
        this.wrapped = wrapped;
        this.client = client;
    }

    public static <T> JPPFJobOptimizationFunction<T> wrapOptimizationFunction(DefaultOptimizationFunction<T> wrapped, JPPFClient client) {
        return new JPPFJobOptimizationFunction<>(wrapped, client);
    }

    @Override
    public double evaluateIndividual(E object, Properties properties) {
        return wrapped.evaluateIndividual(object, properties);
    }

    @Override
    public double[] evaluate(List<Individual<E>> individuals, Properties properties) {
        JPPFJob job = new JPPFJob("Optimization function \"" + wrapped.getClass().getSimpleName() + "\" wrapped job");

        try {
            for (Individual<E> individual : individuals) {
                job.add(new AbstractTask<Double>() {
                    @Override
                    public void run() {
                        setResult(JPPFJobOptimizationFunction.this.evaluateIndividual(individual.getIndividual(), properties));
                    }
                });
            }
        } catch (JPPFException e) {
            throw new Error(e); //Only happens if I don't give a task, never happens
        }

        job.setBlocking(false);
        client.submitJob(job);

        return job.awaitResults().stream().mapToDouble(task -> ((Double) task.getResult())).toArray();
    }

    @Override
    public double min(Properties properties) {
        return wrapped.min(properties);
    }

    @Override
    public double max(Properties properties) {
        return wrapped.max(properties);
    }

    @Override
    public int compare(Double o1, Double o2) {
        return wrapped.compare(o1, o2);
    }

    @Override
    public Key[] requestProperties() {
        return wrapped.requestProperties();
    }
}
