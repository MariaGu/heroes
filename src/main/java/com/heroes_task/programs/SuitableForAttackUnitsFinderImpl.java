package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//Complexity: O(n * m) (n - numer of strings, m - number of units in string)

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        Map<Integer, List<Unit>> suitUnitsMap = new HashMap<>();

        for (int i = 0; i < unitsByRow.size(); i++) {
            List<Unit> row = unitsByRow.get(i);
            List<Unit> suitUnitsInRow = findSuitUnitsInRow(row, isLeftArmyTarget);
            if (!suitUnitsInRow.isEmpty()) {
                suitUnitsMap.put(i, suitUnitsInRow);
            }
        }

        List<Unit> allSuitUnits = new ArrayList<>();
        suitUnitsMap.values().forEach(allSuitUnits::addAll);
        return allSuitUnits;
    }

    private List<Unit> findSuitUnitsInRow(List<Unit> row, boolean isLeftArmyTarget) {
        List<Unit> suitUnits = new ArrayList<>();
        for (int index = 0; index < row.size(); index++) {
            Unit unit = row.get(index);
            if (unit != null && unit.isAlive() &&
                    (isLeftArmyTarget ? isRightmostUnit(row, index) : isLeftmostUnit(row, index))) {
                suitUnits.add(unit);
            }
        }
        return suitUnits;
    }

    private boolean isRightmostUnit(List<Unit> row, int unitIndex) {
        return unitIndex == row.size() - 1
                || row.subList(unitIndex + 1, row.size()).stream().allMatch(Objects::isNull);
    }

    private boolean isLeftmostUnit(List<Unit> row, int unitIndex) {
        return unitIndex == 0
                || row.subList(0, unitIndex).stream().allMatch(Objects::isNull);
    }
}
