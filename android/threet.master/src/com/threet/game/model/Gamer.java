package com.threet.game.model;


public class Gamer extends Player {

	// TODO Constructor
	public Gamer(Owner player, Board board, String name) {
		super(board, name);
		setPlayer(player);
	}
}
