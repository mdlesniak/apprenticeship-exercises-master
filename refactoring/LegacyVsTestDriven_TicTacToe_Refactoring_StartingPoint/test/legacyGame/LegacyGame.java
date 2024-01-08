package legacyGame;

public class LegacyGame {
  public static final int EMPTY = 0;
  public static final int TOTAL_SQUARES_PER_BOARD = 100;
  public static final int SQUARES_PER_SIDE = 10;
  public static final int ZERO_MARK_FOR_COMPUTER = 2;
  public static final int X_MARK_FOR_PLAYER = 1;
  public static final String CR_CHARACTER = "\n";
  public int gameBoard[][] = new int[3][TOTAL_SQUARES_PER_BOARD];

  public int gameState = 0;
  public int moveNumber = -1;
  public int lastMove = NONE;

  private static final String DOUBLE_BLANK_SPACE = "  ";
  private static final String SINGLE_BLANK_SPACE = " ";
  private static final long serialVersionUID = 1L;
  private static final int MAX_DEPTH = 7;
  private static final int NONE = 100;
  private static final int OCCUPIED = 1;
  private static final int SAFE_MODE = 1;
  private static final int CLEAN_MODE = 3;
  private static final int COUNT_MODE = 2;
  private static final int SETFLAGS_MODE = 1;
  private static final int CHECK_MODE = 0;

  private int marksByAxisByPlayerForChecking[] = new int[8];
  private int perhapsaTemporaryBoardHolder[][] = new int[MAX_DEPTH][TOTAL_SQUARES_PER_BOARD];
  private int stagingBoard[] = new int[TOTAL_SQUARES_PER_BOARD];
  private int tempTableForChecks[] = new int[TOTAL_SQUARES_PER_BOARD];
  private int tempRowForChecks[] = new int[SQUARES_PER_SIDE];

  public int makeComputerMove(int x, int y, boolean reporting) {
    int position = 0;

    if (moveNumber == 1)
      return makeArbitraryFirstComputerMoveBasedOnPlayerY(x, y);

    position = closeGapInSeries();
    if ((moveNumber == 2) && (position != NONE)) {
      if (reporting)
        System.out.println("closeGapInSeries() found " + position);
      return position;
    }

    if (moveNumber > 3) {
      position = blockSeriesOfFourOrMore(ZERO_MARK_FOR_COMPUTER, 0, CHECK_MODE);
      if (position != NONE) {
        if (reporting)
          System.out.println("blockSeriesOfFourOrMore() found " + position);
        return position;
      }

      position = blockSeriesOfFourOrMore(X_MARK_FOR_PLAYER, 0, CHECK_MODE);
      if (position != NONE) {
        if (reporting)
          System.out.println("blockSeriesOfFourOrMore() found " + position);
        return position;
      }
    }

    position = responseTo3Or4InaRowOpportunity(ZERO_MARK_FOR_COMPUTER, 0,
        CHECK_MODE);
    if ((moveNumber > 2) && (position != NONE)) {
      if (reporting)
        System.out.println("responseTo3Or4InaRowOpportunity() found "
            + position);
      return position;
    }

    position = tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(
        ZERO_MARK_FOR_COMPUTER, 0);
    if ((moveNumber > 5) && (position != NONE)) {
      if (reporting)
        System.out
            .println("tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes() found "
                + position);
      return position;
    }

    setFlagsForLaterProcessing(X_MARK_FOR_PLAYER);

    position = tryToMake3WithGap_FromVert4IntersectingWithHoriz4(
        ZERO_MARK_FOR_COMPUTER, 0);
    if ((moveNumber > 4) && (position != NONE)) {
      if (reporting)
        System.out
            .println("tryToMake3WithGap_FromVert4IntersectingWithHoriz4() found "
                + position);
      return position;
    }

    position = responseTo3Or4InaRowOpportunity(X_MARK_FOR_PLAYER, 0, CHECK_MODE);
    if ((moveNumber > 2) && (position != NONE)) {
      if (reporting)
        System.out.println("responseTo3Or4InaRowOpportunity() found "
            + position);
      return position;
    }

    position = tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(
        X_MARK_FOR_PLAYER, 0);
    if ((moveNumber > 5) && (position != NONE)) {
      if (reporting)
        System.out
            .println("tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes() found "
                + position);
      return position;
    }

    setFlagsForLaterProcessing(ZERO_MARK_FOR_COMPUTER);

    position = tryToMake3WithGap_FromVert4IntersectingWithHoriz4(
        X_MARK_FOR_PLAYER, 0);
    if ((moveNumber > 4) && (position != NONE)) {
      if (reporting)
        System.out
            .println("tryToMake3WithGap_FromVert4IntersectingWithHoriz4() found "
                + position);
      return position;
    }

    copyBoardZeroToBoardTwo();

    position = checkSeries(ZERO_MARK_FOR_COMPUTER, 0);
    if ((moveNumber > 3) && (position != NONE)) {
      if (reporting)
        System.out.println("checkSeries() found " + position);
      return position;
    }

    if (moveNumber > 3
        && (position = checkSeries(X_MARK_FOR_PLAYER, 0)) != NONE) {
      if (reporting)
        System.out.println("checkSeries() found " + position);
      return position;
    }

    position = check2o3c(ZERO_MARK_FOR_COMPUTER, 0);
    if ((moveNumber > 2) && (position != NONE)) {
      if (reporting)
        System.out.println("check2o3c() found " + position);
      return position;
    }

    position = check2o3c(X_MARK_FOR_PLAYER, 0);
    if ((moveNumber > 20) && (position != NONE)) {
      if (reporting)
        System.out.println("check2o3c() found " + position);
      return position;
    }

    if (moveNumber > 3 && (position = checkCross(X_MARK_FOR_PLAYER)) != NONE) {
      if (reporting)
        System.out.println("checkCross() found " + position);
      return position;
    }

    if (moveNumber > 3
        && (position = checkCross(ZERO_MARK_FOR_COMPUTER)) != NONE) {
      if (reporting)
        System.out.println("checkCross() found " + position);
      return position;
    }

    if (moveNumber > 2 && (position = checkBox(X_MARK_FOR_PLAYER)) != NONE) {
      if (reporting)
        System.out.println("checkBox() found " + position);
      return position;
    }

    if (moveNumber > 2 && (position = closeGapInSeries()) != NONE) {
      if (reporting)
        System.out.println("closeGapInSeries() found " + position);
      return position;
    }

    position = createTwoAxesOrCreateOneAndBlockAnother();
    if (position != NONE) {
      if (reporting)
        System.out.println("createTwoAxesOrCreateOneAndBlockAnother() found "
            + position);
      return position;
    }

    if ((position = responseTo3Or4InaRowOpportunity(ZERO_MARK_FOR_COMPUTER, 0,
        CLEAN_MODE)) != NONE) {
      if (reporting)
        System.out.println("responseTo3Or4InaRowOpportunity() found "
            + position);
      return position;
    }

    if ((position = blockSeriesOfFourOrMore(X_MARK_FOR_PLAYER, 0, CLEAN_MODE)) != NONE) {
      if (reporting)
        System.out.println("blockEitherEndOfSeriesOf4OrMore() found "
            + position);
      return position;
    }

    position = findSpot();
    if (reporting)
      System.out.println("findGoodSpotNearOpponent() found " + position);
    return position;
  }

  private int createTwoAxesOrCreateOneAndBlockAnother() {
    int i;
    seto4cc(X_MARK_FOR_PLAYER);
    for (i = 11; i < 89; i++)
      if (stagingBoard[i] == OCCUPIED
          && (gameBoard[0][i - 11] == X_MARK_FOR_PLAYER
              || gameBoard[0][i - SQUARES_PER_SIDE] == X_MARK_FOR_PLAYER
              || gameBoard[0][i - 9] == X_MARK_FOR_PLAYER
              || gameBoard[0][i - 1] == X_MARK_FOR_PLAYER
              || gameBoard[0][i + 1] == X_MARK_FOR_PLAYER
              || gameBoard[0][i + 9] == X_MARK_FOR_PLAYER
              || gameBoard[0][i + SQUARES_PER_SIDE] == X_MARK_FOR_PLAYER || gameBoard[0][i + 11] == X_MARK_FOR_PLAYER)) {

        return i;
      }
    return NONE;
  }

  private void copyBoardZeroToBoardTwo() {
    for (int i = 0; i < TOTAL_SQUARES_PER_BOARD; i++)
      gameBoard[2][i] = gameBoard[0][i];
  }

  public void markMove(int position, int playerMark) {
    gameBoard[0][position] = playerMark;
  }

  public int findSpot() {
    int position;
    int i;
    i = EMPTY;
    do {
      position = (int) (Math.random() * TOTAL_SQUARES_PER_BOARD);
      if (gameBoard[0][position] != EMPTY)
        continue;
      if ((position > 0 && gameBoard[0][position - 1] != EMPTY)
          || (position > SQUARES_PER_SIDE && (gameBoard[0][position - 11] != EMPTY
              || gameBoard[0][position - SQUARES_PER_SIDE] != EMPTY || gameBoard[0][position - 9] != EMPTY))
          || (position < 99 && gameBoard[0][position + 1] != EMPTY)
          || (position < 88 && (gameBoard[0][position + 9] != EMPTY
              || gameBoard[0][position + SQUARES_PER_SIDE] != EMPTY || gameBoard[0][position + 11] != EMPTY)))
        i = OCCUPIED;
    } while (i == EMPTY);
    return position;
  }

  public int checkForWinOpportunity(int playerMark, int x) {
    int position = 0;

    if ((position = blockSeriesOfFourOrMore(switchPlayers(playerMark), x,
        CHECK_MODE)) != NONE)
      return position;

    if ((position = responseTo3Or4InaRowOpportunity(switchPlayers(playerMark),
        x, CHECK_MODE)) != NONE) {
      return position;
    }

    return (NONE);
  }

  public int win() {
    int j, k, l;

    for (l = 0; l < 6; l++) {
      for (j = 0; j < 10; j++) {
        for (k = 0; k < 8; k++)
          marksByAxisByPlayerForChecking[k] = 0;
        for (k = 0; k < 5; k++) {
          marksByAxisByPlayerForChecking[gameBoard[0][j * 10 + l + k]]++;
          marksByAxisByPlayerForChecking[4 + gameBoard[0][l * 10 + j + k * 10]]++;
        }
        if (marksByAxisByPlayerForChecking[1] == 5
            || marksByAxisByPlayerForChecking[5] == 5)
          return X_MARK_FOR_PLAYER;

        if (marksByAxisByPlayerForChecking[2] == 5
            || marksByAxisByPlayerForChecking[6] == 5)
          return ZERO_MARK_FOR_COMPUTER;
      }

      for (j = 0; j < 6; j++) {
        for (k = 0; k < 8; k++)
          marksByAxisByPlayerForChecking[k] = 0;
        for (k = 0; k < 5; k++) {
          marksByAxisByPlayerForChecking[gameBoard[0][l * 10 + j + k * 11]]++;
          marksByAxisByPlayerForChecking[4 + gameBoard[0][l * 10 + j - k * 9
              + 40]]++;
        }
        if (marksByAxisByPlayerForChecking[1] == 5
            || marksByAxisByPlayerForChecking[5] == 5)
          return X_MARK_FOR_PLAYER;

        if (marksByAxisByPlayerForChecking[2] == 5
            || marksByAxisByPlayerForChecking[6] == 5)
          return ZERO_MARK_FOR_COMPUTER;
      }
    }
    return (EMPTY);
  }

  public int checkSeries(int playerMark, int depth) {
    int winningPosition;
    if (depth == MAX_DEPTH)
      return NONE;

    setc4c(playerMark);

    for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
      int stagingDepth = 2;
      if (stagingBoard[k] == EMPTY || gameBoard[stagingDepth][k] != EMPTY)
        continue;
      copyStagingBoardIntoOddGroupOfBoardsAtDepth(depth);

      gameBoard[stagingDepth][k] = playerMark;

      winningPosition = checkForWinOpportunity(switchPlayers(playerMark),
          stagingDepth);
      if (winningPosition == NONE)
        return NONE;

      gameBoard[stagingDepth][winningPosition] = switchPlayers(playerMark);
      if (blockSeriesOfFourOrMore(playerMark, stagingDepth, CHECK_MODE) != NONE) {
        return k;
      }

      if (blockSeriesOfFourOrMore(switchPlayers(playerMark), stagingDepth,
          CHECK_MODE) != NONE) {
        gameBoard[stagingDepth][k] = EMPTY;
        gameBoard[stagingDepth][winningPosition] = EMPTY;
        copyIntoStagingBoardFromOddBoardGroupAtDepth(depth);
        continue;
      }

      if (checkSeries(playerMark, depth + 1) != NONE) {
        return k;
      }

      gameBoard[stagingDepth][k] = EMPTY;
      gameBoard[stagingDepth][winningPosition] = EMPTY;
      copyIntoStagingBoardFromOddBoardGroupAtDepth(depth);
    }
    return NONE;
  }

  public int switchPlayers(int playerMark) {
    return 3 - playerMark;
  }

  public int checkBox(int playerMark) {
    for (int k = 1; k < 8; k++) {
      for (int l = 1; l < 8; l++) {
        int cnt = 0;
        int pos = -1;
        for (int a = 0; a < 2; a++) {
          for (int b = 0; b < 2; b++) {
            int x = k + a + 10 * (l + b);
            int c = gameBoard[0][x];
            if (c == playerMark)
              cnt++;
            else if (c == 0)
              pos = x;
          }
        }
        if (cnt == 3 && pos != -1)
          return pos;
      }
    }
    return LegacyGame.NONE;
  }

  public int checkCross(int playerMark) {
    int k, l, x;

    for (k = 1; k < 7; k++) {
      for (l = 1; l < 7; l++) {
        x = k + 10 * l;
        if (gameBoard[0][x] == playerMark && gameBoard[0][x + 2] == playerMark
            && gameBoard[0][x + 20] == playerMark
            && gameBoard[0][x + 22] == playerMark && gameBoard[0][x + 11] == 0)
          return (x + 11);
      }
    }
    return LegacyGame.NONE;
  }

  public int countNumberOfAxesAlongWhichSeriesOfFourOccur(int playerMark,
      int x, int type) {
    int j, k, l;
    int zbir = 0;
    int flag, flag2;

    flag = LegacyGame.EMPTY;
    for (j = 0; j < 10; j++) {
      for (l = 0; l < 6; l++) {
        marksByAxisByPlayerForChecking[0] = 0;
        marksByAxisByPlayerForChecking[1] = 0;
        marksByAxisByPlayerForChecking[2] = 0;
        for (k = 0; k < 5; k++)
          marksByAxisByPlayerForChecking[gameBoard[x][j * 10 + l + k]]++;
        if (marksByAxisByPlayerForChecking[playerMark] == 4
            && marksByAxisByPlayerForChecking[LegacyGame.EMPTY] == 1) {
          if (type == LegacyGame.SAFE_MODE) {
            flag2 = LegacyGame.EMPTY;
            for (k = 0; k < 5; k++) {
              if (gameBoard[x][j * 10 + l + k] == LegacyGame.EMPTY
                  && tempTableForChecks[j * 10 + l + k] == LegacyGame.OCCUPIED) {
                flag2 = LegacyGame.OCCUPIED;
              }
            }
            if (flag2 == LegacyGame.EMPTY) {
              zbir++;
              flag = LegacyGame.OCCUPIED;
              break;
            }
          } else {
            zbir++;
            flag = LegacyGame.OCCUPIED;
            break;
          }
        }
      }
      if (flag == LegacyGame.OCCUPIED)
        break;
    }
    flag = LegacyGame.EMPTY;

    for (j = 0; j < 10; j++) {
      for (l = 0; l < 6; l++) {
        marksByAxisByPlayerForChecking[0] = 0;
        marksByAxisByPlayerForChecking[1] = 0;
        marksByAxisByPlayerForChecking[2] = 0;
        for (k = 0; k < 5; k++)
          marksByAxisByPlayerForChecking[gameBoard[x][l * 10 + j + k * 10]]++; /* v */
        if (marksByAxisByPlayerForChecking[playerMark] == 4
            && marksByAxisByPlayerForChecking[LegacyGame.EMPTY] == 1) {
          if (type == LegacyGame.SAFE_MODE) {
            flag2 = LegacyGame.EMPTY;
            for (k = 0; k < 5; k++)
              if (gameBoard[x][l * 10 + j + k * 10] == LegacyGame.EMPTY
                  && tempTableForChecks[l * 10 + j + k * 10] == LegacyGame.OCCUPIED)
                flag2 = LegacyGame.OCCUPIED;
            if (flag2 == LegacyGame.EMPTY) {
              zbir++;
              flag = LegacyGame.OCCUPIED;
              break;
            }
          } else {
            zbir++;
            flag = LegacyGame.OCCUPIED;
            break;
          }
        }
      }
      if (flag == LegacyGame.OCCUPIED)
        break;
    }
    flag = LegacyGame.EMPTY;
    for (l = 0; l < 6; l++) {
      for (j = 0; j < 6; j++) {
        for (k = 0; k < 3; k++)
          /* za diag */
          marksByAxisByPlayerForChecking[k] = 0;
        for (k = 0; k < 5; k++)
          /* diag\ */
          marksByAxisByPlayerForChecking[gameBoard[x][l * 10 + j + k * 11]]++;
        if (marksByAxisByPlayerForChecking[playerMark] == 4
            && marksByAxisByPlayerForChecking[LegacyGame.EMPTY] == 1) {
          if (type == LegacyGame.SAFE_MODE) {
            flag2 = LegacyGame.EMPTY;
            for (k = 0; k < 5; k++) {
              if (gameBoard[x][l * 10 + j + k * 11] == LegacyGame.EMPTY
                  && tempTableForChecks[l * 10 + j + k * 11] == LegacyGame.OCCUPIED) {
                flag2 = LegacyGame.OCCUPIED;
              }
            }
            if (flag2 == LegacyGame.EMPTY) {
              zbir++;
              flag = LegacyGame.OCCUPIED;
              break;
            }
          } else {
            zbir++;
            flag = LegacyGame.OCCUPIED;
            break;
          }
        }
      }
      if (flag == LegacyGame.OCCUPIED)
        break;
    }
    flag = LegacyGame.EMPTY;
    for (l = 0; l < 6; l++) {
      for (j = 0; j < 6; j++) {
        for (k = 0; k < 3; k++)
          marksByAxisByPlayerForChecking[k] = 0;
        for (k = 0; k < 5; k++)
          marksByAxisByPlayerForChecking[gameBoard[x][l * 10 + j - k * 9 + 40]]++;
        if (marksByAxisByPlayerForChecking[playerMark] == 4
            && marksByAxisByPlayerForChecking[LegacyGame.EMPTY] == 1) {
          if (type == LegacyGame.SAFE_MODE) {
            flag2 = LegacyGame.EMPTY;
            for (k = 0; k < 5; k++) {
              if (gameBoard[x][l * 10 + j - k * 9 + 40] == LegacyGame.EMPTY
                  && tempTableForChecks[l * 10 + j - k * 9 + 40] == LegacyGame.OCCUPIED) {
                flag2 = LegacyGame.OCCUPIED;
              }
            }
            if (flag2 == LegacyGame.EMPTY) {
              zbir++;
              flag = LegacyGame.OCCUPIED;
              break;
            }
          } else {
            zbir++;
            flag = LegacyGame.OCCUPIED;
            break;
          }
        }
      }
      if (flag == LegacyGame.OCCUPIED)
        break;
    }
    return zbir;
  }

  public int tryToMake3WithGap_FromVert4IntersectingWithHoriz4(int playerMark,
      int gameBoardLevelToCheck) {
    int k;

    for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
      gameBoard[1][k] = gameBoard[gameBoardLevelToCheck][k];
    for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
      if (gameBoard[1][k] == EMPTY) {
        gameBoard[1][k] = playerMark;
        if (responseTo3Or4InaRowOpportunity(playerMark, 1, CHECK_MODE) != NONE
            && countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1,
                SAFE_MODE) > 0) {
          return k;
        }
        gameBoard[1][k] = EMPTY;
      }
    }
    return (NONE);
  }

  public void setc4c(int playerMark) {
    int j, k, l;
    int position, x = 2;

    for (j = 0; j < TOTAL_SQUARES_PER_BOARD; j++)
      stagingBoard[j] = EMPTY;

    for (j = 0; j < SQUARES_PER_SIDE; j++) {
      for (k = 0; k < 6; k++) {
        marksByAxisByPlayerForChecking[0] = 0;
        marksByAxisByPlayerForChecking[1] = 0;
        for (l = 0; l < 5; l++) {
          position = gameBoard[x][j * SQUARES_PER_SIDE + k + l];
          if (position == playerMark)
            marksByAxisByPlayerForChecking[0]++;
          if (position == EMPTY) {
            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j
                * SQUARES_PER_SIDE + k + l;
            marksByAxisByPlayerForChecking[1]++;
          }
        }
        if (marksByAxisByPlayerForChecking[0] == 3
            && marksByAxisByPlayerForChecking[1] == 2)
          for (l = 0; l < 2; l++)
            stagingBoard[tempRowForChecks[l]] = OCCUPIED;
      }
    }

    for (j = 0; j < SQUARES_PER_SIDE; j++) {
      for (k = 0; k < 6; k++) {
        marksByAxisByPlayerForChecking[0] = 0;
        marksByAxisByPlayerForChecking[1] = 0;
        for (l = 0; l < 5; l++) {
          position = gameBoard[x][k * SQUARES_PER_SIDE + j + l
              * SQUARES_PER_SIDE];
          if (position == playerMark)
            marksByAxisByPlayerForChecking[0]++;
          if (position == EMPTY) {
            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = k
                * SQUARES_PER_SIDE + j + l * SQUARES_PER_SIDE;
            marksByAxisByPlayerForChecking[1]++;
          }
        }
        if (marksByAxisByPlayerForChecking[0] == 3
            && marksByAxisByPlayerForChecking[1] == 2)
          for (l = 0; l < 2; l++)
            stagingBoard[tempRowForChecks[l]] = OCCUPIED;
      }
    }

    for (j = 0; j < 6; j++) {
      for (k = 0; k < 6; k++) {
        marksByAxisByPlayerForChecking[0] = 0;
        marksByAxisByPlayerForChecking[1] = 0;
        for (l = 0; l < 5; l++) {
          position = gameBoard[x][j * SQUARES_PER_SIDE + k + l * 11];
          if (position == playerMark)
            marksByAxisByPlayerForChecking[0]++;
          if (position == EMPTY) {
            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j
                * SQUARES_PER_SIDE + k + l * 11;
            marksByAxisByPlayerForChecking[1]++;
          }
        }
        if (marksByAxisByPlayerForChecking[0] == 3
            && marksByAxisByPlayerForChecking[1] == 2)
          for (l = 0; l < 2; l++)
            stagingBoard[tempRowForChecks[l]] = OCCUPIED;
      }
    }

    for (j = 0; j < 6; j++) {
      for (k = 0; k < 6; k++) {
        marksByAxisByPlayerForChecking[0] = 0;
        marksByAxisByPlayerForChecking[1] = 0;
        for (l = 0; l < 5; l++) {
          position = gameBoard[x][j * SQUARES_PER_SIDE + k - l * 9 + 40];
          if (position == playerMark)
            marksByAxisByPlayerForChecking[0]++;
          if (position == EMPTY) {
            tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j
                * SQUARES_PER_SIDE + k - l * 9 + 40;
            marksByAxisByPlayerForChecking[1]++;
          }
        }
        if (marksByAxisByPlayerForChecking[0] == 3
            && marksByAxisByPlayerForChecking[1] == 2)
          for (l = 0; l < 2; l++)
            stagingBoard[tempRowForChecks[l]] = OCCUPIED;
      }
    }
  }

  public void seto4cc(int playerMark) {
    int j, k, l;
    int position;
    int x = 2;

    for (j = 0; j < TOTAL_SQUARES_PER_BOARD; j++)
      stagingBoard[j] = EMPTY;

    for (j = 0; j < SQUARES_PER_SIDE; j++) {
      for (k = 0; k < 5; k++) {
        position = j * SQUARES_PER_SIDE + k;
        if (gameBoard[x][position] == EMPTY
            && gameBoard[x][position + 5] == EMPTY) {
          marksByAxisByPlayerForChecking[0] = 0;
          marksByAxisByPlayerForChecking[1] = 0;
          for (l = 1; l < 5; l++) {
            position = gameBoard[x][j * SQUARES_PER_SIDE + k + l];
            if (position == playerMark)
              marksByAxisByPlayerForChecking[0]++;
            if (position == EMPTY) {
              tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j
                  * SQUARES_PER_SIDE + k + l;
              marksByAxisByPlayerForChecking[1]++;
            }
          }
          if (marksByAxisByPlayerForChecking[0] == 2
              && marksByAxisByPlayerForChecking[1] == 2)
            for (l = 0; l < 2; l++)
              stagingBoard[tempRowForChecks[l]] = OCCUPIED;
        }
      }
    }

    for (j = 0; j < SQUARES_PER_SIDE; j++) {
      for (k = 0; k < 5; k++) {
        position = k * SQUARES_PER_SIDE + j;
        if (gameBoard[x][position] == EMPTY
            && gameBoard[x][position + 50] == EMPTY) {
          marksByAxisByPlayerForChecking[0] = 0;
          marksByAxisByPlayerForChecking[1] = 0;
          for (l = 1; l < 5; l++) {
            position = gameBoard[x][k * SQUARES_PER_SIDE + j + l
                * SQUARES_PER_SIDE];
            if (position == playerMark)
              marksByAxisByPlayerForChecking[0]++;
            if (position == EMPTY) {
              tempRowForChecks[marksByAxisByPlayerForChecking[1]] = k
                  * SQUARES_PER_SIDE + j + l * SQUARES_PER_SIDE;
              marksByAxisByPlayerForChecking[1]++;
            }
          }
          if (marksByAxisByPlayerForChecking[0] == 2
              && marksByAxisByPlayerForChecking[1] == 2)
            for (l = 0; l < 2; l++)
              stagingBoard[tempRowForChecks[l]] = OCCUPIED;
        }
      }
    }

    for (j = 0; j < 5; j++) {
      for (k = 0; k < 5; k++) {
        position = j * SQUARES_PER_SIDE + k;
        if (gameBoard[x][position] == EMPTY
            && gameBoard[x][position + 55] == EMPTY) {
          marksByAxisByPlayerForChecking[0] = 0;
          marksByAxisByPlayerForChecking[1] = 0;
          for (l = 1; l < 5; l++) {
            position = gameBoard[x][j * SQUARES_PER_SIDE + k + l * 11];
            if (position == playerMark)
              marksByAxisByPlayerForChecking[0]++;
            if (position == EMPTY) {
              tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j
                  * SQUARES_PER_SIDE + k + l * 11;
              marksByAxisByPlayerForChecking[1]++;
            }
          }
          if (marksByAxisByPlayerForChecking[0] == 2
              && marksByAxisByPlayerForChecking[1] == 2)
            for (l = 0; l < 2; l++)
              stagingBoard[tempRowForChecks[l]] = OCCUPIED;
        }
      }
    }

    for (j = 0; j < 5; j++) {
      for (k = 0; k < 5; k++) {
        position = j * SQUARES_PER_SIDE + k;
        if (gameBoard[x][position + 50] == EMPTY
            && gameBoard[x][position + 5] == EMPTY) {
          marksByAxisByPlayerForChecking[0] = 0;
          marksByAxisByPlayerForChecking[1] = 0;
          for (l = 1; l < 5; l++) {
            position = gameBoard[x][j * SQUARES_PER_SIDE + k - l * 9 + 50];
            if (position == playerMark)
              marksByAxisByPlayerForChecking[0]++;
            if (position == EMPTY) {
              tempRowForChecks[marksByAxisByPlayerForChecking[1]] = j
                  * SQUARES_PER_SIDE + k - l * 9 + 50;
              marksByAxisByPlayerForChecking[1]++;
            }
          }
          if (marksByAxisByPlayerForChecking[0] == 2
              && marksByAxisByPlayerForChecking[1] == 2)
            for (l = 0; l < 2; l++)
              stagingBoard[tempRowForChecks[l]] = OCCUPIED;
        }
      }
    }
  }

  public int blockSeriesOfFourOrMore(int playerMark, int x, int type) {
    int j, k, l;
    int position = 0, position2 = 0;

    for (l = 0; l < 6; l++) {
      for (j = 0; j < SQUARES_PER_SIDE; j++) {
        resetAllMarksAlongAxesForFirstHalfOfBoard();

        position = checkFor5AlongHorizAxis(playerMark, x, j, l, position);

        if (marksByAxisByPlayerForChecking[0] == 3
            && marksByAxisByPlayerForChecking[1] == 2) {
          if (type == SETFLAGS_MODE) {
            tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
            tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
          }
          if (type == CLEAN_MODE)
            return tempRowForChecks[0];
        }

        if (marksByAxisByPlayerForChecking[0] == 4
            && marksByAxisByPlayerForChecking[1] == 1 && type == CHECK_MODE)
          return position;

        position = checkFor5AlongVertAxis(playerMark, x, j, l, position);

        if (marksByAxisByPlayerForChecking[2] == 3
            && marksByAxisByPlayerForChecking[3] == 2) {
          if (type == SETFLAGS_MODE) {
            tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
            tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
          }
          if (type == CLEAN_MODE)
            return tempRowForChecks[0];
        }
        if (marksByAxisByPlayerForChecking[2] == 4
            && marksByAxisByPlayerForChecking[3] == 1 && type == CHECK_MODE)
          return position;
      }

      for (j = 0; j < 6; j++) {
        resetAllMarksAlongAxesForFirstHalfOfBoard();

        for (k = 0; k < 5; k++) {
          position = checkFor5AlongDiagDownRightAxis(playerMark, x, j, k, l,
              position);
          position2 = checkFor5AlongDiagUpRightAxis(playerMark, x, j, k, l,
              position2);
        }

        if (marksByAxisByPlayerForChecking[0] == 3
            && marksByAxisByPlayerForChecking[1] == 2) {
          if (type == SETFLAGS_MODE) {
            tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
            tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
          }
          if (type == CLEAN_MODE)
            return tempRowForChecks[0];
        }
        if (marksByAxisByPlayerForChecking[0] == 4
            && marksByAxisByPlayerForChecking[1] == 1 && type == CHECK_MODE)
          return position;

        if (marksByAxisByPlayerForChecking[2] == 3
            && marksByAxisByPlayerForChecking[3] == 2) {
          if (type == SETFLAGS_MODE) {
            tempTableForChecks[tempRowForChecks[0]] = OCCUPIED;
            tempTableForChecks[tempRowForChecks[1]] = OCCUPIED;
          }
          if (type == CLEAN_MODE)
            return tempRowForChecks[0];
        }
        if (marksByAxisByPlayerForChecking[2] == 4
            && marksByAxisByPlayerForChecking[3] == 1 && type == CHECK_MODE)
          return position2;
      }
    }
    return (NONE);
  }

  public int checkFor5AlongDiagUpRightAxis(int playerMark, int x, int j, int k,
      int l, int position2) {
    if (gameBoard[x][l * SQUARES_PER_SIDE + j - k * 9 + 40] == playerMark)
      marksByAxisByPlayerForChecking[2]++;
    if (gameBoard[x][l * SQUARES_PER_SIDE + j - k * 9 + 40] == EMPTY) {
      position2 = l * SQUARES_PER_SIDE + j - k * 9 + 40;
      tempRowForChecks[marksByAxisByPlayerForChecking[3]] = position2;
      marksByAxisByPlayerForChecking[3]++;
    }
    return position2;
  }

  public int checkFor5AlongDiagDownRightAxis(int playerMark, int x, int j,
      int k, int l, int position) {
    if (gameBoard[x][l * SQUARES_PER_SIDE + j + k * 11] == playerMark)
      marksByAxisByPlayerForChecking[0]++;
    if (gameBoard[x][l * SQUARES_PER_SIDE + j + k * 11] == EMPTY) {
      position = l * SQUARES_PER_SIDE + j + k * 11;
      tempRowForChecks[marksByAxisByPlayerForChecking[1]] = position;
      marksByAxisByPlayerForChecking[1]++;
    }
    return position;
  }

  public int checkFor5AlongVertAxis(int playerMark, int x, int j, int l,
      int position) {
    int k;
    for (k = 0; k < 5; k++) {
      if (gameBoard[x][l * SQUARES_PER_SIDE + j + k * SQUARES_PER_SIDE] == playerMark)
        marksByAxisByPlayerForChecking[2]++;
      else if (gameBoard[x][l * SQUARES_PER_SIDE + j + k * SQUARES_PER_SIDE] == EMPTY) {
        position = SQUARES_PER_SIDE * l + j + k * SQUARES_PER_SIDE;
        tempRowForChecks[marksByAxisByPlayerForChecking[3]] = position;
        marksByAxisByPlayerForChecking[3]++;
      } else
        break;
    }
    return position;
  }

  public int checkFor5AlongHorizAxis(int playerMark, int x, int j, int l,
      int position) {
    int k;
    for (k = 0; k < 5; k++) {
      if (gameBoard[x][j * SQUARES_PER_SIDE + l + k] == playerMark)
        marksByAxisByPlayerForChecking[0]++;

      else if (gameBoard[x][j * SQUARES_PER_SIDE + l + k] == EMPTY) {
        position = SQUARES_PER_SIDE * j + l + k;
        tempRowForChecks[marksByAxisByPlayerForChecking[1]] = position;
        marksByAxisByPlayerForChecking[1]++;

      } else
        break;
    }
    return position;
  }

  public int tryToFindPositionGivingSeriesOf4OnTwoOrMoreAxes(int playerMark,
      int indexForBoardToCheck) {
    copyBoardToCheck(indexForBoardToCheck);

    for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
      if (gameBoard[1][k] == EMPTY) {
        gameBoard[1][k] = playerMark;

        if (countNumberOfAxesAlongWhichSeriesOfFourOccur(playerMark, 1,
            CLEAN_MODE) > 1)
          return k;

        gameBoard[1][k] = EMPTY;
      }
    }
    return (NONE);
  }

  public int responseTo3Or4InaRowOpportunity(int playerMark, int boardLevel,
      int type) {
    int j, k, l;
    int place = 0;

    for (k = 0; k < 4; k++)
      tempRowForChecks[k] = 0;

    for (l = 0; l < 5; l++) {
      for (j = 0; j < SQUARES_PER_SIDE; j++) {
        clearMarksByAxisArray();

        if (gameBoard[boardLevel][j * SQUARES_PER_SIDE + l] == EMPTY
            && gameBoard[boardLevel][j * SQUARES_PER_SIDE + l + 5] == EMPTY) {

          place = checkForHoriz4InRow(playerMark, boardLevel, j, l);
          if (anyHoriz4MatchToMark(type, place))
            return place;
        }

        if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j] == EMPTY
            && gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + 50] == EMPTY) {

          place = checkForVert4InRow(playerMark, boardLevel, j, l);
          if (anyVert4MatchToMark(type, place))
            return place;
        }
      }

      for (j = 0; j < 5; j++) {
        clearMarksByAxisArray();

        if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j] == EMPTY
            && gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + 55] == EMPTY) {

          place = checkForDiagDown4InRow(playerMark, boardLevel, j, l);
          if (anyDiagDown4MatchToMark(type, place))
            return place;
        }

        if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + 50] == EMPTY
            && gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + 5] == EMPTY) {

          place = checkForDiagUp4InRow(playerMark, boardLevel, j, l);
          if (anyDiagUp4MatchToMark(type, place))
            return place;
        }
      }
    }

    if (type == COUNT_MODE) {
      return tempRowForChecks[0] + tempRowForChecks[1] + tempRowForChecks[2]
          + tempRowForChecks[3];
    }

    return (NONE);
  }

  public int checkForHoriz4InRow(int playerMark, int boardLevel, int j, int l) { /* horiz */
    int place = NONE;
    int k;
    for (k = 1; k < 5; k++) {
      if (gameBoard[boardLevel][j * SQUARES_PER_SIDE + l + k] == playerMark)
        marksByAxisByPlayerForChecking[0]++;
      else if (gameBoard[boardLevel][j * SQUARES_PER_SIDE + l + k] == EMPTY) {
        place = SQUARES_PER_SIDE * j + l + k;
        marksByAxisByPlayerForChecking[1]++;
      } else
        break;
    }
    return place;
  }

  public int checkForVert4InRow(int playerMark, int boardLevel, int j, int l) {
    int place = NONE;
    int k;
    for (k = 1; k < 5; k++) {
      if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + k * SQUARES_PER_SIDE] == playerMark)
        marksByAxisByPlayerForChecking[2]++;
      else if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + k
          * SQUARES_PER_SIDE] == EMPTY) {
        place = SQUARES_PER_SIDE * l + j + k * SQUARES_PER_SIDE;
        marksByAxisByPlayerForChecking[3]++;
      } else
        break;

    }
    return place;
  }

  public int checkForDiagDown4InRow(int playerMark, int boardLevel, int j, int l) {
    int place = NONE;
    int k;
    for (k = 1; k < 5; k++) {
      if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + k * 11] == playerMark)
        marksByAxisByPlayerForChecking[0]++;
      if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j + k * 11] == EMPTY) {
        place = l * SQUARES_PER_SIDE + j + k * 11;
        marksByAxisByPlayerForChecking[1]++;
      }
    }
    return place;
  }

  public int checkForDiagUp4InRow(int playerMark, int boardLevel, int j, int l) {
    int place = NONE;
    int k;
    for (k = 1; k < 5; k++) {
      if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j - k * 9 + 50] == playerMark)
        marksByAxisByPlayerForChecking[2]++;
      if (gameBoard[boardLevel][l * SQUARES_PER_SIDE + j - k * 9 + 50] == EMPTY) {
        place = l * SQUARES_PER_SIDE + j - k * 9 + 50;
        marksByAxisByPlayerForChecking[3]++;
      }
    }
    return place;
  }

  public boolean anyDiagUp4MatchToMark(int type, int place) {
    boolean match = false;
    if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 3
        && marksByAxisByPlayerForChecking[3] == 1) {
      tempRowForChecks[3] = 1;
      if (type == CHECK_MODE) {
        match = true;
      }
      if (type == SETFLAGS_MODE)
        tempTableForChecks[place] = OCCUPIED;
    }
    if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 2
        && marksByAxisByPlayerForChecking[3] == 2) {
      match = true;
    }
    return match;
  }

  public boolean anyDiagDown4MatchToMark(int type, int place) {
    boolean match = false;

    if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 3
        && marksByAxisByPlayerForChecking[1] == 1) {
      tempRowForChecks[2] = 1;
      if (type == CHECK_MODE) {
        match = true;
      }
      if (type == SETFLAGS_MODE)
        tempTableForChecks[place] = OCCUPIED;
    }
    if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 2
        && marksByAxisByPlayerForChecking[1] == 2) {
      match = true;
    }
    return match;
  }

  public boolean anyVert4MatchToMark(int type, int place) {
    boolean match = false;
    if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 3
        && marksByAxisByPlayerForChecking[3] == 1) {
      tempRowForChecks[1] = 1;
      if (type == CHECK_MODE) {

        match = true;
      }
      if (type == SETFLAGS_MODE)
        tempTableForChecks[place] = OCCUPIED;
    }
    if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[2] == 2
        && marksByAxisByPlayerForChecking[3] == 2) {
      match = true;
    }
    return match;
  }

  public boolean anyHoriz4MatchToMark(int type, int place) {
    if (type != CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 3
        && marksByAxisByPlayerForChecking[1] == 1) {
      tempRowForChecks[0] = 1;
      if (type == CHECK_MODE) {
        return true;
      }
      if (type == SETFLAGS_MODE)
        tempTableForChecks[place] = OCCUPIED;
    }

    if (type == CLEAN_MODE && marksByAxisByPlayerForChecking[0] == 2
        && marksByAxisByPlayerForChecking[1] == 2) {
      return true;
    }
    return false;
  }

  public int check2o3c(int playerMark, int x) {
    int k;

    for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++) {
      if (gameBoard[x][k] != EMPTY)
        continue;

      gameBoard[x][k] = playerMark;

      if (responseTo3Or4InaRowOpportunity(playerMark, x, COUNT_MODE) > 1) {
        return k;
      }

      gameBoard[x][k] = EMPTY;
    }
    return NONE;
  }

  public int closeGapInSeries() {
    int k, l, x, y;

    for (k = 1; k < 7; k++) {
      for (l = 1; l < 9; l++) {
        x = k + SQUARES_PER_SIDE * l;
        y = l + k * SQUARES_PER_SIDE;

        if (gameBoard[0][x] == X_MARK_FOR_PLAYER
            && gameBoard[0][x + 2] == X_MARK_FOR_PLAYER
            && gameBoard[0][x + 1] == 0)
          return (x + 1);

        if (gameBoard[0][y] == X_MARK_FOR_PLAYER
            && gameBoard[0][y + 20] == X_MARK_FOR_PLAYER
            && gameBoard[0][y + SQUARES_PER_SIDE] == 0)
          return (y + SQUARES_PER_SIDE);
      }
    }
    return NONE;
  }

  public void setFlagsForLaterProcessing(int playerMark) {
    int k;

    for (k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
      tempTableForChecks[k] = EMPTY;
    blockSeriesOfFourOrMore(playerMark, 0, SETFLAGS_MODE);
    responseTo3Or4InaRowOpportunity(playerMark, 0, SETFLAGS_MODE);
  }

  public int[][] gameBoard() {
    return gameBoard;
  }

  public void resetAllMarksAlongAxesForFirstHalfOfBoard() {
    for (int k = 0; k < 4; k++)
      marksByAxisByPlayerForChecking[k] = 0;
  }

  public void copyIntoStagingBoardFromOddBoardGroupAtDepth(int depth) {
    for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
      stagingBoard[k] = perhapsaTemporaryBoardHolder[depth][k];
  }

  public void copyStagingBoardIntoOddGroupOfBoardsAtDepth(int depth) {
    for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
      perhapsaTemporaryBoardHolder[depth][k] = stagingBoard[k];
  }

  private int makeArbitraryFirstComputerMoveBasedOnPlayerY(int x, int y) {
    if (y > 5)
      return (y * SQUARES_PER_SIDE + x - 11);
    else
      return (y * SQUARES_PER_SIDE + x + 11);
  }

  public String returnPrintableBoard(String crCharacter) {
    String border = "*****************************" + crCharacter;
    String boardString = border;
    String spacer = SINGLE_BLANK_SPACE;

    spacer = DOUBLE_BLANK_SPACE;
    for (int x = 0; x < 10; x++) {
      boardString = printBoardRow(boardString, spacer, x);
    }
    boardString += border;

    return boardString;
  }

  private String printBoardRow(String boardString, String spacer, int x) {
    int cell;
    for (int y = 0; y < 10; y++) {
      String mark = null;
      cell = getBoardCellNumber(x, y);

      if (doubleDigitRow(cell))
        spacer = SINGLE_BLANK_SPACE;
      else if ((gameBoard[0][0] == 0) && (gameBoard[0][1] == 0))
        spacer = DOUBLE_BLANK_SPACE;
      else
        spacer = DOUBLE_BLANK_SPACE;

      if (gameBoard[0][cell] == 2) {
        mark = "00";
        if (cell < 9)
          spacer = SINGLE_BLANK_SPACE;
      }
      if (gameBoard[0][cell] == 1) {
        mark = "XX";
        if (cell < 9)
          spacer = SINGLE_BLANK_SPACE;
      }
      if (mark == null) {
        boardString += "" + cell + spacer;
      } else {
        boardString += "" + mark + spacer;
      }
    }
    boardString += "\n";
    return boardString;
  }

  private boolean doubleDigitRow(int cell) {
    return cell > 9;
  }

  private int getBoardCellNumber(int x, int y) {
    return (x * 10) + y;
  }

  private void copyBoardToCheck(int indexForBoardToCheck) {
    for (int j = 0; j < TOTAL_SQUARES_PER_BOARD; j++)
      gameBoard[1][j] = gameBoard[indexForBoardToCheck][j];
  }

  private void clearMarksByAxisArray() {
    int k;
    for (k = 0; k < 4; k++)
      marksByAxisByPlayerForChecking[k] = EMPTY;
  }

  public void resetMainGameBoard(int boardLevel) {
    for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
      gameBoard[boardLevel][k] = 0;
  }

  public void resetBoard() {
    for (int k = 0; k < TOTAL_SQUARES_PER_BOARD; k++)
      gameBoard[0][k] = 0;
  }

  public void respondToMouseUp(int playerMove, int x, int y) {
    moveNumber++;
    markMove(playerMove, X_MARK_FOR_PLAYER);
    if (win() == EMPTY) {
      lastMove = makeComputerMove(x, y, true);
      markMove(lastMove, ZERO_MARK_FOR_COMPUTER);
      gameState = 0;
    }
    if (win() == ZERO_MARK_FOR_COMPUTER)
      gameState = 3;

    if (win() == X_MARK_FOR_PLAYER)
      gameState = 2;

  }

  public void run() {
    resetMainGameBoard(0);
    moveNumber = 0;
    gameState = 0;
  }
}
