package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class midthreecargo {

    int autoStep = -1;

    public void reset(){

        autoStep = -1;
    }
    
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){

        t.manualOverride(0, 0, -10, 0);
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

                if(d.driveJank(0, 1, 0.15)){ // Distance subject to change
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
            
            // Turn to terminal 
            case 4: 
            
            if(d.turnJank(10)){
                d.resetEncoders();
                autoStep++; 
            }
        
            break;    

            //drive towards terminal
            case 5:

            if(d.driveJank(10, -6, 0.30)){
                d.resetEncoders();
                autoStep++;
            }


            break;

            //stop and turn towards terminal
            case 6:

            if(d.turnJank(-15)){  //change
                d.resetEncoders();
                i.var =2;
                autoStep++;
            }

            break;

            case 7:

             if(d.driveJank(-22, -4, 0.10)); 

            break;


            //put down intake and intake */
             
            
        

        
    } 
} 

}



