package project5human;

/**
 * How dead the hangman is, out of 255. This value can increase by any step at one time.
 * When the value reaches 252, the hangman is considered dead.
 * (The normal non-cheating step increase is 42 points.)
 */
public class HangedManState {
    char    hangedManState;
    char    countHangedManStateChanges;
    char    minHangedManDivisions = (char) 6;
    char    maxHangedManDivisions = (char) 12;
    char    maxHangedManStep      = (char) 42;
    char    minHangedManStep      = (char) 21;
    char    hangedManIsDeadAmount = (char) 252;
    boolean isHangedManDead       = false;

    HangedManState () {
        this.hangedManState             = (char) 0;
        this.countHangedManStateChanges = (char) 0;
    }

    void increaseStateByDefault () {
        if (this.isHangedManDead) {
            throw new IllegalArgumentException("attempt to hang dead hanged man");
        }

        // if we have already used maxDivisions or more hangman state changes, the hangman is
        // definitely dead by now
        final boolean alreadyDeadByChangeCount =
            this.countHangedManStateChanges >= (this.maxHangedManDivisions - 1);

        // if this
        final boolean additionWouldOverflowChar =
            (((int) this.hangedManState) + ((int) this.maxHangedManStep))
            >= (int) Character.MAX_VALUE;


        // test whether this state change equals or surpasses the amount needed to be dead
        if ((((int) this.hangedManState) + ((int) this.maxHangedManStep))
            >= ((int) this.hangedManIsDeadAmount)) {
            this.isHangedManDead = true;
            this.hangedManState  = this.hangedManIsDeadAmount;
        } else if (alreadyDeadByChangeCount || additionWouldOverflowChar) {
            this.isHangedManDead = true;
            this.hangedManState  = this.hangedManIsDeadAmount;
        } else {
            // well-defined to be char in java 17 JLS, don't know/care about +=
            this.hangedManState = (char) (this.hangedManState + this.maxHangedManStep);
            this.countHangedManStateChanges++;
        }
    }
}
