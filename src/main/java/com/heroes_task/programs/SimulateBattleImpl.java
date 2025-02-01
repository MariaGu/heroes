package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

//Complexity: O(n * m). (n - units of gamer, m - units of PC)
public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        Set<Unit> playerUnits = new HashSet<>(playerArmy.getUnits());
        Set<Unit> compUnits = new HashSet<>(computerArmy.getUnits());

        while (!playerUnits.isEmpty() && !compUnits.isEmpty()) {
            executeAttacks(playerUnits);
            executeAttacks(compUnits);
        }
    }

    private void executeAttacks(Set<Unit> units) throws InterruptedException {
        Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
            Unit attackUnit = iterator.next();
            if (!attackUnit.isAlive()) {
                iterator.remove();
                continue;
            }

            Unit target = attackUnit.getProgram().attack();
            if (target != null) {
                printBattleLog.printBattleLog(attackUnit, attackUnit.getProgram().attack());
            }
        }
    }
}
