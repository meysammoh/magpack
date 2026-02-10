package ir.mjm.util;

/**
 * Created by Serap on 8/25/14.
 */
public class Pair<T, U> {
  private T o1;

  private U o2;

  public Pair(T o1, U o2) {
    this.o1 = o1;
    this.o2 = o2;

  }

  public T getKey() {
    return this.o1;
  }

  public U getValue() {
    return this.o2;
  }

}
