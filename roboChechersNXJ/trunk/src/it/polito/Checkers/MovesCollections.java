package it.polito.Checkers;

import it.polito.util.*;

public class MovesCollections {
	private Square from;
	private Vector<Square[]> tos = new Vector<Square[]>();

	public MovesCollections(Move m) {
		from = m.getFrom();
		tos.addElement(m.getTo());
	}

	public MovesCollections(Square from, Square[] to) {
		this.from = from;
		tos.addElement(to);
	}

	public Square getFrom() {
		return from;
	}

	public Vector<Square[]> getTos() {
		return tos;
	}

	public void SetTo(Move m) {
		this.tos.addElement(m.getTo());
	}

	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move m = (Move) o;
			return this.from.equals(m.getFrom());
		}
		return false;
	}

}
