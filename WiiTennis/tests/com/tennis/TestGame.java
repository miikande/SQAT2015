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

		// Should be zero in the beginning...
		assertTrue(game.getPoints(Game.TEAM_1) == 0);
		assertTrue(game.getPoints(Game.TEAM_2) == 0);
		
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
		assertTrue(game.getTeam(Game.TEAM_1).size() == 1);

		// And in team2 as well..
		assertTrue(game.getTeam(Game.TEAM_2).size() == 1);
	}
	
	@Test
	public void testPlayersAreInRightTeams() {
		initTwoPlayerGame();
		
		Player p1 = game.getTeam(Game.TEAM_1).get(0);
		assertEquals(player1.getName(), p1.getName());
		
		Player p2 = game.getTeam(Game.TEAM_2).get(0);
		assertEquals(player2.getName(), p2.getName());
	}

}
