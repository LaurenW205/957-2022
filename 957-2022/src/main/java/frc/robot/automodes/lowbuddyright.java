package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class lowbuddyright {
    int autoStep = 1;
    public void reset(){
        autoStep = 1;
    }

    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        s.speed = 1500;
        switch (autoStep) {
            case 1: //turns to cargo
                if(d.turnJank(-57)){
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }
            break;

            case 2: //intakes and drives to cargo
               if(d.driveJank(-63, -2.3, 0.15)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 3: //turns to hub
               if(d.turnJank(-80)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 4: //drives to hub
               if(d.driveJank(-87, 4, 0.15)){
                   d.resetEncoders();
                   autoStep++;
      
               }
            break;

            case 5: //turns in line with hub
               if(d.turnJank(-83)){
                   d.resetEncoders();
                   s.caseNumber = 1;
                   autoStep++;
               }
            break;

            case 6: //shoots
               if(s.caseNumber != 2){
                   autoStep = 7;
                   d.resetEncoders();
               }
            break;
        }
    }
}

