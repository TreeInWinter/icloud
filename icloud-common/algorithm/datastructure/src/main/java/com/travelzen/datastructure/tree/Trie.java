package com.travelzen.datastructure.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A character tree map.
 */
public final class Trie<T> {
	private final char start;
	private final Trie<T>[] arr;
	private final T value;

	static char lcase(char ch) {
		if ('A' <= ch && ch <= 'Z') {
			ch |= 32;
		}
		return ch;
	}

	private Trie(char start, Trie<T>[] arr, @Nullable T value) {
		this.start = start;
		this.arr = arr;
		this.value = value;
	}

	@Nullable
	Trie<T> get(char ch) {
		int i = ch - start;
		return 0 <= i && i < arr.length ? arr[i] : null;
	}

	@Nullable
	Trie<T> getIgnoreCase(char ch) {
		int i = lcase(ch) - start;
		return 0 <= i && i < arr.length ? arr[i] : null;
	}

	/**
	 * Returns the value corresponding to s[off:end].
	 */
	@Nullable
	T get(String s, int off, int end) {
		Trie<T> t = this;
		while (off < end) {
			t = t.get(s.charAt(off++));
			if (t == null) {
				return null;
			}
		}
		return t.value;
	}

	@Nullable
	T get(char[] s, int off, int end) {
		Trie<T> t = this;
		while (off < end) {
			t = t.get(s[off++]);
			if (t == null) {
				return null;
			}
		}
		return t.value;
	}

	/**
	 * Returns the value corresponding to s[off:end] ignoring the case of ASCII
	 * characters and matching only against lowercase keys.
	 */
	@Nullable
	T getIgnoreCase(String s, int off, int end) {
		Trie<T> t = this;
		while (off < end) {
			t = t.getIgnoreCase(s.charAt(off++));
			if (t == null) {
				return null;
			}
		}
		return t.value;
	}

	@Nullable
	T getIgnoreCase(char[] s, int off, int end) {
		Trie<T> t = this;
		while (off < end) {
			t = t.getIgnoreCase(s[off++]);
			if (t == null) {
				return null;
			}
		}
		return t.value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb, 0);
		return sb.toString();
	}

	private void toString(StringBuilder sb, int depth) {
		sb.append(value).append(" {");
		++depth;
		for (int i = 0; i < arr.length; ++i) {
			if (arr[i] == null) {
				continue;
			}
			sb.append('\n');
			for (int j = depth; --j >= 0;) {
				sb.append("  ");
			}
			sb.append('\'').append((char) (i + start)).append("':");
			arr[i].toString(sb, depth);
		}
		sb.append("}");
	}

	static <S> Builder<S> builder() {
		return new Builder<S>(null, (char) 0);
	}

	static final class Builder<S> {
		S value;
		final char ch;
		final List<Builder<S>> children = new ArrayList<Builder<S>>();

		Builder(@Nullable S value, char ch) {
			this.value = value;
			this.ch = ch;
		}

		Builder<S> put(String s, @Nullable S newValue) {
			put(this, s, newValue);
			return this;
		}

		static <S> void put(Builder<S> b, String s, @Nullable S newValue) {
			for (int i = 0, n = s.length(); i < n; ++i) {
				char ch = s.charAt(i);
				Builder<S> next = null;
				for (Builder<S> child : b.children) {
					if (child.ch == ch) {
						next = child;
						break;
					}
				}
				if (next == null) {
					next = new Builder<S>(null, ch);
					b.children.add(next);
				}
				b = next;
			}
			b.value = newValue;
		}

		@SuppressWarnings("unchecked")
		Trie<S> build() {
			Trie<S>[] arr;
			int n = children.size();
			char min = 0;
			if (n == 0) {
				arr = (Trie<S>[]) NO_TRIES;
			} else {
				Collections.sort(children, new Comparator<Builder<?>>() {
					public int compare(Builder<?> a, Builder<?> b) {
						return a.ch - b.ch;
					}
				});
				min = children.get(0).ch;
				int max = children.get(n - 1).ch;
				arr = (Trie<S>[]) new Trie<?>[max - min + 1];
				for (int i = 0; i < n; ++i) {
					Builder<S> b = children.get(i);
					arr[b.ch - min] = b.build();
				}
			}
			return new Trie<S>(min, arr, value);
		}
	}

	private static final Trie<?>[] NO_TRIES = new Trie<?>[0];
}
