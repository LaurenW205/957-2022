package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class NothingAuto {

     int autoStep = 0;
    
    public void reset(){
    
     autoStep = 0; 
    }
        
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){

        
        t.manualOverride(0, 0, 0, 0);
        switch(autoStep){

            //drive out of tarmac completely
            case 0:
              if(d.driveJank(0, -3.3, 0.15)){
                d.resetEncoders();
                autoStep++;
            }
            break;
            
            //drive back in tarmac
            case 1: 
                if(d.driveJank(0, 1.5, 0.15)){
                    d.resetEncoders();
                    autoStep++;
                    s.caseNumber = 1;
                }
            break;

            //shoot cargo 
            case 2:
                if(s.caseNumber != 2){
                    d.resetEncoders();
                    autoStep++;

                }
        }
    }
    
}
