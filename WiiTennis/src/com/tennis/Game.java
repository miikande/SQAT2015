package com.tennis;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a single game of a tennis set/match.
 * 
 * @author Miikka Andersson
 *
 */
public class Game {

	/*
	 * Two game modes supported:
	 *   two player game: single
	 *   four player game: doubles
	 */
	public static final int GAME_MODE_SINGLE = 2;
	public static final int GAME_MODE_DOUBLES = 4;
	
	// Teams
	public static final int TEAM_1 = 0;
	public static final int TEAM_2 = 1;
	
	// Keeping points for both teams (idx0=team1, idx1=team2)
	private Integer[] points;
	
	// We'll hold our players in a list to be able to serve both two and four player games
	private List<Player> players;
	
	/**
	 * Creates a new game with given players (either two or four
	 * player game). In two player game, players in first and second
	 * indexes are opponents (team1 and team2). However, in four 
	 * player mode indexes 0 and 1 are on the same side (team1). And 
	 * the same goes for indexes 2 & 3 (team2).  
	 * 
	 * @param players
	 */
	public Game(Player... players) {
		
		// We'll accept either two or four players
		if (players.length != 2 && players.length != 4) {
			throw new IllegalArgumentException("Must provide either two or four players!");
		}
		
		this.players = new LinkedList<Player>(Arrays.asList(players));
		
		// Initialize points for both teams
		points = new Integer[]{0, 0};
	}

	/**
	 * Returns used game mode: single or doubles
	 * @return 2 for single game, 4 in case of doubles
	 */
	public int getGameMode() {
		return players.size();
	}
	
	/**
	 * Returns players belonging to given team
	 * @param integer of team (TEAM_1 or TEAM_2)
	 * @return a list of players
	 */
	public List<Player> getTeam(int team) {
		validateTeam(team);
		
		if (players.size() == GAME_MODE_SINGLE)
			return Arrays.asList(players.get(team));
		
		// If we reached this point it means this is a four player game!
		if (team == TEAM_1) {
			return Arrays.asList(players.get(0), players.get(1));
		}
		
		return Arrays.asList(players.get(2), players.get(3));
	}
	
	/**
	 * Adds a point to given team
	 * @param team
	 */
	public void addPoint(int team) {
		validateTeam(team);
		points[team]++;		
	}
	
	/**
	 * Validates given team and throws an unchecked error if 
	 * unknown team! Yes, this is brutal but this is a programmer
	 * error and should be caught during the development!
	 *  
	 * @param team
	 */
	private void validateTeam(int team) {
		if (team != TEAM_1 && team != TEAM_2) {
			throw new IllegalArgumentException(
					"Unknown team, use either " + TEAM_1 + " or " +
					TEAM_2 + ", was: " + team);
		}
	}

	/**
	 * Returns points for given team
	 * @param team int TEAM_1 or TEAM_2
	 * @return points as an integer value
	 */
	public int getPoints(int team) {
		validateTeam(team);
		
		return points[team];
	}
	
}
