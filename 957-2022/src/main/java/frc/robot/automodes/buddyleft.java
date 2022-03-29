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

    // change distance and angle
    double angleChange = 87; //change in testing
    double distanceChange = 3.5; // subect to change


    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        s.speed = 2250; // shooter speed set to near shooter
        switch (autoStep) {
            case 1:  // turns to cargo
                if(d.turnJank(52)){  
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }
            break;

            case 2:   // drives to cargo and intakes
               if(d.driveJank(60, -2.3, 0.15)){
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 3:  // turns towards hub
               if(d.turnJank(angleChange - 5)){ 
                   d.resetEncoders();
                   autoStep++;
               }
            break;

            case 4: // drives to hub
               if(d.driveJank(angleChange, distanceChange, 0.15)){ 
                   d.resetEncoders();
                   autoStep++;
      
               }
            break;

            case 5: //shoot
               if(s.caseNumber != 2){
                   autoStep = 7;
                   d.resetEncoders();
               }
            break;
        }
    }
}
