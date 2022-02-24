package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class twocargo1 {

    int autoStep = 0;
    public int shooter_case1 = 0;

    public void reset(){

        autoStep = 0;
    }
    
    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){

        switch(autoStep){
 
            // Drive out of tarmac and intake cargo
            case 0:
                if(d.driveJank(0, 3.8)){
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;
                }
            break;

            // Drive to shooting range
            case 1:

                if(d.driveJank(0, 3)) // Distance subject to change
                    d.resetEncoders();
                    autoStep++;
    
            break;

            // Shooter goes brrr
            case 2:    

                if(shooter_case1 == 2){
                    
                    autoStep++;
                    d.resetEncoders();
                }

            break;

            // Wait until all cargo is shot before next case
            case 3:

                if (cargoNum == 0)
                 autoStep++;
                 
            break;
            
            // Turn to terminal
            case 4: 
            
                if(d.turnJank(40)) // Angle subject to change
                    d.resetEncoders();
                    autoStep++; 
            
            break;     
            
            // Drive to terminal and intake second cargo 
            case 5:
            
                if(d.driveJank(45, 8)) // Distance subject to change
                    d.resetEncoders();
                    autoStep++;
                    i.var = 2;

            break;

            }


        
    }
}
