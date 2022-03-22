package frc.robot.automodes;

import frc.robot.DriveTrain;
import frc.robot.Intake;
import frc.robot.Shooter;
import frc.robot.Turret2;

public class singlecargo {
    
    double timer = 0;
    int autoStep = -1;

    public void reset(){
        timer = 0;
        autoStep = -1;
    }

    public void run(DriveTrain d, Shooter s, Intake i, Turret2 t, int cargoNum){

        timer = timer + 0.02;
        s.speed = 2250;
        switch (autoStep) {
            case -1:
                if(timer > 5){
                    autoStep++;
                }
            break;

            case 0:
                if(d.driveJank(0, -3, 0.15)){
                    d.resetEncoders();
                    autoStep++;
                }
            break;

            case 1:
                if(d.turnJank(-7)){
                    d.resetEncoders();
                    autoStep++;
                }
            break;

            case 2:
            if(d.driveJank(-11, 4, 0.15)){
                d.resetEncoders();
                autoStep++;
            }
            break;

            case 3:
                if(d.turnJank(10)){
                    d.resetEncoders();
                    autoStep++;
                }
            break;

            case 4:
                if(d.driveJank(15, 0.6, 0.15)){
                    d.resetEncoders();
                    autoStep++;
                    s.caseNumber = 1;
                }

            break;

            case 5:
                if(s.caseNumber != 2){
                    autoStep = 7;
                    d.resetEncoders();
                }
            break;
        }
    }
}
