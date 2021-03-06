package frc.robot.automodes;

import frc.robot.DriveTrain;

public class JankAuto {

    int autoStep = 0;

    public void reset(){

        autoStep = 0;
    }
    
    public void run(DriveTrain d){

        switch(autoStep){

            case 0:
    
              if(d.driveJank(0, 5, 0.15)){
                autoStep++;
                d.resetEncoders();
              }
            break;
            
            case 1:
    
              if(d.turnJank(-85)){
                autoStep++;
                d.resetEncoders();
              }
            break;
    
            case 2:
    
              if(d.driveJank(-90, 4, 0.15)){
                autoStep++;
               d.resetEncoders();
              }
    
             break;

            }
        
    }
}
