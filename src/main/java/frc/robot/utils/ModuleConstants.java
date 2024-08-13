package frc.robot.utils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.Constants;

public class ModuleConstants {

    private static JSONParser parser = new JSONParser();

    public enum MotorLocation {
        Left,
        Right
    }

    public int driveMotorChannel;

    public double DRIVE_GEAR_RATIO;

    public double DRIVE_KP;
    public double DRIVE_KI;
    public double DRIVE_KD;

    public double DRIVE_FEEDFORWARD_KS;
    public double DRIVE_FEEDFORWARD_KV;
    
    public double DriveMaxAngularVelocity;
    public double DriveMaxAngularAcceleration;

    public double WHEEL_DIAMETER;

    // Class to store types of motors
    public enum MotorType {Falcon, Kraken}

    /**
     * essentially serves as a storage unit for one swerve module, storing every single constant that a module might want to use
     * @param driveMotorChannel drive motor port
     * @param turningMotorChannel turn motor port
     * @param absoluteTurningMotorEncoderChannel abs turn motor encoder port
     * @param turningEncoderOffset offset of the abs turn encoder at a set starting position (which we found through manually testing)
     * @param wheelLocation Translation2d object of where the wheel is relative to robot frame
     * @param turnMotorType The type of the turn motor
     * @param driveMotorType The type of the drive motor
     */

    public ModuleConstants (
        int driveMotorChannel,
        MotorType driveMotorType) {

            // Encoder nums
            this.driveMotorChannel = driveMotorChannel;

            // Max Angular Acceleration & Velocity
            this.DriveMaxAngularVelocity = Constants.Differential.DriveMaxAngularVelocity;
            this.DriveMaxAngularAcceleration = Constants.Differential.DriveMaxAngularAcceleration;

            // Shared miscellaneous variables
            this.WHEEL_DIAMETER = Constants.Differential.WHEEL_DIAMETER;

            // Drive Motor Constants
            switch(driveMotorType){
                case Falcon:
                    this.DRIVE_KP = Constants.Differential.FALCON_DRIVE_KP;
                    this.DRIVE_KI = Constants.Differential.FALCON_DRIVE_KI;
                    this.DRIVE_KD = Constants.Differential.FALCON_DRIVE_KD;
                    this.DRIVE_FEEDFORWARD_KS = Constants.Differential.FALCON_DRIVE_FEEDFORWARD_KS;
                    this.DRIVE_FEEDFORWARD_KV = Constants.Differential.FALCON_DRIVE_FEEDFORWARD_KV;
                    this.DRIVE_GEAR_RATIO = Constants.Differential.FALCON_DRIVE_GEAR_RATIO;
                    break;
                case Kraken:
                    this.DRIVE_KP = Constants.Differential.KRAKEN_DRIVE_KP;
                    this.DRIVE_KI = Constants.Differential.KRAKEN_DRIVE_KI;
                    this.DRIVE_KD = Constants.Differential.KRAKEN_DRIVE_KD;
                    this.DRIVE_FEEDFORWARD_KS = Constants.Differential.KRAKEN_DRIVE_FEEDFORWARD_KS;
                    this.DRIVE_FEEDFORWARD_KV = Constants.Differential.KRAKEN_DRIVE_FEEDFORWARD_KV;
                    this.DRIVE_GEAR_RATIO = Constants.Differential.KRAKEN_DRIVE_GEAR_RATIO;
                    break;
            }
    }

    private static ModuleConstants getDefault(MotorLocation location){
        System.out.println("[MODULE CONSTANTS] Config forced to load default for location " + location);
        switch(location){
            case Left:
                return Constants.Differential.BACKUP_frontLeftConstants;
            case Right:
                return Constants.Differential.BACKUP_frontRightConstants;
        }

        return null;
    }

    /**
     * Loads module constants from deploy/swerve/motors.json file
     * @param location Location of module
     * @param motorType Motor type (determines the falcon/kraken objects in config)
     */
    public static ModuleConstants fromConfig(MotorLocation location, MotorType motorType){
        return fromConfig(location, motorType, motorType);
    }

    /**
     * Loads module constants from deploy/swerve/motors.json file
     * @param location Location of module
     * @param driveMotorType Drive motor type (also determines the falcon/kraken objects in config)
     * @param turnMotorType Turn motor type (only used for the final module creation)
     */
    public static ModuleConstants fromConfig(MotorLocation location, MotorType driveMotorType, MotorType turnMotorType){
        File configFile = new File(Filesystem.getDeployDirectory(), "swerve/motors.json");
        if(!configFile.exists())
            return getDefault(location);
        
        JSONObject obj;
        try {
            FileReader reader = new FileReader(configFile);
            obj = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (IOException | ParseException e) {
            return getDefault(location);
        }

        JSONObject motor;

        switch(location){
            case Left:{
                var val = obj.get("front-left");
                if(val == null)
                    return getDefault(location);
                else
                    motor = (JSONObject)val;
                break;
            }
            case Right:{
                var val = obj.get("front-right");
                if(val == null)
                    return getDefault(location);
                else
                    motor = (JSONObject)val;
                break;
            }
            default:
                return getDefault(location);
        }

        Long driveMotorChannel = (Long)motor.get("driveMotorChannel");
        if(driveMotorChannel == null)
            return getDefault(location);
        
        return new ModuleConstants(Math.toIntExact(driveMotorChannel), driveMotorType);
    }

}
