package org.anomek.omegasim.scenarios.partysynergy;

public class PlayerScenarioInfo {

    PlayerScenarioInfo linkedTo;

    int index;
    int linkedToIndex;

    boolean left;
    int rank;
    boolean stack;
    boolean swapForStack;

    boolean leftForStack() {
        return swapForStack != left;
    }

    void init(int index, Settings settings, ScenarioRandom random) {
        this.index = index;
        this.stack = random.isStack(index);
        for (int i = 0; i < 8; i++) {
            if (i != index && random.markers.get(i).equals(random.markers.get(index))) {
                this.linkedToIndex = i;
                break;
            }
        }


        int[] lookupArray;
        if (linkedToIndex > index) {
            // go left
            left = true;
            lookupArray = settings.orderClose;
        } else {
            // go right
            left = false;
            lookupArray = random.far ? settings.orderFar : settings.orderClose;
        }
        rank = lookupArray[random.markers.get(index)];
    }
}
