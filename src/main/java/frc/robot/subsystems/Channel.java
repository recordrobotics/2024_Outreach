package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.shuffleboard.ShuffleboardUI;

public class Channel extends KillableSubsystem {
    private CANSparkMax channelMotor = new CANSparkMax(RobotMap.Channel.CHANNEL_MOTOR_ID, MotorType.kBrushed);

    public Channel() {
        toggle(ChannelStates.OFF);
        ShuffleboardUI.Test.addSlider("Channel", channelMotor.get(), -1, 1).subscribe(channelMotor::set);
    }

    public enum ChannelStates {
        THROUGH,
        SHOOT,
        REVERSE,
        OFF;
    }

    public void toggle(double speed) {
        channelMotor.set(speed);
    }

    public void toggle(ChannelStates state) {
        switch (state) {
            case THROUGH:
                toggle(Constants.Channel.THROUGH_SPEED);
                break;
            case SHOOT:
                toggle(Constants.Channel.SHOOT_SPEED);
                break;
            case REVERSE:
                toggle(Constants.Channel.REVERSE_SPEED);
                break;
            default:
                toggle(0);
                break;
        }
    }

    @Override
    public void kill() {
        toggle(ChannelStates.OFF);
    }
}