package com.threet.game.model;

// The Player is an abstract class.
public abstract class Player {
	
	// The game board
	private Board board;
	
	// The player
	private Owner player;
	
	// The name of player
	private String name;
	
	/** TODO 
	 * Overload Constructor (The default is not allowed here.)
	 * @param player
	 * @param board
	 */
	public Player(Board board, String name) {
		this.board = board;
		this.name = name;
	}

	// TODO Public Methods
	public String getName() {
		return name;
	}
	
	public void setPlayer(Owner player) {
		this.player = player;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Status take(int row, int column) {
		if (getBoard() == null) return Status.UNKNOWN;
		
		return getBoard().take(getPlayer(), row, column);
	}
	
	// TODO Protected Methods
	protected Owner getPlayer() {
		return player;
	}

	protected Board getBoard() {
		return board;
	}
}
