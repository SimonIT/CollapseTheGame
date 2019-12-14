package de.nilsim.collapse;

import java.util.Objects;

public class Point<T> {
	public T x;
	public T y;

	public Point(T x, T y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point<?> p = (Point<?>) o;
		return p.x.equals(x) && p.y.equals(y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
