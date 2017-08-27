package hr.fer.zemris.neural;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Neural network transition function.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 27.5.2017.
 * @param <T>
 *            input parameter type
 * @param <R>
 *            result type
 */
public interface ITransitionFunction<T, R> extends Function<T, R>, Serializable {

}
