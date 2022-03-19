package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class buddyright {
    int autoStep = 1;
    public void reset(){
        autoStep = 1;
    }

    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        s.speed = 2250;
        switch (autoStep) {
            case 1:
                if(d.turnJank(-57)){
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }
            break;

            case 2:
               if(d.driveJank(-63, -2.3, 0.15)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 3:
               if(d.turnJank(-80)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 4:
               if(d.driveJank(-87, 3.35, 0.15)){
                   d.resetEncoders();
                   autoStep++;
                   s.caseNumber = 1;
               }
            break;

            case 5:
               if(s.caseNumber != 2){
                   autoStep = 6;
                   d.resetEncoders();
               }
            break;
        }
    }
}
