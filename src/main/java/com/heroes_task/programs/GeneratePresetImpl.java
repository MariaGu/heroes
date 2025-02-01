package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class GeneratePresetImpl implements GeneratePreset {

    // Complexity: Sorting - O(n log n), Unit choice - O(n), In general: ~ O(n log n)
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {


        //sort units according their efficiency
        unitList.sort(Comparator.comparingDouble(unit ->
                -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())));

        List<Unit> selUnits = new ArrayList<>();
        int curPoints = 0;
        int unitsToAdd = 0;

        for (Unit unit : unitList) {
            unitsToAdd = calculateMaxUnitsToAdd(unit, maxPoints, curPoints);
            addUnitsToArmy(unit, unitsToAdd, selUnits);
            curPoints += unitsToAdd * unit.getCost();
        }

        // set coordinates for units
        Set<String> occupiedCoords = new HashSet<>();
        Random random = new Random();
        for (Unit unit : selUnits) {
            assignRandomCoordinates(unit, occupiedCoords, random);
        }

        Army army = new Army();
        army.setUnits(selUnits);
        army.setPoints(curPoints);
        return army;
    }

    // calculate max units for adding
    private int calculateMaxUnitsToAdd(Unit unit, int maxPoints, int curPoints) {
        return Math.min(11, (maxPoints - curPoints) / unit.getCost());
    }

    // add units to army
    private void addUnitsToArmy(Unit unit, int unitsToAdd, List<Unit> selUnits) {
        for (int i = 0; i < unitsToAdd; i++) {
            Unit newUnit = createNewUnit(unit, i);
            selUnits.add(newUnit);
        }
    }

    private Unit createNewUnit(Unit unit, int index) {
        return new Unit(unit.getUnitType() + " " + index, unit.getUnitType(), unit.getHealth(),
                unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
                unit.getAttackBonuses(), unit.getDefenceBonuses(), -1, -1);
    }

    private void assignRandomCoordinates(Unit unit, Set<String> occupiedCoords, Random random) {
        int coordX, coordY;
        do {
            coordX = random.nextInt(3);
            coordY = random.nextInt(21);
        } while (occupiedCoords.contains(coordX + "," + coordY));
        occupiedCoords.add(coordX + "," + coordY);
        unit.setxCoordinate(coordX);
        unit.setyCoordinate(coordY);
    }
}
