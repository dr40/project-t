package fr.vodoji.highscore;

/**
 * 
 * @author Dorian RODDE, Vivian RODDE
 */
public interface HighscoreListener {

	public void onScoreSaved(Highscore highscore, HighscoreItem item, Highscore.State state);
	public void onScoreSaveError(Highscore highscore, HighscoreItem item);

	public void onScoreRefreshed(Highscore highscore);
	public void onScoreRefreshError(Highscore highscore);

}
