package com.tennis;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a single game of a tennis set/match.
 * This is just an over-simplified example of tennis game
 * and is utilized only for testing purposes, not for
 * any real life use case.
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
	
	/*
	 * Scores according to specification:
	 * 
	 *   Scores from zero to three points are described as 
	 *   "love", "fifteen", "thirty", and "forty"
	 */
	public static final String SCORE_ZERO 		= "Love";
	public static final String SCORE_FIFTEEN 	= "Fifteen";
	public static final String SCORE_THIRTY		= "Thirty";
	public static final String SCORE_FORTY 		= "Forty";
	
	// Game statuses (keys)
	public static final int GAME_STATUS_ONGOING 		 = 0;
	public static final int GAME_STATUS_TEAM_1_WON 		 = 1;
	public static final int GAME_STATUS_TEAM_2_WON 		 = 2;
	public static final int GAME_STATUS_TEAM_1_ADVANTAGE = 3;
	public static final int GAME_STATUS_TEAM_2_ADVANTAGE = 4;
	public static final int GAME_STATUS_DEUCE 			 = 5;
		
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
		
		// No null values accepted
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null)
				throw new IllegalArgumentException("Null values are not accepted as parameters!");
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
	public List<Player> getPlayersForTeam(int team) {
		validateTeam(team);
		List<Player> teamMembers;
		
		if (players.size() == GAME_MODE_SINGLE) {
			teamMembers = Arrays.asList(players.get(team));
		}
		else if (team == TEAM_1) {
			teamMembers = Arrays.asList(players.get(0), players.get(1));
		}
		else {
			teamMembers = Arrays.asList(players.get(2), players.get(3));
		}
		
		return teamMembers;
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

	/**
	 * Returns given team's score in string format.
	 * First numbers are in textual format whereas rest
	 * are in digits in increment of ten.
	 * @param team id
	 * @return team's score as a String
	 */
	public String getScore(int team) {
		validateTeam(team);
		String score;
		
		switch (points[team]) {
		case 0:
			score = SCORE_ZERO;
			break;
			
		case 1:
			score = SCORE_FIFTEEN;
			break;
			
		case 2:
			score = SCORE_THIRTY;
			break;
			
		case 3:
			score = SCORE_FORTY;
			break;

		default:
			/* 
			 * We'll need to calculate the score. The first three points
			 * are worth of forty points and rest are 10pts each.
			 */
			score = Integer.toString(40 + (10 * (points[team] - 3)));
		}
		
		return score;
	}

	/**
	 * Returns status of this game. Status can be:
	 *   1) Ongoing
	 *   2) Team 1 won
	 *   3) Team 2 won
	 *   4) Team 1 has advantage
	 *   5) Team 2 has advantage
	 *   
	 * Specification says:
	 *   A game is won by the first player to have won at least four 
	 *   points in total and at least two points more than the opponent.
	 *   
	 *   If at least three points have been scored by each player, and 
	 *   the scores are equal, the score is "deuce".
	 *   
	 *   If at least three points have been scored by each side and a 
	 *   player has one more point than his opponent, the score of the 
	 *   game is "advantage" for the player in the lead.
	 *   
	 * @return status code as an integer
	 */
	public int getGameStatus() {
		int status = GAME_STATUS_ONGOING;
		
		// Check for deuce & advantage
		if (points[TEAM_1] >= 3 || points[TEAM_2] >= 3) {
			if ((points[TEAM_1] - points[TEAM_2]) == 1) {
				status = GAME_STATUS_TEAM_1_ADVANTAGE;
			}
			else if ((points[TEAM_2] - points[TEAM_1]) == 1) {
				status = GAME_STATUS_TEAM_2_ADVANTAGE;
			}
			else if (points[TEAM_1] == points[TEAM_2]) {
				status = GAME_STATUS_DEUCE;
			}
		}
		
		// Check if either team has won this game
		if (points[TEAM_1] >= 4 || points[TEAM_2] >= 4) {
			if ((points[TEAM_1] - points[TEAM_2]) >= 2) {
				status = GAME_STATUS_TEAM_1_WON;
			}
			else if ((points[TEAM_2] - points[TEAM_1]) >= 2) {
				status = GAME_STATUS_TEAM_2_WON;
			}
		}
		
		return status;
	}
	
}
