package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class buddyleft {
    int autoStep = 1;
    public void reset(){
        autoStep = 1;
    }

    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        s.speed = 1500;
        switch (autoStep) {
            case 1:
                if(d.turnJank(52)){
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }
            break;

            case 2:
               if(d.driveJank(60, -2.3, 0.15)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 3:
               if(d.turnJank(80)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 4:
               if(d.driveJank(87, 3.75, 0.15)){
                   d.resetEncoders();
                   autoStep++;
      
               }
            break;

            case 5:
               if(d.turnJank(78)){
                   d.resetEncoders();
                   s.caseNumber = 1;
                   autoStep++;
               }
            break;

            case 6:
               if(s.caseNumber != 2){
                   autoStep = 7;
                   d.resetEncoders();
               }
            break;
        }
    }
}
