package com.tinkerpop.blueprints.computer;

import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.function.BiFunction;

/**
 * The {@link GraphComputer} is responsible for the execution of a {@link VertexProgram} against the vertices in the
 * {@link Graph}. A {@link GraphComputer} maintains a {@link VertexMemory} (local vertex memory) and
 * {@link GraphMemory} (global graph memory). It is up to the {@link GraphComputer} implementation to determine the
 * appropriate memory structures given the computing substrate. All {@link GraphComputer} implementations also
 * maintains levels of memory isolation: Bulk Synchronous Parallel and Dirty Bulk Synchronous Parallel.
 *
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @author Matthias Broecheler (me@matthiasb.com)
 */
public interface GraphComputer {

    public enum Isolation {
        /**
         * Computations are carried out in a bulk synchronous manner.
         * The results of a vertex property update are only visible after the round is complete.
         */
        BSP,
        /**
         * Computations are carried out in a bulk synchronous manner.
         * The results of a vertex property update are visible before the end of the round.
         */
        DIRTY_BSP
    }

    public GraphComputer isolation(final Isolation isolation);

    public GraphComputer program(final VertexProgram program);

    public <K, V, R> GraphComputer reduction(final BiFunction<K, Iterator<V>, R> reduction);

    public Future<ComputeResult> submit();

    public static GraphComputer.Features getFeatures() {
        return new Features() {
        };
    }

    public interface Features {
        public default boolean supportsGlobalMessageTypes() {
            return true;
        }

        public default boolean supportsLocalMessageTypes() {
            return true;
        }

        public default boolean supportsVertexAddition() {
            return true;
        }

        public default boolean supportsVertexRemoval() {
            return true;
        }

        public default boolean supportsVertexPropertyAddition() {
            return true;
        }

        public default boolean supportsVertexPropertyRemoval() {
            return true;
        }

        public default boolean supportsVertexPropertyUpdates() {
            return true;
        }

        public default boolean supportsEdgeAddition() {
            return true;
        }

        public default boolean supportsEdgeRemoval() {
            return true;
        }

        public default boolean supportsEdgePropertyAddition() {
            return true;
        }

        public default boolean supportsEdgePropertyRemoval() {
            return true;
        }

        public default boolean supportsEdgePropertyUpdates() {
            return true;
        }

        public default boolean supportsIsolation(final Isolation isolation) {
            return true;
        }

    }

    public static class Exceptions {
        public static IllegalStateException adjacentElementPropertiesCanNotBeRead() {
            return new IllegalStateException("The properties of an adjacent element can not be read, only its id");
        }

        public static IllegalStateException adjacentElementPropertiesCanNotBeWritten() {
            return new IllegalStateException("The properties of an adjacent element can not be written");
        }

        public static IllegalArgumentException providedKeyIsNotAComputeKey(final String key) {
            return new IllegalArgumentException("The provided key is not a compute key: " + key);
        }

        public static IllegalStateException constantComputeKeyHasAlreadyBeenSet(final String key, final Object id) {
            return new IllegalStateException("The constant compute " + key + " has already been set for annotation " + id + ":" + key);
        }

        public static IllegalStateException adjacentVerticesCanNotBeQueried() {
            return new IllegalStateException("It is not possible to query an adjacent vertex in a vertex program");
        }
    }

}
