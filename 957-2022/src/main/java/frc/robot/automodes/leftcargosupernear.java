package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class leftcargosupernear {

    int autoStep = 0;
    double timer = 0;
    public void reset(){
        timer = 0;
        autoStep = 0;
    }
    
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        timer = timer + 0.02;
        s.speed = 2250;
        t.manualOverride(0, 0, 0, 0);
        switch(autoStep){
    
            case 0: // Drive out of tarmac
                if(d.driveJank(0, -1.5, 0.1)){
                    d.resetEncoders();
                    autoStep++;
                }
            break;

            case 1: //turn to cargo and intake go brr
                if(d.turnJank(10)){
                    d.resetEncoders();
                    i.var = 2;
                    autoStep++;
                }
            break;

            case 2: // Drive to cargo
                if(d.driveJank(17, -3.5, 0.1)){
                    d.resetEncoders();
                    autoStep++;
                }    
            break;

            case 3: //drive away from cargo
               if (d.driveJank(17, 1.5, 0.1)){

                d.resetEncoders();
                autoStep++;
               }
            break;
            
            case 4: //turn back to hub
                if(d.turnJank(5)){
                    d.resetEncoders();
                    autoStep++;
                }
                timer = 0;
            break;
  
            case 5: //drive to hub
            d.driveJank(0, 3.5, 0.1);
            if(timer > 1.5){
                d.resetEncoders();
                autoStep++;
                s.caseNumber = 1;
            }
            break;
        
            case 6: //shooter go brr
                if(s.caseNumber != 2){
                    autoStep = 7;
                    d.resetEncoders();
                }
            break;
      } 
    } 
}



