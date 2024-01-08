package rules;

import org.junit.Test;


public class WhenDeterminingWhetherCellLivesInNextGeneration {
  // Test list
  // TODO a dead cell with 0 live neighbors stays dead.
  // TODO a dead cell with 2 live neighbors stays dead.
  // TODO a dead cell with 3 live neighbors comes alive.
  // TODO a dead cell with 4 live neighbors stays dead.

  // TODO a live cell with 0 live neighbors dies.
  // TODO a live cell with 2 live neighbors stays alive.
  // TODO a live cell with 3 live neighbors stays alive.
  // TODO a live cell with 4 live neighbors dies.

  
  @Test
  public void deadCellWith_0_LiveNeighbors_StaysDead() throws Exception {
  }

  @Test
  public void deadCellWith_2_LiveNeighbors_StaysDead() throws Exception {
  }

  @Test
  public void deadCellWith_3_LiveNeighbors_ComesAlive() throws Exception {
  }

  @Test
  public void deadCellWith_4_LiveNeighbors_StaysDead() throws Exception {
  }

  @Test
  public void liveCellWith_0_LiveNeighbors_Dies() throws Exception {
  }


  @Test
  public void liveCellWith_2_LiveNeighbors_StaysAlive() throws Exception {
  }

  @Test
  public void liveCellWith_3_LiveNeighbors_StaysAlive() throws Exception {
  }

  @Test
  public void liveCellWith_4_LiveNeighbors_Dies() throws Exception {
  }
  


}

