package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret;

public class twocargo3 {
    
    int autoStep = 0;
    public int shooter_case3 = 0;

    public void reset(){

        autoStep = 0;
    }

    public void run(DriveTrain d, Shooter s, Intake i, Turret t, int cargoNum){

        switch(autoStep){

            case 0:

                i.var = 2;
                autoStep++;
            break;

            //Drive out of tarmac and intake 
            case 1:

                if(d.driveJank(0, -4.5)){
                    d.resetEncoders();
                    autoStep++;
                }
            break;

            //drive to shooting position
            case 2:

                if(d.driveJank(0, -12)){
                    d.resetEncoders();
                    autoStep++;
                    s.caseNumber = 2;
                }
            break;

            //shoot that sucker!
            case 3: 
                
                if(s.caseNumber != 2){
                   
                    autoStep++;
                    d.resetEncoders();
                }
            break;

            //turn to terminal
            case 4:

                if(d.turnJank(-5)){
                    d.resetEncoders();
                    autoStep++;
                    i.var =2;
                }
            break;

            //drive to terminal and prepare intake 
            case 5:

                if(d.driveJank(-10, -7)){
                    d.resetEncoders();
                    autoStep++;

                }
                
            break;

            case 6: 

                d.arcadeDrive(0, 0);
            break;


        }
    }
}
