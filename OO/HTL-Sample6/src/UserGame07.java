import Engine.Vector2;
import TowerDefense.HTL;
import TowerDefense.Tile;
import TowerDefense.Tower;
import TowerDefense.TowerMedic;
import TowerDefense.TowerSpeedy;
import TowerDefense.Walker;
import TowerDefense.WalkerBasic;
import TowerDefense.WalkerQuick;


/**
 * This little "game" has multiple Towers casting spells on multiple Walkers.
 * It also shows how you can use the HUD.
 * 
 * @author Branden Drake
 */
public class UserGame07 extends HTL
{	
	private Tower healer1 = null;
	private Tower healer2 = null;
	private Tower speeder = null;
	
	private Walker puff = null;
	private Walker worm = null;
	
	private Tower selectedTower = null;
	
	private int healSpellsCast = 0;
	private int speedSpellsCast = 0;
	private int spellsCastOnWorm = 0;
	private int spellsCastOnPuff = 0;
	private int movesMade = 0;
	
	
	public UserGame07()
	{
		super();
	}
	
	
	public void initializeWorld()
	{
		super.initializeWorld();
		
		TowerMedic.preloadResources();
		WalkerBasic.preloadResources();
		
		createDefaultPath();
		this.setHUDVisibilityTo(false);
		
		healer1 = new TowerMedic();
		healer1.setTile(grid.getTile(5, 5));
		healer1.moveToDrawingLayer(layerTowers);
		
		healer2 = new TowerMedic();
		healer2.setTile(grid.getTile(9, 3));
		healer2.moveToDrawingLayer(layerTowers);
		
		speeder = new TowerSpeedy();
		speeder.setTile(grid.getTile(7, 5));
		speeder.moveToDrawingLayer(layerTowers);
		
		puff = new WalkerBasic(grid.getPath());
		puff.moveToDrawingLayer(layerWalkers);
		
		worm = new WalkerQuick(grid.getPath());
		worm.moveToDrawingLayer(layerWalkers);
		
		Walker.setDamageTakenPerSecond(5);	
		
		setHUDVisibilityTo(true);
		this.setHUDTime(-1);
	}

	
	
	
	public void updateWorld()
	{
		super.updateWorld();
		
		updateShoot();
		updateGameplayClicks();
		updateHUD();
		
		puff.update();
		worm.update();
		healer1.update();
		healer2.update();
		speeder.update();
	}
	
	
	
	private void updateHUD()
	{
		setHUDNumberOfTowersMedic(healSpellsCast);
		setHUDNumberOfTowersSpeedy(speedSpellsCast);
		setHUDNumberOfWalkersBasic(spellsCastOnPuff);
		setHUDNumberOfWalkersQuick(spellsCastOnWorm);
		setHUDNumberOfMoves(movesMade);
		
		int score = healSpellsCast + speedSpellsCast + spellsCastOnPuff + spellsCastOnWorm;
		
		setHUDScore(score);
	}
	
	
	
	private void updateShoot()
	{		
		updateShoot(healer1);
		updateShoot(healer2);
		updateShoot(speeder);
	}
	
	
	private void updateShoot(Tower shooter)
	{
		if(shooter.cooldownIsReady())
		{		
			if(!puff.isDead() && walkerIsInTowerSpellcastArea(puff, shooter))
			{							
				if(shooter.getTowerType() == Tower.Type.MEDIC)
				{
					puff.addHealth(10);
					spellsCastOnPuff++;
					healSpellsCast++;
				}
				else
				{
					puff.applySpeedBuff(4, 2);
					spellsCastOnPuff++;
					speedSpellsCast++;
				}				
				shooter.cooldownStart(1);
			}
			
			if(!worm.isDead() && walkerIsInTowerSpellcastArea(worm, shooter))
			{							
				if(shooter.getTowerType() == Tower.Type.MEDIC)
				{
					worm.addHealth(10);
					spellsCastOnWorm++;
					healSpellsCast++;
				}
				else
				{
					worm.applySpeedBuff(4, 2);
					spellsCastOnWorm++;
					speedSpellsCast++;
				}
				shooter.cooldownStart(1);
			}
			
			// so that we only play the effects and sound once regardless of number of targets
			if(!shooter.cooldownIsReady())
			{
				shooter.playEffectSpellcast();
				shooter.playSoundSpellcast();
			}
		}	
	}
	
	
	/**
	 * If the Walker is within spell range of the Tower, return true.
	 * Current range is a 3x3 Tile grid with the Tower in the center.
	 * @param tower		The Tower.
	 * @param walker	The Walker.
	 * @return			True if the Walker is in range.
	 */
	private boolean walkerIsInTowerSpellcastArea(Walker walker, Tower tower)
	{
		if(walker == null || tower == null)
		{
			return false;
		}
		
		Tile towerTile = tower.getTile();
		if(towerTile == null)
		{
			return false;
		}
		
		int mapColumnOfTower = towerTile.getGridColumn();
		int mapRowOfTower = towerTile.getGridRow();		
		Vector2 walkerPos = walker.getCenter();

		// search surrounding tiles in 3x3 for walker
		for(int row = mapRowOfTower-1; row <= mapRowOfTower+1; row++)
		{
			for(int column = mapColumnOfTower-1; column <= mapColumnOfTower+1; column++)
			{
				Tile testTile = grid.getTile(column, row);
				if(testTile != null && testTile.containsPoint(walkerPos))
				{								
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
	
	private void updateGameplayClicks()
	{	
		if(!mouse.isButtonTapped(1))
		{
			return;
		}
						
		
		Tile clickedTile = grid.getClickedTile();		
		if(clickedTile == null) return;				

		// if a Tower is selected, can it be moved to this Tile?
		if(selectedTower != null && clickedTile.isAvailable())
		{
			selectedTower.teleportTo(clickedTile);
			unselectTower();
			
			selectedTower.playSoundMove();
			movesMade++;
		}			
		// otherwise, if there's a Tower on the tile, select that tower
		else if (clickedTile.hasOccupant())
		{		
			unselectTower();
			selectedTower = clickedTile.getOccupant();
			selectedTower.setSelectedTo(true);
		}		
	}
		
	
	
	private void unselectTower()
	{
		if(selectedTower != null)
		{
			selectedTower.setSelectedTo(false);
		}
		selectedTower = null;
	}
	
}
