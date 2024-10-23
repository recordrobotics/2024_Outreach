package frc.robot.commands.manual;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.KillSpecified;
import frc.robot.subsystems.Channel;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Channel.ChannelStates;

public class ChannelLoad extends SequentialCommandGroup {

  private static Shooter _acquisition;
  private static Channel _channel;

  private final double loadTime = .25;


  public ChannelLoad (Channel channel, Shooter acquisition) {
    _acquisition = acquisition;
    _channel = channel;
    addRequirements(acquisition);
    addRequirements(channel);

        final Runnable killSpecified = () -> new KillSpecified(_channel, _acquisition);

        addCommands(

        new InstantCommand(()->_channel.toggle(ChannelStates.LOAD), _channel).handleInterrupt(killSpecified),
        new WaitCommand(loadTime),
        new InstantCommand(()-> _channel.toggle(ChannelStates.OFF), _channel).handleInterrupt(killSpecified)
    );
  }


}
