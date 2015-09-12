package com.tennis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.tennis.Game;
import com.tennis.Player;

public class TestGame {
	
	private Player player1;
	private Player player2;
	private Game game;
	
	@Before
	public void init() {
		player1 = new Player("Ronald McBurger");
		player2 = new Player("Tony Tiger");
	}
	
	private void initTwoPlayerGame() {
		// Create a game with two players
		game = new Game(new Player[] {player1, player2});
	}
	
	@Test
	public void testAddPointsToTeams() {
		initTwoPlayerGame(); 
		
		// Add one point for team 1
		game.addPoint(Game.TEAM_1);
		
		// And two points for team 2
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		
		// Team1 has one point?
		assertTrue(game.getPoints(Game.TEAM_1) == 1);
		
		// And team2 two points?
		assertTrue(game.getPoints(Game.TEAM_2) == 2);
	}

	@Test
	public void testGameInitializesWithTwoPlayers() {
		initTwoPlayerGame();
		
		// Let's ensure our game has two players...
		assertTrue(game.getGameMode() == Game.GAME_MODE_SINGLE);
	}
	
	@Test
	public void testBothTeamsHasPlayer() {
		initTwoPlayerGame();
		
		// Ensure we have a player in team1
		assertTrue(game.getPlayersForTeam(Game.TEAM_1).size() == 1);

		// And in team2 as well..
		assertTrue(game.getPlayersForTeam(Game.TEAM_2).size() == 1);
	}
	
	@Test
	public void testPlayersAreInRightTeams() {
		initTwoPlayerGame();
		
		Player p1 = game.getPlayersForTeam(Game.TEAM_1).get(0);
		assertEquals(player1.getName(), p1.getName());
		
		Player p2 = game.getPlayersForTeam(Game.TEAM_2).get(0);
		assertEquals(player2.getName(), p2.getName());
	}

	@Test
	public void testPointToScoreConversion() {
		initTwoPlayerGame(); 
		
		/* 
		 * We should test according to spec:
		 * scores from zero to three points are described as
		 * "love", "fifteen", "thirty", and "forty"
		 */
		assertTrue("LOVE".equalsIgnoreCase(game.getScore(Game.TEAM_1)));
		
		// First point
		game.addPoint(Game.TEAM_1);
		assertTrue("FIFTEEN".equalsIgnoreCase(game.getScore(Game.TEAM_1)));
		
		// Second one
		game.addPoint(Game.TEAM_1);
		assertTrue("THIRTY".equalsIgnoreCase(game.getScore(Game.TEAM_1)));
		
		// And third point
		game.addPoint(Game.TEAM_1);
		assertTrue("FORTY".equalsIgnoreCase(game.getScore(Game.TEAM_1)));
		
		/* 
		 * Next we'll test some numeric values up to, say, 120 (there 
		 * are no limits set by the system)... 
		 */		
		game.addPoint(Game.TEAM_1);
		assertEquals("50", game.getScore(Game.TEAM_1));
		
		game.addPoint(Game.TEAM_1);
		assertEquals("60", game.getScore(Game.TEAM_1));
		
		game.addPoint(Game.TEAM_1);
		assertEquals("70", game.getScore(Game.TEAM_1));
		
		game.addPoint(Game.TEAM_1);
		assertEquals("80", game.getScore(Game.TEAM_1));
		
		game.addPoint(Game.TEAM_1);
		assertEquals("90", game.getScore(Game.TEAM_1));
		
		game.addPoint(Game.TEAM_1);
		assertEquals("100", game.getScore(Game.TEAM_1));
		
		game.addPoint(Game.TEAM_1);
		assertEquals("110", game.getScore(Game.TEAM_1));
		
		game.addPoint(Game.TEAM_1);
		assertEquals("120", game.getScore(Game.TEAM_1));
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorExceptionHandling1Param() {
		game = new Game(new Player[] {player1}); 
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorExceptionHandling3Param() {
		game = new Game(new Player[] {player1, player1, player1}); 
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorExceptionHandling5Param() {
		game = new Game(new Player[] {player1, player1, player1, player1, player1}); 
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorExceptionHandlingEmptyParam() {
		game = new Game(new Player[] {}); 
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorExceptionHandlingNullParam() {
		game = new Game(new Player[] {null, null}); 
	}
	
	@Test
	public void testGameStatusTeam1Won() {
		initTwoPlayerGame();
		
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_1);
		
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		
		assertEquals(Game.GAME_STATUS_TEAM_1_WON, game.getGameStatus());
	}
	
	@Test
	public void testGameStatusTeam2Won() {
		initTwoPlayerGame();
		
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_1);
		
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		
		assertEquals(Game.GAME_STATUS_TEAM_2_WON, game.getGameStatus());
	}
	
	@Test
	public void testGameStatusOngoing() {
		initTwoPlayerGame();
		
		// Initial status should be "ongoing"
		assertEquals(Game.GAME_STATUS_ONGOING, game.getGameStatus());
		
		// Let's add some points and check if the game is still ongoing (as it should)
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		assertEquals(Game.GAME_STATUS_ONGOING, game.getGameStatus());
	}
	
	@Test
	public void testGameStatusDouce() {
		initTwoPlayerGame();
		
		// Generate a deuce state...
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		assertEquals(Game.GAME_STATUS_DEUCE, game.getGameStatus());
	}
	
	@Test
	public void testGameStatusTeam1Advantage() {
		/*
		 * From the spec:
		 * 
		 * If at least three points have been scored by each side and 
		 * a player has one more point than his opponent, the score of 
		 * the game is "advantage" for the player in the lead.
		 */
		initTwoPlayerGame();
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_1);
		assertEquals(Game.GAME_STATUS_TEAM_1_ADVANTAGE, game.getGameStatus());
	}
	
	@Test
	public void testGameStatusTeam2Advantage() {
		initTwoPlayerGame();
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_1);
		game.addPoint(Game.TEAM_2);
		game.addPoint(Game.TEAM_2);
		assertEquals(Game.GAME_STATUS_TEAM_2_ADVANTAGE, game.getGameStatus());
	}
}
