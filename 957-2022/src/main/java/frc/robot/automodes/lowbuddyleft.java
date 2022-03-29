package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class lowbuddyleft {
    int autoStep = 1;
    public void reset(){
        autoStep = 1;
    }

    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        s.speed = 1500; // shooter speed set to puke
        switch (autoStep) {   
            case 1:  // turns to cargo
                if(d.turnJank(52)){
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }
            break;

            case 2:  // drives to cargo and intakes
               if(d.driveJank(60, -2.3, 0.15)){  
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 3:  // turns towards hub
               if(d.turnJank(80)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 4:  // drives towards hub
               if(d.driveJank(87, 4, 0.15)){
                   d.resetEncoders();
                   autoStep++;
      
               }
            break;

            case 5:  // turns to adjust position
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
