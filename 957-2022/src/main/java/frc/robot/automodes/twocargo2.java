package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class twocargo2 {

    int autoStep = 0;
    public int shooter_case2 = 0;

    public void reset(){

        autoStep = 0;
    }
    
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){
        
        switch(autoStep){

            // Drive out of tarmac
            case 0:

                if(d.driveJank(0, -3.8, -0.15)){
                    d.resetEncoders();
                    autoStep++;
                }
            
            break;

            // Turn to terminal
            case 1:
            
                if(d.turnJank(5)){ //angle value subject to change
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }

            break;
            
            // Drive towards terminal
            case 2:
                
                if(d.driveJank(10, -12, 0.15)){ // Distance subject to change
                    d.resetEncoders();
                    autoStep++;
                }

            break;

            // Drive to shooting position, and shooter go brrr
            case 3:

                if(d.driveJank(0, 10.5, 0.15)){
                    d.resetEncoders();
                    s.caseNumber = 2;
                    autoStep++;
                }
            break;
            
            // Wait until all cargo is shot before next case
            case 4:    

                if (cargoNum == 0){  
                    autoStep++;
                    d.resetEncoders();
                }

            break;

            // Drive back to terminal and intake second cargo 
            case 5:

                if(d.driveJank(0, -10.5, -0.15)){
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }

            break;

            case 6:

                d.arcadeDrive(0, 0);

            
        }
        
    }
}