package com.threet.game.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * The Robot
 * @author tchin
 *
 */
public class Robot extends Player {
	
	// The empty slots on the board
	private List<MagicSlot> emptySlots;

	// TODO Constructor
	public Robot(Board board, String name) {
		super(board, name);
		setPlayer(Owner.ROBOT);
		emptySlots = new ArrayList<MagicSlot>();
	}
	
	/**
	 * Take one from the board.
	 * @return the taken status
	 */
	public Status take() {
		MagicSlot selectedSlot = null;
		
		// 1). Find the empty slots.
		findEmptySlot();
		
		// 2). Try each one to find the winning slots.
		List<MagicSlot> winningSlots = getOpportunity();
		if (winningSlots.size() > 0) {
			// 2.1). Pick one from the winning list.
			selectedSlot = pick(winningSlots);
		} else {
			// 2.2). Pick one from the empty slots. 
			selectedSlot = pick();
		}
		
		// 3). Finally, decide to occupy one.
		return take(selectedSlot.getRow(), selectedSlot.getColumn());
	}

	/** TODO Private Methods
	 * Pick one from the winning slots.
	 * @return the random one
	 */
	private MagicSlot pick(List<MagicSlot> winningSlots) {
		// Pick from my chance.
		List<MagicSlot> list = filter(winningSlots, Owner.ROBOT);
		
		// If I have no chance, so do I have risk? Pick from my risk.
		if (list.size() == 0) list = filter(winningSlots, Owner.P1);
		
		// Random one.
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}
	
	/**
	 * Filter slots by owner.
	 * @param target
	 * @param owner
	 * @return the filtered list
	 */
	private List<MagicSlot> filter(List<MagicSlot> target, Owner owner) {
		List<MagicSlot> list = new ArrayList<MagicSlot>();
		Iterator<MagicSlot> it = target.iterator();
		while (it.hasNext()) {
			MagicSlot slot = it.next();
			if (slot.getOwner() == owner) list.add(slot);
		}
		return list;
	}
	
	/**
	 * Pick one from the empty slots.
	 * @return the random one
	 */
	private MagicSlot pick() {
		// Find center first.
		MagicSlot center = pickCenterSlot(emptySlots);
		if (center != null) return center;
		
		// Find the edges secondly.
		List<MagicSlot> list = pickEdges(emptySlots);
		
		// If not found edges, random others.
		if (list.size() == 0) list = emptySlots;
		
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}
	
	/**
	 * Search for the center slot.
	 * @param list
	 * @return the center
	 */
	private MagicSlot pickCenterSlot(List<MagicSlot> target) {
		MagicSlot center = null;
		Iterator<MagicSlot> it = target.iterator();
		while (it.hasNext()) {
			MagicSlot slot = it.next();
			int row = slot.row;
			int col = slot.column;
			int centerPos = (Board.DIMENSION - 1) / 2;
			if (row == centerPos && col == centerPos) {
				center = slot;
				break;
			}
		}
		return center;
	}
	
	/**
	 * Search for the edges
	 * @param target
	 * @return the edges
	 */
	private List<MagicSlot> pickEdges(List<MagicSlot> target) {
		List<MagicSlot> list = new ArrayList<MagicSlot>();
		Iterator<MagicSlot> it = target.iterator();
		while (it.hasNext()) {
			MagicSlot slot = it.next();
			int row = slot.row;
			int col = slot.column;
			int last = Board.DIMENSION - 1;
			if ((row == 0 && col == 0) ||
				(row == 0 && col == last) ||
				(row == last && col == 0) || 
				(row == last && col == last)) {
				list.add(slot);
			}
		}
		return list;
	}
	
	/**
	 * Search for the winning slots.
	 * @return the winning slots
	 */
	private List<MagicSlot> getOpportunity() {
		List<MagicSlot> list = new ArrayList<MagicSlot>();
		Iterator<MagicSlot> it = emptySlots.iterator();
		while (it.hasNext()) {
			MagicSlot slot = it.next();
			boolean chance = isWin(Owner.ROBOT, slot.getRow(), slot.getColumn());
			boolean risk = isWin(Owner.P1, slot.getRow(), slot.getColumn());
			if (chance) {
				slot.setOwner(Owner.ROBOT);
				list.add(slot);
			} else if (risk) {
				slot.setOwner(Owner.P1);
				list.add(slot);
			}
		}
		return list;
	}
	
	/**
	 * Find empty slots.
	 */
	private void findEmptySlot() {
		// 1). Clear first.
		emptySlots.clear();
		
		// 2). Find empty.
		Board board = getBoard();
		int[][] slots = board.getSlots();
		for (int i = 0; i < slots.length; i++) {
			for (int j = 0; j < slots[i].length; j++) {
				Owner owner = Board.convert(slots[i][j]);
				if (owner == Owner.NONE) {
					emptySlots.add(new MagicSlot(i, j));
				}
			}
		}
	}
	
	/**
	 * Check whether the current player is now winner.
	 * @param player
	 * @return the winner status.
	 */
	private boolean isWin(Owner player, int row, int column) {
		// Row Validation
		if (verifyRow(player, row, column)) return true;
		
		// Column Validation
		if (verifyColumn(player, row, column)) return true;
		
		// Oblique Validation (Top-Down)
		if (verifyObliqueTopDown(player, row, column)) return true;
		
		// Oblique Validation (Bottom-Up)
		if (verifyObliqueBottomUp(player, row, column)) return true;
		
		return false;
	}
	
	/** 
	 * Verify the winner in oblique (left to right, top-down).
	 * @param player
	 * @param row
	 * @param column
	 * @return the winner status
	 */
	private boolean verifyObliqueTopDown(Owner player, int row, int column) {
		int[][] slots = getBoard().getSlots();
		// Left to Right (Top-Down)
		for (int i = 0; i < slots.length; i++) {
			Owner owner = Owner.NONE;
			
			// What happen if that empty slot is owned by this player?
			if (i == row && i == column) {
				owner = player;
			} else {
				// Get its original value.
				owner = Board.convert(slots[i][i]);
			}
			
			if (owner != player) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Verify the winner in oblique (left to right, bottom-up).
	 * @param player
	 * @param row
	 * @param column
	 * @return the winner status
	 */
	private boolean verifyObliqueBottomUp(Owner player, int row, int column) {
		// Left to Right (Bottom-Up)
		int j = 0;
		int[][] slots = getBoard().getSlots();
		for (int i = slots.length - 1; i >= 0; i--) {
			Owner owner = Owner.NONE;
			
			// What happen if that empty slot is owned by this player?
			if (i == row && j == column) {
				owner = player;
			} else {
				// Get its original value.
				owner = Board.convert(slots[i][j]);
			}
						
			if (owner != player) {
				return false;
			}
			
			// Move next column.
			j++;
		}
		
		return true;
	}
	
	/**
	 * Verify the winner in row.
	 * @param player
	 * @param row
	 * @return the winner status
	 */
	private boolean verifyRow(Owner player, int row, int column) {
		int[][] slots = getBoard().getSlots();
		for (int i = 0; i < slots[row].length; i++) {
			Owner owner = Owner.NONE;
			
			// What happen if that empty slot is owned by this player?
			if (i == column) {
				owner = player;
			} else {
				// Get its original value.
				owner = Board.convert(slots[row][i]);
			}
			
			if (owner != player) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Verify the winner in column.
	 * @param player
	 * @param column
	 * @return the winner status
	 */
	private boolean verifyColumn(Owner player, int row, int column) {
		int[][] slots = getBoard().getSlots();
		for (int i = 0; i < slots.length; i++) {
			Owner owner = Owner.NONE;
			
			// What happen if that empty slot is owned by this player?
			if (i == row) {
				owner = player;
			} else {
				// Get its original value.
				owner = Board.convert(slots[i][column]);
			}
			
			if (owner != player) {
				return false;
			}
		}
		return true;
	}
	
	/** TODO
	 * The inner class used for keep slots.
	 * @author tchin
	 *
	 */
	private class MagicSlot {
		
		private int row;
		private int column;
		private Owner owner;
		
		public MagicSlot(int row, int column) {
			this.setRow(row);
			this.setColumn(column);
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}
		
		public Owner getOwner() {
			return owner;
		}

		public void setOwner(Owner owner) {
			this.owner = owner;
		}
	}
}
