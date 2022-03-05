package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class righttwocargonear {

    int autoStep = -1;

    public void reset(){

        autoStep = -1;
    }
    
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){

        t.manualOverride(0, 0, 30, 0);
        switch(autoStep){

          case -1:

            i.var = 2;
            autoStep++;
          break;
 
            // Drive out of tarmac and intake cargo
            case 0:
                if(d.driveJank(0, -3.3, 0.15)){
                    d.resetEncoders();
                    autoStep++;
                }
            break;

            // Drive to shooting range
            case 1:

                if(d.driveJank(0, 2.2, 0.15)){ // Distance subject to change
                    d.resetEncoders();
                   // i.var = 5;
                    autoStep++;
                    s.caseNumber = 1;
                }
    
            break;

            // Shooter goes brrr
            case 2:    

                if(s.caseNumber != 2){
                    
                    autoStep = 4;
                    d.resetEncoders();
                }

            break;
            
            /* Turn to terminal 
            case 4: 
            
                if(d.turnJank(35)){// Angle subject to change
                    d.resetEncoders();
                    autoStep++; 
                }
            
            break;     
            
            // Drive to in direction of terminal
            case 5:
            
                if(d.driveJank(40, 19)){// Distance subject to change
                    d.resetEncoders();
                    autoStep++;
                }

            break;

            //turn to terminal and prepare to intake
            case 6:

                if(d.turnJank(85)){
                  d.resetEncoders();
                  autoStep++;
                  i.var = 2;
                }
            break;

            //drive to terminal
            case 7:

                if(d.driveJank(90, -12)){
                  d.resetEncoders();
                  autoStep++;
                }
            break;

            case 8:

              d.arcadeDrive(0, 0);

            break;
            } */
        

        
    } 
} 

}


