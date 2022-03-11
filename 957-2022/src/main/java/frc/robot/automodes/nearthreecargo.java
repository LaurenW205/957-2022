package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class nearthreecargo {

    int autoStep = 0;

    public void reset(){

        autoStep = 0;
    }
    
    
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){

    t.manualOverride(0, 0, -10, 0);

    switch(autoStep){

      // Drive out of tarmac
      case 0: 

        if(d.driveJank(0, -1.3, 0.15)){
            d.resetEncoders();
            autoStep++;
        }
      break;

      //turn towards cargo

      case 1: 
        
        if(d.turnJank(-10)){
            d.resetEncoders();
            i.var = 2;
            autoStep++;
        }

      break;

      //drive and intake auto 

      case 2:

        if(d.driveJank(-10, -7, 0.15)){
            d.resetEncoders();
            autoStep++;
        }
    
      break;  

     //drive back to midpoint


      case 3:

        if(d.driveJank(-10, 3.3, 0.15)){
            d.resetEncoders();
            autoStep++;
            

        }
      
      break;

      //turn 

      case 4:

        if(d.turnJank(15)){
            d.resetEncoders();
            autoStep++;

        }
        
      break;

      //drive to shooting range

      case 5:

        if(d.driveJank(15, 2, 0.15)){
            d.resetEncoders();
            autoStep++;
            s.caseNumber = 1;
        }
      break;

      //Shoot that sucker!

      case 6:

        if(s.caseNumber !=2){

            autoStep =4;
            d.resetEncoders();
        }

      break;



      //drive and intake other cargo

      //turn towards midpoint

      //drive towards midpoint 

      //turn towards hub
      
      //drive towards hub 

      //Shoot that sucker!

    

}
    }
}
