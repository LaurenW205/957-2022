package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class midtwocargofar {

    int autoStep = -1;
    double timer  = 0;

    public void reset(){
        timer  = 0;
        autoStep = -1;
    }
    
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){

        timer = timer + 0.02;
        s.speed = 2250;
        switch(autoStep){

          case -1:

            i.var = 2;
            autoStep++;
          break;
 
            // Drive out of tarmac and intake cargo
            case 0:
                if(d.driveJank(0, -4.8, 0.15)){
                    d.resetEncoders();
                    autoStep++;
                }
            break;

            case 1: 
            if(d.turnJank(8)){ // turn to go into shooting range, angle subject to change
                d.resetEncoders();
                autoStep++;
            }
            break;

            // back into tarmac
            case 2:

                if(d.driveJank(12, 6, 0.15)){ // Distance & angle subject to change
                    d.resetEncoders();
                   // i.var = 5;
                    autoStep++;
                }
    
            break;

            case 3: // turn to hub 
            if(d.turnJank(-40)){   //angle subject to change
                d.resetEncoders();
                autoStep++;
                timer = 0;
            }
            break;

            case 4:  // drive closer to hub

            if((d.driveJank(-30, 1, 0.15)) || timer > 1){ // Distance & angle subject to change
                d.resetEncoders();
               // i.var = 5;
                autoStep++;
                s.caseNumber = 1;
                d.arcadeDrive(0, 0);

            }

            break;


            // Shooter goes brrr
            case 5:    

                if(s.caseNumber != 2){
                    
                    autoStep = 4;
                    d.resetEncoders();
                }

            break;

        

        
    } 
} 

}



