package it.polito.util;

public class Vector<T> {
	private int size;
	Object[] arr;
	
	public Vector () {
		this.size = 0;
		this.arr = new Object[size];
	}
	public int size() {
		return size;
	}

	public void addElement(T newElem) {
		Object[] array = new Object[arr.length + 1];
		System.arraycopy(arr, 0, array, 0, arr.length);
		array[arr.length] = newElem;
		this.arr = array;
		this.size++;
	}

	public T elementAt(int i) {
		return (T) arr[i];
	}

}
